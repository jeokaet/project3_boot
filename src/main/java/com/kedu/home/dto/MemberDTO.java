package com.kedu.home.dto;

import java.util.Date;

public class MemberDTO {

	private String loginId;
	private String loginPw;
	private String nickName;
	private Date createAt;
	private String profilePicture;
	private String phoneNumber;
	private String mainAddress;
	private String subAddress;
	private int postCode;
	private String emailAddress;
	private String firstCategory;
	private String secondCategory;
	
	public MemberDTO(String loginId, String loginPw, String nickName, Date createAt, String profilePicture,
			String phoneNumber, String mainAddress, String subAddress, int postCode, String emailAddress,
			String firstCategory, String secondCategory) {
		super();
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.nickName = nickName;
		this.createAt = createAt;
		this.profilePicture = profilePicture;
		this.phoneNumber = phoneNumber;
		this.mainAddress = mainAddress;
		this.subAddress = subAddress;
		this.postCode = postCode;
		this.emailAddress = emailAddress;
		this.firstCategory = firstCategory;
		this.secondCategory = secondCategory;
	}
	public MemberDTO() {
		super();
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginPw() {
		return loginPw;
	}
	public void setLoginPw(String loginPw) {
		this.loginPw = loginPw;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMainAddress() {
		return mainAddress;
	}
	public void setMainAddress(String mainAddress) {
		this.mainAddress = mainAddress;
	}
	public String getSubAddress() {
		return subAddress;
	}
	public void setSubAddress(String subAddress) {
		this.subAddress = subAddress;
	}
	public int getPostCode() {
		return postCode;
	}
	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstCategory() {
		return firstCategory;
	}
	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}
	public String getSecondCategory() {
		return secondCategory;
	}
	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}
	
	
}
