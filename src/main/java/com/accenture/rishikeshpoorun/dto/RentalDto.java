package com.accenture.rishikeshpoorun.dto;

import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.services.CarService;
import com.accenture.rishikeshpoorun.services.CustomerService;
import com.accenture.rishikeshpoorun.services.RentalService;

@Component
public class RentalDto {

	@Autowired
	private RentalService rentalService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CarService carService;

	private Long rentalId;
	private Long carId;
	private Car car;
	private User user;
	private boolean specificDates;
	private Long userId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private boolean returned;
	private Double charges;
	private String role;

	private String sex;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	private String nationalId;
	private String registrationNumber;
	private String model;
	private Double pricePerDay;
	private String name;

	public RentalDto() {

	}

	public RentalDto(Long rentalId) {
		Rental rental = rentalService.findByRentalId(rentalId);
		new RentalDto(rental);
	}

	public RentalDto(Rental rental) {
		try {
			this.car = rental.getCar();
			this.user = rental.getUser();
			this.startDate = rental.getStartDate();
			this.endDate = rental.getEndDate();
			this.returned = rental.isReturned();
			this.nationalId = customerService.findById(user.getUserId()).getNationalId();
			this.registrationNumber = carService.findById(car.getCarId()).getRegistrationNumber();
			this.model = carService.findById(car.getCarId()).getModel();
			this.pricePerDay = carService.findById(car.getCarId()).getPricePerDay();
			this.name = customerService.findById(user.getUserId()).getName();
			this.sex = customerService.findById(user.getUserId()).getSex();
			this.dob = customerService.findById(user.getUserId()).getDob();
			this.role = customerService.findById(user.getUserId()).getRole();
		} catch (Exception e) {

		}

	}
	
	/**
	 * Creates a Dummy DTO populated with User Details
	 * @param authenticatedDetails
	 */
	public RentalDto(User authenticatedDetails) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.nationalId = user.getNationalId();
		this.dob = user.getDob();
		this.role = user.getRole();
		this.sex = user.getSex();
		
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isSpecificDates() {
		return specificDates;
	}

	public void setSpecificDates(boolean specificDates) {
		this.specificDates = specificDates;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(String returned) {
		if(returned.equalsIgnoreCase("True")) {
			this.returned = true;
		}
		else if(returned.equalsIgnoreCase("False")) {
			this.returned = false;
		}
	}
	
	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(Double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Double getCharges() {
		return charges;
	}

	public void setCharges(Double charges) {
		this.charges = charges;
	}

	@Override
	public String toString() {
		return "RentalDto [rentalService=" + rentalService + ", customerService=" + customerService + ", carService="
				+ carService + ", rentalId=" + rentalId + ", carId=" + carId + ", car=" + car + ", user=" + user
				+ ", specificDates=" + specificDates + ", userId=" + userId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", returned=" + returned + ", charges=" + charges + ", role=" + role + ", sex=" + sex
				+ ", dob=" + dob + ", nationalId=" + nationalId + ", registrationNumber=" + registrationNumber
				+ ", model=" + model + ", pricePerDay=" + pricePerDay + ", name=" + name + "]";
	}
	
	
}
