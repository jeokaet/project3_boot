package com.kedu.home.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.dto.PlaceDTO;
import com.kedu.home.dto.getPlaceListDTO;
import com.kedu.home.utils.JsonCleanUtils;
import com.kedu.home.utils.PromptBuilder;

@Service
public class GooglePlaceApiService {
	
	@Autowired
	private GeminiService geminiServ;
	
	private final String API_KEY = "AIzaSyCQAXNR2leohogYaE68RNn7LvwFwQK5Ybo&libraries=places";
    private final RestTemplate restTemplate = new RestTemplate();
    private Map<String, Object> getPlaceDetails(String placeId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", placeId)
                .queryParam("fields", "name,formatted_address,geometry,types,photos")
                .queryParam("key", API_KEY)
                .build().toUriString();

        Map<String, Object> detailResp = restTemplate.getForObject(url, Map.class);
        return (Map<String, Object>) detailResp.get("result");
    }

    private String extractImageUrl(List<Map<String, Object>> photos) {
        if (photos == null || photos.isEmpty()) return "null";
        String photoRef = (String) photos.get(0).get("photo_reference");
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + API_KEY;
    }

    private String extractType(List<String> types) {
        if (types == null || types.isEmpty()) return "unknown";
        if (types.contains("restaurant")) return "음식점";
        if (types.contains("cafe")) return "카페";
        if (types.contains("tourist_attraction")) return "관광지";
        if (types.contains("shopping_mall")) return "쇼핑몰";
        if (types.contains("park") || types.contains("natural_feature")) return "자연경관";
        return types.get(0);
    }

    
    public String getImageUrl(String latitude, String longitude) {
        try {
            // 1. Nearby Search
            String nearbyUrl = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                    .queryParam("location", latitude + "," + longitude)
                    .queryParam("radius", 5)
                    .queryParam("key", API_KEY)
                    .build().toUriString();

            Map<String, Object> nearbyResp = restTemplate.getForObject(nearbyUrl, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) nearbyResp.get("results");

            if (results == null || results.isEmpty()) return "";

            // 조건에 맞는 장소 선택
            Map<String, Object> poi = results.stream()
            	    .filter(r -> {
            	        Map<String, Object> geometry = (Map<String, Object>) r.get("geometry");
            	        if (geometry == null) return false;

            	        Map<String, Object> location = (Map<String, Object>) geometry.get("location");
            	        if (location == null) return false;

            	        double latValue = ((Number) location.get("lat")).doubleValue();
            	        double lngValue = ((Number) location.get("lng")).doubleValue();

            	        // 문자열을 double 로 변환 후 비교 (오차 허용 범위 포함)
            	        double targetLat = Double.parseDouble(latitude);
            	        double targetLng = Double.parseDouble(longitude);

            	        // 아주 약간의 오차를 허용 (0.0001 정도)
            	        return Math.abs(latValue - targetLat) < 0.0002 && Math.abs(lngValue - targetLng) < 0.0002;
            	    })
            	    .findFirst()
            	    .orElse(results.get(0)); // 없으면 첫 번째 fallback


            String placeId = (String) poi.get("place_id");

            // 2. Place Details
            String detailsUrl = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                    .queryParam("place_id", placeId)
                    .queryParam("fields", "name,formatted_address,formatted_phone_number,geometry,website,photos")
                    .queryParam("key", API_KEY)
                    .build().toUriString();

            Map<String, Object> detailResp = restTemplate.getForObject(detailsUrl, Map.class);
            Map<String, Object> detail = (Map<String, Object>) detailResp.get("result");


            List<Map<String, Object>> photos = (List<Map<String, Object>>) detail.get("photos");
            String photoRef = (photos != null && !photos.isEmpty()) ? (String) photos.get(0).get("photo_reference") : null;

            String photoUrl = photoRef != null
                    ? "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + API_KEY
                    : null;

       

            return photoUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "이미지 없음";
        }
    }
    
    
    
    public List<Map<String, String>> getPlaceList(getPlaceListDTO request) throws InterruptedException {
    	List<PlaceDTO> results = new ArrayList<>();

        String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        String nextPageToken = null;
        int pageCount = 0;

        do {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("location", request.getLatitude() + "," + request.getLongitude())
                    .queryParam("radius", 10000)
                    .queryParam("rankby", "prominence")
                    .queryParam("key", API_KEY)
                    .queryParamIfPresent("pagetoken", nextPageToken == null ? Optional.empty() : Optional.of(nextPageToken))
                    .build().toUriString();

            Map<String, Object> body = restTemplate.getForObject(url, Map.class);
            if (body == null || !"OK".equals(body.get("status"))) break;

            List<Map<String, Object>> places = (List<Map<String, Object>>) body.get("results");
            for (Map<String, Object> place : places) {
                String placeId = (String) place.get("place_id");
                Map<String, Object> detail = getPlaceDetails(placeId); // 👉 상세 정보 요청

                if (detail == null) continue;

                Map<String, Object> geometry = (Map<String, Object>) detail.get("geometry");
                Map<String, Object> location = (Map<String, Object>) geometry.get("location");

                PlaceDTO dto = new PlaceDTO();
                dto.setName((String) detail.get("name"));
                dto.setRegion((String) detail.get("formatted_address"));
                dto.setLatitude(((Number) location.get("lat")).doubleValue());
                dto.setLongitude(((Number) location.get("lng")).doubleValue());
                dto.setType(extractType((List<String>) detail.get("types")));
                dto.setDescription("null");
                dto.setReason("null");
                dto.setImageUrl(extractImageUrl((List<Map<String, Object>>) detail.get("photos")));

                results.add(dto);
            }

            nextPageToken = (String) body.get("next_page_token");
            pageCount++;

            if (nextPageToken != null) Thread.sleep(2000); // 구글 정책

        } while (nextPageToken != null && pageCount < 3);
        
        long start = System.currentTimeMillis();
        
        System.out.println(results);
        

        try {
        	 String dateStr = request.getDate();
            if (dateStr == null || dateStr.trim().isEmpty()) {
                dateStr = LocalDate.now().toString(); // yyyy-MM-dd 형식
            }
            String prompt = PromptBuilder.buildPrompt2(results, dateStr);
            String llmRaw = geminiServ.call(prompt);
            String llmCleaned = JsonCleanUtils.removeJsonComments(llmRaw);
            
            String cleaned = llmRaw
                    .replaceAll("(?s)```json\\s*|```", "")  // ```json 제거
                    .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "") // 기타 제어문자 제거
                    .replaceAll("[\\r\\n\\t]", " ") // 줄바꿈 계열은 공백으로 변환
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(llmCleaned);
            JsonNode resultsNode = root.get("results");
            

            List<Map<String, String>> filteredResults = mapper.convertValue(resultsNode, new TypeReference<>() {});

            
            long end = System.currentTimeMillis(); // 끝 시간 기록d
            long duration = end - start;
            System.out.println("⏱️ 전체 응답 소요 시간: " + duration + "ms");
            
            

            return filteredResults;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
            
        }

        
    }


}
