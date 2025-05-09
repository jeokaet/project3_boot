package com.kedu.home.services;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedu.home.dao.RegionDAO;
import com.kedu.home.dto.RegionDTO;

@Service
public class RegionService {
	
	@Autowired
	public RegionDAO rDao;
	
	public void insertRegion(RegionDTO dto) {
		if(dto.getRegionName()==null||dto.getRegionName().trim().isEmpty()) {
			throw new IllegalArgumentException("지역 명은 필수 입력 사항입니다.");
		}
		if(dto.getRegionDetail()==null||dto.getRegionDetail().trim().isEmpty()) {
			throw new IllegalArgumentException("지역 설명은 필수입니다.");
		}
		rDao.insertRegion(dto);
	}
	
	public List<RegionDTO> selectRegionList(){
		return rDao.selectRegionList();
	}
	
	public void deleteRegions(List<Integer> idList) {
		rDao.deleteRegions(idList);
	}
	
	public void updateRegion(RegionDTO region) {
		rDao.updateRegion(region);
	}

}
