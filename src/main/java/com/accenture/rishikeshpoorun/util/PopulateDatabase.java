package com.accenture.rishikeshpoorun.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.CarRepo;
import com.accenture.rishikeshpoorun.services.CustomerService;

@Component
public class PopulateDatabase {

	private static CarRepo carRepo;
	private static CustomerService userService;

	@Autowired
	public PopulateDatabase(CarRepo carRepo, CustomerService userService) {
		PopulateDatabase.carRepo = carRepo;
		PopulateDatabase.userService = userService;
	}

	public static void populateCarDatabase() {
		// String fileName = "/carrental/src/main/resources/files/carsDev.csv";
		// try {
		// List<Car> list = carService.readCSVToCar(fileName);
		// for (Car c : list) {
		// carService.saveCar(c);
		// }
		// } catch (FileNotFoundException e) {
		// } catch (Exception e) {
		// }

		List<Car> l = new ArrayList<>();
		l.add(new Car("123-Mar-09", "Nissan March", 900.0));
		l.add(new Car("123-Jan-17", "Mazda axela", 500.0));
		l.add(new Car("123-Dec-10", "Nissan Caravan", 700.0));
		l.add(new Car("123-Nov-08", "Ashok Leyland", 1000.0));
		l.add(new Car("321-Mar-09", "Nissan GTR", 1500.0));
		l.add(new Car("097-Jan-08", "Honda CR-V", 700.0));
		l.add(new Car("348-Mar-98", "Ford F-150", 550.0));
		l.add(new Car("309-Dec-00", "Subaru Outback", 340.0));
		l.add(new Car("830-Jan-00", "Honda Accord", 450.0));
		l.add(new Car("728-Jun-09", "Mazda CX-5", 1200.0));
		l.add(new Car("920-Sep-17", "Toyota Highlander", 2300.0));
		l.add(new Car("922-Oct-18", "Honda Pilot", 1300.0));
		l.add(new Car("923-Oct-18", "Honda HR-V", 1400.0));
		l.add(new Car("303-Jan-07", "Ford Mustrang", 750.0));
		l.add(new Car("042-Feb-09", "Nissan Sentra", 550.0));
		l.add(new Car("901-Mar-10", "Toyota Camry", 650.0));
		l.add(new Car("511-Jul-11", "Honda Civi", 550.0));
		l.add(new Car("662-Dec-12", "Ford Escape", 1600.0));
		l.add(new Car("99-Dec-09", "Jeep Grand", 2000.0));
		l.add(new Car("89-Apr-14", "Acura RDX", 2000.0));
		l.add(new Car("1208-Apr-18", "Lexus RX", 3500.0));
		l.add(new Car("198-May-18", "Hyundai Sonata", 2000.0));
		l.add(new Car("904-May-12", "Jeep Wrangler", 1000.0));
		l.add(new Car("903-May-14", "Nissan Rouge", 340.0));
		l.add(new Car("808-Dec-13", "Ford Edge", 500.0));
		l.add(new Car("101-Jan-10", "Kia Sorento", 350.0));
		l.add(new Car("440-Nov-15", "Toyota Prius", 550.0));
		l.add(new Car("745-Apr-17", "Ford Fusion", 780.0));
		l.add(new Car("111-Dec-11", "Audi Q5", 1700.0));
		l.add(new Car("222-Jan-12", "Audi A3", 3300.0));
		l.add(new Car("309-Jun-18", "BMW x5", 2300.0));
		l.add(new Car("012-Jul-17", "BMW x3", 3400.0));

		for (Car c : l) {
			carRepo.save(c);
		}
	}

	public static void populateUserDatabase() {

		// String fileName = "carrental/src/main/resources/files/usersDev.csv";
		// try {
		// List<User> list = customerService.readCSVToCar(fileName);
		// customerService.saveListToDatabase(list);
		// } catch (FileNotFoundException e) {
		// } catch (IOException e) {
		// }

		User poorun = new User();
		poorun.setName("poorun");
		poorun.setNationalId("poorun");
		poorun.setPassword("poorun");
		poorun.setRole("ROLE_ADMIN");
		userService.saveCustomer(poorun);

		User admin = new User();
		admin.setName("admin");
		admin.setNationalId("admin");
		admin.setPassword("1234");
		admin.setRole("ROLE_ADMIN");
		userService.saveCustomer(admin);

		User customer1 = new User();
		customer1.setName("customer");
		customer1.setNationalId("customer");
		customer1.setPassword("1234");
		customer1.setRole("ROLE_CUSTOMER");
		userService.saveCustomer(customer1);

		User john = new User();
		john.setName("john");
		john.setNationalId("john");
		john.setPassword("1234");
		john.setRole("ROLE_CUSTOMER");
		userService.saveCustomer(john);

		User axelle = new User();
		axelle.setName("Axelle");
		axelle.setNationalId("Axelle");
		axelle.setPassword("1234");
		axelle.setRole("ROLE_CUSTOMER");
		userService.saveCustomer(axelle);

	}

}
