package com.accenture.rishikeshpoorun.dto;

import org.springframework.stereotype.Component;

@Component
public class TextDto {
	
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}
