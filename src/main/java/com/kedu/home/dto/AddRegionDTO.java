package com.kedu.home.dto;

import org.springframework.web.multipart.MultipartFile;

public class AddRegionDTO {
	
	private String regionName;
	private String regionDetail;
	private MultipartFile file;
	
	
	public AddRegionDTO() {}
	

	public AddRegionDTO(String regionName, String regionDetail, MultipartFile file) {
		super();
		this.regionName = regionName;
		this.regionDetail = regionDetail;
		this.file = file;
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
	};
	
	

}
