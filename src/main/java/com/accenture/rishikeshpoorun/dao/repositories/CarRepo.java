package com.accenture.rishikeshpoorun.dao.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.accenture.rishikeshpoorun.dao.entities.Car;

public interface CarRepo extends CrudRepository<Car, Long> {
	
	public Car findByRegistrationNumber(String registrationNumber);

	public void deleteByRegistrationNumber(String registrationNumber);

	public List<Car> findAllByModel(String model);

	public List<Car> findAllByPricePerDay(Double pricePerDay);

}
