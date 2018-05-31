package com.accenture.rishikeshpoorun.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
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
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.CustomerService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Controller
@RequestMapping("/secured/rental")
@Secured(value = { "ROLE_ADMIN" })
public class AdminController_Rental {

	@Autowired
	private RentalService rentalService;

	@Autowired
	private CarService carService;

	@Autowired
	private CustomerService customerService;

	@GetMapping(value = "/all")
	public String goToShowAllRental(Model model) {
		List<Rental> rentalList = rentalService.showAllRental();
		List<RentalDto> rentalDtoList = new ArrayList<>();
		for (Rental r : rentalList) {
			rentalDtoList.add(new RentalDto(r));
		}

		model.addAttribute("rentalDtoList", rentalDtoList);
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());

		return "secured_page/rentalList";
	}

	@GetMapping(value = "/search")
	public String goToSearchPage(Model model) {

		model.addAttribute("rentalDto", new RentalDto());
		return "secured_page/searchForm";
	}

	@PostMapping(value = "/search")
	public String goToSearchResults(@ModelAttribute RentalDto r, Model model) {
		List<User> userlist = new ArrayList<>();
		List<Car> carlist = new ArrayList<>();
		List<Rental> rentalList = new ArrayList<>();
		try {
			if (!r.getNationalId().equalsIgnoreCase("")) {
				userlist.add(customerService.findByNationalId(r.getNationalId()));
			}
			
			if (!r.getRegistrationNumber().equalsIgnoreCase("")) {
				carlist.add(carService.findByRegistrationNumber(r.getRegistrationNumber()));
			} else if (!r.getModel().equalsIgnoreCase("")) {
				carlist = carService.findByModel(r.getModel());
			} else if (r.getPricePerDay() != null) {
				carlist = carService.findByPricePerDay(r.getPricePerDay());
			}
			
		} catch (Exception e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}

		model.addAttribute("userlist", userlist);
		model.addAttribute("carlist", carlist);
		model.addAttribute("rentalList", rentalList);
		return "secured_page/searchResult";
	}

	@GetMapping("/query")
	public String goToRentalQuery(Model model) {
		List<Rental> rentalList = rentalService.showAllRental();
		List<RentalDto> rentalDtoList = new ArrayList<>();
		for (Rental r : rentalList) {
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
