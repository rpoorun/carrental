package com.accenture.rishikeshpoorun.dao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;

public interface RentalRepo extends JpaRepository<Rental, Long> {

	@Query("SELECT c FROM Car c WHERE c.carId NOT IN (SELECT car FROM Rental r WHERE r.returned = false)")
	public List<Car> carAvailForRent();

}
