package com.kedu.home.dto;

import java.sql.Timestamp;

public class MemberDTO {

	private int memberId;
	private String loginId;
	private String pw;
	private String userName;
	private Timestamp created_at;
	private String gender;
	private int birth;
	private String email;
	private String address1;
	private String address2;
	private int postCode;
	private String agreement;
	private String profileImageId;
	
	
	public MemberDTO() {};
	
	public MemberDTO(int memberId, String loginId, String pw, String userName, Timestamp created_at, String gender,
			int birth, String email, String address1, String address2, int postCode, String agreement,
			String profileImageId) {
		super();
		this.memberId = memberId;
		this.loginId = loginId;
		this.pw = pw;
		this.userName = userName;
		this.created_at = created_at;
		this.gender = gender;
		this.birth = birth;
		this.email = email;
		this.address1 = address1;
		this.address2 = address2;
		this.postCode = postCode;
		this.agreement = agreement;
		this.profileImageId = profileImageId;
	}

	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getBirth() {
		return birth;
	}
	public void setBirth(int birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public int getPostCode() {
		return postCode;
	}
	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public String getProfileImageId() {
		return profileImageId;
	}
	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}
	
	
	
	
	
}
