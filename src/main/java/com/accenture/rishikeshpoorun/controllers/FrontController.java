package com.accenture.rishikeshpoorun.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.CustomerService;
import com.accenture.rishikeshpoorun.services.RentalService;
/**
 * Handles requests from hostDomain:port/** 
 * @author rishikesh.poorun
 *
 */
@Controller
public class FrontController {

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected RentalService rentalService;

	@Autowired
	protected CarService carService;

	@GetMapping(value = { "/", "/home", "/index", "/homepage" })
	public String goToHomePage(Model model) {

		return "index";
	}

	/**
	 * Redirects to the Administrator DashBoard
	 * 
	 * @return
	 */
	@Secured(value = "ROLE_ADMIN")
	@GetMapping(value = "/secured")
	public String goToAdminDashBoard() {
		return "adminDashboard";
	}

	/**
	 * Handles requests to the Customer page, Draws the customer National Id from
	 * the signed in user principle
	 * 
	 * @param principal, model
	 * @return Go to the Customer DashBoard with User Details</br>
	 *         Attribute: List of cars available for rent </br>
	 *         Attribute: List of rental entries made by logged user
	 */

	@GetMapping(value = "/customer")
	public String goToCustomerDashBoard(Model model) {
		
		try {
			User user = customerService.authenticatedDetails();
			user.setNationalId("POORUN TESTING");
			model.addAttribute("carlist", rentalService.carAvailForRent());
			model.addAttribute("rentalList", rentalService.allRentalByUserId(user.getUserId()));
			model.addAttribute("rentDto", new RentalDto()); // to catch the rental queries from user
			model.addAttribute("car", new Car()); // to catch the car queries from user
			model.addAttribute("stillOnRent", rentalService.isUserStillOnRent(user.getUserId()));
			model.addAttribute("user", user);
			
		} catch (Exception e) {
			model.addAttribute("status", e.getMessage());
			
		}
		
		return "customerDashboard";
	}

}
