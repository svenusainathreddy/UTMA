package hospital.HospitalWebApp2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hospital.HospitalWebApp2.model.User;
import hospital.HospitalWebApp2.service.RegisterService;

@Controller
public class RegisterController {

	@Autowired
	private RegisterService regService;

	@GetMapping("/index")
	public String showIndex() {
		return "index";
	}

	// Saves New User into databasea
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, Model model) {
	    	System.out.println(user.isStaffMember());
		try {
			regService.registerUser(user);
			return "redirect:/index";
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "register";
		}
	}

	// Opens Registration Page
	@GetMapping("/register")
	public String showForm(Model model, @RequestParam(value = "success", required = false) String success) {
		if (!model.containsAttribute("user")) {
			model.addAttribute("user", new User());
		}
		if (success != null) {
			model.addAttribute("successMessage", "Registration successful!");
		}
		return "register";
	}

}
