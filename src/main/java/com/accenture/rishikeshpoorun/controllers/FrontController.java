package com.accenture.rishikeshpoorun.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.services.CarService;

@Controller
public class FrontController {
	
	@Autowired
	private CarService carService;
	
	@GetMapping(value= {"/home","/index","/homepage", "/"})
	public String goToHomePage(Model model) {
		
		// Add a list of all cars as model variable to enable to print on index page
		
		Iterable<Car> carlist = carService.getAllCars();
		model.addAttribute("carlist", carlist);
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
