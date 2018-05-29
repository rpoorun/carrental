package com.accenture.rishikeshpoorun.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.CarRepo;
import com.accenture.rishikeshpoorun.dao.repositories.RentalRepo;
import com.accenture.rishikeshpoorun.dao.repositories.UserRepo;
import com.accenture.rishikeshpoorun.exceptions.CarNotAvailException;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.DateConflictException;
import com.accenture.rishikeshpoorun.exceptions.RentalEntryNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.UserStillOnLentException;

@Service
@Transactional
public class RentalService {

	private RentalRepo rentalRepo;
	private UserRepo customerRepo;
	private CarRepo carRepo;

	public RentalService(RentalRepo rentalRepo, UserRepo customerRepo, CarRepo carRepo) {
		this.rentalRepo = rentalRepo;
		this.carRepo = carRepo;
		this.customerRepo = customerRepo;
	}

	public List<Car> carAvailForRent() {
		List<Car> list = new ArrayList<>();

		list = rentalRepo.allCarsAvailForRent();

		return list;
	}

	public List<User> userRentOnPeriod(LocalDate startDate, LocalDate endDate) {
		List<User> list = new ArrayList<>();

		list = rentalRepo.userRentOnPeriod(startDate, endDate);

		return list;
	}

	// Method() to get the total rent amount for all cars rented and returned
	public Double amountForAllCarRented() {
		Double total = 0.0;

		// Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		// For each entry in the rental table, get the price and calculate days
		// Calculate the amount by product of price and amount of days
		for (Rental r : rlist) {
			LocalDate sDate = r.getStartDate();
			LocalDate eDate = r.getEndDate();
			Double price = r.getCar().getPricePerDay();

			Long days = ChronoUnit.DAYS.between(sDate, eDate);

			total += days * price;

		}

		// return the sum of all rent amount
		return total;
	}

	// Method() to get the total rent amount for the specific car rented and
	// returned given Car's Registration Number
	public Double amountForCarRented(String registrationNumber) {
		Double total = 0.0;

		// Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		// For each entry in the rental table, get the price and calculate days
		// Calculate the amount by product of price and amount of days for the given car
		for (Rental r : rlist) {

			// sort the list for entries matching the parameter registration number
			if (r.getCar().getRegistrationNumber() == registrationNumber) {
				LocalDate sDate = r.getStartDate();
				LocalDate eDate = r.getEndDate();
				Double price = r.getCar().getPricePerDay();

				Long days = ChronoUnit.DAYS.between(sDate, eDate);

				total += days * price;
			}

		}

		return total;
	}

	// Method() to get the total rent amount for the specific car rented and
	// returned for given Car JAVA ID
	public Double amountForCarRented(Long carId) {
		Double total = 0.0;

		// Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		// For each entry in the rental table, get the price and calculate days
		// Calculate the amount by product of price and amount of days for the given car
		for (Rental r : rlist) {

			// sort the list for entries matching the parameter registration number
			if (r.getCar().getCarId() == carId) {
				LocalDate sDate = r.getStartDate();
				LocalDate eDate = r.getEndDate();
				Double price = r.getCar().getPricePerDay();

				Long days = ChronoUnit.DAYS.between(sDate, eDate);

				total += days * price;
			}

		}

		return total;
	}

	// Method() to get the total rent amount for the cars rented and returned for
	// given User's JAVA ID
	public Double amountForUserRented(Long userId) {
		Double total = 0.0;

		// Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		// For each entry in the rental table, get the price and calculate days
		// Calculate the amount by product of price and amount of days for the given car
		for (Rental r : rlist) {

			// sort the list for entries matching the parameter registration number
			if (r.getUser().getUserId() == userId) {
				LocalDate sDate = r.getStartDate();
				LocalDate eDate = r.getEndDate();
				Double price = r.getCar().getPricePerDay();

				Long days = ChronoUnit.DAYS.between(sDate, eDate);

				total += days * price;
			}

		}

		return total;
	}

	// Method() to get the total rent amount for the cars rented and returned by
	// given User's National ID
	public Double amountForUserRented(String nationalId) {
		Double total = 0.0;

		// Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		// For each entry in the rental table, get the price and calculate days
		// Calculate the amount by product of price and amount of days for the given car
		for (Rental r : rlist) {

			// sort the list for entries matching the parameter registration number
			if (r.getUser().getNationalId() == nationalId) {
				LocalDate sDate = r.getStartDate();
				LocalDate eDate = r.getEndDate();
				Double price = r.getCar().getPricePerDay();

				Long days = ChronoUnit.DAYS.between(sDate, eDate);

				total += days * price;
			}

		}

		return total;
	}

	// Method to get the list of cars rented by user of given user Id
	public List<Car> carsRentedByUser(Long userId) throws CustomerNotFoundException {
		List<Car> list = new ArrayList<>();
		List<Rental> rlist = new ArrayList<>();

		User u = customerRepo.findById(userId).get(); // customerService.findById(userId);
		u.getRentals().forEach(rlist::add);

		for (Rental c : rlist) {
			list.add(c.getCar());
		}

		return list;
	}

	// Method to get the list of cars rented by user of given national Id
	public List<Car> carsRentedByUser(String nationalId) throws CustomerNotFoundException {
		List<Car> list = new ArrayList<>();
		List<Rental> rlist = new ArrayList<>();

		User u = customerRepo.findByNationalId(nationalId);// customerService.findByNationalId(nationalId);

		u.getRentals().forEach(rlist::add);

		for (Rental c : rlist) {
			list.add(c.getCar());
		}

		return list;
	}

	// Method to make entry for a new rental service
	public boolean createRentalEntry(Long userId, Long carId, LocalDate startDate, LocalDate endDate)
			throws DateConflictException, UserStillOnLentException, CarNotAvailException {

		// Validation criteria if not satisfied throws exception
		validateDates(startDate, endDate);
		isUserStillOnLent(userId);
		isCarAvailForRent(carId);
		
		Rental r = new Rental();

		r.setCar(carRepo.findById(userId).get());
		r.setUser(customerRepo.findById(userId).get());
		r.setStartDate(startDate);
		r.setEndDate(endDate);
		r.setReturned(false);

		rentalRepo.save(r);

		return true;
	}

	private void isUserStillOnLent(Long userId) throws UserStillOnLentException {
		// if user has no car rented, null is returned
		if (rentalRepo.isUserOnLent(userId) != null) {
			throw new UserStillOnLentException("This user already has a car on rent!");
		}
	}

	private void isCarAvailForRent(Long carId) throws CarNotAvailException {
		// if the car is available for rent, null is returned
		if (rentalRepo.isCarAvail(carId) != null) {
			throw new CarNotAvailException("This car is not available and is still on rent!");
		}

	}

	private void validateDates(LocalDate startDate, LocalDate endDate) throws DateConflictException {
		if (startDate.isAfter(endDate)) {
			throw new DateConflictException("The Start Date cannot occur after the End Date!");
		}

		if (startDate.isBefore(LocalDate.now())) {
			throw new DateConflictException("The Start Date must be a futur date!");
		}

		if (endDate.isBefore(LocalDate.now())) {
			throw new DateConflictException("The End Date cannot occur prior the Start Date!");
		}

		if (startDate.equals(endDate)) {
			throw new DateConflictException("Start Date and End Date cannot be on the same day!");
		}

	}

	
	//Method to update the entry and release the car from user
	public boolean updateRentalEntry(Long carId, Long userId) throws RentalEntryNotFoundException {

		Rental rtest = rentalRepo.findByUserAndCar(carId, userId);
		if( rtest !=null) {
			rtest.setReturned(true);
			rentalRepo.save(rtest);
			return true;
		}
		else {
			throw new RentalEntryNotFoundException("Cannot find the specified rental entry!");
		}
		
		
	}

	public List<Rental> showAllRental() {
		List<Rental> list = new ArrayList<>();
		
		list = rentalRepo.findAll();
		
		return list;
	}

	public List<Car> carsRentedByCarId(Long carId) {
		List<Car> list = new ArrayList<>();
		List<Rental> rlist = new ArrayList<>();

		Car c = carRepo.findById(carId).get();

		c.getRentals().forEach(rlist::add);

		for (Rental r : rlist) {
			list.add(r.getCar());
		}

		return list;
	}

}
