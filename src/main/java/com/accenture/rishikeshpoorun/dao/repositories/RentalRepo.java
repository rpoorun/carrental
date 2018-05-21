package com.accenture.rishikeshpoorun.dao.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;

public interface RentalRepo extends JpaRepository<Rental, Long> {

	@Query("SELECT c FROM Car c WHERE c.carId NOT IN (SELECT car FROM Rental r WHERE r.returned = false)")
	public List<Car> carAvailForRent();
	
	@Query("SELECT u FROM User u WHERE u.userId IN (SELECT user FROM Rental r WHERE r.startDate =:startDate AND r.endDate=:endDate)")
	public List<User> userRentOnPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	@Query("SELECT r FROM Rental r WHERE r.returned = false")
	public List<Rental> rentedCarReturned();

}
