package com.kedu.home.dto;

import org.springframework.web.multipart.MultipartFile;

public class AddRegionDTO {
	
	private String regionName;
	private String regionDetail;
	private MultipartFile file;
	private double latitude;
	private double longitude;
	
	
	public AddRegionDTO() {}


	public AddRegionDTO(String regionName, String regionDetail, MultipartFile file, double latitude, double longitude) {
		super();
		this.regionName = regionName;
		this.regionDetail = regionDetail;
		this.file = file;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	public String getRegionName() {
		return regionName;
	}


	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


	public String getRegionDetail() {
		return regionDetail;
	}


	public void setRegionDetail(String regionDetail) {
		this.regionDetail = regionDetail;
	}


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
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
	
	
	

}
