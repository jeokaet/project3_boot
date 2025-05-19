package com.kedu.home.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.home.dto.GetRegionDTO;
import com.kedu.home.dto.InsertRegionDTO;
import com.kedu.home.dto.RegionDTO;

@Repository
public class RegionDAO {
	
	@Autowired
    private SqlSession mybatis;
	
	public void insertRegion(InsertRegionDTO dto) {
		mybatis.insert("RegionMapper.insertRegion", dto);
	}
	
	public List<GetRegionDTO> selectRegionList(){
		List<GetRegionDTO> list = mybatis.selectList("RegionMapper.selectRegionList");
		return list;
	}
	
	public List<GetRegionDTO> searchByRegionName(String searchWord){
		System.out.println("DAO 에서 검색어 : " + searchWord);
		List<GetRegionDTO> list = mybatis.selectList("RegionMapper.searchByRegionName", searchWord);

		return list;
	}
	
	public void deleteRegions(List<Integer> idList) {
		mybatis.delete("RegionMapper.deleteRegions", idList);
	}
	
	public void updateRegion(RegionDTO region) {
		mybatis.update("RegionMapper.updateRegion", region);
	}

}
