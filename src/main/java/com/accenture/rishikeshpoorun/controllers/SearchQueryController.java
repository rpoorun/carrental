package com.accenture.rishikeshpoorun.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dto.RentalDto;

@Controller
@RequestMapping(value="/secured/query")
public class SearchQueryController extends FrontController{
	
	
	@GetMapping(value="/search")
	public String goToSearchForm(Model model){
		model.addAttribute("rentalDto", new RentalDto());
		return "secured_page/searchForm";
	}
	
	@PostMapping(value = "/search")
	public String goToSearchResults(@ModelAttribute RentalDto r, Model model) {
		List<User> userlist = new ArrayList<>();
		List<Car> carlist = new ArrayList<>();
		List<Rental> rentalList = new ArrayList<>();
		try {
			User u = new User();
			u.setNationalId(r.getNationalId());
			u.setName(r.getName());
			
			userlist = customerService.processUserQuery(u, model);
			model.addAttribute("userlist", userlist);
			
			Car c = new Car();
			c.setRegistrationNumber(r.getRegistrationNumber());
			c.setModel(r.getModel());
			
			if (!c.getRegistrationNumber().equalsIgnoreCase("")) {
				carlist.add(carService.findByRegistrationNumber(c.getRegistrationNumber()));
			} else if (!c.getModel().equalsIgnoreCase("")) {
				carlist = carService.findByModel(c.getModel());
			} 
			
			model.addAttribute("carlist", carlist);

			
			List<Rental> fetch = rentalService.showAllRental();
			if (r.getRentalId() != null) {
			rentalList.add(rentalService.findByRentalId(r.getRentalId()));
			}
	
			else {
	
				rentalList = rentalService.processRentalQuery(r, fetch, model);
			}
	
	
			} catch (Exception e) {
				model.addAttribute("status", e.getLocalizedMessage());
				e.printStackTrace();
				return "secured_page/searchForm";
			}

		model.addAttribute("userlist", userlist);
		model.addAttribute("carlist", carlist);
		model.addAttribute("rentalList", rentalList);
		return "secured_page/searchResult";
	}

}
