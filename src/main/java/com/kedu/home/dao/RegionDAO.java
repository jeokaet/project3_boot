package com.kedu.home.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.GetRegionDTO;
import com.kedu.home.dto.RegionDTO;

@Repository
public class RegionDAO {
	
	@Autowired
    private SqlSession mybatis;
	
	public void insertRegion(RegionDTO dto) {
		mybatis.insert("RegionMapper.insertRegion", dto);
	}
	
	public List<GetRegionDTO> selectRegionList(){
		List<GetRegionDTO> list = mybatis.selectList("RegionMapper.selectRegionList");
		return list;
	}
	
	public void deleteRegions(List<Integer> idList) {
		mybatis.delete("RegionMapper.deleteRegions", idList);
	}
	
	public void updateRegion(RegionDTO region) {
		mybatis.update("RegionMapper.updateRegion", region);
	}

}
