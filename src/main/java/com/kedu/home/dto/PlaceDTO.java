package com.kedu.home.dto;

public class PlaceDTO {
	
	private String placeId;
	private String name;
	private String type;
	private String region;
	private String description;
	private String reason;
	private double latitude;
	private double longitude;
	private String imageUrl;

	
	public PlaceDTO() {
	}


	public PlaceDTO(String placeId, String name, String type, String region, String description, String reason,
			double latitude, double longitude, String imageUrl) {
		super();
		this.placeId = placeId;
		this.name = name;
		this.type = type;
		this.region = region;
		this.description = description;
		this.reason = reason;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
	}


	public String getPlaceId() {
		return placeId;
	}


	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
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
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	
	
}
