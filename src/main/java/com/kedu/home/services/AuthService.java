package com.kedu.home.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedu.home.dao.MemberDAO;
import com.kedu.home.dto.MemberDTO;
import com.kedu.home.utils.SHA512Util;

@Service
public class AuthService {
	
	@Autowired
	private MemberDAO mDAO;

	public boolean isAvailableIdAndPw(String id, String pw) {
	    String encryptedPw = SHA512Util.encrypt(pw);
	    return mDAO.isAvailableIdAndPw(id, encryptedPw);
	}

	public boolean registerMember(MemberDTO dto) {
	    if (mDAO.existsByLoginId(dto.getLoginId())) {
	        return false;
	    }
	    String encryptedPw = SHA512Util.encrypt(dto.getLoginPw());
	    dto.setLoginPw(encryptedPw);
	    return mDAO.insertMember(dto);
	}
}
