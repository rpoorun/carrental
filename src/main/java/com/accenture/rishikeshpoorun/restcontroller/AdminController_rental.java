package com.accenture.rishikeshpoorun.restcontroller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.accenture.rishikeshpoorun.exceptions.CarNotAvailException;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.DateConflictException;
import com.accenture.rishikeshpoorun.exceptions.RentalEntryNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.UserStillOnLentException;
import com.accenture.rishikeshpoorun.services.RentalService;

@RestController
@RequestMapping("/rest/rental")
public class AdminController_rental {

	@Autowired
	private RentalService rentalService;

	@GetMapping("/avail")
	public List<Car> carAvailForRent() {

		return rentalService.carAvailForRent();
	}
	
	@GetMapping("/allrented")
	public List<Rental> showAllRental(){
		List<Rental> list = new ArrayList<>();
		
		list = rentalService.showAllRental();
		
		return list;
	}

	@PostMapping("/rentedperiod")
	public List<User> userRentOnPeriod(@RequestBody Rental rent) {

		LocalDate startDate = rent.getStartDate();
		LocalDate endDate = rent.getEndDate();

		return rentalService.userRentBetweenPeriod(startDate, endDate);
	}

	@GetMapping("/totalamount")
	public Double totalAmountRent() {

		return rentalService.amountForAllCarRented();
	}

	@PostMapping("/rentedbyuser")
	public List<?> carRentedByCustomer(@RequestBody User u) {
		List<Car> list = new ArrayList<>();

		if (u.getNationalId() != null) {
			try {
				list = rentalService.carsRentedByUser(u.getNationalId());
			} catch (CustomerNotFoundException e) {
				// e.printStackTrace();
			}
		}

		else if (u.getUserId() != null) {
			try {
				list = rentalService.carsRentedByUser(u.getUserId());
			} catch (CustomerNotFoundException e) {
				// e.printStackTrace();
			}
		}

		return list;

	}

	@PostMapping("/newrent")
	public String createNewRental(@RequestBody Rental r) {
		boolean status = false;
		try {
			status = rentalService.createRentalEntry(r.getUser().getUserId(), r.getCar().getCarId(), r.getStartDate(),
					r.getEndDate());
		} catch (DateConflictException e) {
			return e.getMessage();
		} catch (UserStillOnLentException e) {
			return e.getMessage();
		} catch (CarNotAvailException e) {
			return e.getMessage();
		}
		
		
		if (status) {
			return "Car Allocated to specified User!";
		}
		else {
			return "Failed to Allocate car to specified User!";
		}

	}
	
	
	@PostMapping("/updaterent")
	public String updateRental(@RequestBody Rental r) {
		boolean status = false;
		
		if(!r.isReturned()) {
			return "Car marked as not returned!";
		}
		
			try {
				status = rentalService.releaseCar(r.getRentalId());
			} catch (RentalEntryNotFoundException e) {
				
				return e.getMessage();
			}
		
		if(status) {
			return "Rental Entry cleared, and car marked as avaialable";
		}
		else {
			return "Failed to update the specified rental entry!";
		}
	}
	
	

}
