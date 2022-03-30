package biketour.purchase;

import biketour.bike.Bike;
import biketour.bike.BikeManager;
import biketour.booking.Booking;
import biketour.tour.ConcreteTourManager;
import biketour.user.UserManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Component
public class PurchaseManager {

	@Autowired
	private OrderManager orderManager;
	private ConcreteTourManager concreteTourManager;
	private BikeManager bikeManager;

	public PurchaseManager(OrderManager orderManager,BikeManager bikeManager) {
		this.orderManager = orderManager;
		this.bikeManager = bikeManager;

	}

	/**
	 * @return the orderManager instance
	 */
	public OrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param pageable objects that equals the entity purchase
	 * @return all purchases
	 */
	public Streamable<Purchase> getAllPurchases(Pageable pageable) {
		return this.orderManager.findAll(pageable).filter(element -> element.getClass().equals(Purchase.class));
	}

	public List<PurchaseView> getPurchaseViewFromStreamable(){
		List<PurchaseView> purchaseView = new LinkedList<PurchaseView>();
		Iterator<Purchase> iterator = this.getAllPurchases(Pageable.unpaged()).iterator();
		while(iterator.hasNext()) {
			Purchase purchase = iterator.next();
			ArrayList<Bike> bikes = new ArrayList<>();
			for(OrderLine line : purchase.getOrderLines()){
				System.out.println(line.toString());
				bikes.add(bikeManager.findBikeById(line.getProductIdentifier()).get());
			}
			int points = purchase.getPoints();
			purchaseView.add(new PurchaseView(
				purchase.getId().toString(),
				bikes,
				LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
					.withLocale(Locale.GERMAN)),
				purchase.getUserAccount().getUsername(),
				Integer.toString(points),
				Double.toString(round(purchase.getTotal().getNumber().doubleValue()*(1-(0.1*points)),2)),
				purchase.getPaymentMethod().toString().contains("card")?"card":"cash",
				purchase.getOrderStatus().toString()));
		}
		return purchaseView;
	}

	/**
	 * @param userAccount user accounts that has purchases
	 * @return all purchases of a user
	 */
	public Streamable<Purchase> getPurchasesFromUserAccount(UserAccount userAccount) {
		return this.orderManager.findBy(userAccount).filter(element -> element.getClass().equals(Purchase.class));
	}

	/**
	 * @param orderStatus the status of an order
	 * @return all purchases with the given status
	 */
	public Streamable<Purchase> getPurchasesWithOrderStatus(OrderStatus orderStatus) {

		return this.orderManager.findBy(orderStatus).filter(element -> element.getClass().equals(Purchase.class));
	}

	/**
	 * @param userAccount user that has bought the bikes
	 * @param paymentMethod chosen payment method by the user
	 * @param points used by the user
	 * @param booking booking that has the user made in advance
	 * @param bikes that will be bought
	 */
	public void saveBikes(UserAccount userAccount, PaymentMethod paymentMethod, int points,
						  Booking booking, ArrayList<Bike> bikes){
		Purchase purchase =new Purchase(userAccount,paymentMethod,points,booking,bikes);
		System.out.println("Purchase erstellt:"+purchase.getId());
		purchase.getAllChargeLines().get();
		orderManager.save(purchase);
	}

	/**
	 * @param value given number that will be rounded
	 * @param places decimal places to which it will be rounded
	 * @return a rounded double
	 */
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * @param userAccount the user that wants to see his purchases
	 * @return a view of all his purchases
	 */
	public List<PurchaseView> getPurchaseViewFromUserAccount(UserAccount userAccount) {
		List<PurchaseView> purchaseView = new LinkedList<PurchaseView>();
		Iterator<Purchase> iterator = this.getPurchasesFromUserAccount(userAccount).iterator();
		while(iterator.hasNext()) {
			Purchase purchase = iterator.next();
			ArrayList<Bike> bikes = new ArrayList<>();
			for(OrderLine line : purchase.getOrderLines()){
				System.out.println(line.toString());
				bikes.add(bikeManager.findBikeById(line.getProductIdentifier()).get());
			}
			int points = purchase.getPoints();
			System.out.println("Wo sind die pointS?"+points);
			purchaseView.add(new PurchaseView(
					purchase.getId().toString(),
					bikes,
					LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
							.withLocale(Locale.GERMAN)),
					userAccount.getUsername(),
				   	Integer.toString(points),
					Double.toString(round(purchase.getTotal().getNumber().doubleValue()*(1-(0.1*points)),2)),
					purchase.getPaymentMethod().toString().contains("card")?"card":"cash",
					purchase.getOrderStatus().toString()));
		}
		return purchaseView;
	}
}