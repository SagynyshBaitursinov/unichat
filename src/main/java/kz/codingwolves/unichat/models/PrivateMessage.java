package kz.codingwolves.unichat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PrivateMessage {

	private int type;
	
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private String message;
	
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private String name;
	
	public PrivateMessage(int type, String message, String name) {
		this.type = type;
		this.message = message;
		this.name = name;
	}
	
	public PrivateMessage(int type, String name) {
		this.type = type;
		this.name = name;
		this.message = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
