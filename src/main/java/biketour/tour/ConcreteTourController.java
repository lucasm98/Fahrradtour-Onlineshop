package biketour.tour;

import biketour.booking.BookingManager;
import biketour.insurancecase.InsuranceCaseManager;
import biketour.user.UserManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;

/**
 * handles all mapping for concrete tours
 * @author julian
 */
@Controller
public class ConcreteTourController {

	private final ConcreteTourManager concreteTourManager;
	private final TourManager tourManager;
	private final UserManager userManager;
	private final InsuranceCaseManager insuranceCaseManager;
	private final BookingManager bookingManager;

	ConcreteTourController(ConcreteTourManager concreteTourManager, TourManager tourManager,
						   UserManager userManager, InsuranceCaseManager insuranceCaseManager,
						   BookingManager bookingManager){
		Assert.notNull(concreteTourManager, "ConcreteTourManager must not be null.");
		Assert.notNull(tourManager, "TourRepository must not be null.");
		Assert.notNull(userManager, "UserAccountManager must not be null.");
		Assert.notNull(tourManager, "TourManager must not be null.");
		Assert.notNull(bookingManager, "bookingManager must not be null.");

		this.insuranceCaseManager = insuranceCaseManager;
		this.concreteTourManager = concreteTourManager;
		this.tourManager = tourManager;
		this.userManager = userManager;
		this.bookingManager = bookingManager;
	}

	/**
	 * Shows all tours on the web page that have the status active
	 * @param model will never be null
	 * @return the filled tourCatalog.html
	 */
	@GetMapping("/tours")
	public String getCatalog(Model model){
		concreteTourManager.validateDate();
		model.addAttribute("catalog", concreteTourManager.getConcreteTourCatalog().
				findByStatus(ConcreteTour.Status.ACTIVE));
		model.addAttribute("title", "tourManager.catalog.title");
		return "tourCatalog";
	}

	/**
	 * return a detail view for one tour
	 * @param tour tour id that is parsed by the uri
	 * @param model will never be null
	 * @return the view name
	 */
	@GetMapping("/tour/{tour}")
	public String getDetail(@PathVariable ConcreteTour tour, Model model){
		model.addAttribute("tour", tour);
		return "tourDetail";
	}

	/**
	 * @param id of the desired tour
	 * @param state in which the tour will be set
	 * @return tourManager view
	 */
	@PostMapping("/management/setState/{id}")
	public String setState(@PathVariable("id") ProductIdentifier id, @RequestParam("state") String state){
		concreteTourManager.setState(id, state);
		return "redirect:/management/concreteTourManager";
	}

	/**
	 * returns a view of all concrete tours and manage options
	 * @param model will never be null
	 * @return concreteTourManagerView
	 */
	@GetMapping("management/concreteTourManager")
	public String getConcreteTourManager(Model model){
		concreteTourManager.validateDate();
		model.addAttribute("catalog", concreteTourManager.getConcreteTourCatalog().findAll());
		model.addAttribute("title", "tourManager.manage.title");
		return "tourConcreteManager";
	}

	/**
	 * @param model will never be null
	 * @param id of the tour template that will be used to create a concrete tour
	 * @return a form to create a new concrete Tour
	 */
	@GetMapping("management/{id}/concreteTourForm")
	public String getConcreteTourForm(Model model, @PathVariable("id")final long id){
		model.addAttribute("tour", tourManager.findById(id));
		model.addAttribute("tourGuides", userManager.findByRole(Role.of("TOURGUIDE")));
		return "tourConcreteForm";
	}

	/**
	 * @param id of the tour template
	 * @param form all information needed to create a concrete tour
	 * @return a new concrete Tour and redirects to the concreteTourManager
	 * @throws ParseException if date cannot be parsed
	 */
	@PostMapping("management/{id}/concreteTourForm")
	public String createNewConcreteTour(@PathVariable("id") long id,
										@Valid @ModelAttribute("form") ConcreteTourForm form) throws ParseException {
		if (!concreteTourManager.isAfterActualDate(form.getStart()) && !concreteTourManager.
				isAfterActualDate(form.getEnd())){
			return "redirect:/management/" + id + "/concreteTourForm";
		}

		if (concreteTourManager.dateIsAfterOtherDate(form.getStart(), form.getEnd())){
			return "redirect:/management/" + id + "/concreteTourForm";

		}
		concreteTourManager.createConcreteTour(tourManager.findById(id),
				userManager.getUserAccountManager().findByUsername(form.getTourGuide()).get(),form);
		return "redirect:/management/concreteTourManager";
	}

	/**
	 * shows all tours a tour guide is linked to
	 * @param userAccount with role tour guide
	 * @param model will never be null
	 * @return a view with all tours linked to a tour guide
	 */
	@GetMapping("/tourCoach")
	public String coach(@LoggedIn Optional<UserAccount> userAccount, Model model){
		concreteTourManager.validateDate();
		if(userAccount.isEmpty()){
			return "redirect:/";
		}else{
			model.addAttribute("catalog",concreteTourManager.getAllToursFromTourguide(userAccount.get()));
			return "tourCoach";
		}
	}

	/**
	 * @param id of the concreteTour
	 * @param model will never be null
	 * @return all resupply stations of a tour
	 */
	 @GetMapping("/tourResupply/{tour}")
	public String tourResupply(@PathVariable("tour")ProductIdentifier id,Model model){
		model.addAttribute("tour", concreteTourManager.getConcreteTourCatalog().findById(id).get());
		model.addAttribute("catalog", concreteTourManager.getAllResupplies(id));
		if(insuranceCaseManager.findByTour(id).isEmpty()){
			model.addAttribute("bool",false);
		}else{
			model.addAttribute("insuranceCases", insuranceCaseManager.findByTour(id));
			model.addAttribute("bool",true);
		}
		return "tourResupply";
	 }

	/**
	 * @param productIdentifier of the concrete tour that will be canceled
	 * @return the manager view
	 */
	 @PostMapping("management/cancelTour/{id}")
	public String cancelTourPost(@PathVariable("id")ProductIdentifier productIdentifier) {
	 	bookingManager.cancelAllBookingsFromConcreteTour(productIdentifier);
	 	return "redirect:/management/concreteTourManager";
	 }
}
