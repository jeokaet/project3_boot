package com.kedu.home.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.MemberDTO;

@Repository
public class MemberDAO {
	
    @Autowired
    private SqlSession mybatis;

    private static final String NAMESPACE = "MemberMapper";

    public boolean isAvailableIdAndPw(String id, String pw) {
        MemberDTO result = mybatis.selectOne(NAMESPACE + ".selectByLoginIdAndPw", Map.of("loginId", id, "pw", pw));
        return result != null;
    }

    public boolean existsByLoginId(String loginId) {
        MemberDTO result = mybatis.selectOne(NAMESPACE + ".selectByLoginId", loginId);
        return result != null;
    }

    public boolean insertMember(MemberDTO dto) {
        int rows = mybatis.insert(NAMESPACE + ".insertMember", dto);
        return rows == 1;
    }
}
