package com.kedu.home.dto;

public class FileDTO {
	
	private int imageId;
	private String oriname;
	private String sysname;
	private String filePath;
	
	public FileDTO() {}
	
	

	public FileDTO(int imageId, String oriname, String sysname, String filePath) {
		super();
		this.imageId = imageId;
		this.oriname = oriname;
		this.sysname = sysname;
		this.filePath = filePath;
	}



	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getOriname() {
		return oriname;
	}

	public void setOriname(String oriname) {
		this.oriname = oriname;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

	
	
	
	
	

}
