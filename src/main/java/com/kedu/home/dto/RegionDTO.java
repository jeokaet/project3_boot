package com.kedu.home.dto;

public class RegionDTO {
	
	private int regionId;
	private String regionName;
	private String regionDetail;
	private int fileId;
	private String filePath;
	
	public RegionDTO() {};
	public RegionDTO(int regionId, String regionName, String regionDetail, int fileId, String filePath) {
		super();
		this.regionId = regionId;
		this.regionName = regionName;
		this.regionDetail = regionDetail;
		this.fileId = fileId;
		this.filePath = filePath;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
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
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	

}
