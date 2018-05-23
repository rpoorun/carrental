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
		registry.addViewController("/loginform").setViewName("LoginForm");
		
		
	}
	
	

}
