package biketour.booking;

import biketour.bike.Bike;
import biketour.bike.BikeManager;
import biketour.tour.ConcreteTour;
import biketour.tour.ConcreteTourManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.awt.print.Book;
import java.util.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Stream;


/**
 * @author Marcel Körner
 * javadocs by julian moeller ;)
 */

@Component
@Order(value = 5)
public class BookingManager {

	private BikeManager bikeManager;
	@Autowired
	private OrderManager orderManager;
	private ConcreteTourManager concreteTourManager;

	public BookingManager(OrderManager orderManager, ConcreteTourManager concreteTourManager, BikeManager bikeManager){
		this.orderManager = orderManager;
		this.concreteTourManager = concreteTourManager;
		this.bikeManager = bikeManager;
	}

	/**
	 * @return the bookingManager instance
	 */
	public OrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param booking a new booking that will be saved
	 * @return Booking instance
	 */
	public Booking save(Booking booking) {
		return (Booking) this.orderManager.save(booking);
	}

	/**
	 * @param pageable get all objects that can be displayed by the view
	 * @return all bookings
	 */
	public Streamable<Booking> getAllBookings(Pageable pageable) {
		return this.orderManager.findAll(pageable).filter(element -> element.getClass().equals(Booking.class));
	}

	/**
	 * @param userAccount that has booked tours
	 * @return all bookings from a user account
	 */
	public Streamable<Booking> getBookingsFromUserAccount(UserAccount userAccount) {
		return this.orderManager.findBy(userAccount).filter(element -> element.getClass().equals(Booking.class));

	}

	/**
	 * @param orderStatus status of a booking
	 * @return all bookings with given status
	 */
	public Streamable<Booking> getBookingsWithOrderStatus(OrderStatus orderStatus) {
		return this.orderManager.findBy(orderStatus).filter(element -> element.getClass().equals(Booking.class));
	}

	/**
	 * @param identifier id of a booking
	 * @return the booking searched with given id
	 */
	public Booking getBookingsById(OrderIdentifier identifier) {
		return (Booking)this.orderManager.get(identifier).get();
	}

	/**
	 * creates a list of bookings from a streamable
	 * @param bookings steamable of bookings
	 * @return a list of bookings
	 */
	public List<BookingView> getBookingViews(Streamable<Booking> bookings) {
		LinkedList<BookingView> bookingViews = new LinkedList<BookingView>();
		Iterator<Booking> iterator = bookings.iterator();
		while(iterator.hasNext()) {

			Booking booking = iterator.next();
			ConcreteTour concreteTour = concreteTourManager.getConcreteTourById(booking.
					getOrderLine().getProductIdentifier());
			bookingViews.add(new BookingView(
					booking.getId().toString(),
					concreteTour.getName(),
					booking.getUserAccount().getUsername(),
					concreteTour.getStart().toString(),
					concreteTour.getEnd().toString(),
					booking.getOrderLine().getQuantity().toString(),
					booking.getOrderStatus().toString(),
					isBuyable(booking),
					booking.getOrderLine().getPrice().toString(),
					booking.getPaymentMethod().toString().contains("card")?"Überweisung":"Bar",
					booking.getOrderStatus().name()
					));

		}
		return bookingViews;
	}

	public Booking createBooking(UserAccount userAccount, ConcreteTour concreteTour, Quantity quantity) {
		Booking booking = new Booking(userAccount, Cash.CASH);
		ArrayList<Bike> bikes = bikeManager.getBikesForOrder(
				bikeManager.getBikeTypeFromPriceCategory(concreteTour.getPriceCategory()),
				Integer.parseInt(quantity.toString()));
		this.addBikesToBooking(bikes, booking);
		booking.addOrderLine(concreteTour, quantity);
		return booking;
	}

	public Booking checkAndSaveBooking(Booking booking, Quantity quantity, ConcreteTour concreteTour){
		if (booking.bookingSuccessfully(concreteTour, quantity)) {
			concreteTour.subFromFree(Integer.parseInt(quantity.toString()));
			this.save(booking);
		}
		return booking;
	}

	/**
	 * @param userAccount get booking from a specified user
	 * @param productIdentifier from bikes
	 * @return a collection of bikes from a booking
	 */
	public Collection<Bike> getBikesFromBooking(UserAccount userAccount, ProductIdentifier productIdentifier) {

		return this.getBookingsFromUserAccount(userAccount).filter(booking ->
				booking.getId().equals(productIdentifier)).toList().get(0).getActivBikes();
	}

	/**
	 * @param orderIdentifier id of a booking
	 * @return a booking
	 */
	public Optional<Booking> getOrderById(OrderIdentifier orderIdentifier){
		return this.orderManager.get(orderIdentifier);
	}

	/**
	 * sets the bike type to reserved when a tour is started
	 * @param bikes all bikes that are in a tour
	 */
	public void changeBikeTypeToReserved(List<Bike> bikes) {
		Iterator<Bike> iterator = bikes.iterator();
		while(iterator.hasNext()) {
			bikeManager.findBikeById(iterator.next().getId()).get().setBikeStatus(Bike.BikeStatus.RESERVED);
		}
	}

	/**
	 * @param bikes all bikes that are getting the status free
	 */
	public void changeBikeTypeToFree(List<Bike> bikes) {
		Iterator<Bike> iterator = bikes.iterator();
		while(iterator.hasNext()) {
			bikeManager.findBikeById(iterator.next().getId()).get().setBikeStatus(Bike.BikeStatus.AVAILABLE);
		}
	}

	/**
	 * @param bikes that will belong to a unique booking
	 * @param booking that gets bikes
	 */
	public void addBikesToBooking(ArrayList<Bike> bikes, Booking booking) {
		changeBikeTypeToReserved(bikes);
		booking.setBikes(bikes);
	}

	/**
	 * when a booking is canceled the bikes have to be freed again
	 * @param booking the bikes will be freed from
	 */
	public void freeBikesFromBooking(Booking booking) {
		changeBikeTypeToFree((ArrayList<Bike>) booking.getBikes());
		ArrayList<Bike> returnList = new ArrayList<Bike>();
		booking.setBikes(returnList);
	}

	/**
	 * @param orderIdentifier to get the booking that will be canceled
	 */
	public void cancelBooking(OrderIdentifier orderIdentifier) {
		Booking booking = this.getBookingsById(orderIdentifier);
		int addToFree = booking.getOrderLine().getQuantity().getAmount().intValue();
		this.cancelOrder(booking);
		bikeManager.setBikesFree(booking);
		ConcreteTour concreteTour = concreteTourManager.findById(booking.getOrderLine().getProductIdentifier()).get();
		concreteTour.addToFree(addToFree);
		return;
	}

	public boolean isPurchaseBuyableRegardingTime(Booking booking) {
		LocalDate currentDate = LocalDate.now();
		LocalDate tourEndDate = concreteTourManager.getConcreteTourById(booking.getOrderLine().
				getProductIdentifier()).getEnd();
		LocalDate endDate = tourEndDate.plusDays(14);
		if ((currentDate.isEqual(tourEndDate) || currentDate.isAfter(tourEndDate)) &&
						(currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) &&
						(!booking.getActivBikes().isEmpty())){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param booking get the booking to check the dates
	 * @return true if you are allowed to buy the bikes
	 */
	public boolean isBuyable(Booking booking){
		if(isPurchaseBuyableRegardingTime(booking) && booking.getOrderStatus().equals(OrderStatus.COMPLETED)) {
			booking.setBuyable(true);
		} else {
//			freeBikesFromBooking(booking);
			booking.setBuyable(false);
		}
		return booking.getBuyable();
	}

	/**
	 * @param productIdentifier of a concrete Tour
	 * @return all bookings that belong to a concrete Tour
	 */
	public Streamable<Booking> findBookingsByConcreteTourId(ProductIdentifier productIdentifier) {
		return getAllBookings(Pageable.unpaged()).filter(booking -> booking.getOrderLine().
				getProductIdentifier().equals(productIdentifier));
	}

	/**
	 * @param productIdentifier of a concrete Tour
	 * @return all users that have booked the tour
	 */
	public Streamable<UserAccount> getUserAccountsFromConcreteTour(ProductIdentifier productIdentifier) {
		return findBookingsByConcreteTourId(productIdentifier).map(booking -> booking.getUserAccount());
	}

	public void refreshBookingStatus() {
		Streamable<Booking> bookings = this.getBookingsWithOrderStatus(OrderStatus.PAID);
		if(!bookings.isEmpty()) {
			Iterator<Booking> iterator = bookings.filter(booking ->
					concreteTourManager.findById(booking.getOrderLine().getProductIdentifier())
							.get().getEnd().isBefore(LocalDate.now())).iterator();
			while (iterator.hasNext()) {
				this.getOrderManager().completeOrder(iterator.next());
			}
			System.out.println("Booking status refreshed");
		}
	}

	public boolean payOrder(Booking booking) {
		return orderManager.payOrder(booking);
	}

	public boolean cancelOrder(Booking booking) {
		return orderManager.cancelOrder(booking);
	}

	public void completeOrder(Booking booking) {
		orderManager.completeOrder(booking);
	}

	public void cancelAllBookingsFromConcreteTour(ProductIdentifier productIdentifier) {

		Iterator<Booking> iterator = this.getAllBookings(Pageable.unpaged())
				.filter(booking -> booking.getOrderLine().getProductIdentifier().equals(productIdentifier)).iterator();
		while (iterator.hasNext()) {
			this.cancelBooking(iterator.next().getId());
		}
		concreteTourManager.findById(productIdentifier).get().setCanceled();

		System.out.println("Tour wurde gecanceled");
		System.out.println(concreteTourManager.findById(productIdentifier).get().getStatus().toString());
	}
}
