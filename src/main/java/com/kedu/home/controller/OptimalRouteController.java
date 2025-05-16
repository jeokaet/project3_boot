package com.kedu.home.controller;

import com.kedu.home.dto.PlaceDTO;
import com.kedu.home.services.OptimalRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class OptimalRouteController {

    @Autowired
    private OptimalRouteService optimalRouteService;

    @PostMapping("/optimize")
    public List<PlaceDTO> getOptimizedRoute(@RequestBody List<PlaceDTO> places) {
    	List<PlaceDTO> list = optimalRouteService.getOptimizedRoute(places);
    	for (PlaceDTO place : list) {
    	    System.out.println(place.getName());
    	}
        return list;
    }
}
