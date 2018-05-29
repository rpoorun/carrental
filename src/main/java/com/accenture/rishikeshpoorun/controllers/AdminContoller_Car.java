package com.accenture.rishikeshpoorun.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.exceptions.CustomerNotFoundException;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Controller
@RequestMapping(value = "/secured/car")
public class AdminContoller_Car {

	@Autowired
	private CarService carService;

	@Autowired
	private RentalService rentalService;
	
	@GetMapping(value = "/all")
	public String showAllCar(Model model) {
		List<Car> list = new ArrayList<>();
		list = carService.getAllCars();
		model.addAttribute("carlist", list);
		model.addAttribute("car", new Car());
		
		return "secured_page/carList";
	}

	@GetMapping(value = "/add")
	public String goToAddCarPage(Model model) {

		model.addAttribute("car", new Car());
		return "secured_page/carAdd";
	}

	@GetMapping(value="/read/{carId}")
	public String readUser(@PathVariable("carId") Long carId, Model model) {
		
		try {
			Car c = carService.findById(carId);
			List<Car> rentedCar = rentalService.carsRentedByCarId(carId);
			model.addAttribute("car", c);
			model.addAttribute("rentalList", rentedCar);
			model.addAttribute("rent", new Rental());
			
		} catch ( CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		return "secured_page/carProfile";
	}
	@GetMapping(value = "/delete/{carId}")
	public String deleteCar(@PathVariable("carId") Long carId, Model model) {

		try {
			boolean status = carService.deleteCar(carId);
			if (status) {
				model.addAttribute("status", "Car Entry deleted!");
			}

		} catch (CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());

		}

		return showAllCar(model);
	}

	@PostMapping(value = "/add")
	public String goToNewShowAllCar(@ModelAttribute Car c, Model model) {

		try {
			boolean status = carService.createCar(c);
			if (status) {
				model.addAttribute("new", c.getRegistrationNumber());
				return showAllCar(model);
			} else {

				model.addAttribute("status", "Failed to add car " + c.getRegistrationNumber());
				return "secured_page/carAdd";
			}
		}

		catch (Exception e) {

			model.addAttribute("status",
					"Failed to add Car: " + c.getRegistrationNumber() + ", Reason: Car Entry already exists!");
			return "secured_page/carAdd";
		}

	}

	@GetMapping("/query")
	public String goToCarQuery(Model model) {
		List<Car> list = new ArrayList<>();
		list = carService.getAllCars();
		model.addAttribute("carlist", list);
		model.addAttribute("car", new Car());

		return "secured_page/carQuery";

	}

	@PostMapping("/find")
	public String findCar(@ModelAttribute Car c, Model model) {

		List<Car> list = new ArrayList<>();
		try {
			if (!c.getRegistrationNumber().equalsIgnoreCase("")) {
				list.add(carService.findByRegistrationNumber(c.getRegistrationNumber()));
			} else if (!c.getModel().equalsIgnoreCase("")) {
				list = carService.findByModel(c.getModel());
			} else if (c.getPricePerDay() != null) {
				list = carService.findByPricePerDay(c.getPricePerDay());
			}
		} catch (CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
			return "secured_page/carQuery";
		}

		model.addAttribute("carlist", list);
		return "secured_page/carList";
	}
	

	@Profile(value = "dev")
	@PostConstruct
	private void populateCarDatabase() {
		String fileName = "/Users/rishikesh.poorun/OneDrive - Accenture/Spring Boot Project/carrental/src/main/resources/files/carsDev.csv";
		try {
			List<Car> list = carService.readCSVToCar(fileName);
			carService.saveListToDatabase(list);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

}
