package com.kedu.home.dto;

import java.util.Date;

public class getPlaceListDTO {
	
	private String date;
	private String startingLocation;
	
	public getPlaceListDTO() {};
	public getPlaceListDTO(String date, String startingLocation) {
		super();
		this.date = date;
		this.startingLocation = startingLocation;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStartingLocation() {
		return startingLocation;
	}
	public void setStartingLocation(String startingLocation) {
		this.startingLocation = startingLocation;
	}
	
	

}
