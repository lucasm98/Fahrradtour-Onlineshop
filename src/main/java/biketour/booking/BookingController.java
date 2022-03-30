package biketour.booking;

import biketour.bike.Bike;
import biketour.bike.BikeManager;
import biketour.tour.ConcreteTour;
import biketour.tour.ConcreteTourManager;
import biketour.user.UserManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.DebitCard;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Marcel Körner
 */
@Controller
@SessionAttributes("order")
public class BookingController {

	private final ConcreteTourManager concreteTourManager;
	private final BookingManager bookingManager;
	private final BikeManager bikeManager;
	private final UserManager userManager;


	public BookingController(BookingManager bookingManager, ConcreteTourManager tourManager,
							 BikeManager bikeManager, UserManager userManager) {
		Assert.notNull(bookingManager, "OrderManager must not be null!");
		Assert.notNull(tourManager, "TourManager must not be null!");
		Assert.notNull(bikeManager, "BikeManager must not be null!");
		Assert.notNull(userManager, "BikeManager must not be null!");
		this.bookingManager = bookingManager;
		this.concreteTourManager = tourManager;
		this.bikeManager = bikeManager;
		this.userManager = userManager;
	}


	/**
	 * @param model  will never be null
	 * @param tourId from the tour that will be booked
	 * @return all information from a tour needed to display for a booking
	 */
	@GetMapping("/booking/{id}")
	String bookingGet(Model model, @LoggedIn Optional<UserAccount> userAccount,
					  @PathVariable("id") Optional<ProductIdentifier> tourId) {
		bookingManager.refreshBookingStatus();

		System.out.println(userAccount.get().getRoles().toList().get(0));
		if (userAccount.isEmpty() || !userAccount.get().hasRole(Role.of("CUSTOMER")) ||
				concreteTourManager.getConcreteTourById(tourId.get()).getFree() == 0) {
			return "redirect:/";
		}
		return tourId.map(id -> {
			model.addAttribute("tour", concreteTourManager.findById(id).get());
			return "booking";
		}).orElse("redirect:/");
	}

	/**
	 * @param model       will never be null
	 * @param userAccount has to be 'BOSS' to see view
	 * @return a manager view with all bookings
	 */
	@GetMapping("/management/bookingManager")
	String BookingStatus(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		bookingManager.refreshBookingStatus();
		return userAccount.map(account -> {
			if (account.hasRole(Role.of("BOSS"))) {
				model.addAttribute("bookingViews", bookingManager.getBookingViews
					(bookingManager.getAllBookings(Pageable.unpaged())));
			}else{
				return "redirect:/";
			}
			return "bookingManager";
		}).orElse("redirect:/");
	}

	/**
	 * @param userAccount from user that will book the tour
	 * @param quantity    of person that will take the tour
	 * @param tourId      id of the tour that will be booked
	 * @return a view for my bookings
	 */
	@PostMapping("/booking/{id}")
	String bookingPost(@LoggedIn Optional<UserAccount> userAccount,
					   @RequestParam(name = "quantity") Optional<String> quantity,
					   @PathVariable("id") Optional<ProductIdentifier> tourId,
					   @RequestParam("ids")Optional<String> name) {
		if (userAccount.isPresent() && quantity.isPresent() && tourId.isPresent()) {
			if(concreteTourManager.getConcreteTourById(tourId.get()).getFree() == 0) {
				return "redirect:/";
			}
			Booking booking = new Booking(userAccount.get());
			ConcreteTour concreteTour = concreteTourManager.getConcreteTourById(tourId.get());
			ArrayList<Bike> bikes = bikeManager.getBikesForOrder(
					bikeManager.getBikeTypeFromPriceCategory(concreteTour.getPriceCategory()),
					Integer.parseInt(quantity.get()));
			bookingManager.addBikesToBooking(bikes, booking);
			booking.setPaymentMethod(name.get().contains("card")? new DebitCard(
				"FlyingCard", "123456789",
				userAccount.get().getFirstname()+" "+userAccount.get().getLastname(),
				userManager.findUserByUserAccount(userAccount.get()).get().getAddress().toString(),
				LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(300),
				"wrhfbwurht547f34", Money.of(100,"EUR") ) : Cash.CASH);


			if (booking.bookingSuccessfully(concreteTour, Quantity.of(Integer.parseInt(quantity.get())))) {
				concreteTourManager.findById(tourId.get()).get().subFromFree(Integer.parseInt(quantity.get()));
				bookingManager.save(booking);
			} else {
				return "redirect:/";
			}
			return "redirect:/myBooking";
		} else {
			return "redirect:/";
		}
	}

	/**
	 * @param orderIdentifier to find the order that will be canceled
	 * @return my bookings view
	 */
	@PostMapping("/cancelBooking/{id}")
	public String cancelBooking(@PathVariable("id") Optional<OrderIdentifier> orderIdentifier) {
		bookingManager.cancelBooking(orderIdentifier.get());
		return "redirect:/myBooking";
	}

	@PostMapping("management/bookingManager/cancel/{id}")
	public String cancelBookingAdmin(@PathVariable("id") Optional<OrderIdentifier> orderIdentifier) {
		bookingManager.cancelBooking(orderIdentifier.get());
		return "redirect:/management/bookingManager";
	}

	/**
	 * @param model       will never be null
	 * @param userAccount who gets all his bookings listed
	 * @return a view with all bookings by a user
	 */
	@GetMapping("/myBooking")
	public String myBookingGet(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		return userAccount.map(account -> {
			model.addAttribute("enoughCredits", userManager.getCreditsOfUser(userAccount.get()) >= 3 ? true : false);
			model.addAttribute("bookingViews", bookingManager.getBookingViews(
					bookingManager.getBookingsFromUserAccount(account)));
			model.addAttribute("title", "bookingList.title");
//			model.addAttribute("bookingManager", bookingManager);
			return "bookingList";
		}).orElse("redirect:/");
	}

	@PostMapping("/management/bookingManager/makePaid/{id}")
	public String postBookingManagerMakePaid(@LoggedIn Optional<UserAccount> userAccount,
											 @PathVariable("id") Optional<OrderIdentifier> orderIdentifier) {
		return userAccount.map(account -> {
			Booking booking = bookingManager.getBookingsById(orderIdentifier.get());
			if(booking.getOrderStatus() == OrderStatus.OPEN) {
				bookingManager.payOrder(booking);
			}
			return "redirect:/management/bookingManager";
		}).orElse("redirect:/");
	}


}