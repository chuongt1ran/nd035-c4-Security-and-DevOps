package com.example.demo.model.requests;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDetail {
	@JsonProperty
	private Date date;
	@JsonProperty
	private String message;
	@JsonProperty
	private String description;

	public ErrorDetail(Date date, String message, String description) {
		this.date = date;
		this.message = message;
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
