package com.kedu.home.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.getPlaceListDTO;

@Repository
public class RecommendDAO {
	@Autowired
    private SqlSession mybatis;
	
	public void insertPlaces(getPlaceListDTO request) {
		
	}

}
