package biketour.purchase;

import biketour.bike.BikeManager;
import biketour.bike.BikeForm;
import biketour.booking.BookingManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@SessionAttributes("order")
class PurchaseController {

	private final BookingManager bookingManager;
	private final PurchaseManager purchaseManager;
	private final BikeManager bikeManager;

	PurchaseController(PurchaseManager purchaseManager, BikeManager bikeManager, BookingManager bookingManager) {
		Assert.notNull(purchaseManager, "PurchaseManager must not be null!");
		Assert.notNull(bikeManager, "BikeManager must not be null!");
		Assert.notNull(bookingManager, "BookingManager must not be null!");

		this.purchaseManager = purchaseManager;
		this.bikeManager = bikeManager;
		this.bookingManager = bookingManager;
	}

	/**
	 * validates the role of the logged in person and shows their orders
	 *
	 * @param model       model
	 * @param userAccount account which created the order
	 * @return purchaseManager
	 */
	@GetMapping("/purchaseManager")
	String OrderStatus(Model model, @LoggedIn Optional<UserAccount> userAccount) {

		return userAccount.map(account -> {

			if (account.hasRole(Role.of("BOSS"))) {
				model.addAttribute("purchasesViews", purchaseManager.getPurchaseViewFromStreamable());
			} else if (account.hasRole(Role.of("USER"))) {
				return "redirect:/";
			}
			return "purchaseManager";
		}).orElse("redirect:/");
	}

	/**
	 * @param model       will never be null
	 * @param userAccount has to be logged in
	 * @param id          of the bikes
	 * @param bikeForm    saves the bike ids
	 * @return the checkBike view
	 */
	@GetMapping("/checkBike/{id}")
	String checkBikeGet(Model model,
						@LoggedIn Optional<UserAccount> userAccount,
						@PathVariable("id") Optional<ProductIdentifier> id, BikeForm bikeForm) {
		if (!bookingManager.getBikesFromBooking(userAccount.get(), id.get()).isEmpty()) {
			model.addAttribute("bikes", bookingManager.getBikesFromBooking(userAccount.get(), id.get()));
			model.addAttribute("bool", true);
		} else {
			model.addAttribute("bool", false);
		}
		model.addAttribute("form", bikeForm);
		model.addAttribute("booking", id);
		return "checkBike";
	}

	/**
	 * @param model will never be null
	 * @param userAccount user that has bought the bikes
	 * @return a view of all purchased bikes
	 */
	@GetMapping("/myPurchases")
	String myPurchasesGet(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		return userAccount.map(account -> {
			model.addAttribute("purchasesViews", purchaseManager.getPurchaseViewFromUserAccount(userAccount.get()));

			return "purchaseList";
		}).orElse("redirect:/");
	}

}