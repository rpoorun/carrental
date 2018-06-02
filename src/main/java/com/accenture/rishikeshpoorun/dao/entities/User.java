package com.accenture.rishikeshpoorun.dao.entities;

import java.time.LocalDate;
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="USER_TABLE")
public class User {
	
	
	@Id
	@Column(name="USER_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userId;
	
	@Column(name="NATIONAL_ID", nullable=false, unique=true)
	private String nationalId;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ROLE")
	private String role;
	
	@Column(name="GENDER")
	private String sex;
	
	@Column(name="DATE_OF_BIRTH")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	@OneToMany (targetEntity=Rental.class, mappedBy="user", fetch=FetchType.EAGER)
	private List<Rental> rentals;
	
	@Column(name="USER_DELETED")
	private boolean userDeleted = false;
	
	// Default Constructor 
	public User() {
	}
	
	
	// Parametrized Constructor 
	public User(String nationalId, String password, String name, String role, String sex, LocalDate dob) {
		this.nationalId = nationalId;
		this.password = password;	
		this.name = name;
		this.role = role;
		this.sex = sex;
		this.dob = dob;
	}

	public List<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(ArrayList<Rental> rentals) {
		this.rentals = rentals;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRentals(List<Rental> rentals) {
		this.rentals = rentals;
	}
	
	
	

	public boolean isUserDeleted() {
		return userDeleted;
	}


	public void setUserDeleted(boolean userDeleted) {
		if(userDeleted) {
			this.userDeleted = true;
		}
		else {
			this.userDeleted= false;
		}
	}


	@Override
	public String toString() {
		return "User [userId=" + userId + ", nationalId=" + nationalId + ", password=" + password + ", name=" + name
				+ ", role=" + role + ", sex=" + sex + ", dob=" + dob + ", rentals=" + rentals + ", userDeleted="
				+ userDeleted + "]";
	}


	
	
	
	

}
