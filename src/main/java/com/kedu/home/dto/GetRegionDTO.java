package com.kedu.home.dto;

public class GetRegionDTO {
	
	private String regionName;
	private String regionDetail;
	private String filePath;
	
	public GetRegionDTO() {};
	public GetRegionDTO(String regionName, String regionDetail, String filePath) {
		super();
		this.regionName = regionName;
		this.regionDetail = regionDetail;
		this.filePath = filePath;
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	

}
