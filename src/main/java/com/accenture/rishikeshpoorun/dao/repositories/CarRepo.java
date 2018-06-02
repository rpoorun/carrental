package com.accenture.rishikeshpoorun.dao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.rishikeshpoorun.dao.entities.Car;

/**
 * Data Access Only class for the car entities 
 * 
 * Handles the delete of entities as set attributes carDeleted to true 
 * @author rishikesh.poorun
 *
 */

@Repository
public interface CarRepo extends CrudRepository<Car, Long> {
	
	/**
	 * Fetch the Car entry from database for the matching carId and not set as deleted
	 * @param carId
	 * @return Car entity of the matching carId
	 */
	@Query("SELECT c FROM Car c WHERE c.carId=:carId AND c.carDeleted = false")
	public Car findByCarId(@Param("carId") Long carId);
	
	/**
	 * Fetch the Car entry from database for the matching Registration Number and not set as deleted
	 * @param registrationNumber
	 * @return Car entity of the matching Registration Number
	 */
	@Query("SELECT c FROM Car c WHERE c.registrationNumber =:regNum AND c.carDeleted = false")
	public Car findByRegistrationNumber(@Param("regNum") String registrationNumber);

	
	//public void deleteByRegistrationNumber(String registrationNumber);

	/**
	 * Fetch all the car entries from the database which matches the specified model and not set as deleted
	 * @param model
	 * @return List of Car entities
	 */
	@Query("SELECT c FROM Car c WHERE c.model =:model AND c.carDeleted = false")
	public List<Car> findAllByModel(@Param("model") String model);

	/**
	 * Fetch all the car entries from database which matches the specified price and not set as deleted 
	 * @param pricePerDay
	 * @return List of Car entities
	 */
	@Query("SELECT c FROM Car c WHERE c.pricePerDay =:price and c.carDeleted = false")
	public List<Car> findAllByPricePerDay(@Param("price") Double pricePerDay);
	
	/**
	 * Fetch all car entries which are marked as deleted
	 * @return List of deleted car entities
	 */
	@Query("SELECT c FROM Car c WHERE c.carDeleted = true")
	public List<Car> allDeletedCar();

	
	

}
