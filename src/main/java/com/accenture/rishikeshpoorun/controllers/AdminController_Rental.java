package com.accenture.rishikeshpoorun.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.RentalEntryNotFoundException;

@Controller
@RequestMapping("/secured/rental")
@Secured(value = { "ROLE_ADMIN" })
public class AdminController_Rental extends FrontController{

	Logger logger = Logger.getLogger(AdminController_Rental.class);


	/**
	 * Redirects to the Show All Rental Entries
	 * 
	 * @param model
	 * @return Go to Secured_Rental List page
	 */

	@GetMapping(value = "/all")
	public String goToShowAllRental(Model model) {
		List<Rental> rentalList = rentalService.showAllRental();
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentalDto", new RentalDto());

		return "secured_page/rentalList";
	}

	/**
	 * Redirects to the Secured Make Rental Request Form
	 * 
	 * @param model
	 * @return Go to Secured_Rental Form
	 */
	@GetMapping(value = "/allocate")
	public String goToCreateNewRentalPage(Model model) {

		model.addAttribute("rentalDto", new RentalDto());
		model.addAttribute("carlist", carService.getAllCars());
		model.addAttribute("userlist", customerService.getAllCustomer());
		return "secured_page/rentalForm";
	}

	/**
	 * Redirect to the Secured Make Rental Request Form 
	 * Return Car entity as model to prefill the form
	 * @param carId
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/allocate/{carId}")
	public String goToCreateRentalPage(@PathVariable("carId") Long carId, Model model) {
		RentalDto rentalDto = new RentalDto();
		try {
			rentalDto.setRegistrationNumber(carService.findById(carId).getRegistrationNumber());
		} catch (CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		model.addAttribute("rentalDto", rentalDto); //to catch any query 
		model.addAttribute("carlist", carService.getAllCars());
		model.addAttribute("userlist", customerService.getAllCustomer());
		return "secured_page/rentalForm";
	}

	/**
	 * Handles the post request from the Rental Form page
	 * @param rentalDto
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/allocate")
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

		catch (Exception e) {
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

	

	@GetMapping("/query")
	public String goToRentalQuery(Model model) {
		List<Rental> rentalList = rentalService.showAllRental();
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentDto", new RentalDto());

		return "secured_page/rentalQuery";

	}

	/**
	 * Process the rental query
	 * @param dto
	 * @param model
	 * @return
	 */
	@PostMapping("/find")
	public String processRentalQuery(@ModelAttribute RentalDto dto, Model model) {
		List<Rental> rentalList = new ArrayList<>();
		List<Rental> fetch = rentalService.showAllRental();
		if (dto.getRentalId() != null) {
			rentalList.add(rentalService.findByRentalId(dto.getRentalId()));
		}

		else {

			rentalList = rentalService.processRentalQuery(dto, fetch, model);
		}

		model.addAttribute("rentalDto", new RentalDto());
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("mode", "Query Results");
		return "secured_page/rentalList";
	}

	@GetMapping("/release")
	public String goToListRentCar(Model model) {

		List<Rental> rentalList = rentalService.allCurrentCarOnRent();
		model.addAttribute("mode", "- Cars currently on rent");
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentalDto", new RentalDto());
		return "secured_page/rentalList";
	}

	@Profile(value = "dev")
	@PostConstruct
	private void populateRentalDatabase() {

	}

	/**
	 * Handles request from releasing car button
	 * 
	 * @param rentalId
	 * @param model
	 * @return Go to the Show all Rental entries
	 */

	@GetMapping("/releasing/{rentalId}")
	public String releaseRentedCar(@PathVariable("rentalId") Long rentalId, Model model) {

		try {
			rentalService.releaseCar(rentalId);
		} catch (RentalEntryNotFoundException e) {
			model.addAttribute("status", e.getMessage());
		}
		List<Rental> rentalList = rentalService.allCurrentCarOnRent();
		model.addAttribute("mode", "- Cars currently on rent");
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("rentalDto", new RentalDto());

		return "secured_page/rentalList";

	}

	/**
	 * Redirects to the Rental Profile page
	 * @param rentalId
	 * @param model
	 * @return
	 */
	@GetMapping("/read/{rentalId}")
	public String goToRentalProfilePage(@PathVariable("rentalId") Long rentalId, Model model) {
		Rental r = rentalService.findByRentalId(rentalId);
		model.addAttribute("rentalList", rentalService.allRentalByUserId(r.getUser().getUserId()));
		model.addAttribute("rent", r);
		model.addAttribute("carprofilelist", rentalService.allRentalByCarId(r.getCar().getCarId()));
		model.addAttribute("user", rentalService.findByRentalId(rentalId).getUser());
		return "secured_page/rentalProfile";
	}
}
