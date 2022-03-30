package biketour.resupply;

import biketour.material.Material;
import biketour.material.MaterialCatalog;
import biketour.material.MaterialManager;
import com.mysema.commons.lang.Assert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;

@Controller
public class ResupplyController {

	private final ResupplyCatalog catalog;
	private final ResupplyManager resupplyManager;
	private final MaterialManager materialManager;

	public ResupplyController(ResupplyCatalog catalog, ResupplyManager resupplyManager, MaterialManager materialManager) {
		this.catalog = catalog;
		this.resupplyManager = resupplyManager;
		this.materialManager = materialManager;
	}

	/**
	 * Displays all Resupply Stations
	 *
	 * @param model holds attributes
	 * @return resupplyManager.html
	 */
	@GetMapping("/management/resupplyManager")
	String resupplyCatalog(Model model) {

		model.addAttribute("catalog", catalog.findAll());
		model.addAttribute("title", "resupplyManager.title");
		return "resupplyManager";
	}

	/**
	 * Displays the details of a specific Resupply Station
	 *
	 * @param id    of the given Resupply Station
	 * @param model holds attributes
	 * @return resupplyDetails.html
	 */
	@GetMapping("/management/resupplyStation/{id}")
	public String resupplyDetails(@PathVariable("id") Long id, Model model) {

		Assert.notNull(model, "Model must not be null");
		model.addAttribute("id", id);
		Resupply resupply = resupplyManager.findResupplyById(id).get();
		if (resupplyManager.findResupplyById(id).isPresent()) {

			model.addAttribute("name", resupply.getName());
			model.addAttribute("address", resupply.getAddress());
			model.addAttribute("id", resupply.getId());
			model.addAttribute("isEnabled", resupply.getEnabled());
			if (resupply.getEnabled()) {
				model.addAttribute("materialCatalog", materialManager.findByLoc(resupply.getId()));
			}

		}
		return "resupplyDetails";
	}

	/**
	 * Form to create a new Resupply Station
	 *
	 * @param model        holds attributes
	 * @param resupplyForm to get the name and address of the new Resupply Station
	 * @return resupplyForm.html
	 */
	@GetMapping("/management/resupplyManager/resupplyForm")
	public String getResupplyForm(Model model, ResupplyForm resupplyForm) {

		model.addAttribute("resupplyForm", resupplyForm);
		model.addAttribute("title", "resupplyManager.create.title");
		return "resupplyForm";
	}

	/**
	 * Creates a new Resupply Station using the given data
	 *
	 * @param resupplyForm containing name and address of the new Resupply Station
	 * @param result       if there are any errors
	 * @return to resupplyManager; returns to resupplyForm in case of errors
	 * @throws ParseException if the given information cannot be parsed to a new resupply station
	 */
	@PostMapping("/management/resupplyManager/resupplyForm")
	public String createResupply(@Valid ResupplyForm resupplyForm, Errors result) throws ParseException {
		if (result.hasErrors()) {
			return "/management/resupplyManager/resupplyForm";
		}
		resupplyManager.createResupply(resupplyForm);
		return "redirect:/management/resupplyManager";
	}

	/**
	 * Toggles the state from active to inactive and vice versa
	 *
	 * @param id of the given Resupply Station
	 * @return to resupplyManager
	 */
	@PostMapping("/management/resupplyManager/toggleState/{id}")
	public String toggleState(@PathVariable("id") long id) {
		resupplyManager.toggleState(id);
		return "redirect:/management/resupplyManager";
	}

	/**
	 * Form to edit an existing Resupply Station
	 *
	 * @param id    of the existing Resupply Station
	 * @param model holds attributes
	 * @return resupplyEditForm.html
	 */
	@GetMapping("/management/resupplyManager/editResupply/{id}")
	public String getResupplyEditForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("resupply", resupplyManager.findResupplyById(id).get());
		model.addAttribute("id", id);
		return "resupplyEditForm";
	}

	/**
	 * Edits an existing Resupply Station using the given data
	 *
	 * @param id           of the existing Resupply Station
	 * @param resupplyForm containing the new name and address of the existing Resupply Station
	 * @param result       if there are any errors
	 * @return to resupplyManager; returns to resupplyForm in case of errors
	 * @throws ParseException if the given information cannot be parsed to a resupply station
	 */
	@PostMapping("/management/resupplyManager/editResupply/{id}")
	public String editResupply(@PathVariable("id") long id, @Valid ResupplyForm resupplyForm,
							   Errors result) throws ParseException {
		if (result.hasErrors()) {
			return "/management/resupplyManager/editResupply/{id}";
		}
		resupplyManager.editResupply(id, resupplyForm);
		return "redirect:/management/resupplyManager";
	}

}
