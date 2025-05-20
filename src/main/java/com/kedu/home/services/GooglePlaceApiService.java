package com.kedu.home.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
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
   
   @Value("${google.api-key}")
    private String API_KEY;
   
    private final RestTemplate restTemplate = new RestTemplate();

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

    
    
    public List<Map<String, String>> getPlaceList(getPlaceListDTO request) throws InterruptedException {
       List<PlaceDTO> results = new ArrayList<>();
       Set<String> seenPlaceIds = new HashSet<>();
       Map<String, String> photoMap = new HashMap<>();

        String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        List<String> keywords = List.of( "관광지", "음식점", "카페", "쇼핑" );
       

        for (String keyword : keywords) {
            String nextPageToken = null;
            int pageCount = 0;
            
            do {

           
               String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                   .queryParam("location", request.getLatitude() + "," + request.getLongitude())
                   .queryParam("radius", 10000)
                   .queryParam("keyword", keyword)
                   .queryParam("key", API_KEY)
                   .build().toUriString();
               
               Map<String, Object> body = restTemplate.getForObject(url, Map.class);
               if (body == null || !"OK".equals(body.get("status"))) break;
   
               List<Map<String, Object>> places = (List<Map<String, Object>>) body.get("results");
               
               
               for (Map<String, Object> place : places) {
                  String placeId = (String) place.get("place_id"); 
                   List<Map<String, Object>> photos = (List<Map<String, Object>>) place.get("photos");
                   if (seenPlaceIds.contains(placeId)) continue;
                   seenPlaceIds.add(placeId);
                   Map<String, Object> geometry = (Map<String, Object>) place.get("geometry");
                   Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                   
                   String imageUrl = extractImageUrl(photos);
                   System.out.println("장소아이디 : " + placeId);
                   photoMap.put(placeId, imageUrl);
                   
                   PlaceDTO dto = new PlaceDTO();
                   dto.setPlaceId(placeId);
                   dto.setName((String) place.get("name"));
                   dto.setRegion((String) place.get("vicinity"));
                   dto.setLatitude(((Number) location.get("lat")).doubleValue());
                   dto.setLongitude(((Number) location.get("lng")).doubleValue());
                   dto.setType(extractType((List<String>) place.get("types")));
                   dto.setDescription("null");
                   dto.setReason("null");
                   dto.setImageUrl(null);


                   results.add(dto);
               }
               
               nextPageToken = (String) body.get("next_page_token");
               pageCount++;
   
               if (nextPageToken != null) Thread.sleep(2000);
               
            } while (nextPageToken != null && pageCount < 3);
        }
        
        long start = System.currentTimeMillis();
        
        try {
            String dateStr = request.getDate();
            if (dateStr == null || dateStr.trim().isEmpty()) {
                dateStr = LocalDate.now().toString(); // yyyy-MM-dd 형식
            }
            String prompt = PromptBuilder.buildPrompt2(results, dateStr);
            String llmRaw = geminiServ.call(prompt);
            String llmCleaned = JsonCleanUtils.removeJsonComments(llmRaw);
            
            

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> filteredResults = new ArrayList<>();
            
            try {
                JsonNode root = mapper.readTree(llmCleaned); // 여기서 깨짐
                JsonNode resultsNode = root.get("results");
                
                if (resultsNode != null && resultsNode.isArray()) {
                    filteredResults = mapper.convertValue(resultsNode, new TypeReference<>() {});
                } else {
                    System.err.println("⚠️ 'results' 노드가 없거나 배열이 아님: " + root.toPrettyString());
                }

            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            
            for (Map<String, String> place : filteredResults) {

                for (PlaceDTO dto : results) {
                    String placeId = place.get("placeId");
                    String imageUrl = photoMap.get(placeId);
                    place.put("imageUrl", imageUrl);
                    break;
                    }
                }
            
            long duration = System.currentTimeMillis() - start;
            System.out.println("⏱ 응답 시간: " + duration + "ms");
            
            return filteredResults;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
            
        }

        
    }


}
