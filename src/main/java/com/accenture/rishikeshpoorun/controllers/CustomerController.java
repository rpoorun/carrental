package com.accenture.rishikeshpoorun.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Controller
@RequestMapping("/customer")
@Secured(value= {"ROLE_CUSTOMER", "ROLE_ADMIN"})
public class CustomerController {
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private RentalService rentalService;
	
	@GetMapping("/car/showallavail")
	public String goToAvailCarList(Model model) {
		
		List<Car> carlist = rentalService.carAvailForRent();
		
		model.addAttribute("carlist", carlist);
		model.addAttribute("car", new Car());
		return "customer_page/listAvailCar";
	}

}
