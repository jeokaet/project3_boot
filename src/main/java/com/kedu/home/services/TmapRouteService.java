package com.kedu.home.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TmapRouteService {
	
	
	@Value("${tmap.api.key}")
	private String tmapApiKey;
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public TmapRouteService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	 public String getTmapTransitRoute(double startX, double startY, double endX, double endY) {
	        // Tmap API URL
	        String url = "https://apis.openapi.sk.com/transit/routes";

	        // 요청 파라미터 준비
	        String body = "{\n" +
	                "    \"startX\": " + startX + ",\n" +
	                "    \"startY\": " + startY + ",\n" +
	                "    \"endX\": " + endX + ",\n" +
	                "    \"endY\": " + endY + ",\n" +
	                "    \"searchOption\": 0,\n" +
	                "    \"trafficInfo\": \"Y\"\n" +
	                "}";

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("appKey", tmapApiKey); // Tmap API Key
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // 요청 엔티티 생성
	        HttpEntity<String> entity = new HttpEntity<>(body, headers);

	        // POST 요청 보내기
	        try {
	            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	            return response.getBody();  // 응답 반환
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "{\"error\": \"Tmap 대중교통 경로 요청 실패\"}";
	        }
	    }
	
	
}
