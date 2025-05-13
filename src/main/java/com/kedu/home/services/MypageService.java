package com.kedu.home.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedu.home.dao.MypageDAO;
import com.kedu.home.dto.MemberDTO;

@Service
public class MypageService {
	
	@Autowired
	private MypageDAO mDao;
	
	public MemberDTO getMembers(String loginId) {
		return mDao.getMembers(loginId);
	}

	public int updateMember(MemberDTO request) {
		return mDao.updateMember(request);
	}
	
	public int  deleteMember(String loginId) {
		return mDao. deleteMember(loginId);
	}
}
