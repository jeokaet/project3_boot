package com.kedu.home.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoRouteService {

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;

    public KakaoRouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRoute(String origin, String destination, String waypoints, String priority, boolean alternatives, boolean summary) {
        String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";

        StringBuilder urlBuilder = new StringBuilder(baseUrl)
            .append("?origin=").append(origin)
            .append("&destination=").append(destination)
            .append("&priority=").append(priority)
            .append("&alternatives=").append(alternatives)
            .append("&summary=").append(summary);

        if (waypoints != null && !waypoints.isEmpty()) {
            // waypoints 내 파이프 문자는 인코딩하지 않고 그대로 전달
            urlBuilder.append("&waypoints=").append(waypoints);
        }

        String url = urlBuilder.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Kakao API error: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}
