package com.accenture.rishikeshpoorun.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.dto.TextDto;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.RentalService;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;

@Controller
@RequestMapping(value = "/secured/car")
@Secured(value= {"ROLE_ADMIN"})
public class AdminController_Car {

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
		return "secured_page/carForm";
	}

	@GetMapping(value="/read/{carId}")
	public String readCar(@PathVariable("carId") Long carId, Model model) {
		
		try {
			Car c = carService.findById(carId);
			List<Rental> rentalList = rentalService.allRentalByCarId(carId);
			List<RentalDto> rentalDtoList = new ArrayList<>();
			for(Rental r: rentalList) {
				rentalDtoList.add(new RentalDto(r));
			}
			model.addAttribute("rentalDtoList", rentalDtoList);
			model.addAttribute("car", c);
			model.addAttribute("rentalList", rentalList);
			model.addAttribute("rentDto", new RentalDto());
			
		} catch ( CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		return "secured_page/carProfile";
	}
	
	
	@GetMapping(value="/update/{carId}")
	public String goToUpdateCarProfile(@PathVariable("carId") Long carId, Model model) {
		try {
			Car c = carService.findById(carId);
			
			model.addAttribute("car", c);
		} catch (CarNotFoundException e) {
		}
		
		model.addAttribute("mode", "Update");
	
		return "secured_page/carForm";
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
			boolean status = carService.saveCar(c);
			if (status) {
				model.addAttribute("new", c.getRegistrationNumber());
				return showAllCar(model);
			} else {

				model.addAttribute("status", "Failed to add car " + c.getRegistrationNumber());
				return "secured_page/carForm";
			}
		}

		catch (Exception e) {

			model.addAttribute("status",
					"Failed to add Car: " + c.getRegistrationNumber() + ", Reason: Car Entry already exists!");
			return "secured_page/carForm";
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
	
	
	@GetMapping("/csvFileTocar")
	public String goToCSVReaderPage(Model model, MultipartFile file) {
		
		List<Car> list;
		try {
			list = ReadFileUtil.importCSV(file.getInputStream());
			for (Car c : list) {
				carService.saveCar(c);
			}
		
		} catch (IOException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		
		model.addAttribute("textDto", new TextDto());
		return "secured_page/importCarCsv";
	}
	
	@PostMapping("/readTextarea")
	public String readTextareaToCar(@ModelAttribute TextDto textDto, Model model) {
		
		String[] lines = textDto.getText().split("\n");
		for(int i = 0; i< lines.length; i++) {
			String[] args = lines[i].split(",");
			Car c = new Car();
			c.setRegistrationNumber(args[0]);
			c.setModel(args[1]);
			c.setPricePerDay(Double.parseDouble(args[2]));
			
			carService.saveCar(c);
		}
				
		return showAllCar(model);
	}
	
	@PostMapping("/readFileCarCsv")
	public String readFileCarCsv(@RequestParam("file") CommonsMultipartFile FileData, Model model) {
		
		return showAllCar(model);
	}
	
	
	@Profile(value = "dev")
	@PostConstruct
	private void populateCarDatabase() {
		String fileName = "/Users/rishikesh.poorun/OneDrive - Accenture/Spring Boot Project/carrental/src/main/resources/files/carsDev.csv";
		try {
			List<Car> list = carService.readCSVToCar(fileName);
			for (Car c : list) {
				carService.saveCar(c);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	
}
