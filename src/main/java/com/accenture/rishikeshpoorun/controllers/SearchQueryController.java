package com.accenture.rishikeshpoorun.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.rishikeshpoorun.dao.entities.Car;
import com.accenture.rishikeshpoorun.dao.entities.Rental;
import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dto.RentalDto;

@Controller
@RequestMapping(value = "/secured/query")
public class SearchQueryController extends FrontController {

	private static List<Rental> fetchrental;
	private static List<User> fetchuser;
	private static List<Car> fetchcar;
	private static List<User> userlist;
	private static List<Car> carlist;
	private static List<Rental> rentalList;

	/**
	 * Redirects requests for the Query Form
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/search")
	public String goToSearchForm(Model model) {
		model.addAttribute("rentalDto", new RentalDto());
		return "secured_page/searchForm";
	}

}
