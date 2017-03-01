package kz.codingwolves.unichat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RouletteMessage {
	
	private int type;
	
	private String message;
	
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private String message2;
	
	public RouletteMessage(int type, String message) {
		this.type = type;
		this.message = message;
		this.message2 = null;
	}
	
	public RouletteMessage(int type, String message, String message2) {
		this.type = type;
		this.message = message;
		this.message2 = message2;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage2() {
		return message2;
	}
	
	public void setMessage2(String message2) {
		this.message2 = message2;
	}
}
