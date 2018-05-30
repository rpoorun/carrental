package com.accenture.rishikeshpoorun.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.services.CustomerService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Controller
@RequestMapping("/secured/user")
public class AdminController_User {
	
	@Autowired 
	private CustomerService customerService;
	
	@Autowired
	private RentalService rentalService;
	
	@GetMapping("/all")
	public String showAllUsers(Model model, String nationalId) {
		
		List<User> list = customerService.getAllCustomer();
		model.addAttribute("userlist", list);
		model.addAttribute("user", new User());
		nationalId = (nationalId == null)? "none":nationalId;
		model.addAttribute("new", nationalId);
		return "secured_page/userList";
	}
	
	
	@GetMapping(value = "/add")
	public String goToAddUserPage(Model model) {

		model.addAttribute("user", new User());
		return "secured_page/userForm";
	}
	
	@PostMapping(value = "/add")
	public String goToNewShowAllUser(@ModelAttribute User u, Model model) {
		
		try {
			boolean status = customerService.saveCustomer(u);
			if (status) {
				
				return showAllUsers(model, u.getNationalId());
			} else {

				model.addAttribute("status", "Failed to add User " + u.getNationalId());
				return "secured_page/userForm";
			}
		}

		catch (Exception e) {

			model.addAttribute("status", "Failed to add User: " + u.getNationalId() +", Reason: User Entry already exists! ");
			return "secured_page/userForm";
		}

	}
	
	@GetMapping(value="/update/{userId}")
	public String goToUpdateUserPage(@PathVariable("userId") Long userId, Model model) {
		try {
			User u = customerService.findById(userId);
			model.addAttribute("user", u);
		} catch (CustomerNotFoundException e) {
		}
		
		model.addAttribute("mode", "Update");
	
		return "secured_page/userForm";
	}
	
	
	@GetMapping(value="/read/{userId}")
	public String readUser(@PathVariable("userId") Long userId, Model model) {
		
		try {
			User u = customerService.findById(userId);
			List<Car> rentedCar = rentalService.carsRentedByUser(userId);
			model.addAttribute("user", u);
			model.addAttribute("rentalList", rentedCar);
			model.addAttribute("rent", new Rental());
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		return "secured_page/userProfile";
	}
	
	@GetMapping(value="/delete/{userId}")
	public String deleteUser(@PathVariable("userId") Long userId, Model model) {
		
		try {
			boolean status = customerService.deleteCustomer(userId);
			if(status) {
				model.addAttribute("status", "User Entry deleted!");
			}
			
		} catch ( CustomerNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
			
		}
		
		return showAllUsers(model, "none");
	}
	
	
	@GetMapping(value="/query")
	public String goToUserQuery(Model model) {
		List<User> list = customerService.getAllCustomer();
		model.addAttribute("userlist", list);
		model.addAttribute("user", new User());
		return "secured_page/userQuery";
	}
	
	
	@PostMapping(value="/find")
	public String findUser(@ModelAttribute User u, Model model) {
		
		List<User> fetch = customerService.getAllCustomer();
		List<User> list = new ArrayList<>();
		try {
			
			if(!u.getNationalId().equalsIgnoreCase("")) {
				list.add(customerService.findByNationalId(u.getNationalId()));
			}
			
			if(!u.getName().equalsIgnoreCase("")) {
				list = filterName(u.getName(), fetch);
				fetch = list;
			}
			
			if(!u.getSex().equalsIgnoreCase("")) {
				list = filterGender(u.getSex(), fetch);
				fetch = list;				
			}
			
//			if(u.getDob() == null) {
//				list = filterDob(u.getDob(), fetch);
//				fetch = list;
//			}
			
			if(!u.getRole().equalsIgnoreCase("")) {
				list = filterRole(u.getRole(), fetch);
			}
		}
		catch (CustomerNotFoundException e){
			model.addAttribute("status", e.getMessage());
			return "secured_page/userQuery";
		}
		
		model.addAttribute("userlist", list);
		return "secured_page/userList";
	}
	
	
	private List<User> filterRole(String role, List<User> fetch) {
		List<User> list = new ArrayList<>();
		for(User user: fetch) {
			if(user.getRole().equalsIgnoreCase(role)) {
				list.add(user);
			}
		}		
		
		return list;
	}


	private List<User> filterName(String filter, List<User> fetch){
		List<User> list = new ArrayList<>();
		for(User user: fetch) {
			if(user.getName().equalsIgnoreCase(filter)) {
				list.add(user);
			}
		}		
		
		return list;
	}
	
	private List<User> filterGender(String filter, List<User> fetch){
		List<User> list = new ArrayList<>();
		for(User user: fetch) {
			if(user.getSex().equalsIgnoreCase(filter)) {
				list.add(user);
			}
		}		
		
		return list;
	}
	
	private List<User> filterDob(LocalDate date, List<User> fetch){
		List<User> list = new ArrayList<>();
		for(User user: fetch) {
			if(user.getDob().isEqual(date)) {
				list.add(user);
			}
		}		
		
		return list;
	}

	
	@Profile(value = "dev")
	@PostConstruct
	private void populateUserDatabase() {
		String fileName = "/Users/rishikesh.poorun/OneDrive - Accenture/Spring Boot Project/carrental/src/main/resources/files/usersDev.csv";
		try {
			List<User> list = customerService.readCSVToCar(fileName);
			customerService.saveListToDatabase(list);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}
}
