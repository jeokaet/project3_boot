package com.kedu.home.dto;

public class PlaceDTO {
	private String name;
	private String type;
	private String region;
	private String description;
	private String reason;

	public PlaceDTO() {
	}

	public PlaceDTO(String name, String type, String region, String description, String reason) {
		super();
		this.name = name;
		this.type = type;
		this.region = region;
		this.description = description;
		this.reason = reason;
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

	@Override
	public String toString() {
		return String.format("PlaceDTO{name='%s', type='%s', region='%s', description='%s', reason='%s'}", name, type,
				region, description, reason);
	}

}
