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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.exceptions.CarNotAvailException;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.DateConflictException;
import com.accenture.rishikeshpoorun.exceptions.UserStillOnLentException;
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

	@GetMapping(value="/allocate")
	public String goToCreateNewRentalPage(Model model) {
		
		model.addAttribute("rentalDto", new RentalDto());
		model.addAttribute("carlist", carService.getAllCars());
		model.addAttribute("userlist", customerService.getAllCustomer());
		return "secured_page/rentalForm";
	}
	
	@GetMapping(value="/allocate/{carId}")
	public String goToCreateRentalPage(@PathVariable("carId") Long carId, Model model) {
		RentalDto rentalDto = new RentalDto();
		try {
			rentalDto.setRegistrationNumber(carService.findById(carId).getRegistrationNumber());
		} catch (CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		model.addAttribute("rentalDto", rentalDto);
		model.addAttribute("carlist", carService.getAllCars());
		model.addAttribute("userlist", customerService.getAllCustomer());
		return "secured_page/rentalForm";
	}
	
	@PostMapping(value="/allocate")
	public String createRentalEntry(@ModelAttribute RentalDto rentalDto, Model model) {
		
		try {
			String nationalId = rentalDto.getNationalId();
			String registrationNumber = rentalDto.getRegistrationNumber();
			rentalDto.setCarId(carService.findByRegistrationNumber(registrationNumber).getCarId());
			rentalDto.setUserId(customerService.findByNationalId(nationalId).getUserId());
			LocalDate startDate = rentalDto.getStartDate();
			LocalDate endDate = rentalDto.getEndDate();
			
			rentalService.createRentalEntry(rentalDto.getUserId(), rentalDto.getCarId(), startDate, endDate);
			
		}
		
		catch (Exception e)
		{
			model.addAttribute("status", e.getMessage());
			e.printStackTrace();
			return goToCreateRentalPage(rentalDto.getCarId(), model);
		}
		
		return goToListRentCar(model);
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
			
			if(! r.getStartDate().toString().equals("") && r.getEndDate().toString().equals("")) {
				if(r.isSpecificDates()) {
					rentalList = rentalService.rentedOnDates(r.getStartDate(), r.getEndDate());
				}
				else {
					rentalList = rentalService.rentedBetweenPeriod(r.getStartDate(), r.getEndDate());
				}
			}
			
		} catch (Exception e) {
			model.addAttribute("status", e.getLocalizedMessage());
			return "secured_page/searchForm";
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
	
	@GetMapping("/release")
	public String goToListRentCar(Model model) {
		
		List<Rental> rentalList = rentalService.allCurrentCarOnRent();
		model.addAttribute("mode", "- Cars currently on rent");
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());
		return "secured_page/rentalList";
	}

	@Profile(value = "dev")
	@PostConstruct
	private void populateRentalDatabase() {

	}
	
	@GetMapping("/releasing/{rentalId}")
	public String releaseRentedCar(@PathVariable("rentalId") Long rentalId, Model model){
		
		rentalService.updateRentalEntry(rentalId);
		List<Rental> rentalList = rentalService.allCurrentCarOnRent();
		model.addAttribute("mode", "- Cars currently on rent");
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());
		
		return "secured_page/rentalList";
		
	}

}
