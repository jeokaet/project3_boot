package com.kedu.home.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kedu.home.dto.KakaoRouteDTO;

@Service
public class KakaoApiService {
	
	@Value("${kakao.rest.api.key}") 
	private String kakaoApiKey; //카카오 api 키
	
	private final RestTemplate restTemplate;
	
	public KakaoApiService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String getRoute(KakaoRouteDTO request) {
		String url = "https://apis-navi.kakaomobility.com/v1/destinations/directions";
		
		// 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);  // 카카오 API 키를 Authorization 헤더에 포함
        headers.setContentType(MediaType.APPLICATION_JSON); // 요청 본문은 JSON 형식으로 설정
        
        Map<String, Object> body = new HashMap<>();
        body.put("origin", request.getOrigin());
        body.put("destinations", request.getDestinations());
        // 요청 본문 설정 (출발지와 목적지 정보를 JSON 형식으로 전달)
//        String requestBody = "{ \"origin\": " + request.getOrigin() + ", \"destinations\": " + request.getDestinations() + " }";

        // HTTP 요청 엔티티 생성 (요청 본문과 헤더 포함)
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);

        // 카카오 API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody(); // 카카오 API 응답 결과를 반환
	}

}
