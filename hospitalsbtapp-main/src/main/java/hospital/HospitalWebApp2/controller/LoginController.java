package hospital.HospitalWebApp2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hospital.HospitalWebApp2.model.User;
import hospital.HospitalWebApp2.service.LoginService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	LoginService loginservice;

	// opens Login Page
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		// Fetch user by username
		User user = loginservice.checkUser(username);

		// If user doesn't exist
		if (user == null) {
			model.addAttribute("errorMessage", "User not found");
			return "login";
		}

		// Validate the login
		boolean isValid = loginservice.validateLogin(user, password);

		if (!isValid) {
			// If account is locked or password is wrong
			if (user.isAccountLocked()) {
				model.addAttribute("errorMessage",
						"Your account is locked due to too many failed attempts. Please try again after 1 Minute.");
			} else {
				model.addAttribute("errorMessage", "Incorrect username or password.");
			}
			return "login";
		}

		// If successful login

		if (!user.isStaffMember()) {
			session.setAttribute("username", user.getUsername());
			session.setAttribute("email", user.getEmail());
			model.addAttribute("patientName", user.getFirstname() + " " + user.getLastname());
			return "patientHome"; // Redirect to patient home if not staff
		} else {
			session.setAttribute("docusername", user.getUsername());
			session.setAttribute("doctorname", user.getFirstname() + " " + user.getLastname());
			model.addAttribute("DoctorName", user.getFirstname() + " " + user.getLastname());
			System.out.println((String)session.getAttribute("doctorname"));
			return "doctor-home"; // Redirect to staff home
		}
	}
}
