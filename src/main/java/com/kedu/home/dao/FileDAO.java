package com.kedu.home.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.FileDTO;

@Repository
public class FileDAO {
	
	@Autowired
    private SqlSession mybatis;
	
	public void saveImage(FileDTO file) {
		mybatis.insert("FileMapper.saveImage", file);
	}

}
