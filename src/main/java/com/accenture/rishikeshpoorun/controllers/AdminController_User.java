package com.accenture.rishikeshpoorun.controllers;

import java.util.List;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.dto.TextDto;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;

@Controller
@RequestMapping("/secured/user")
@Secured(value= {"ROLE_ADMIN"})
public class AdminController_User extends FrontController{
	
	/**
	 * Redirects the request to the list of all User page	
	 * @param model
	 * @param nationalId
	 * @return
	 */
	@GetMapping("/all")
	public String showAllUsers(Model model) {
		model.addAttribute("userlist", customerService.getAllCustomer());
		model.addAttribute("user", new User());
		return "secured_page/userList";
	}
	
	/**
	 * Redirects to the Secured User Form page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/add")
	public String goToAddUserPage(Model model) {

		model.addAttribute("user", new User());
		return "secured_page/userForm";
	}
	
	/**
	 * Handles the post request from the User Form
	 * @param u
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/add")
	public String goToNewShowAllUser(@ModelAttribute User u, Model model) {
		
		try {
			boolean status = customerService.saveCustomer(u);
			if (status) {
				
				return showAllUsers(model);
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
	
	/**
	 * Handles the request for updating a current user
	 * Catches the userId from the URL path
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping(value="/update/{userId}")
	public String goToUpdateUserProfile(@PathVariable("userId") Long userId, Model model) {
		try {
			User u = customerService.findById(userId);
			model.addAttribute("user", u);
		} catch (CustomerNotFoundException e) {
		}
		
		model.addAttribute("mode", "Update");
	
		return "secured_page/userForm";
	}
	
	
	/**
	 * Redirects to the Secured User Profile page
	 * Catches the user Id from the URL to return the User entity as model attribute
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping(value="/read/{userId}")
	public String readUser(@PathVariable("userId") Long userId, Model model) {
		
		try {
			User u = customerService.findById(userId);
			model.addAttribute("user", u);
			model.addAttribute("rentalList", rentalService.allRentalByUserId(userId));
			model.addAttribute("rentalDto", new RentalDto()); // to catch rental query row
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		return "secured_page/userProfile";
	}
	
	/**
	 * Handles the delete request of a user 
	 * The user entry is set as deleted 
	 * Catches the userId from the URL path
	 * @param userId
	 * @param model
	 * @return
	 */
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
		
		return showAllUsers(model);
	}
	
	/**
	 * Redirects for the Secured User query page/row
	 * @param model
	 * @return
	 */
	@GetMapping(value="/query")
	public String goToUserQuery(Model model) {
		model.addAttribute("userlist", customerService.getAllCustomer());
		model.addAttribute("user", new User());
		return "secured_page/userQuery";
	}
	
	
	/**
	 * Handles the post requests from the User query row
	 * 
	 * @param u
	 * @param model
	 * @return
	 */
	@PostMapping(value="/find")
	public String findUser(@ModelAttribute User u, Model model) {
		
		try {
			List<User> list = customerService.processUserQuery(u, model);
			model.addAttribute("userlist", list);
			return "secured_page/userList";
		}
		catch (Exception e){
			model.addAttribute("status", e.getMessage());
			return "secured_page/userQuery";
		}
		
		
	}
	
	/**
	 * Redirects to the Import CSV Page
	 * @param model
	 * @return
	 */
	@GetMapping("/csvFileToUser")
	public String goToCSVReaderPage(Model model) {
		
		model.addAttribute("textDto", new TextDto());
		return "secured_page/importUserCsv";
	}
	
	/**
	 * Handles the post request from the Import CSV page 
	 * When a csv file is uploaded
	 * 
	 * @param file
	 * @param model
	 * @return
	 */
	@PostMapping("/readFileUserCsv")
	public String readFileCarCsv(@RequestParam("file") MultipartFile file, Model model) {
		List<User> list;
		try {
			list = ReadFileUtil.importUserCSV(file.getInputStream());
			for (User u: list) {
				customerService.saveCustomer(u);
			}
		
		} catch (Exception e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		
		return showAllUsers(model);
	}
	
	
}
