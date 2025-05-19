package com.kedu.home.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.dto.LLMRequestDTO;
import com.kedu.home.dto.getPlaceListDTO;
import com.kedu.home.services.GeminiService;
import com.kedu.home.services.GooglePlaceApiService;
import com.kedu.home.services.PerspectiveService;
import com.kedu.home.utils.AbuseFilterUtils;
import com.kedu.home.utils.GridConverter;
import com.kedu.home.utils.JsonCleanUtils;
import com.kedu.home.utils.PromptBuilder;
import com.kedu.home.utils.WeatherUtils;

@RestController
@RequestMapping("/api")
public class RecommendController {

	@Autowired
	private GeminiService GServ;

	@Autowired
	private PerspectiveService PServ;

	@Autowired
	private GooglePlaceApiService googlePlaceService;

	@Autowired
	private WeatherUtils weatherUtils;

	@PostMapping("/llm-recommend")
	public ResponseEntity<?> recommendPlaces(@RequestBody LLMRequestDTO request) {
		try {
			String userInput = request.getUserInput();

			if (PServ.isToxic(userInput) || AbuseFilterUtils.isAbusiveOnly(userInput)) {
				return ResponseEntity.ok(Map.of("error", "입력에 욕설 및 공격적인 표현이 들어가 있어 추천을 중단합니다."));
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

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});
			System.out.println("추천 결과 수: " + results.size());
			results.stream().limit(3).forEach(r -> System.out.println("👉 " + r.get("name")));

			return ResponseEntity.ok(Map.of("results", results));

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

			// ✅ 날짜 없으면 오늘로
			String dateStr = request.getDate();
			if (dateStr == null || dateStr.trim().isEmpty()) {
				dateStr = LocalDate.now().toString(); // yyyy-MM-dd
			}

			// ✅ 위경도 받아서 날씨 조회
			double lat = request.getLatitude();
			double lon = request.getLongitude();

			GridConverter.GridCoord grid = GridConverter.convertToGrid(lat, lon);
			int nx = grid.nx;
			int ny = grid.ny;

			System.out.printf("📍 변환된 nx: %d, ny: %d\n", nx, ny);
			String weather = weatherUtils.getTodayWeatherDescription(nx, ny, dateStr);

			// ✅ 날씨 포함한 프롬프트 생성
			String prompt = PromptBuilder.buildPrompt2(request.getStartingLocation(), dateStr, weather);
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

			List<Map<String, String>> results = mapper.convertValue(resultsNode, new TypeReference<>() {
			});

			// 이미지 추가 로직 생략 가능
			for (Map<String, String> place : results) {
				String latStr = place.get("latitude");
				String lonStr = place.get("longitude");
				String currentImage = place.get("imageUrl");

				if (currentImage == null || "null".equals(currentImage)) {
					String imageUrl = googlePlaceService.getImageUrl(latStr, lonStr);
					place.put("imageUrl", imageUrl != null ? imageUrl : null);
				}
			}

			long duration = System.currentTimeMillis() - start;
			System.out.println("⏱️ 응답 시간: " + duration + "ms");
			return ResponseEntity.ok(Map.of("results", results,"weather", weather));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "LLM 호출 실패"));
		}
	}

}
