package com.accenture.rishikeshpoorun.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all request from domain:port/login
 *
 */
@Controller
public class LoginController {
	
	
	@GetMapping("/login")
	public String goToLoginFormPage() {
		return "loginForm";
	}

	
	
}
