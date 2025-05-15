package com.kedu.home.dto;

public class PlaceDTO {
	private String name;
	private String type;
	private String region;
	private String description;
	private String reason;
	private String latitude;
	private String longitude;
	private String imageUrl;

	public PlaceDTO() {
	}

	
	public PlaceDTO(String name, String type, String region, String description, String reason, String latitude,
			String longitude, String imageUrl) {
		super();
		this.name = name;
		this.type = type;
		this.region = region;
		this.description = description;
		this.reason = reason;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
}
