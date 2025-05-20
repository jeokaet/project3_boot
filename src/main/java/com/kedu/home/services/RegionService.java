package com.kedu.home.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedu.home.dao.RegionDAO;
import com.kedu.home.dto.AddRegionDTO;
import com.kedu.home.dto.GetRegionDTO;
import com.kedu.home.dto.InsertRegionDTO;
import com.kedu.home.dto.RegionDTO;

@Service
public class RegionService {
	
	@Autowired
	public RegionDAO rDao;
	
	@Autowired
	private FileService fileServ;
	
	public void insertRegion(AddRegionDTO dto) throws Exception {
//		if(dto.getRegionName()==null||dto.getRegionName().trim().isEmpty()) {
//			throw new IllegalArgumentException("지역 명은 필수 입력 사항입니다.");
//		}
//		if(dto.getRegionDetail()==null||dto.getRegionDetail().trim().isEmpty()) {
//			throw new IllegalArgumentException("지역 설명은 필수입니다.");
//		}
		
		int fileId = fileServ.upload(dto);
		
		InsertRegionDTO region = new InsertRegionDTO();
		region.setRegionName(dto.getRegionName());
		region.setRegionDetail(dto.getRegionDetail());
		region.setFileId(fileId);
		
		rDao.insertRegion(region);
	}
	
	public List<GetRegionDTO> selectRegionList(){
		return rDao.selectRegionList();
	}
	
	public List<GetRegionDTO> searchByRegionName(String searchWord){
		return rDao.searchByRegionName(searchWord);
	}
	
	public void deleteRegions(List<Integer> idList) {
		rDao.deleteRegions(idList);
	}
	
	public void updateRegion(RegionDTO region) {
		rDao.updateRegion(region);
	}

}
