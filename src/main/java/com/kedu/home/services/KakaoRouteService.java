package com.kedu.home.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoRouteService {
	
	 @Value("${kakao.rest.api.key}")  // application.properties 에서 읽음
	    private String kakaoRestApiKey;

	    private final RestTemplate restTemplate;

	    public KakaoRouteService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }
	    
	    public String getRoute(String origin, String destination, String waypoints, String priority, boolean alternatives, boolean summary) {
	        String url = "https://apis-navi.kakaomobility.com/v1/directions";

	        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
	                .queryParam("origin", origin)
	                .queryParam("destination", destination)
	                .queryParam("priority", priority)
	                .queryParam("alternatives", alternatives)
	                .queryParam("summary", summary);

	        if (waypoints != null && !waypoints.isEmpty()) {
	            builder.queryParam("waypoints", waypoints);
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        ResponseEntity<String> response = restTemplate.exchange(
	                builder.toUriString(),
	                HttpMethod.GET,
	                entity,
	                String.class
	        );

	        return response.getBody();
	    }

}
