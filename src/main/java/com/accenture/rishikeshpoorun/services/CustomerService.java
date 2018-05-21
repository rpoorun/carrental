package com.accenture.rishikeshpoorun.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.UserRepo;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {

	private UserRepo userRepo;

	public CustomerService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public boolean createCustomer(User customer) {

		if (userRepo.save(customer) == null) {
			return false;
		} else {

			return true;
		}
	}
	
	
	public boolean updateCustomer(User customer) throws CustomerNotFoundException {
		
		if(userRepo.findByNationalId(customer.getNationalId()) != null) {
			userRepo.save(customer);
			
		}
		else {
			throw new CustomerNotFoundException("Cannot find customer!");
		}
		
		return true;
	}
	
	
	
	public boolean deleteCustomer(String nationalId) throws CustomerNotFoundException{
		if(userRepo.findByNationalId(nationalId) != null) {
			userRepo.deleteByNationalId(nationalId);
			
		}
		else {
			throw new CustomerNotFoundException("Cannot find customer!");
		}
		
		return true;
		
	}
	
	public User findById(Long userId) throws CustomerNotFoundException {
		
		User customer = userRepo.findById(userId).get();
		
		if( customer == null) {
			throw new CustomerNotFoundException("Cannot find customer of specified National ID");
		}
		
		return customer;
		
		
	}
	
	public User findByNationalId(String nationalId) throws CustomerNotFoundException {
		
		User customer = userRepo.findByNationalId(nationalId);
		
		if( customer == null) {
			throw new CustomerNotFoundException("Cannot find customer of specified National ID");
		}
		
		return customer;
	}
	
	
	public List<User> getAllCustomer(){
		
		List<User> list = new ArrayList<>();
		
		userRepo.findAll().forEach(list::add);
		
		return list;
	}

}
