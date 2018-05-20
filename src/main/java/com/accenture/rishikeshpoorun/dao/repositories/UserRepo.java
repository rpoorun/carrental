package com.accenture.rishikeshpoorun.dao.repositories;

import org.springframework.data.repository.CrudRepository;

import com.accenture.rishikeshpoorun.dao.entities.User;

public interface UserRepo extends CrudRepository<User, Long> {
	
	public User findByNationalId(String nationalId);

	public void deleteByNationalId(String nationalId);

}
