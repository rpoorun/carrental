package com.accenture.rishikeshpoorun.dto;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
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
	private Car car;
	private User user;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean returned;
	private String sex;
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
		} catch (Exception e) {

		}

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

}
