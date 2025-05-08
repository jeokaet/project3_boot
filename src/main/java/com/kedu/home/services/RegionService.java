package com.kedu.home.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedu.home.dao.RegionDAO;
import com.kedu.home.dto.RegionDTO;

@Service
public class RegionService {
	
	@Autowired
	public RegionDAO rDao;
	
	public void insertRegion(RegionDTO dto) {
		rDao.insertRegion(dto);
	}
	
	public List<RegionDTO> selectRegionList(){
		return rDao.selectRegionList();
	}

}
