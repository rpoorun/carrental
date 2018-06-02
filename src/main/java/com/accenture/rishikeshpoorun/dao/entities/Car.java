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
	
	@Column(name="REGISTRATION_NUMBER", unique=true, nullable=false)
	private String registrationNumber;
	
	@Column(name="CAR_MODEL")
	private String model;
	
	@Column(name="PRICE_PER_DAY", columnDefinition="Decimal(10,2)")
	private Double pricePerDay;
	
	@OneToMany(targetEntity=Rental.class , mappedBy="car", fetch=FetchType.EAGER)
	private List<Rental> rentals;
	
	@Column(name="CAR_DELETED")
	private boolean carDeleted;

	// Default Constructor
	public Car() {
	}

	// Parametrized Constructor
	public Car(String registrationNumber, String model, Double pricePerDay) {
		this.registrationNumber = registrationNumber;
		this.model = model;
		this.pricePerDay = pricePerDay;
		
	}

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
	public boolean isCarDeleted() {
		return carDeleted;
	}

	public void setCarDeleted(boolean carDeleted) {
		// any other values reset the attribute to false
		if(carDeleted) {
			this.carDeleted = true;
		}
		else {
			this.carDeleted = false;
		}
		
	}

	@Override
	public String toString() {
		return "Car [carId=" + carId + ", registrationNumber=" + registrationNumber + ", model=" + model
				+ ", pricePerDay=" + pricePerDay + ", rentals=" + rentals + ", carDeleted=" + carDeleted + "]";
	}
	
}
