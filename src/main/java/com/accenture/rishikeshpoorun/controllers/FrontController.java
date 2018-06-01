package com.accenture.rishikeshpoorun.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.accenture.rishikeshpoorun.dto.RentalDto;


@Controller
public class FrontController {
	
	
	@GetMapping(value= {"/","/home","/index","/homepage"})
	public String goToHomePage(Model model) {
		
		return "index";
	}
	
	@Secured(value="ROLE_ADMIN")
	@GetMapping(value="/secured")
	public String goToAdminDashBoard() {
		return "adminDashboard";
	}

	
	@GetMapping(value="/customer")
	public String goToCustomerDashBoard() {
		return "customerDashboard";
	}

}
