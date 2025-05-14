package com.kedu.home.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.MemberDTO;

@Repository
public class MypageDAO {
	
	@Autowired
	private SqlSession mybatis;
	
	private static final String NAMESPACE = "MypageMapper";
	
	public MemberDTO getMembers(String loginId) {
		return mybatis.selectOne(NAMESPACE + ".getMembers",loginId);
		
	}
	
	public int updateMember(MemberDTO request) {
		return mybatis.update(NAMESPACE + ".updateMember",request);
	}
	
	public int  deleteMember(String loginId) {
		return mybatis.delete(NAMESPACE + ".deleteMember",loginId);
	}
}
