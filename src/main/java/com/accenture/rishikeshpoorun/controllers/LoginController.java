package com.accenture.rishikeshpoorun.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all request from HostDomain:port/login
 * @author rishikesh.poorun
 *
 */
@Controller
public class LoginController {
	
	
	@GetMapping("/login")
	public String goToLoginFormPage() {
		return "loginForm";
	}

	
	
}
