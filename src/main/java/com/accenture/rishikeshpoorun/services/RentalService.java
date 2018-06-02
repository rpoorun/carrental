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

	public List<User> userRentOnDates(LocalDate startDate, LocalDate endDate) {
		return rentalRepo.userRentOnDates(startDate, endDate);
	}

	public List<User> userRentBetweenPeriod(LocalDate startDate, LocalDate endDate) {
		return rentalRepo.userRentBetweenPeriod(startDate, endDate);
	}

	public List<Rental> rentedBetweenPeriod(LocalDate startDate, LocalDate endDate) {
		return rentalRepo.rentalBetweenPeriod(startDate, endDate);
	}

	public List<Rental> rentedOnDates(LocalDate startDate, LocalDate endDate) {
		return rentalRepo.rentalOnDates(startDate, endDate);
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
	// given User ID
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
		isUserStillOnRent(userId);
		isCarAvailForRent(carId);
		
		Long days = ChronoUnit.DAYS.between(startDate, endDate);
		Double charges = carRepo.findById(carId).get().getPricePerDay() * days;
		Rental r = new Rental();

		r.setCar(carRepo.findById(carId).get());
		r.setUser(customerRepo.findById(userId).get());
		r.setStartDate(startDate);
		r.setEndDate(endDate);
		r.setCharges(charges);
		r.setReturned(false);

		rentalRepo.save(r);

		return true;
	}
	
	/**
	 * Checks if the user has an open rental entry for a car not yet returned
	 * @param userId
	 * @return <b>False</b> if the user is not renting a car else returns, else <b>Throws</b>
	 * @throws UserStillOnLentException
	 */
	public boolean isUserStillOnRent(Long userId) throws UserStillOnLentException {
		// if user has no car rented, null is returned
		if (rentalRepo.isUserOnLent(userId) != null) {

			String status = String.format("This User [Name: %s NIC: %s] is already renting a car [%s] till: %s",
					customerRepo.findById(userId).get().getName(), 
					customerRepo.findById(userId).get().getNationalId(),
					rentalRepo.isUserOnLent(userId).getCar().getRegistrationNumber(),
					rentalRepo.isUserOnLent(userId).getEndDate());

			throw new UserStillOnLentException(status);
		}
		else {
			return false;
		}
	}

	private void isCarAvailForRent(Long carId) throws CarNotAvailException {
		// if the car is available for rent, null is returned
		if (rentalRepo.isCarAvail(carId) != null) {

			String status = String.format("This car [Registration: %s] is not available and on rent till: %s",
					carRepo.findById(carId).get().getRegistrationNumber(), rentalRepo.isCarAvail(carId).getEndDate());

			throw new CarNotAvailException(status);
		}

	}

	private void validateDates(LocalDate startDate, LocalDate endDate) throws DateConflictException {
		if (startDate.isAfter(endDate)) {
			throw new DateConflictException(
					"The Start Date [" + startDate.toString() + "] cannot occur after the End Date ["+ endDate.toString()+"]!");
		}

		if (startDate.isBefore(LocalDate.now())) {
			throw new DateConflictException("The Start Date [" + startDate.toString() + "] must be a futur date!");
		}

		if (endDate.isBefore(LocalDate.now())) {
			throw new DateConflictException(
					"The End Date [" + endDate.toString() + "] cannot occur prior the Start Date!");
		}

		if (startDate.equals(endDate)) {
			throw new DateConflictException("Start Date [" + startDate.toString() + "] and End Date ["
					+ endDate.toString() + "] cannot be on the same day!");
		}

	}

	// Method to update the entry and release the car from user
	public boolean releaseCar(Long rentalId) throws RentalEntryNotFoundException {

		
		Rental rtest = rentalRepo.findById(rentalId).get();
		if (rtest != null) {
			rtest.setReturned(true);
			rentalRepo.save(rtest);
			return true;
		} else {
			throw new RentalEntryNotFoundException("Cannot find the specified rental entry!");
		}

	}

	public void updateRentalEntry(Long rentalId) {
		// TODO Auto-generated method stub
		//to changes changes 
		//calculate new charges

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

	public Rental findByRentalId(Long rentalId) {

		return rentalRepo.findById(rentalId).get();
	}

	public List<Rental> allRentalByCarId(Long carId) {

		List<Rental> list = rentalRepo.findAllByCarId(carId);

		return list;
	}

	public List<Rental> allRentalByUserId(Long userId) {
		List<Rental> list = rentalRepo.findAllByUserId(userId);

		return list;
	}

	public void saveRental(Rental r) {

		rentalRepo.save(r);

	}

	public List<Rental> allCurrentCarOnRent() {
		// TODO Auto-generated method stub
		return rentalRepo.findAllNotReturned();
	}

}
