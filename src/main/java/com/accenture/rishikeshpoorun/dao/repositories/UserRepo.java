package com.accenture.rishikeshpoorun.dao.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.accenture.rishikeshpoorun.dao.entities.User;

public interface UserRepo extends CrudRepository<User, Long> {
	
	public User findByNationalId(String nationalId);

	public void deleteByNationalId(String nationalId);

	public List<User> findAllByName(String name);
	
	public List<User> findAllByRole(String role);
	
	public List<User> findAllBySex(String sex);
	
	public List<User> findAllByDob(LocalDate dob);
	

}
