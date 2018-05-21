package com.accenture.rishikeshpoorun.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.services.RentalService;

@RestController
@RequestMapping("/rest/rental")
public class AdminController_rental {
	
	@Autowired
	private RentalService rentalService;
	
	@GetMapping("/avail")
	public List<Car> carAvailForRent(){
		
		return rentalService.carAvailForRent();
	}
	
	
	@PostMapping("/rentedperiod")
	public List<User> userRentOnPeriod(@RequestBody Rental rent){
		
		LocalDate startDate = rent.getStartDate();
		LocalDate endDate = rent.getEndDate();
		
		return rentalService.userRentOnPeriod(startDate, endDate);
	}
	
	
	@GetMapping("/totalamount")
	public Double totalAmountRent() {
		
		return rentalService.amountForAllCarRented();
	}

}
