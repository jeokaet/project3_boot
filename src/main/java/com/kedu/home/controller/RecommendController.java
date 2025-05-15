package com.kedu.home.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.kedu.home.utils.AbuseFilterUtils;
import com.kedu.home.utils.PromptBuilder;

@RestController
@RequestMapping("/api")
public class RecommendController {

	@Autowired
	private GeminiService GServ;

	@PostMapping("/llm-recommend")
	public ResponseEntity<?> recommendPlaces(@RequestBody LLMRequestDTO request) {
		try {
			if(AbuseFilterUtils.isAbusiveOnly(request.getUserInput())) {
				return ResponseEntity.ok(Map.of("error","요청이 불명확하다."));
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
				return ResponseEntity.ok(Map.of("error", "추천할 만한 장소가 없습니다."));
			}

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});

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
			System.out.println("요청 확인 : " + request.getStartingLocation() + " / 날짜 : " + request.getDate());
			
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
				return ResponseEntity.ok(Map.of("error", "추천할 만한 장소가 없습니다."));
			}

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});
			
			System.out.println("컨틀롤러에서 확인 : " + results);

			return ResponseEntity.ok(Map.of("results", results));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
		}
	}
	
	
}
