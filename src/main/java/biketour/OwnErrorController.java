package biketour;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class OwnErrorController implements ErrorController {

	@GetMapping("/error")
	public String getErrorPage(HttpServletRequest request, Model model){
		//model.addAttribute("errorNumber", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
