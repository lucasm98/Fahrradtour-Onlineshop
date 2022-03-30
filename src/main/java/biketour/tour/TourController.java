package biketour.tour;

import org.hibernate.validator.constraints.Range;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.text.ParseException;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Includes all views that interact with tours
 * @author julian
 */
@Controller
public class TourController {

	private final TourManager tourManager;
	private final BusinessTime businessTime;

	TourController(ConcreteTourCatalog catalog, TourManager tourManager,BusinessTime businessTime){

		Assert.notNull(catalog, "TourCatalog must not be null.");

		this.tourManager = tourManager;
		this.businessTime = businessTime;
	}

	/**
	 * tour Manager that is only accessible by the admin can see all tours
	 * @param model will never be null
	 * @return the view name
	 */
	@GetMapping("management/tourManager")
	public String getManager(Model model){
		model.addAttribute("repository", tourManager.getRepository().findAll());
		model.addAttribute("title", "tourManager.manage.title");
		return "tourManager";
	}

	/**
	 * form to create a tour only accessible by the admin
	 * @param model is never null
	 * @param form special object to get the data needed for a new tour
	 * @return tour form view
	 */
	@GetMapping("management/tourForm")
	public String getTourForm(Model model, TourForm form){
		model.addAttribute("form", form);
		model.addAttribute("title", "tourManager.create.title");
		return "tourForm";
	}

	/**
	 * @param model will never be null
	 * @param id of the tour
	 * @return a filled form with the previous data
	 */
	@GetMapping("/management/{id}/update")
	public String updateTour(Model model, @PathVariable("id") long id){
		model.addAttribute("tour", tourManager.getRepository().findById(id).get());
		return "tourEdit";
	}

	/**
	 * @param id of the tour
	 * @param form with all the new data to update the tour
	 * @return all tours with the new edited tour
	 */
	@PostMapping("/management/{id}/update")
	public String saveUpdatedTour(@PathVariable long id ,@Valid @ModelAttribute("form") TourForm form){
		tourManager.editTour(form, id);
		return "redirect:/management/tourManager";
	}

	/**
	 * handler for the data inserted by admin for a new tour
	 * @param form with filled data
	 * @param results if some fields are not filled or with wrong data errors will be shown
	 * @return if errors happen back to tour form if all correct go to all tours
	 * @throws ParseException if date input cannot be parsed to date object
	 */
	@PostMapping("management/tourForm")
	public String createTourFromForm(@Valid @ModelAttribute("form") TourForm form, Errors results)
			throws ParseException {
		/**if (results.hasErrors()){
			return "tourForm";
		}**/
		tourManager.createTour(form);
		return "redirect:/management/tourManager";
	}

	/**
	@PostMapping("/tour/{id}/comments")
	public String createComment(@PathVariable("id") ProductIdentifier id, @Valid CommentAndRating payload){
		concreteTourManager.getCatalog().findById(id).orElseThrow(NullPointerException::new).
	addComment(payload.toComment(businessTime.getTime()));
		concreteTourManager.getCatalog().save(concreteTourManager.getCatalog().findById(id).
	orElseThrow(NullPointerException::new));
		return "redirect:/tour/" + id;
	}*/
}
