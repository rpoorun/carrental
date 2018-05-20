package com.accenture.rishikeshpoorun.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.services.admin.CustomerService;

@RestController
@RequestMapping("/rest")
public class AdminController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/add")
	public String createCustomer(@RequestBody User customer) {

		boolean status = customerService.createCustomer(customer);

		if (status) {
			return "Customer has been created!";
		}

		else {

			return "Failed to create the customer!";
		}
	}

	@PostMapping("/update")
	public String updateCustomer(@RequestBody User customer) {

		boolean status;
		try {
			status = customerService.updateCustomer(customer);

			if (status) {
				return "Customer has been created!";
			}

			else {

				return "Failed to update the customer's details!";
			}
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "Failed to update the customer profile!";
		}

	}
	
	
	@DeleteMapping
	public String deleteCustomer(@RequestBody User customer) {
		
		boolean status;
		
		try {
			status = customerService.deleteCustomer(customer.getNationalId());
			if (status) {
				return "Customer has been deleted!";
			}

			else {

				return "Failed to delete the customer's details!";
			}
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "Failed to delete the customer profile!";
		}
	}
	
	
	@GetMapping("/all")
	public List<User> showAllCustomer(){
		
		return customerService.getAllCustomer();
	}

}
