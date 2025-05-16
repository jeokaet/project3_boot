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
import com.kedu.home.utils.JsonCleanUtils;

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

            String prompt = PromptBuilder.buildPrompt(userInput, request.getExamplePlaces());
            String llmRaw = GServ.call(prompt);
            String llmCleaned = JsonCleanUtils.removeJsonComments(llmRaw);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(llmCleaned);
            JsonNode resultsNode = root.get("results");
            if (resultsNode == null || !resultsNode.isArray()) {
                return ResponseEntity.ok(Map.of("error", "추천 장소가 없습니다."));
            }

            List<Map<String, String>> results1 = mapper.convertValue(resultsNode, new TypeReference<>() {});
            System.out.println("추천 결과 수: " + results1.size());
            results1.stream().limit(3).forEach(r -> System.out.println("👉 " + r.get("name")));

            return ResponseEntity.ok(Map.of("results", results1));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
        }
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getPlaceList(@RequestBody getPlaceListDTO request) {
    	long start = System.currentTimeMillis();
        try {
            if (AbuseFilterUtils.isAbusiveOnly(request.getStartingLocation())) {
                return ResponseEntity.ok(Map.of("error", "요청이 불명확하다."));
            }

            String prompt = PromptBuilder.buildPrompt2(request.getStartingLocation(), request.getDate());
            String llmRaw = GServ.call(prompt);
            String llmCleaned = JsonCleanUtils.removeJsonComments(llmRaw);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(llmCleaned);

            if (root.has("error")) {
                return ResponseEntity.ok(Map.of("error", root.get("error").asText()));
            }

            JsonNode resultsNode = root.get("results");
            if (resultsNode == null || !resultsNode.isArray()) {
                return ResponseEntity.ok(Map.of("error", "추천 장소가 없습니다."));
            }

            List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {});

            for (Map<String, String> place : results) {
                String lat = place.get("latitude");
                String lng = place.get("longitude");

                String currentImage = place.get("imageUrl");
                if (currentImage == null || "null".equals(currentImage)) {
                    String imageUrl = googlePlaceService.getImageUrl(lat, lng);
                    place.put("imageUrl", imageUrl != null ? imageUrl : null);
                }
            }
            long end = System.currentTimeMillis(); // 끝 시간 기록d
            long duration = end - start;
            System.out.println("⏱️ 전체 응답 소요 시간: " + duration + "ms");
            
            System.out.println("추천 결과 수: " + results.size());
            results.stream().limit(3).forEach(r -> System.out.println("👉 " + r.get("name")));

            return ResponseEntity.ok(Map.of("results", results));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
        }
    }
}
