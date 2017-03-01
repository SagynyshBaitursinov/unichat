package kz.codingwolves.unichat.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ChatMessage {

	private String sender;
	
	private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) 
	private Date date;
	
	public ChatMessage(String sender, Date date, String message) {
		this.sender = sender;
		this.message = message;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
