package com.accenture.rishikeshpoorun.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
	
	@GetMapping(value= {"/home","/index","/homepage"})
	public String goToHomePage() {
		return "index";
	}
	
	@Secured(value="ROLE_ADMIN")
	@GetMapping(value="/secured")
	public String goToAdminDashBoard() {
		return "adminDashboard";
	}
	
	@Secured(value="ROLE_CUSTOMER")
	@GetMapping(value="/rent")
	public String goToCustomerDashBoard() {
		return "customerDashboard";
	}

}
