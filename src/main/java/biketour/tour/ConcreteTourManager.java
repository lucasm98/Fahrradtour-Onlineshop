package biketour.tour;

import biketour.booking.Booking;
import biketour.booking.BookingManager;
import biketour.resupply.Resupply;
import biketour.resupply.ResupplyManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.Monetary;
import javax.transaction.Transactional;
import java.awt.print.Book;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * manages concrete tours
 * @author julian
 */
@Service
@Transactional
@Order(value = 3)
public class ConcreteTourManager {

	private final ConcreteTourCatalog concreteTourCatalog;
	private final ResupplyManager resupplyManager;
	private final UniqueInventory<UniqueInventoryItem> inventory;

	ConcreteTourManager(ConcreteTourCatalog concreteTourCatalog, ResupplyManager resupplyManager,
						UniqueInventory<UniqueInventoryItem> inventory){
		Assert.notNull(concreteTourCatalog,"Tour Catalog must not be null");
		Assert.notNull(resupplyManager,"Resupply Manager must not be null");
		Assert.notNull(inventory,"Inventory must not be null");

		this.concreteTourCatalog = concreteTourCatalog;
		this.resupplyManager = resupplyManager;
		this.inventory = inventory;
	}

	/**
	 * creates a new Tour and saves in TourCatalog
	 * @param tour tour template that will be used to create a concrete tour
	 * @param userAccount useraccount that is needed to create a concrete tour
	 * @param form TourForm has all the necessary data needed to create a new tour as String values
	 * @return a new concrete Tour
	 * @throws ParseException if dates cannot be parsed by the given String an exception is thrown
	 */
	public ConcreteTour createConcreteTour(Tour tour, UserAccount userAccount, ConcreteTourForm form)
			throws ParseException {
		Assert.notNull(form, "Concrete Tour form must not be null.");
		return concreteTourCatalog.save(validateForm(tour, userAccount, form));
	}

	/**
	 * @param name name of a tour
	 * @return the concrete tour
	 */
	public ConcreteTour findConcreteTourByName(String name) {
		return this.concreteTourCatalog.findAll().filter(tour -> tour.getName().equals(name)).toList().get(0);
	}

	/**
	 * @param userAccount tourguide that is associated to the tours
	 * @return a stream of all tours that are associated to a tour guide and are right now or before the actual date
	 */
	public Streamable<ConcreteTour> getAllToursFromTourguide(UserAccount userAccount){
		return getConcreteTourCatalog().findByTourGuide(userAccount).filter(concreteTour ->
				concreteTour.getStart().isBefore(LocalDate.now()) || concreteTour.getStart().isEqual(LocalDate.now()));
	}

	/**
	 * casts the date string to a local date
	 * @param formInput date string
	 * @return LocalDate
	 */
	protected LocalDate getLocalDate(String formInput){
		//	"2019-12-16" --> LocalDate.of(2019, 12, 16
		String dates[] = formInput.split("-");
		return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
	}

	/**
	 * Checks if the amout of booked tours can be applied
	 * @param id tour identifier
	 * @param quantity of bookings
	 * @return true if you can book this amount
	 */
	public boolean setFree(ProductIdentifier id, String quantity){
		return concreteTourCatalog.findById(id).get().subFromFree(Integer.parseInt(quantity));
	}

	/**
	 * set the state of the tour to active or inactive
	 * @param id product identifier of the tour the state is changed
	 * @param state of the tour
	 */
	public void setState(ProductIdentifier id ,String state){
		concreteTourCatalog.findById(id).get().setStatus(ConcreteTour.Status.valueOf(state));
	}

	/**
	 * @param date inserted date that has to be checked
	 * @return true if it is after the actual date
	 */
	public boolean isAfterActualDate(String date){
		if (getLocalDate(date).isAfter(LocalDate.now())){
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param date1 start date
	 * @param date2 end date
	 * @return true if start is after end date
	 */
	public boolean dateIsAfterOtherDate(String date1, String date2){
		if (getLocalDate(date1).isAfter(getLocalDate(date2))){
			return true;
		}
		return false;
	}

	/**
	 * validates the string inputs and casts them to the right objects
	 * @param tour Tour-template entity
	 * @param userAccount Tourguide that will guide the tour
	 * @param form other inputs like dates and priceCategory, Status
	 * @return a ConcreteTour
	 * @throws ParseException if it cannot be cast to the desired dates
	 */
	public ConcreteTour validateForm(Tour tour, UserAccount userAccount, ConcreteTourForm form) throws ParseException {
		Assert.notNull(tour,"Tour must not be null");
		Assert.notNull(userAccount, "UserAccount must not be null");
		Assert.notNull(form.getStart(), "Start date must not be null.");
		Assert.notNull(form.getEnd(), "End Date must not be null");
		Assert.notNull(form.getPrice(), "Price must not be null");
		Assert.notNull(form.getPriceCategory(), "Price Category must not be null");
		Assert.notNull(form.getStatus(), "Status must not be null");

		NumberFormat nf = NumberFormat.getInstance();
		Money price = Money.of(nf.parse(form.getPrice()), Monetary.getCurrency("EUR"));

		ConcreteTour concreteTour = new ConcreteTour(tour, userAccount, getLocalDate(form.getStart()),
				getLocalDate(form.getEnd()),
				price, ConcreteTour.PriceCategory.valueOf(form.getPriceCategory()),
				ConcreteTour.Status.valueOf(form.getStatus()));
		inventory.save(new UniqueInventoryItem(concreteTour, Quantity.of(tour.getCapacity())));
		return concreteTour;
	}

	/**
	 * @return the catalog of concrete Tours
	 */
	public ConcreteTourCatalog getConcreteTourCatalog() {
		return this.concreteTourCatalog;
	}

	/**
	 * @param productIdentifier id of the tour that should be given
	 * @return a concrete Tour by id
	 */
	public ConcreteTour getConcreteTourById(ProductIdentifier productIdentifier){
		return this.concreteTourCatalog.findById(productIdentifier).get();
	}

	/**
	 * @param id of the concrete tour
	 * @return all resupply stations
	 */
	public ArrayList<Resupply> getAllResupplies(ProductIdentifier id){
		ArrayList<Resupply> resupplies = new ArrayList<>();
		for(Long l : this.concreteTourCatalog.findById(id).get().getTour().getResupplyLocations()){
			if(resupplyManager.isActive(l)) {
				resupplies.add(resupplyManager.findResupplyById(l).get());
			}
		}
		return resupplies;
	}

	/**
	 * @param productIdentifier to get the tour
	 * @return the tour
	 */
	public Optional<ConcreteTour> findById(ProductIdentifier productIdentifier) {
		return concreteTourCatalog.findById(productIdentifier);
	}

	/**
	 * sets all the tours that have a start date before the actual date to inactive
	 */
	public void validateDate(){
		for ( ConcreteTour tour : getConcreteTourCatalog().findAll().toList()){
			if (tour.getStart().isBefore(LocalDate.now().plusDays(1))){
				tour.setStatus(ConcreteTour.Status.INACTIVE);
			}
		}
	}

}
