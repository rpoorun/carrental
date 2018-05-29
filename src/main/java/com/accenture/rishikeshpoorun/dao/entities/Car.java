package com.accenture.rishikeshpoorun.dao.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
@Table(name="CAR_TABLE")
public class Car {
	
	@Id
	@Column(name="CAR_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long carId;
	
	@Column(name="REGISTRATION_NUMBER", unique=true)
	private String registrationNumber;
	
	@Column(name="CAR_MODEL")
	private String model;
	
	@Column(name="PRICE_PER_DAY")
	private Double pricePerDay;
	
	@OneToMany(targetEntity=Rental.class , mappedBy="car", fetch=FetchType.EAGER)
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

	
}
