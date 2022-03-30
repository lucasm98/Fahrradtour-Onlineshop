package biketour.user;

import biketour.update.UserUpdateForm;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Marcel Körner
 */
@Controller
public class UserController {

	private final UserManager userManager;

	public UserController(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 *
	 * @param model will never be null
	 * @return a manager view with all users
	 */
	@GetMapping("management/userManager")
	String userManagerGet(Model model) {
		model.addAttribute("userList", userManager.findAll());
		return "userManager";
	}

	/**
	 * manages the users
	 * @param userName is like the id for a user
	 * @return the usermanager if logged in as an admin
	 */
	@PostMapping("management/userManager")
	String userManagerPost(@RequestParam("userName") Optional<String> userName) {

		return userName.map(uname -> {

			if(userManager.isTourGuide(userManager.findUserAccountByUserName(uname).get())) {
				userManager.removeRoleTourGuide(userManager.findUserAccountByUserName(uname).get());
			} else {
				userManager.addRoleTourGuide(userManager.findUserAccountByUserName(uname).get());
			}
			return "redirect:/management/userManager";
		}).orElse("/management/userManager");
	}

	@GetMapping("myAccount")
	public String getAccount(Model model, @LoggedIn Optional<UserAccount> userAccount,
							 UserUpdateForm userUpdateForm, RedirectAttributes redirectAttributes) {
		if(!userAccount.isPresent()) {
			return "redirect:/";
		}
		model.addAttribute("user", userManager.findUserByUserAccount(userAccount.get()).get());
		model.addAttribute("userUpdateForm", userUpdateForm);
		redirectAttributes.addFlashAttribute("userUpdateForm", redirectAttributes.getFlashAttributes().get("userUpdateForm"));
		model.addAttribute("userCredits", userManager.getCreditsOfUser(userAccount.get()));
		return "account";
	}

	@GetMapping("/management/userManager/{username}")
	public String getUserOverview(Model model, @LoggedIn Optional<UserAccount> userAccount,
								  @PathVariable("username") Optional<String> username){
		model.addAttribute("user", userManager.findUserByUserAccount(userManager.
				findUserAccountByUserName(username.get()).get()).get());

		return "userOverview";
	}
}
