package com.kedu.home.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PerspectiveService {
	@Value("${perspective.api.key}")
	private String PapiKey;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public boolean isToxic(String text) {
		try {
			String url = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + PapiKey;
			
			Map<String, Object> request = new HashMap<>();
			request.put("comment", Map.of("text", text));
			request.put("languages", List.of("ko","en"));
			request.put("requestedAttributes", Map.of("TOXICITY", Map.of()));
			 
			HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            Map<?, ?> scores = (Map<?, ?>) ((Map<?, ?>) response.getBody().get("attributeScores")).get("TOXICITY");
            double score = (double) ((Map<?, ?>) scores.get("summaryScore")).get("value");

            return score >= 0.75; // 기준점: 0.75
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
