package biketour.login;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController {

	/**
	 * Method for request of type get. If {@link UserAccount} object is empty there will be access
	 * else index will be loaded.
	 * @param userAccount spring instance of the current user account
	 * @return a String with the name of the loaded page
	 */
	@GetMapping("/login")
	public String loginGet(@LoggedIn Optional<UserAccount> userAccount) {
		return userAccount.isEmpty() ? "login" : "redirect:/";
	}

	/**
	 * @return the index after successful login
	 */
	@PostMapping("/login")
	public String loginPost() {

		return "redirect:/";
	}


}
