package com.kedu.home.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.AddRegionDTO;
import com.kedu.home.dto.GetRegionDTO;
import com.kedu.home.dto.RegionDTO;
import com.kedu.home.services.FileService;
import com.kedu.home.services.RegionService;

@RestController
@RequestMapping("/region")
public class RegionController {
	
	@Autowired
	public RegionService regionServ;
	
	@Autowired
	public FileService fileServ;
	
	@PostMapping
	public ResponseEntity<?> insertRegion(@ModelAttribute AddRegionDTO dto){
	    try {
	        regionServ.insertRegion(dto);
	        return ResponseEntity.ok().build();
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());  
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
	    }
	}

	
	@GetMapping
	public ResponseEntity<List<GetRegionDTO>> selectRegionList(){
		List<GetRegionDTO> list = regionServ.selectRegionList();
		return ResponseEntity.ok(list); 
	}
	
	@GetMapping("/searchByRegionName")
	public ResponseEntity<List<GetRegionDTO>> searchByRegionName(@RequestParam String searchWord){
		System.out.println("검색어 : " + searchWord);
		List<GetRegionDTO> list = regionServ.searchByRegionName(searchWord);
		System.out.println("컨트롤러 : " + list.get(0).getRegionName());
		return ResponseEntity.ok(list); 
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteRegions(@RequestBody List<Integer> Ids){
		regionServ.deleteRegions(Ids);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> updateRegion(@RequestBody RegionDTO region){
		regionServ.updateRegion(region);
		return ResponseEntity.ok().build();
	}

}
