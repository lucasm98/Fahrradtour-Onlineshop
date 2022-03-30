package biketour.register;

import biketour.user.UserManager;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class RegistrationController {

	private final UserManager userManager;

	public RegistrationController(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 *
	 * @param model will never be null
	 * @param registration form to get the needed information to create a new user
	 * @param errors if there are any
	 * @param userAccount checks if there is a user already logged in
	 * @return the register view if there is none logged in
	 */
	@GetMapping("/register")
	String registerGet(Model model, Registration registration, Errors errors,
					   @LoggedIn Optional<UserAccount> userAccount) {
		model.addAttribute("registration", registration);
		return userAccount.isEmpty() ? "register" : "redirect:/";
	}

	/**
	 * @param model will never be null
	 * @param registration form to get the information to create a new user
	 * @param errors if there are any
	 * @return the index view
	 */
	@PostMapping("/register")
	String registerPost(Model model, Registration registration, Errors errors) {
		this.userManager.createUser(registration);
		return "redirect:/";
	}

}
