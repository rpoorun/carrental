package com.accenture.rishikeshpoorun.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.CustomerService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Controller
@RequestMapping("/secured/rental")
@Secured(value= {"ROLE_ADMIN"})
public class AdminController_Rental {
	
	@Autowired
	private RentalService rentalService;
	
	@Autowired
	private CarService carService; 
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping(value="/all")
	public String goToShowAllRental(Model model)	{
		List<Rental> rentalList = rentalService.showAllRental();
		List<RentalDto> rentalDtoList = new ArrayList<>();
		for(Rental r : rentalList) {
			rentalDtoList.add(new RentalDto(r));
		}

		model.addAttribute("rentalDtoList", rentalDtoList);
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());	
		
		return "secured_page/rentalList";
	}

	
	@GetMapping("/query")
	public String goToRentalQuery(Model model) {
		List<Rental> rentalList = rentalService.showAllRental();
		List<RentalDto> rentalDtoList = new ArrayList<>();
		for(Rental r : rentalList) {
			rentalDtoList.add(new RentalDto(r));
		}

		model.addAttribute("rentalDtoList", rentalDtoList);
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());	

		return "secured_page/rentalQuery";

	}
	
	
	@Profile(value = "dev")
	@PostConstruct
	private void populateRentalDatabase() {
		
		
	}
	
	
}
