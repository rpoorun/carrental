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
	public List<Car> allCarsAvailForRent();

	@Query("SELECT u FROM User u WHERE u.userId IN (SELECT user FROM Rental r WHERE r.startDate =:startDate AND r.endDate=:endDate)")
	public List<User> userRentOnPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT r FROM Rental r WHERE r.returned = true")
	public List<Rental> rentedCarReturned();

	/**
	 * SQL query to fetch the car if not available, if car is avail null is returned
	 * 
	 * @param carId
	 * @return Car if Car.carId is not marked as returned or Null if Car.carId is
	 *         marked as returned
	 */
	@Query("SELECT r FROM Rental r WHERE r.returned = false AND r.car = (SELECT c FROM Car c WHERE c.carId =:carId)")
	public Rental isCarAvail(@Param("carId") Long carId);

	/**
	 * SQL query to fetch the user if still on lent, if user has no car rented null
	 * is returned
	 * 
	 * @param userId
	 * @return User if User.userId is currently renting a car Or Null if User.userId
	 *         has not car rented to him
	 */
	@Query("SELECT r FROM Rental r WHERE r.returned = false AND r.user = (SELECT u FROM User u WHERE u.userId =:userId)")
	public Rental isUserOnLent(@Param("userId") Long userId);

	/**
	 * SQL query to fetch the rental entry for matching Car.carId, User.userId and
	 * car not released
	 * 
	 * @param carId
	 * @param userId
	 * @return Rental entity is returned for matching record if found else return Null
	 */
	@Query("SELECT r FROM Rental r WHERE r.returned = false AND r.car = (SELECT c FROM Car c WHERE c.carId =:carId) AND r.user = (SELECT u FROM User u WHERE u.userId =:userId)")
	public Rental findByUserAndCar(@Param("carId") Long carId, @Param("userId") Long userId);

}
