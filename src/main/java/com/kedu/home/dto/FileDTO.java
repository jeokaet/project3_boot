package com.kedu.home.dto;

public class FileDTO {
	
	private int fileId;
	private int fileTypeId;
	private String oriname;
	private String sysname;
	private String filePath;
	
	public FileDTO() {};
	
	public FileDTO(int fileId, int fileTypeId, String oriname, String sysname, String filePath) {
		super();
		this.fileId = fileId;
		this.fileTypeId = fileTypeId;
		this.oriname = oriname;
		this.sysname = sysname;
		this.filePath = filePath;
	}
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public int getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(int fileTypeId) {
		this.fileTypeId = fileTypeId;
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
