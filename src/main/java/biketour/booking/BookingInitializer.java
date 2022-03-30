package biketour.booking;

import biketour.bike.Bike;
import biketour.bike.BikeManager;
import biketour.purchase.Purchase;
import biketour.tour.ConcreteTour;
import biketour.tour.ConcreteTourManager;
import biketour.tour.Tour;
import biketour.tour.TourRepository;
import biketour.user.UserManager;
import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.money.Monetary;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.salespointframework.core.Currencies.EURO;

/**
 * @author Marcel Körner
 */

@Component
@Order(value = 30)
public class BookingInitializer implements DataInitializer {

	private UserManager userManager;
	private ConcreteTourManager concreteTourManager;
	private BookingManager bookingManager;

	BookingInitializer(UserManager userManager, BookingManager bookingManager,
					   ConcreteTourManager concreteTourManager){


		Assert.notNull(userManager, "UserAccount must not be null!");
		Assert.notNull(bookingManager, "BookingManager must not be null");
		Assert.notNull(concreteTourManager, "ConcreteTourManager must not be null");

		this.userManager = userManager;
		this.bookingManager = bookingManager;
		this.concreteTourManager=concreteTourManager;
	}

	@Override
	public void initialize() {

		UserAccount user1 = userManager.findUserAccountByUserName("Frodo95").get();

		ArrayList<String> userNames = new ArrayList<>();
		userNames.add("BH");
		userNames.add("Potto");
		userNames.add("frankguenther1965");
		userNames.add("Hofen");

		for (ConcreteTour concreteTour : concreteTourManager.getConcreteTourCatalog().findAll()){
			for (int i = 0; i < userNames.size(); i++){
				int x = new Random().nextInt(4) + 1;
				Booking booking = bookingManager.createBooking(userManager.findUserAccountByUserName(userNames.get(i)).get(),
						concreteTour,
						Quantity.of(x));
					booking.setPaymentMethod(Cash.CASH);
				bookingManager.checkAndSaveBooking(
						booking,
						Quantity.of(x),
						concreteTour
				);
				if (i%3 == 0){
					bookingManager.payOrder(booking);
				}

			}
		}


		ConcreteTour concreteTour_scenario_older_than_14_days = concreteTourManager.
				findConcreteTourByName("Manchester Tour (more than 14 days ago)");


		Booking booking_scenario_older_than_14_days = bookingManager.createBooking(user1,
				concreteTour_scenario_older_than_14_days,
				Quantity.of(4));


		bookingManager.checkAndSaveBooking(
				booking_scenario_older_than_14_days,
				Quantity.of(4),
				concreteTour_scenario_older_than_14_days
		);
		bookingManager.payOrder(booking_scenario_older_than_14_days);
		bookingManager.completeOrder(booking_scenario_older_than_14_days);

		ConcreteTour concreteTour_less_than_14_days = concreteTourManager.
				findConcreteTourByName("Dublin Tour (less than 14 days ago)");
		Booking booking_scenario_less_than_14_days = bookingManager.createBooking(user1,
				concreteTour_less_than_14_days,
				Quantity.of(2));
		bookingManager.checkAndSaveBooking(
				booking_scenario_less_than_14_days,
				Quantity.of(4),
				concreteTour_less_than_14_days
		);
		bookingManager.payOrder(booking_scenario_less_than_14_days);
		bookingManager.completeOrder(booking_scenario_less_than_14_days);

		ConcreteTour concreteTour_active = concreteTourManager.findConcreteTourByName("Wales Tour (active)");
		Booking booking_scenario_active = bookingManager.createBooking(user1,
				concreteTour_active,
				Quantity.of(2));
		bookingManager.checkAndSaveBooking(
				booking_scenario_active,
				Quantity.of(4),
				concreteTour_active
		);
		bookingManager.payOrder(booking_scenario_active);
		bookingManager.completeOrder(booking_scenario_active);

		System.out.println("Yay");
	}
}