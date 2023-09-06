package com.springproject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class WebController {

	@GetMapping(value = "/login")
	   public String showForm(Model model) {
		        User user = new User();
		        model.addAttribute("user", user);
		     return "login";
	}
	@PostMapping(value = "/login")
	public String submitForm(@ModelAttribute User user,Model model) {
	    model.addAttribute("user", user);
	    String Id = user.getLoginId();
	    String pass = user.getPassword();
	    System.out.println("Login Id:" + Id);
	    System.out.println("Password:" + pass);
	    return "login";
	}
}
