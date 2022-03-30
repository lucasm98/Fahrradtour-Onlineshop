package biketour.update;

import biketour.user.UserManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UpdateController {

	private final UserManager userManager;

	public UpdateController(UserManager userManager) {
		this.userManager = userManager;
	}


	@PostMapping("/updateUserInformation")
	String postUpdateUserInformation(UserUpdateForm userUpdateForm, @LoggedIn Optional<UserAccount> userAccount) {
		return userAccount.map(account -> {
			userManager.updateUserInformation(account, userUpdateForm);
			return "redirect:/myAccount";
		}).orElse("redirect:/")	;
	}

	@PostMapping("/updatePassword")
	String postUpdatePassword(Model model, PasswordUpdateForm passwordUpdateForm,
							  @LoggedIn Optional<UserAccount> userAccount,
							  final RedirectAttributes redirectAttributes) {
		return userAccount.map(account -> {
			if(userManager.updateUserPassword(account, passwordUpdateForm)) {
				redirectAttributes.addFlashAttribute("passwordUpdateError", "1");
			} else {
				redirectAttributes.addFlashAttribute("passwordUpdateError", "2");
			}
			return "redirect:/myAccount";
		}).orElse("redirect:/");
	}
}
