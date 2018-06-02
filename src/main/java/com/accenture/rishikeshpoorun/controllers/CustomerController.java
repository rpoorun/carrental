package com.accenture.rishikeshpoorun.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dto.RentalDto;

@Controller
@RequestMapping("/customer")
@Secured(value = { "ROLE_ADMIN", "ROLE_CUSTOMER" })
public class CustomerController extends FrontController {

	final private Logger logger = Logger.getLogger(CustomerController.class);

	@GetMapping("/car/showallavail")
	public String goToAvailCarList(Model model) {

		List<Car> carlist = rentalService.carAvailForRent();

		model.addAttribute("carlist", carlist);
		model.addAttribute("car", new Car());
		return "customer_page/listAvailCar";
	}

	/**
	 * Redirects to the Create Rental Form
	 * 
	 * @param captures the carId and userId
	 * @param model
	 * @exception Logs any exception thrown and records into the status model attributes
	 * @return customer_rentalForm
	 */
	@GetMapping(value = "car/rentRequest/{carId}&{userId}")
	public String goToCreateRentalEntry(@PathVariable("carId") Long carId, @PathVariable("userId") Long userId,
			Model model) {

		try {
			RentalDto dto = new RentalDto();

			dto.setCarId(carId);
			dto.setUserId(userId);
			dto.setNationalId(customerService.findById(userId).getNationalId());
			dto.setRegistrationNumber(carService.findById(carId).getRegistrationNumber());

			// check if user is not currently renting a car
			rentalService.isUserStillOnRent(userId);

			model.addAttribute("rentalDto", dto);

			// String nationalId = rentalDto.getNationalId();
			// String registrationNumber = rentalDto.getRegistrationNumber();
			// rentalDto.setCarId(carService.findByRegistrationNumber(registrationNumber).getCarId());
			// rentalDto.setUserId(customerService.findByNationalId(nationalId).getUserId());
			// LocalDate startDate = rentalDto.getStartDate();
			// LocalDate endDate = rentalDto.getEndDate();
			//
			// rentalService.createRentalEntry(rentalDto.getUserId(), rentalDto.getCarId(),
			// startDate, endDate);
			return "customer_page/rentalForm";
		}

		catch (Exception e) {
			model.addAttribute("status", e.getMessage());
			logger.error("Could not load the Rental Request Form. Message: " + e.getMessage());
			return "customerDashboard";
		}

	}

	/**
	 * Handles the post request from the Customer Rental Form
	 * @param dto
	 * @param principal
	 * @param model
	 * @exception Logs any exception thrown and records into the status model attributes
	 * @return Go to the Customer DashBoard page </br> Else, Redirects for the Rental Form with status
	 */
	@PostMapping(value = "/rental/allocate")
	public String processRentRequest(@ModelAttribute RentalDto dto, Model model) {
		
		try {
			dto.setCarId(carService.findByRegistrationNumber(dto.getRegistrationNumber()).getCarId());
			dto.setUserId(customerService.findByNationalId(dto.getNationalId()).getUserId());
			rentalService.createRentalEntry(dto.getUserId(), dto.getCarId(), dto.getStartDate(), dto.getEndDate());
		} catch (Exception e) {
			model.addAttribute("status", e.getMessage());
			
			logger.error("Failed to rent a car. Message: "+e.getMessage());
			
			return "customer_page/rentalForm";
		}
		
		logger.info("User: " + dto.getNationalId() + "confirmed request to rent Car: " + dto.getRegistrationNumber()
		+ " as from " + dto.getStartDate().toString() + " to " + dto.getEndDate().toString());

		return goToCustomerDashBoard(model);
	}
}
