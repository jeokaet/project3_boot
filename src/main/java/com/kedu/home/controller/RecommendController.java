package com.kedu.home.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.dto.LLMRequestDTO;
import com.kedu.home.dto.getPlaceListDTO;
import com.kedu.home.services.GeminiService;
import com.kedu.home.services.GooglePlaceApiService;
import com.kedu.home.services.PerspectiveService;
import com.kedu.home.utils.AbuseFilterUtils;
import com.kedu.home.utils.PromptBuilder;

@RestController
@RequestMapping("/api")
public class RecommendController {

	@Autowired
	private GeminiService GServ;
	
	@Autowired
	private PerspectiveService PServ;
	
	@Autowired
	private GooglePlaceApiService googlePlaceService;

	

	@PostMapping("/llm-recommend")
	public ResponseEntity<?> recommendPlaces(@RequestBody LLMRequestDTO request) {
		try {
			String userInput = request.getUserInput();
			
			 if (PServ.isToxic(userInput) || AbuseFilterUtils.isAbusiveOnly(userInput)) {
		            return ResponseEntity.ok(Map.of(
		                "error", "입력에 욕설 및 공격적인 표현이 들어가 있어 추천을 중단합니다."));
		        }
			
			
			String prompt = PromptBuilder.buildPrompt(request.getUserInput(), request.getExamplePlaces());

			String llmResponse = GServ.call(prompt) ; // cleaned JSON string
			System.out.println("🟢 최종 클린 JSON:\n" + llmResponse);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(llmResponse);

			if (root.has("error")) {
				// Gemini 응답에 error가 있으면 그대로 전달
				return ResponseEntity.ok(Map.of("error", root.get("error").asText()));
			}

			JsonNode resultsNode = root.get("results");
			if (resultsNode == null || !resultsNode.isArray()) {
				return ResponseEntity.ok(Map.of("error", "추천 장소가 없습니다."));
			}

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});
			for (Map<String, String> place : results) {
			    String lat = place.get("latitude");
			    String lng = place.get("longitude");

			    // 기존 imageUrl 값이 null이거나 "null" 문자열인 경우에만 호출
			    String currentImage = place.get("imageUrl");
			    if (currentImage == null || currentImage.equals("null")) {

			        // ⛳ Google Places API 통해 대표 이미지 URL 가져오기
			        String imageUrl = googlePlaceService.getImageUrl(lat, lng);

			        // 💾 결과 map에 다시 저장
			        place.put("imageUrl", imageUrl != null ? imageUrl : null);
			    }
			}

			return ResponseEntity.ok(Map.of("results", results));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
		}
	}
	
	
	@PostMapping("/getList")
	public ResponseEntity<?> getPlaceList(@RequestBody getPlaceListDTO request) {
		try {
			if(AbuseFilterUtils.isAbusiveOnly(request.getStartingLocation())) {
				return ResponseEntity.ok(Map.of("error","요청이 불명확하다."));
			}
			
			String prompt = PromptBuilder.buildPrompt2(request.getStartingLocation(), request.getDate());

			String llmResponse2 = GServ.call(prompt); // cleaned JSON string
			System.out.println("🟢 최종 클린 JSON:\n" + llmResponse2);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(llmResponse2);

			if (root.has("error")) {
				return ResponseEntity.ok(Map.of("error", root.get("error").asText()));
			}

			JsonNode resultsNode = root.get("results");
			if (resultsNode == null || !resultsNode.isArray()) {
				return ResponseEntity.ok(Map.of("error", "추천 장소가 없습니다."));
			}

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});
			
			
			for (Map<String, String> place : results) {
			    String lat = place.get("latitude");
			    String lng = place.get("longitude");

			    // 기존 imageUrl 값이 null이거나 "null" 문자열인 경우에만 호출
			    String currentImage = place.get("imageUrl");
			    if (currentImage == null || currentImage.equals("null")) {

			        // ⛳ Google Places API 통해 대표 이미지 URL 가져오기
			        String imageUrl = googlePlaceService.getImageUrl(lat, lng);

			        // 💾 결과 map에 다시 저장
			        place.put("imageUrl", imageUrl != null ? imageUrl : null);
			    }
			}
			
			System.out.println("컨트롤러에서 확인 : " + results);

			return ResponseEntity.ok(Map.of("results", results));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
		}
	}
	

	
}
