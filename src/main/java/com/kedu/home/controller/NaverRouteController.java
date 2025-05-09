package com.kedu.home.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.services.NaverRouteService;

@RestController
@RequestMapping("/api")
public class NaverRouteController {
	
	@Autowired
	private NaverRouteService naverRouteService;
	
	@GetMapping("/getNaverRoute")
	public String getRoute(
			@RequestParam double startX,
			@RequestParam double startY,
			@RequestParam String goals) throws IOException {
		String decodedGoals = URLDecoder.decode(goals,"UTF-8");
		List<Map<String, Double>> goalList = new ObjectMapper().readValue(decodedGoals, new TypeReference<List<Map<String, Double>>>() {});
		return naverRouteService.getNaverRoute(startX, startY, goalList);
	}
	
	
}
