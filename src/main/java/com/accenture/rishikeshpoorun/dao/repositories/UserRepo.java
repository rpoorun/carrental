package com.accenture.rishikeshpoorun.dao.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.rishikeshpoorun.dao.entities.User;

/**
 * Data Access Only class for the user entities 
 * 
 * Handles the delete of entities as set attributes userDeleted to true 
 * @author rishikesh.poorun
 *
 */
@Repository
public interface UserRepo extends CrudRepository<User, Long> {
	
	/**
	 * Fetch the User entry from database for the matching userId and not set as deleted
	 * @param nationalId
	 * @return User entity of the matching userId
	 */
	@Query("SELECT u FROM User u WHERE u.userId =:userId AND u.userDeleted=false")
	public User findByUserId(@Param("userId") Long userId);
	
	/**
	 * Fetch the User entry from database for the matching nationalId and not set as deleted
	 * @param nationalId
	 * @return User entity of the matching nationalId
	 */
	@Query("SELECT u FROM User u WHERE u.nationalId=:nationalId AND u.userDeleted= false")
	public User findByNationalId(@Param("nationalId")String nationalId);


//	public void deleteByNationalId(String nationalId);

	@Query("SELECT u FROM User u WHERE u.name=:name AND u.userDeleted=false")
	public List<User> findAllByName(@Param("name") String name);
	
	@Query("SELECT u FROM User u WHERE u.role=:role AND u.userDeleted=false")
	public List<User> findAllByRole(@Param("role") String role);
	
	@Query("SELECT u FROM User u WHERE u.sex=:sex AND u.userDeleted=false")
	public List<User> findAllBySex(@Param("sex") String sex);
	
	@Query("SELECT u FROM User u WHERE u.dob=:dob and u.userDeleted=false")
	public List<User> findAllByDob(@Param("dob") LocalDate dob);
	

}
