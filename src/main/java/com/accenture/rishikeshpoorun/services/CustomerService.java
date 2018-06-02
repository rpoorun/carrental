package com.accenture.rishikeshpoorun.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.accenture.rishikeshpoorun.configs.CustomAuthenticationProvider;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.UserRepo;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;

@Service
@Transactional
public class CustomerService {

	Logger logger = Logger.getLogger(CustomerService.class);

	@Autowired
	private BCryptPasswordEncoder encoder;

	private UserRepo userRepo;

	public CustomerService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	/**
	 * Takes the Dummy User dto from the controller and save to database
	 * 
	 * @param dto
	 * @return <b>True</b> if the user is saved, else <b>False</b>
	 */
	public boolean saveCustomer(User dto) {
		User fetch = userRepo.findByNationalId(dto.getNationalId());

		if (fetch == null) {
			User u = new User();
			u.setName(dto.getName());
			u.setNationalId(dto.getNationalId());
			u.setSex(dto.getSex());
			u.setPassword(encoder.encode(dto.getPassword()));
			u.setRole(dto.getRole());
			u.setDob(dto.getDob());
			u.setRentals(dto.getRentals());
			u.setUserDeleted(false);
			userRepo.save(u);
			logger.info("Created new User!");
			return true;
		}

		return false;

	}

	/**
	 * THIS METHOD CANNOT UPDATE PASSWORD
	 * 
	 * @param dto
	 * @return
	 * @throws CustomerNotFoundException
	 */
	public boolean updateCustomer(User dto) throws CustomerNotFoundException {

		if (userRepo.findByNationalId(dto.getNationalId()) != null) {
			dto.setPassword(encoder.encode(dto.getPassword()));
			userRepo.save(dto);

		} else {
			logger.error("Cannot find customer!");
			throw new CustomerNotFoundException("Cannot find customer!");
		}

		return true;
	}

	/**
	 * Delete the user specified by nationalId <b>Note</b> The entry is not deleted
	 * for cascade deletion issues, only the status userDelete is set true, thus
	 * entry is ignore henceforth
	 * 
	 * @param nationalId
	 * @return <b>True</b> if the entry is set as deleted, else <b>False</b> if
	 *         failed to mark as delete
	 * @throws CustomerNotFoundException
	 */
	public boolean deleteCustomer(String nationalId) throws CustomerNotFoundException {
		User u = userRepo.findByNationalId(nationalId);

		if (u != null) {
			// userRepo.deleteByNationalId(nationalId);
			return deleteCustomer(u.getUserId());

		} else {
			return false;
		}

	}

	/**
	 * Delete the user specified by userId </br>
	 * <b>Note</b> The entry is not deleted for cascade deletion issues, only the
	 * status carDelete is set true, thus entry is ignore henceforth </br>
	 * <b>Note</b> The National ID of the user entity is update to "DELETE"
	 * 
	 * @param userId
	 * @return <b>True</b> if the entry is set as deleted, else <b>False</b> if
	 *         failed to mark as delete
	 * @throws CustomerNotFoundException
	 */
	public boolean deleteCustomer(Long userId) throws CustomerNotFoundException {

		if (userRepo.existsById(userId)) {
			// userRepo.deleteById(userId);
			// Update the National ID to "Deleted" which display in the rental table
			User u = userRepo.findByUserId(userId);
			String oldNationalId = u.getNationalId();
			u.setNationalId("Deleted[" + oldNationalId + "]");
			u.setUserDeleted(true);
			userRepo.save(u);
		} else {
			logger.error("Cannot find customer!");
			throw new CustomerNotFoundException("Cannot find customer!");
		}

		return true;
	}

	/**
	 * Fetch the User entity for the specified User ID
	 * 
	 * @param userId
	 * @return User entity for the specified carId
	 * @throws CustomerNotFoundException
	 */
	public User findById(Long userId) throws CustomerNotFoundException {

		User u = userRepo.findByUserId(userId);

		if (u == null) {

			if (userRepo.existsById(userId)) {
				// This filter the database if user existed and deleted
				logger.error("This user is removed and cannot be further processed!");
				throw new CustomerNotFoundException("This user is removed and no longer active");
			}
			logger.error("Cannot find customer of specified user id");
			throw new CustomerNotFoundException("Cannot find customer of specified User ID");
		}

		return u;

	}

	/**
	 * Fetch the list of user by the given name
	 * @param name
	 * @return
	 * @throws CustomerNotFoundException
	 */
	public List<User> findByName(String name) throws CustomerNotFoundException {
		List<User> customer = userRepo.findAllByName(name);

		if (customer == null) {
			logger.error("Cannot find customer of specified name!");
			throw new CustomerNotFoundException("Cannot find customer of specified Name");
		}
		return customer;
	}

	/**
	 * Fetch the User entity for the specified National ID
	 * 
	 * @param userId
	 * @return Userr entity for the specified National ID
	 * @throws CustomerNotFoundException
	 */
	public User findByNationalId(String nationalId) throws CustomerNotFoundException {

		User customer = userRepo.findByNationalId(nationalId);

		if (customer == null) {
			logger.error("Cannot find customer by national id!");
			throw new CustomerNotFoundException("Cannot find customer of specified National ID");
		}

		return customer;
	}

	/**
	 * @return List of the active user entities from the database
	 */
	public List<User> getAllCustomer() {

		Iterable<User> fetch = userRepo.findAll();
		List<User> list = new ArrayList<>();

		// filter out all not deleted users
		for (User u : fetch) {
			if (!u.isUserDeleted()) {
				list.add(u);
			}
		}

		return list;
	}

	/**
	 * Takes a populated list of user and persist to the database
	 * 
	 * @param list
	 */
	public void saveListToDatabase(List<User> list) {
		for (User u : list) {
			userRepo.save(u);
		}

	}

	/**
	 * Method to call the CSV Reader Utility
	 * 
	 * @param fileName
	 * @return List of User entities read from the CSV File
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<User> readCSVToUser(String fileName) throws FileNotFoundException, IOException {

		return ReadFileUtil.readCSVToUser(fileName);
	}

	/**
	 * Gets the current logged in user details
	 * 
	 * @return The User Entity
	 */
	public User authenticatedDetails() {

		return CustomAuthenticationProvider.getuTest();
	}

	/**
	 * Process the query request for user
	 * 
	 * @param dto
	 * @param model
	 * @return
	 * @throws CustomerNotFoundException 
	 */
	public List<User> processUserQuery(User dto, Model model) throws CustomerNotFoundException {
		List<User> fetch = getAllCustomer();
		List<User> list = new ArrayList<>();

		try {
		if (!dto.getNationalId().equalsIgnoreCase("")) {
			list.add(findByNationalId(dto.getNationalId()));
		}
		} catch(Exception e) {
			
		}
		try {
			if (dto.getSex().equalsIgnoreCase("")) {
				list = filterGender(dto.getSex(), fetch);
				fetch = list;
			}
		}
		catch(Exception e) {
			
		}
		if (!dto.getName().equalsIgnoreCase("")) {
			list = filterName(dto.getName(), fetch);
			fetch = list;
		}
		
		try {
			if (dto.getDob().isSupported(ChronoUnit.DAYS)) {
				list = filterDob(dto.getDob(), fetch);
				fetch = list;
			}
		}
		catch(Exception e) {
			
		}
		
		try {
			if (!dto.getRole().equalsIgnoreCase("")) {
				list = filterRole(dto.getRole(), fetch);
			}
		}
		catch(Exception e) {
			
		}
		
		

		return list;
	}

	// private method to filter the fetched list of user by role
	private List<User> filterRole(String role, List<User> fetch) {
		List<User> list = new ArrayList<>();
		for (User user : fetch) {
			if (user.getRole().equalsIgnoreCase(role)) {
				list.add(user);
			}
		}

		return list;
	}

	// private method to filter the fetched list of user by name
	private List<User> filterName(String filter, List<User> fetch) {
		List<User> list = new ArrayList<>();
		for (User user : fetch) {
			if (user.getName().equalsIgnoreCase(filter)) {
				list.add(user);
			}
		}

		return list;
	}

	// private method to filter the fetched list of user by sex
	private List<User> filterGender(String filter, List<User> fetch) {
		List<User> list = new ArrayList<>();
		for (User user : fetch) {
			if (user.getSex().equalsIgnoreCase(filter)) {
				list.add(user);
			}
		}

		return list;
	}

	// private method to filter the fetched list of user by date of birth
	private List<User> filterDob(LocalDate date, List<User> fetch) {
		List<User> list = new ArrayList<>();
		for (User user : fetch) {
			if (user.getDob().isEqual(date)) {
				list.add(user);
			}
		}

		return list;
	}

}
