package com.accenture.rishikeshpoorun.dao.repositories;

import org.springframework.data.repository.CrudRepository;

import com.accenture.rishikeshpoorun.dao.entities.Car;

public interface CarRepo extends CrudRepository<Car, Long> {
	
	public Car findByRegistrationNumber(String registrationNumber);

	public void deleteByRegistrationNumber(String registrationNumber);

}
