package com.kedu.home.dto;

import java.util.Date;

public class getPlaceListDTO {
	
	private String date;
	private String startingLocation;
	private double latitude;
	private double longitude;
	
	public getPlaceListDTO() {}

	public getPlaceListDTO(String date, String startingLocation, double latitude, double longitude) {
		super();
		this.date = date;
		this.startingLocation = startingLocation;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	};


}
