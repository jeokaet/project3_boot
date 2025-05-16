package com.kedu.home.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.dto.PlaceDTO;
import com.kedu.home.services.OptimalRouteService;

@RestController
@RequestMapping("/api/route")
public class OptimalRouteController {

    @Autowired
    private OptimalRouteService optimalRouteService;

    @PostMapping("/optimize")
    public List<PlaceDTO> getOptimizedRoute(@RequestBody  Map<String, Object> data) {
    	 ObjectMapper mapper = new ObjectMapper();
    	
    	List<PlaceDTO> places = mapper.convertValue(data.get("places"), new TypeReference<List<PlaceDTO>>() {});
        double longitude = Double.parseDouble(data.get("longitude").toString());
        double latitude = Double.parseDouble(data.get("latitude").toString());
        
    	List<PlaceDTO> list = optimalRouteService.getOptimizedRoute(data);
    	for (PlaceDTO place : list) {
    	    System.out.println(place.getName());
    	}
        return list;
    }
}
