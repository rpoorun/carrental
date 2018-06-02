package com.accenture.rishikeshpoorun.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.repositories.CarRepo;
import com.accenture.rishikeshpoorun.exceptions.CarNotFoundException;
import com.accenture.rishikeshpoorun.util.ReadFileUtil;
/**
 * Service layer for the Car entity
 * 
 * NOTE: the delete of a referenced entry is managed as setting the attribute carDeleted to true
 * 
 * @author rishikesh.poorun
 *
 */
@Service
@Transactional
public class CarService {
	
	Logger logger = Logger.getLogger(CarService.class);

	private CarRepo carRepo;

	public CarService(CarRepo carRepo) {
		this.carRepo = carRepo;
	}

	/**
	 * Persist the Car entity to the database upon validation
	 * 
	 * If car already, compare prices and persists
	 * 
	 * @param car
	 * @return <b>True</b> if entity car is save or updated, <b>False</b> if entity already exists
	 * @throws CarNotFoundException 
	 */
	public boolean saveCar(Car car) throws CarNotFoundException {

		Car fetch = carRepo.findByRegistrationNumber(car.getRegistrationNumber());

		if (fetch != null) {
			fetch.setPricePerDay(car.getPricePerDay());
			//update if already exists
			carRepo.save(fetch);
			logger.info("Car entity has been updated");
			return true;
		}
		
		if(fetch == null) {
			// if entry does not exists, create new entry
			logger.warn("Car entity has been created");
			carRepo.save(car);
		}
		
		return true;
		
		
	}

//	/**
//	 * Persist updated details for the car
//	 * 
//	 * @param fetch
//	 * @param car
//	 * @return <b>True</b> if update is successful, else <b>False</b> if failed to update
//	 * @throws CarNotFoundException
//	 */
//	public boolean updateCar(Car fetch, Car car) throws CarNotFoundException {
//		if(carRepo.existsById(car.getCarId())) {	
//			//comapre and perform update the new rental price
//			if(fetch.getPricePerDay() != car.getPricePerDay()) {
//				carRepo.save(car);
//				return true;
//			}
//			else if(fetch.getPricePerDay() == car.getPricePerDay()) {
//				// if all attributes are matching
//				return false;
//			}
//			
//		} else {
//			throw new CarNotFoundException("Cannot find car of specified profile!");
//		}
//		return true;
//	}
	
	
	/**
	 * Delete the car specified by registrationNumber 
	 * <b>Note</b> The entry is not deleted for cascade deletion issues, only the 
	 * status carDelete is set true, thus entry is ignore henceforth
	 * 
	 * @param registrationNumber
	 * @return <b>True</b> if the entry is set as deleted, else <b>False</b> if failed to mark as delete
	 * @throws CarNotFoundException
	 */
	public boolean deleteCar(String registrationNumber) throws CarNotFoundException {
		Car c = carRepo.findByRegistrationNumber(registrationNumber);
			if(c !=null) {
				
			return deleteCar(c.getCarId());
			}
			else {
				logger.warn("Failed to perform the set delete operation");
				return false;
			}
		
	}

	/**
	 * Delete the car specified by carId 
	 * </br><b>Note</b> The entry is not deleted for cascade deletion issues, only the 
	 * status carDelete is set true, thus entry is ignore henceforth
	 * </br><b>Note</b> The Registration Number of the car entity is update to "DELETE" 
	 * 
	 * @param carId
	 * @return <b>True</b> if the entry is set as deleted, else <b>False</b> if failed to mark as delete
	 * @throws CarNotFoundException
	 */
	public boolean deleteCar(Long carId) throws CarNotFoundException {
		
		if (carRepo.existsById(carId)) {
			Car c = new Car();
			//carRepo.deleteById(carId);
			c.setCarDeleted(true);
			//Update the Registration Number to 'Deleted' which displays in the rentalList page
			String oldRegNum = c.getRegistrationNumber();
			c.setRegistrationNumber("Deleted["+oldRegNum+"]");
			carRepo.save(c);
		}
		else {
			logger.error("Car not found!");
			throw new CarNotFoundException("Cannot find the specified car");
		}
		logger.info("Delete operation performed successfully");
		return true;
	}

	/**
	 * Fetch the Car entity for the specified Car ID
	 * @param carId
	 * @return Car entity for the specified carId
	 * @throws CarNotFoundException
	 */
	public Car findById(Long carId) throws CarNotFoundException {

		Car car = carRepo.findByCarId(carId);

		if (car == null) {
			
			if(carRepo.existsById(carId)) {
				//This filter the database if car existed and deleted 
				logger.warn("Cannot perform further operation on an already removed entity");
				throw new CarNotFoundException("This car is removed and no longer available");
			}
			logger.error("Cannot find car!");
			throw new CarNotFoundException("Cannot find car by the specified ID");
		}

		return car;
	}

	/**
	 * Fetch the Car entity for the specified Registration Number
	 * @param carId
	 * @return Car entity for the specified registrationNumber
	 * @throws CarNotFoundException
	 */
	public Car findByRegistrationNumber(String registrationNumber) throws CarNotFoundException {
		Car car = carRepo.findByRegistrationNumber(registrationNumber);

		if (car == null) {
			
			throw new CarNotFoundException("Cannot find car by the specified Registration Number");
		}

		return car;
	}

	/**
	 * @param model
	 * @return List of Car entities filtered by the same model attribute
	 * @throws CarNotFoundException
	 */
	public List<Car> findByModel(String model) throws CarNotFoundException {
		List<Car> list = carRepo.findAllByModel(model);

		if (list.isEmpty()) {
			throw new CarNotFoundException("Cannot find car by the specified Model Name");
		}

		return list;
	}

	/**
	 * @param pricePerDay
	 * @return List of Car entities filtered by the same rental price attribute
	 * @throws CarNotFoundException
	 */
	public List<Car> findByPricePerDay(Double pricePerDay) throws CarNotFoundException {
		List<Car> list = carRepo.findAllByPricePerDay(pricePerDay);

		if (list.isEmpty()) {
			throw new CarNotFoundException("Cannot find car by the specified Rental Price");
		}

		return list;
	}

	/**
	 * @return List of the active car entities from the database
	 */
	public List<Car> getAllCars() {

		Iterable<Car> fetch = carRepo.findAll();
		
		List<Car> list = new ArrayList<>();
		// filter out all not deleted cars
		for(Car c: fetch) {
			if(!c.isCarDeleted())
			{
				list.add(c);
			}
		}
		
		return list;
	}

	/**
	 * Method to call the CSV Reader Utility 
	 * @param fileName
	 * @return List of Car entities read from the CSV File
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Car> readCSVToCar(String fileName) throws FileNotFoundException, IOException {

		return ReadFileUtil.readCSVToCar(fileName);
	}

}
