package com.accenture.rishikeshpoorun.controllers;

import java.time.temporal.ChronoUnit;
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
@RequestMapping(value = "/secured/query")
public class SearchQueryController extends FrontController {

	private static List<Rental> fetchrental;
	private static List<User> fetchuser;
	private static List<Car> fetchcar;
	private static List<User> userlist;
	private static List<Car> carlist;
	private static List<Rental> rentalList;
	private static boolean noResultFound = false;

	/**
	 * Redirects requests for the Query Form
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/search")
	public String goToSearchForm(Model model) {
		model.addAttribute("rentalDto", new RentalDto());
		return "secured_page/searchForm";
	}

	@PostMapping(value = "/search")
	public String processQueryForm(@ModelAttribute RentalDto dto, Model model) {

		//fetch the updated database
		initializeDatalists();
		
		try {
			
			//if specifiDates is set true query to matching dates, else in between
			if((dto.getStartDate().isSupported(ChronoUnit.DAYS) && (dto.getEndDate().isSupported(ChronoUnit.DAYS)))){
				if(dto.isSpecificDates()) {
					//updates the fetch rental datalist for only entries matching the dates
					fetchrental = rentalService.rentedOnDates(dto.getStartDate(), dto.getEndDate());
				}
				else {
					//updates the fetch rental datalist for only entries inbetween the two dates
					fetchrental = rentalService.rentedBetweenPeriod(dto.getStartDate(), dto.getEndDate());
				}
				
				//triggers the filter by dates
				queryFilterByDates(dto);
				
			}
			else if(dto.getStartDate().isSupported(ChronoUnit.DAYS)){
				//updates the fetch rental datalist for only the records set on the startDate
				fetchrental = rentalService.rentalsOnStartDate(dto.getStartDate());
				
			}
			else if(dto.getEndDate().isSupported(ChronoUnit.DAYS)) {
				
			}
			
			//if the name field is not empty
			if((dto.getName().length() >=1)) { System.out.println("inside the trigger by name");
				//triggers the filter by name 
				queryFilterByName(dto);
			}
			
			//if the national id field is not empty
			if(dto.getNationalId().length()>=1) { System.out.println("inside the trigger by national");
				//triggers the filter by national id
				queryFilterByNationalId(dto);
			}
			
			//if the registration number field is not empty
			if(dto.getRegistrationNumber().length()>=1) { System.out.println("inside the trigger by registration number");
				//triggers the filter by registrationNumber 
				queryFilterByRegistrationNumber(dto);
			}
			
			//if the registration number field is not empty
			if(dto.getModel().length()>=1) { System.out.println("inside the trigger by registration number");
				//triggers the filter by registrationNumber 
				queryFilterByModel(dto);
			}
			
			
		}
		
		catch(Exception e) {
			model.addAttribute("status", e.getMessage());
			return "secured_page/searchForm";
		}
		//if no results were found following any queryfilter was triggered
		if(noResultFound) {
			model.addAttribute("status", "No Results Found!");
			return "secured_page/searchForm";
		}
					
		model.addAttribute("rentalList", fetchrental);
		model.addAttribute("carlist", fetchcar);
		model.addAttribute("userlist",fetchuser);
		return "secured_page/searchResult";
	}

	
	/**
	 * Local method to process the query for given dates parameters
	 * @param dto
	 */
	private void queryFilterByDates(RentalDto dto) {
		//iterates over the fetch rental datalists for the given two dates
		for(Rental r : fetchrental) {
			if(!userlist.contains(r.getUser())) {
				userlist.add(r.getUser());
			}
			if(!carlist.contains(r.getCar())) {
				carlist.add(r.getCar());
			}
		}
		// if no records were stored in the dummy lists, set flag noResultFound true
		if (carlist.isEmpty() && userlist.isEmpty() && rentalList.isEmpty()) {
			noResultFound = true;
		} else {
			noResultFound = false;
		}
		// update the datalists with the filters one for next filter
		fetchrental = rentalList;
		fetchcar = carlist;
		fetchuser = userlist;
		// reset the dummy list
		rentalList = new ArrayList<>();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
	}

	/**
	 * Local method to process the query for given registration number
	 * @param dto
	 */
	private void queryFilterByRegistrationNumber(RentalDto dto) {
		// sort out the cars from database
		for (Car c : fetchcar) {
			if (c.getRegistrationNumber().contains(dto.getRegistrationNumber())) {
				if (!carlist.contains(c)) { // avoid duplicate addition from other filter
					carlist.add(c);
				}
				if (!c.getRentals().isEmpty()) {
					// iterates the rentals for this car
					for (Rental r : c.getRentals()) {
						if (!rentalList.contains(r)) {
							rentalList.add(r);
							if (!userlist.contains(r.getUser())) {
								userlist.add(r.getUser());
							}
						}
					}
				}
			}
		}
		// if no records were stored in the dummy lists, set flag noResultFound true
		if (rentalList.isEmpty() && carlist.isEmpty() && userlist.isEmpty()) {
			noResultFound = true;
		}
		else {
			noResultFound = false;
		}
		// update the datalists with the filters one for next filter
		fetchrental = rentalList;
		fetchcar = carlist;
		fetchuser = userlist;
		// reset the dummy list
		rentalList = new ArrayList<>();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
		
	}
	
	
	/**
	 * Local method to process the query for given model
	 * @param dto
	 */
	private void queryFilterByModel(RentalDto dto) {
		// sort out the cars from database
		for (Car c : fetchcar) {
			if (c.getModel().contains(dto.getModel())) {
				if (!carlist.contains(c)) { // avoid duplicate addition from other filter
					carlist.add(c);
				}
				if (!c.getRentals().isEmpty()) {
					// iterates the rentals for this car
					for (Rental r : c.getRentals()) {
						if (!rentalList.contains(r)) {
							rentalList.add(r);
							if (!userlist.contains(r.getUser())) {
								userlist.add(r.getUser());
							}
						}
					}
				}
			}
		}
		// if no records were stored in the dummy lists, set flag noResultFound true
		if (rentalList.isEmpty() && carlist.isEmpty() && userlist.isEmpty()) {
			noResultFound = true;
		}
		else {
			noResultFound = false;
		}
		// update the datalists with the filters one for next filter
		fetchrental = rentalList;
		fetchcar = carlist;
		fetchuser = userlist;
		// reset the dummy list
		rentalList = new ArrayList<>();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
		
	}
	
	

	/**
	 * Local method to process the query for given name 
	 * @param dto
	 */
	private void queryFilterByName(RentalDto dto) {

		//sort out the users from database
		for(User u : fetchuser) { 
			if((u.getName().contains(dto.getName()))) { 
				if(!userlist.contains(u)) { //avoid duplicate addition from other filter
					userlist.add(u);
				}
				if(!u.getRentals().isEmpty()) {
					//iterates the rentals for this user
					for(Rental r : u.getRentals()) {
						if(!rentalList.contains(r)) {
							rentalList.add(r);
							if(!carlist.contains(r.getCar())) {
								carlist.add(r.getCar());
							}
						}
					}
				}
			}
		}
		// if no records were stored in the dummy lists, set flag noResultFound true
		if(rentalList.isEmpty() && carlist.isEmpty() && userlist.isEmpty()) {
			noResultFound = true;
		}
		else {
			noResultFound = false;
		}
		// update the datalists with the filters one for next filter
		fetchrental = rentalList;
		fetchcar = carlist; 
		fetchuser = userlist;
		// reset the dummy list
		rentalList = new ArrayList<>();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
	}

	
	/**
	 * Local method to process the query for given name
	 * @param dto
	 */
	private void queryFilterByNationalId(RentalDto dto) {

		//sort out the users from database
		for(User u : fetchuser) {
			if((u.getNationalId().contains(dto.getNationalId()))) {
				if(!userlist.contains(u)) { //avoid duplicate addition from other filter
					userlist.add(u);
				}
				if(!u.getRentals().isEmpty()) {
					//iterates the rentals for this user
					for(Rental r : u.getRentals()) {
						if(!rentalList.contains(r)) {
							rentalList.add(r);
							if(!carlist.contains(r.getCar())) {
								carlist.add(r.getCar());
							}
						}
					}
				}
			}
		}
		// if no records were stored in the dummy lists, set flag noResultFound true
		if(rentalList.isEmpty() && carlist.isEmpty() && userlist.isEmpty()) {
			noResultFound = true;
		}
		else {
			noResultFound = false;
		}
		// update the datalists with the filters one for next filter
		fetchrental = rentalList;
		fetchcar = carlist; 
		fetchuser = userlist;
		// reset the dummy list
		rentalList = new ArrayList<>();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
	}

	
	
	/**
	 * Local method to fetch the updated database and 
	 * set the dummy list to accept filtered results
	 */
	private void initializeDatalists() {
		// initialize the datalists
		fetchrental = rentalService.showAllRental();
		fetchuser = customerService.getAllCustomer();
		fetchcar = carService.getAllCars();
		userlist = new ArrayList<>();
		carlist = new ArrayList<>();
		rentalList = new ArrayList<>();
	}
}
