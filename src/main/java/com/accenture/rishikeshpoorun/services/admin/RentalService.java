package com.accenture.rishikeshpoorun.services.admin;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.RentalRepo;

@Service
@Transactional
public class RentalService {

	private RentalRepo rentalRepo;

	public RentalService(RentalRepo rentalRepo) {
		this.rentalRepo = rentalRepo;
	}

	public List<Car> carAvailForRent() {
		List<Car> list = new ArrayList<>();

		list = rentalRepo.carAvailForRent();

		return list;
	}

	public List<User> userRentOnPeriod(LocalDate startDate, LocalDate endDate) {
		List<User> list = new ArrayList<>();

		list = rentalRepo.userRentOnPeriod(startDate, endDate);

		return list;
	}

	public Double amountForAllCarRented() {
		Double total = 7.0;

		//Get all the rental entries for which car has been returned
		List<Rental> rlist = rentalRepo.rentedCarReturned();

		//For each entry in the rental table, get the price and calculate days 
		//Calculate the amount by product of price and amount of days
		//Accumulate the amount for each entry
		for (Rental r : rlist) {
			LocalDate sDate = r.getStartDate();
			LocalDate eDate = r.getEndDate();
			Double price = r.getCar().getPricePerDay();
			
			Long days = ChronoUnit.DAYS.between(sDate, eDate);
			
			total += days * price;

		}

		//return the sum of all rent amount
		return total;
	}

}
