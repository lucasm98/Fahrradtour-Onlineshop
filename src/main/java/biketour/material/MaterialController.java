package biketour.material;

import biketour.resupply.Resupply;
import biketour.resupply.ResupplyManager;
import com.mysema.commons.lang.Assert;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;


@Controller
public class MaterialController {

	private final MaterialManager manager;
	private final ResupplyManager resupplyManager;

	MaterialController(MaterialManager manager,ResupplyManager resupplyManager) {
		Assert.notNull(resupplyManager,"Manager must not be null");
		Assert.notNull(manager,"Manager must not be null");
		this.manager = manager;
		this.resupplyManager=resupplyManager;
	}


	/**
	 * This Method gives all Material from the Catalog to the Model
	 * @param model will never be null
	 * @return to materialManager.html
	 */
	@GetMapping("/management/materialManager")
	String materialCatalog(Model model) {
		Assert.notNull(model,"Model must not be null");
		model.addAttribute("catalog", manager.findAllUnique());
		model.addAttribute("title", "materialManager.title");
		return "materialManager";
	}

	/**
	 * This Method gives the Material to the Model
	 * @param material that includes the information
	 * @param model will never be null
	 * @return materialDetail.html
	 */
	@GetMapping("/management/material/{material}")
	String materialDetail(@PathVariable Material material, Model model) {
		Assert.notNull(model,"Model must not be null");
		Assert.notNull(material,"Material must not be null");
		model.addAttribute("prim", material);
		model.addAttribute("catalog", manager.findSameMats(material));
		model.addAttribute("resupplyManager", resupplyManager);
		return "materialDetail";
	}

	/**
	 * This Method gives all "VERSORGUNG"-Materials from the Catalog to the Model
	 * @param model will never be null
	 * @return to materialManager.html
	 */
	@GetMapping("/management/material/versorgung")
	String materialVersorgung(Model model) {
		Assert.notNull(model,"Model must not be null");
		model.addAttribute("catalog", manager.findVersorgung());
		return "materialManager";
	}

	/**
	 * This Method gives all "ERSATZTEILE"-Materials from the Catalog to the Model
	 * @param model will never be null
	 * @return to materialManager.html
	 */
	@GetMapping("/management/material/ersatz")
	String materialErsatz(Model model) {
		Assert.notNull(model,"Model must not be null");
		model.addAttribute("catalog", manager.findErsatz());
		return "materialManager";
	}

	/**
	 * This Method is used to get Material from the Catalog with the given Search input
	 * @param search Search input
	 * @param model will never be null
	 * @return to to materialManager.html
	 */
	@PostMapping("/management/materialSearch")
	String search(@RequestParam("search") String search, Model model) {
		Assert.notNull(model,"Model must not be null");
		Assert.notNull(search,"search must not be null");
		model.addAttribute("catalog", manager.findName(search));
		model.addAttribute("search", search);
		return "materialManager";
	}

	/**
	 * Addes an Material to the Catalog with the given Form
	 * @param form   User input
	 * @param result Errors
	 * @return the material manager view
	 */
	@PostMapping("/management/addMaterialToCatalog")
	String materialAddToCatalog(@Valid MaterialForm form, Errors result) {
		Assert.notNull(form,"Form must not be null");
		if (result.hasErrors()) {
			System.out.println("fähler | " + form.getName() + " | " + form.getDescription() + " | "
					+ form.getType() + " | " + form.getQuantity() + " | " + form.getLoc());
			return "materialAdd";
		}
		manager.createMaterial(form);
		return "redirect:/management/materialManager";
	}

	/**
	 * This Method gives the Form to the Model
	 * @param model will never be null
	 * @param form that includes the information necessary to create a new material
	 * @return to materialAdd.html
	 */
	@GetMapping("/management/addMaterial")
	String materialAdd(Model model, MaterialForm form) {
		Assert.notNull(form,"Form must not be null");
		Assert.notNull(model,"Model must not be null");
		model.addAttribute("form", form);
		model.addAttribute("array",resupplyManager.findAllActive());
		return "materialAdd";
	}


	/**
	 * this method loads all equal material to the given one to edit them
	 * @param material material that will be edited
	 * @param model will never be null
	 * @return the edit form to edit the material
	 */
	@GetMapping("/management/materialedit/{material}")
		String editMaterial(@PathVariable Material material, Model model) {
		Assert.notNull(material,"Material must not be null");
		Assert.notNull(model,"Model must not be null");
		System.out.println(material.getName());
		model.addAttribute("material", material);
		for(Resupply resupply:resupplyManager.getAll()){
			resupply.setMaterialCheck(false);

			for(Material material2:manager.findAll()){
				if(resupply.getId()==material2.getLocid()){
					resupply.setMaterialCheck(true);
				}
			}
		}
		model.addAttribute("array",resupplyManager.findAllActive());
		return "materialEdit";
	}

	/**
	 * this methog gets the Material an changes all equal Material with the infos from the Materialform
	 * @param form to get the information to edit the material
	 * @param result find all errors
	 * @param material that will be edited
	 * @return a edited material
	 */
	@PostMapping("/management/editMaterial")
	String editMaterialInCatalog(@Valid MaterialForm form, Errors result, @RequestParam("pid") Material material) {
		Assert.notNull(material,"material must not be null");
		Assert.notNull(form,"form must not be null");
		if (result.hasErrors()) {
			System.out.println("fähler edit| " + form.getName() + " | " + form.getDescription() + " | "
					+ form.getType() + " | " + form.getQuantity() + " | " + material.getLoc());
			return "materialAdd";
		}
		manager.editMaterial(form, material);
		return "redirect:/management/materialManager";
	}


	/**
	 * deletes all equal Materials to the given one
	 * @param material that will be deleted
	 * @return the materialManager view
	 */
	@GetMapping("/management/deleteMaterial/{material}")
	String deleteMaterial(@PathVariable Material material){
		Assert.notNull(material,"Material must not be null");
		System.out.println("Delete : "+material.getName());
		manager.deleteMaterial(material);
		return "redirect:/management/materialManager";
	}


	/**
	 * decreade the Quantity from the given Material
	 * @param material material that will get the quantity decreased
	 * @param prim another material
	 * @return the decreased quantity
	 */
	@GetMapping("/management/decQuantity/{material}/{prim}")
	String decQuantity(@PathVariable Material material, @PathVariable Material prim){
		Assert.notNull(material,"material must not be null");
		Assert.notNull(prim,"prim must not be null");
		manager.decQuantity(material);
			return "redirect:/management/material/"+prim.getId();

	}

	/**
	 * increase the Quantity from the given Material
	 * @param material which will get an increased quantity
	 * @param prim another material
	 * @return the increased quantity
	 */
	@GetMapping("/management/addQuantity/{material}/{prim}")
	String addQuantity(@PathVariable Material material ,@PathVariable Material prim){
		Assert.notNull(material,"material must not be null");
		Assert.notNull(prim,"prim must not be null");
		manager.addQuantity(material);
		return "redirect:/management/material/"+prim.getId();
	}


	/**
	 * loads StockForm and low Materials from Manager
	 * @param model will never be null
	 * @param form to manage the stock
	 * @return the materialStock view
	 */
	@GetMapping("/management/materialStock")
	String stock(Model model, MaterialForm form){
		Assert.notNull(model,"Model must not be null");
		Assert.notNull(form,"Form must not be null");
		model.addAttribute("catalog",manager.getLowMaterials());
		if(manager.getLowMaterials().iterator().hasNext()){
			model.addAttribute("bool",true);
		}else{
			model.addAttribute("bool",false);
		}
		model.addAttribute("form",form);
		model.addAttribute("resupplyManager", resupplyManager);
		return ("materialStock");
	}


	/**
	 * restocks selected material with 3 and loads the Site again
	 * @param model will never be null
	 * @param form to get the stock information
	 * @return the materialstock view
	 */
	@PostMapping("/management/reStock")
	String reStock(Model model,@Valid StockForm form){
		Assert.notNull(form,"Form must not be null");
		Assert.notNull(model,"Model must not be null");
		System.out.println("Sucess add : "+form.toString());
		manager.reStock(form.getIDs());
		if(manager.getLowMaterials().iterator().hasNext()){
			model.addAttribute("bool",true);
		}else{
			model.addAttribute("bool",false);
		}
		model.addAttribute("catalog",manager.getLowMaterials());
		return ("redirect:/management/materialStock");
	}

	@PostMapping("/saveRequest")
	String saveRequest(@RequestParam("pid") Optional<Material> material,
					   @RequestParam("quantity")Optional<String> quantity){
			Assert.notNull(material,"Material must not be null");
			Assert.notNull(quantity,"Quantity must not be null");
			manager.createRequest(Integer.parseInt(quantity.get()),material.get());
		System.out.println("MaterialRequest : "+material.get().getName()+"   "+quantity.get()+" mal");
		return "redirect:/management/resupplyStation/"+material.get().getLocid();
	}

	@GetMapping("/management/materialRequest")
	String materialRequest(Model model){
		model.addAttribute("list",manager.getAllRequests());


		return "materialRequest";
	}

//	@PostMapping("/management/materialRequest")
//	String finishRequest(@RequestParam("id")Optional<String> materialRequest){
//		System.out.println("Finish : "+materialRequest);
//		manager.toggel(Integer.parseInt(materialRequest.get()));
//		return "redirect:/management/materialRequest";
//	}

	@PostMapping("/management/materialRequest/confirm/{id}")
	String confirmRequest(@PathVariable("id")Optional<Integer> id) {

		MaterialRequest materialRequest = manager.getAllRequests().get(id.get());
		if(materialRequest.getStatus() == MaterialRequest.Status.OPEN) {
			materialRequest.setStatus(MaterialRequest.Status.CONFIRMED);
		}
		return "redirect:/management/materialRequest";
	}

	@PostMapping("/management/materialRequest/deny/{id}")
	String denyRequest(@PathVariable("id")Optional<Integer> id) {

		MaterialRequest materialRequest = manager.getAllRequests().get(id.get());
		if(materialRequest.getStatus() == MaterialRequest.Status.OPEN) {
			materialRequest.setStatus(MaterialRequest.Status.DENIED);
		}
		return "redirect:/management/materialRequest";
	}

	@PostMapping("/management/materialRequest/undo/{id}")
	String undoRequest(@PathVariable("id")Optional<Integer> id) {

		MaterialRequest materialRequest = manager.getAllRequests().get(id.get());
		if(materialRequest.getStatus() == MaterialRequest.Status.CONFIRMED) {
			materialRequest.setStatus(MaterialRequest.Status.OPEN);
		} else if (materialRequest.getStatus() == MaterialRequest.Status.DENIED) {
			materialRequest.setStatus(MaterialRequest.Status.OPEN);
		}
		return "redirect:/management/materialRequest";
	}




}
