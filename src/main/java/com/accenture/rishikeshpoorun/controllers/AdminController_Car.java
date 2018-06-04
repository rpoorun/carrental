package com.accenture.rishikeshpoorun.controllers;

import java.util.ArrayList;
import java.util.List;
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
import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dto.RentalDto;
import com.accenture.rishikeshpoorun.dto.TextDto;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;

@Controller
@RequestMapping(value = "/secured/car")
@Secured(value= {"ROLE_ADMIN"})
public class AdminController_Car extends FrontController{
		

	/**
	 * Redirects to the Secured List of Car Page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/all")
	public String showAllCar(Model model) {
		List<Car> list = carService.getAllCars();
		model.addAttribute("carlist", list);
		model.addAttribute("car", new Car());
		
		return "secured_page/carList";
	}

	

	/**
	 * Redirects to the Secured Car profile page, catches the carId from the request
	 * @param carId
	 * @param model
	 * @return Go to Secured_Car profile
	 */
	@GetMapping(value="/read/{carId}")
	public String readCar(@PathVariable("carId") Long carId, Model model) {
		
		try {
			Car c = carService.findById(carId);
			model.addAttribute("car", c);
			model.addAttribute("rentalList", rentalService.allRentalByCarId(carId));
			model.addAttribute("rentalDto", new RentalDto());
			
		} catch ( CarNotFoundException e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		return "secured_page/carProfile";
	}
	
	/**
	 * Redirects to the Secured Car Update Form 
	 * @param carId
	 * @param model
	 * @return Go to Secured_CarForm
	 * Passing model attribute car to prefill the Update Form
	 */
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
	
	
	/**
	 * Handles the delete request for a car entities
	 * @param carId
	 * @param model
	 * @return
	 */
	
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
	
	/**
	 * Redirects requests to the Secured created a car entity form
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/add")
	public String goToAddCarPage(Model model) {

		model.addAttribute("car", new Car());
		return "secured_page/carForm";
	}

	/**
	 * Handles the post request from the Secured Car Form for adding new car
	 * @param c
	 * @param model
	 * @return
	 */
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
					"Failed to add Car: " + c.getRegistrationNumber() + ", Reason: "+e.getMessage());
			return "secured_page/carForm";
		}

	}

	/**
	 * Redirects to the Securey Query for Car entities page
	 * @param model
	 * @return
	 */
	@GetMapping("/query")
	public String goToCarQuery(Model model) {
		model.addAttribute("carlist", carService.getAllCars());
		model.addAttribute("car", new Car());

		return "secured_page/carQuery";

	}
	
	/**
	 * Handles the post requests from the Car query form/row
	 * @param c
	 * @param model
	 * @return
	 */
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
	
	/**
	 * Redirects to the Import CSV Page
	 * @param model
	 * @return
	 */
	@GetMapping("/csvFileTocar")
	public String goToCSVReaderPage(Model model) {
		
		model.addAttribute("textDto", new TextDto());
		return "secured_page/importCarCsv";
	}
	
	/** 
	 * Handles the post request from the Import CSV Page
	 * When using the paste bin or textarea
	 * 
	 */
	@PostMapping("/readTextarea")
	public String readTextareaToCar(@ModelAttribute TextDto textDto, Model model) {
		
		String[] lines = textDto.getText().split("\n");
		try {
			for (int i = 0; i < lines.length; i++) {
				String[] args = lines[i].split(",");
				Car c = new Car();
				c.setRegistrationNumber(args[0]);
				c.setModel(args[1]);
				c.setPricePerDay(Double.parseDouble(args[2]));

				carService.saveCar(c);
			}
		} catch (Exception e) {
			model.addAttribute("status", e.getMessage());
		}
		return showAllCar(model);
	}
	
	/**
	 * Handles the post request from the Import CSV page 
	 * When a csv file is uploaded
	 * 
	 * @param file
	 * @param model
	 * @return
	 */
	@PostMapping("/readFileCarCsv")
	public String readFileCarCsv(@RequestParam("file") MultipartFile file, Model model) {
		List<Car> list;
		try {
			list = ReadFileUtil.importCarCSV(file.getInputStream());
			for (Car c : list) {
				carService.saveCar(c);
			}
		
		} catch (Exception e) {
			model.addAttribute("status", e.getLocalizedMessage());
		}
		
		
		return showAllCar(model);
	}
	
	
	
}
