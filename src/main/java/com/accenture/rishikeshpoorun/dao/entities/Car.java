package com.accenture.rishikeshpoorun.dao.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="CAR_TABLE")
public class Car {
	
	@Id
	@Column(name="CAR_ID")
	private Long carId;
	
	@Column(name="REGISTRATION_NUMBER")
	private String registrationNumber;
	
	@Column(name="CAR_MODEL")
	private String Model;
	
	@Column(name="PRICE_PER_DAY")
	private Double pricePerDay;
	
	@OneToMany
	private List<Rental> rentals;

	@JsonIgnore
	public List<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(ArrayList<Rental> rentals) {
		this.rentals = rentals;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public Double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(Double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	
}
