package com.accenture.rishikeshpoorun.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController{

	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    model.addAttribute("errorException", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
    	model.addAttribute("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
	    model.addAttribute("errorCode", status.toString());
	  
	    return "error";
	}
	
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
