package com.example.demo;

import java.io.Serializable;

import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class MyModel implements Serializable {
	private String messageString;

	
	public String getMessageString() {
		return messageString;
	}
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	
	
	
}
