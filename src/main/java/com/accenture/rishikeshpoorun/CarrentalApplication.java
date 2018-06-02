package com.accenture.rishikeshpoorun;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.accenture.rishikeshpoorun.dao.repositories.CarRepo;
import com.accenture.rishikeshpoorun.dao.repositories.RentalRepo;
import com.accenture.rishikeshpoorun.dao.repositories.UserRepo;
import com.accenture.rishikeshpoorun.util.PopulateDatabase;

@SpringBootApplication
public class CarrentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrentalApplication.class, args);

	}

	@Bean
	public CommandLineRunner initialize(RentalRepo rentalRepo, CarRepo carRepo, UserRepo userRepo) {
		return (args) -> {
			// Initialize database if empty
			if (carRepo.count() == 0) {
				PopulateDatabase.populateCarDatabase();
			}
			
			if (userRepo.count() == 0) {
				PopulateDatabase.populateUserDatabase();
			}

		};
	}
}
