package com.accenture.rishikeshpoorun.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.services.CarService;

@RestController
@RequestMapping("/rest/car")
public class AdminController_car {

	@Autowired
	private CarService carService;

	@PostMapping("/add")
	public String createCar(@RequestBody Car car) {

		try {
			boolean status = carService.saveCar(car);

			if (status) {
				return "Car has been created!";
			}

			else {

				return "Failed to create the car!";
			}
		} catch (Exception e) {
			return "Failed to create the car!";
		}
	}

	@PostMapping("/update")
	public String updateCar(@RequestBody Car car) {

		boolean status;
		try {
			status = carService.saveCar(car);

			if (status) {
				return "Car has been created!";
			}

			else {

				return "Failed to update the car's details!";
			}
		} catch ( CarNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "Failed to update the car profile!";
		}

	}

	@DeleteMapping
	public String deleteCar(@RequestBody Car car) {

		boolean status;

		try {
			status = carService.deleteCar(car.getRegistrationNumber());
			if (status) {
				return "Customer has been deleted!";
			}

			else {

				return "Failed to delete the customer's details!";
			}

		} catch (CarNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "Failed to delete the car profile!";
		}
	}

	@GetMapping("/all")
	public List<Car> showAllCar() {

		return carService.getAllCars();
	}
	
	@GetMapping("/readcsv")
	public List<Car> readCSVToCar(){
		
		List<Car> list = new ArrayList<>();
		
		try {
			String fileName = "/Users/rishikesh.poorun/OneDrive - Accenture/Spring Boot Project/carrental/src/main/resources/files/cars.csv";
			list = carService.readCSVToCar(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
