package com.kedu.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.KakaoRouteDTO;
import com.kedu.home.services.KakaoApiService;

@RestController
@RequestMapping("/api")
public class KakaoMapController {
	
	@Autowired
	private KakaoApiService kakaoApiService;
	
	@PostMapping("/getRoute")
	public String getRoute(@RequestBody KakaoRouteDTO requestBody) {
		return kakaoApiService.getRoute(requestBody);
	}
	
}
