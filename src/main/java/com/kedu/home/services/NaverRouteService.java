package com.kedu.home.services;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
public class NaverRouteService {
	
	@Value("${naver.client.id}")
	private String naverClientId;
	
	@Value("${naver.client.secret}")
	private String naverClientSecret;
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public NaverRouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
	
	 public String getNaverRoute(double startX, double startY, List<Map<String,Double>> goalList) {
		 if (startX == 0 || startY == 0 || goalList == null || goalList.isEmpty()) {
		        return "{\"error\": \"Start or Goal is null\"}";
		    }
		 StringBuilder urlBuilder = new StringBuilder("https://maps.apigw.ntruss.com/map-direction-15/v1/driving?");
	        urlBuilder.append("start=").append(startX).append(",").append(startY);

	        for (Map<String, Double> goal : goalList) { try {
	            String encodedGoal = URLEncoder.encode(goal.get("x") + "," + goal.get("y"), "UTF-8");
	            urlBuilder.append("&goal=").append(encodedGoal);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	            return "{\"error\": \"URL 인코딩 실패\"}";
	        }
	        }
	        
	        String url = urlBuilder.toString();
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-ncp-apigw-api-key-id", naverClientId);
	        headers.set("x-ncp-apigw-api-key", naverClientSecret);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	       
	   

	        try {
	            ResponseEntity<String> response = restTemplate.exchange(
	                url, HttpMethod.GET, new HttpEntity<>(headers), String.class
	            );
	            return response.getBody();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "{\"error\": \"경로 계산 실패\"}";
	        }
	    }
	
}
