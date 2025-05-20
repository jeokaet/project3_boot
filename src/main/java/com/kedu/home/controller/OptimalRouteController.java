package com.kedu.home.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.PlaceDTO;
import com.kedu.home.dto.getPlaceListDTO;
import com.kedu.home.services.GooglePlaceApiService;
import com.kedu.home.services.OptimalRouteService;

@RestController
@RequestMapping("/opti")
public class OptimalRouteController {

    @Autowired
    private OptimalRouteService optimalRouteService;
    
    @Autowired
    private GooglePlaceApiService googleApi;

    @PostMapping("/optimize")
    public List<PlaceDTO> getOptimizedRoute(@RequestBody  Map<String, Object> data) {
        
    	List<PlaceDTO> list = optimalRouteService.getOptimizedRoute(data);
    	for (PlaceDTO place : list) {
    	    System.out.println(place.getName());
    	}
        return list;
    }
    
    
    @PostMapping("/getList")
    public ResponseEntity<?> getPlaceList(@RequestBody getPlaceListDTO request){
    	try {
    		List<Map<String, String>> results =googleApi.getPlaceList(request);
    		return ResponseEntity.ok(Map.of("results", results));
    		
	    }catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
	    }
    }
}
