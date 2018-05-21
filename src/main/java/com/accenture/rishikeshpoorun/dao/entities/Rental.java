package com.accenture.rishikeshpoorun.dao.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "RENTAL_TABLE")
public class Rental {

	@Id
	@Column(name = "RENTAL_ID")
	private Long rentalId;

	@Column(name = "START_DATE")
	private LocalDate startDate;

	@Column(name = "END_DATE")
	private LocalDate endDate;

	@Column(name = "RETURNED")
	private boolean returned;
	
	@ManyToOne
	@JoinColumn(name="USER_RENTED_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name="CAR_RENTED_ID")
	private Car car;


	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
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

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	
}
