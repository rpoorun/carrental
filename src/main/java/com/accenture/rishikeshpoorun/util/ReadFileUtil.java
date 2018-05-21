package com.accenture.rishikeshpoorun.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.accenture.rishikeshpoorun.dao.entities.Car;

@Component
public class ReadFileUtil {
	
	public static List<Car> readCSVToCar() throws FileNotFoundException, IOException{
		List<Car> list = new ArrayList<>();
		
		FileReader file = new FileReader("/Users/rishikesh.poorun/OneDrive - Accenture/Spring Boot Project/carrental/src/main/resources/files/cars.csv");
		BufferedReader br = new BufferedReader(file);
		String line = null;
		while((line = br.readLine()) != null) {
			String[] args = line.split(",");
			// #carId, #registrationNumber, #model, #pricePerDay
			Car e = new Car();
			e.setCarId(Long.parseLong(args[0]));
			e.setRegistrationNumber(args[1]);
			e.setModel(args[2]);
			e.setPricePerDay(Double.parseDouble(args[3]));
			list.add(e);
		}
		
		br.close();
		file.close();
		return list;
		
	}

	
}
