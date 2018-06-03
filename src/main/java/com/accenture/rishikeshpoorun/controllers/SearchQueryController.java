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
	public String goToSearchResults(@ModelAttribute RentalDto dto, Model model) {
		
		List<Rental> fetchrental = rentalService.showAllRental();
		List<User> fetchuser = customerService.getAllCustomer();
		List<Car> fetchcar = carService.getAllCars();
		
		List<User> userlist = new ArrayList<>();
		List<Car> carlist = new ArrayList<>();
		List<Rental> rentalList = new ArrayList<>();
		try {
			
			if(!dto.getNationalId().equalsIgnoreCase("")) {
				//filter the fetch list if national Id field is not empty
				for(Rental r : fetchrental) {
					if(r.getUser().getNationalId() == dto.getNationalId()) {
						rentalList.add(r);
						userlist.add(r.getUser());
						carlist.add(r.getCar());
					}
				}
				
				// filter the fetch user list if national id field is not empty and no rental entry
				for(User u : fetchuser) {
					if(u.getNationalId() == dto.getNationalId() && userlist.isEmpty()) {
						//list.isEmpty() avoids duplicate filtering
						userlist.add(u);
					}
				}
				
				// the fetch list to the filtered ones for next filter to process
				fetchrental = rentalList;
				fetchuser = userlist;
				fetchcar = carlist;
			}
			
			if(!dto.getRegistrationNumber().equalsIgnoreCase("")) {
				//filter the fetch list if registration number field is not empty
				//reset the output list for new filter if ever filtered previously
				userlist = new ArrayList<>();
				carlist = new ArrayList<>();
				rentalList = new ArrayList<>();
				
				for(Rental r : fetchrental) {
					if(r.getCar().getRegistrationNumber() == dto.getRegistrationNumber()) {
						rentalList.add(r);
						userlist.add(r.getUser());
						carlist.add(r.getCar());
					}
				}
				
				//filter the fetch car list if registration number field is not empty and no rental entry
				for(Car c : fetchcar) {
					if(c.getRegistrationNumber() == dto.getRegistrationNumber() && carlist.isEmpty()) {
						//list.isEmpty() avoids duplicate filtering
						carlist.add(c);
					}
				}
				// the fetch list to the filtered ones for next filter to process
				fetchrental = rentalList;
				fetchuser = userlist;
				fetchcar = carlist;
			}
			
//			User u = new User();
//			u.setNationalId(r.getNationalId());
//			u.setName(r.getName());
//			
//			userlist = customerService.processUserQuery(u, model);
//			model.addAttribute("userlist", userlist);
//			
//			Car c = new Car();
//			c.setRegistrationNumber(r.getRegistrationNumber());
//			c.setModel(r.getModel());
//			
//			if (!c.getRegistrationNumber().equalsIgnoreCase("")) {
//				carlist.add(carService.findByRegistrationNumber(c.getRegistrationNumber()));
//			} else if (!c.getModel().equalsIgnoreCase("")) {
//				carlist = carService.findByModel(c.getModel());
//			} 
//			
//			model.addAttribute("carlist", carlist);
//
//			
//			List<Rental> fetch = rentalService.showAllRental();
//			if (r.getRentalId() != null) {
//			rentalList.add(rentalService.findByRentalId(r.getRentalId()));
//			}
//	
//			else {
//	
//				rentalList = rentalService.processRentalQuery(r, fetch, model);
//			}
	
	
			} catch (Exception e) {
				model.addAttribute("status", e.getLocalizedMessage());
				return "secured_page/searchForm";
			}

		model.addAttribute("userlist", userlist);
		model.addAttribute("carlist", carlist);
		model.addAttribute("rentalList", rentalList);
		if(userlist.isEmpty() && carlist.isEmpty() && rentalList.isEmpty()) {
			model.addAttribute("status", "No Result Found!");
		}
		return "secured_page/searchResult";
	}

}
