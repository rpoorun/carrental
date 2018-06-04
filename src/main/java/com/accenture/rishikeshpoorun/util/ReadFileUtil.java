package com.accenture.rishikeshpoorun.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.User;

@Component
public class ReadFileUtil {

	public static List<Car> readCSVToCar(String fileName) throws FileNotFoundException, IOException {
		List<Car> list = new ArrayList<>();

		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] args = line.split(",");
			// #registrationNumber, #model, #pricePerDay
			Car e = new Car();
			// e.setCarId(Long.parseLong(args[0]));
			e.setRegistrationNumber(args[0]);
			e.setModel(args[1]);
			e.setPricePerDay(Double.parseDouble(args[2]));
			list.add(e);
		}

		br.close();
		file.close();
		return list;

	}

	public static List<User> readCSVToUser(String fileName) throws FileNotFoundException, IOException {
		List<User> list = new ArrayList<>();

		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] args = line.split(",");
			// #nationalId, #password, #name, #role, #sex, #dob
			User e = new User();
			e.setNationalId(args[0]);
			e.setPassword(args[1]);
			e.setName(args[2]);
			e.setRole(args[3]);
			e.setSex(args[4]);
			e.setDob(LocalDate.parse(args[5]));
			list.add(e);
		}

		br.close();
		file.close();
		return list;
	}

	public static List<Car> importCarCSV(InputStream file) throws NumberFormatException, IOException {
		Reader fr = new InputStreamReader(file);
		BufferedReader br = new BufferedReader(fr);

		List<Car> list = new ArrayList<>();
		String str = null;
		while ((str = br.readLine()) != null) {
			String[] args = str.split(",");
			Car e = new Car();
			e.setRegistrationNumber(args[0]);
			e.setModel(args[1]);
			e.setPricePerDay(Double.parseDouble(args[2]));
			list.add(e);
		}
		br.close();
		fr.close();
		
		return list;
	}
	
	public static List<User> importUserCSV(InputStream file) throws NumberFormatException, IOException {
		Reader fr = new InputStreamReader(file);
		BufferedReader br = new BufferedReader(fr);

		List<User> list = new ArrayList<>();
		String str = null;
		while ((str = br.readLine()) != null) {
			String[] args = str.split(",");
			User e = new User();
			e.setNationalId(args[0]);
			e.setPassword(args[1]);
			e.setName(args[2]);
			e.setRole((!args[3].contains("ROLE_"))? ("ROLE_"+args[3]):args[3]);
			e.setSex(args[4]);
			e.setDob(LocalDate.parse(args[5]));
			list.add(e);
		}
		br.close();
		fr.close();
		
		return list;
	}

}
