package com.example.demo;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class MyModelWithTimestamp implements Serializable {
	private String messageString;
	private String timestampString;
	
	public String getMessageString() {
		return messageString;
	}
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	public String getTimestampString() {
		return timestampString;
	}
	public void setTimestampString(String timestampString) {
		this.timestampString = timestampString;
	}
	
	
}
