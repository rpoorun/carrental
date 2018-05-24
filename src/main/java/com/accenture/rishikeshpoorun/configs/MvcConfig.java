package com.accenture.rishikeshpoorun.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	
	// Configuration for custom login form
	// Alternative to view resolver
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//Resolver list for direct domain http://localhost:8080/* 
		
		/* // Duplicate entry in the Front Controller
		 * registry.addViewController("/").setViewName("index");
		 */
		/* // Duplicate Entry in the Front Controller
		 * registry.addViewController("/index").setViewName("index");
		 */
		/*
		 * registry.addViewController("/secured").setViewName("adminDashboard");
		 */
		/*
		 * registry.addViewController("/rent").setViewName("customerDashboard");
		 */
		registry.addViewController("/login").setViewName("loginForm");
		
		
	}
	
	

}
