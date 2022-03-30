package biketour;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Marcel Körner
 */
@Controller
public class ManagementController {

	/**
	 * @return the management if logged in as boss
	 */
	@GetMapping("/management")
	public String managementGet() {
		return "management";
	}

	@GetMapping("/howto")
	public String howtoGet() { return "howto";}


}
