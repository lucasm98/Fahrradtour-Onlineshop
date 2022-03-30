package biketour.bike;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BikeController {

	private final BikeManager bikeManager;

	public BikeController(BikeManager bikeManager) {
		this.bikeManager = bikeManager;
	}

	/**
	 * @param model will never be null
	 * @return a view of all available bikes
	 */
	@GetMapping("/management/bikeManager")
	public String bikeManagerGet(Model model){
		model.addAttribute("bikes", bikeManager.findAll());

		return "bikeManager";
	}

	@PostMapping("management/addBikes")
	public String createNewBikes(@RequestParam("quantity") int quantity, @RequestParam("bikeType")String type){
		bikeManager.createBikeByQuantityAndType(quantity, type);
		return "redirect:/management/bikeManager";
	}

	/**
	 * @return the view of an ebike
	 */
	@GetMapping("/ebike")
	public String ebike(){
		return "ebike";
	}

	/**
	 * @return the view of a normal bike
	 */
	@GetMapping("/bike")
	public String bike(){
		return "bike";
	}

}


