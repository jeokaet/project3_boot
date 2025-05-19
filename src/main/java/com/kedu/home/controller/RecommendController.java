package com.kedu.home.controller;

import java.time.LocalDate;
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
import com.kedu.home.utils.JsonCleanUtils;
import com.kedu.home.utils.PromptBuilder;

@RestController
@RequestMapping("/llm")
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


}
