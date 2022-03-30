package biketour.insurancecase;

import biketour.booking.Booking;
import biketour.booking.BookingManager;
import biketour.tour.ConcreteTourManager;
import com.mysema.commons.lang.Assert;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.QUserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class InsuranceCaseController {

	@Autowired
	private final InsuranceCaseManager insuranceCaseManager;
	private final ConcreteTourManager concreteTourManager;
	private final BookingManager bookingManager;
	private final UserAccountManager userAccountManager;

	public InsuranceCaseController(InsuranceCaseManager insuranceCaseManager,
								   ConcreteTourManager concreteTourManager,
								   BookingManager bookingManager,
								   UserAccountManager userAccountManager) {
		Assert.notNull(insuranceCaseManager, "InsuranceCaseManager must not be null!");
		Assert.notNull(concreteTourManager, "ConcreteTourManager must not be null!");

		this.insuranceCaseManager = insuranceCaseManager;
		this.concreteTourManager = concreteTourManager;
		this.bookingManager = bookingManager;
		this.userAccountManager = userAccountManager;
	}

	/**
	 * @param model will never be null
	 * @return a view of all insurance cases
	 */
	@GetMapping("/management/insuranceCaseManager")
	public String insuranceCaseManagerGet(Model model) {
		model.addAttribute("insuranceCases", insuranceCaseManager.findAll());

		System.out.println(insuranceCaseManager.findAll().toList().size());

		return "insuranceCaseManager";
	}

	/**
	 * form to create a new insurance case
	 * @param id of a concrete tour
	 * @param model will never be null
	 * @return a form to fill in the details of an insurance case
	 */
	@GetMapping("/insuranceCaseForm/{id}")
	public String insuranceCaseFormGet(@PathVariable("id") ProductIdentifier id, Model model) {
		System.out.println("Insurance : "+id);
		model.addAttribute("concreteTour", concreteTourManager.findById(id).get());
		model.addAttribute("users", bookingManager.getUserAccountsFromConcreteTour(id));
		return "insuranceCaseForm";
	}

	/**
	 * @param description text that is neccessary to create an insurance case
	 * @param id of the concrete tour
	 * @param userName of the user that belongs to the insurance case
	 * @return creates a new insurance case and redirects the view to the index
	 */
	@PostMapping("/insuranceCaseForm/{id}")
	public String createNewCase(@RequestParam("description") Optional<String> description,
								@PathVariable("id")Optional<ProductIdentifier> id,
								@RequestParam("userName") Optional<String> userName) {

		insuranceCaseManager.createInsuranceCase(
				concreteTourManager.findById(id.get()).get(),
				userAccountManager.findByUsername(userName.get()).get(),
				LocalDate.now(),
				description.get());
		return "redirect:/";
	}
}
