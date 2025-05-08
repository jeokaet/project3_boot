package com.kedu.home.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.RegionDTO;
import com.kedu.home.services.RegionService;

@RestController
@RequestMapping("/region")
public class RegionController {
	
	@Autowired
	public RegionService regionServ;
	
	@PostMapping
	public ResponseEntity<Void> insertRegion(@RequestBody RegionDTO dto){
		regionServ.insertRegion(dto);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<List<RegionDTO>> selectRegionList(){
		List<RegionDTO> list = regionServ.selectRegionList();
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
