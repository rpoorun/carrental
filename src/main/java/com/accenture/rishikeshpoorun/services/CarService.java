package com.accenture.rishikeshpoorun.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.CarRepo;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;

@Service
@Transactional
public class CarService {

	private CarRepo carRepo;

	public CarService(CarRepo carRepo) {
		this.carRepo = carRepo;
	}

	public boolean saveCar(Car car) {

		Car fetch = carRepo.findByRegistrationNumber(car.getRegistrationNumber());

		if (fetch != null) {
			fetch.setModel(car.getModel());
			
		} else if (carRepo.save(car) == null) {
			return false;
		} 
			return true;
		
	}

	public boolean updateCar(Car car) throws CarNotFoundException {

		if (carRepo.findByRegistrationNumber(car.getRegistrationNumber()) != null) {
			carRepo.save(car);
		} else {
			throw new CarNotFoundException("Cannot find car of specified profile!");
		}

		return true;
	}

	public boolean deleteCar(String registrationNumber) throws CarNotFoundException {

		if (carRepo.findByRegistrationNumber(registrationNumber) != null) {
			carRepo.deleteByRegistrationNumber(registrationNumber);
		}

		else {
			throw new CarNotFoundException("Cannot find car of specified registration number");
		}

		return true;
	}

	public boolean deleteCar(Long carId) throws CarNotFoundException {
		if (carRepo.findById(carId) != null) {
			carRepo.deleteById(carId);
			;
		}

		else {
			throw new CarNotFoundException("Cannot find car of specified registration number");
		}

		return true;
	}

	public Car findById(Long carId) throws CarNotFoundException {

		Car car = carRepo.findById(carId).get();

		if (car == null) {
			throw new CarNotFoundException("Cannot find car by the specified ID");
		}

		return car;
	}

	public Car findByRegistrationNumber(String registrationNumber) throws CarNotFoundException {
		Car car = carRepo.findByRegistrationNumber(registrationNumber);

		if (car == null) {
			throw new CarNotFoundException("Cannot find car by the specified Registration Number");
		}

		return car;
	}

	public List<Car> findByModel(String model) throws CarNotFoundException {
		List<Car> list = carRepo.findAllByModel(model);

		if (list.isEmpty()) {
			throw new CarNotFoundException("Cannot find car by the specified Model Name");
		}

		return list;
	}

	public List<Car> findByPricePerDay(Double pricePerDay) throws CarNotFoundException {
		List<Car> list = carRepo.findAllByPricePerDay(pricePerDay);

		if (list.isEmpty()) {
			throw new CarNotFoundException("Cannot find car by the specified Rental Price");
		}

		return list;
	}

	public List<Car> getAllCars() {

		List<Car> list = new ArrayList<>();

		carRepo.findAll().forEach(list::add);

		return list;
	}

	public List<Car> readCSVToCar(String fileName) throws FileNotFoundException, IOException {

		return ReadFileUtil.readCSVToCar(fileName);
	}

	public void saveListToDatabase(List<Car> list) {

		for (Car c : list) {
			carRepo.save(c);
		}
	}

}
