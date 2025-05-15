package com.kedu.home.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GooglePlaceApiService {
	
	private final String API_KEY = "AIzaSyCQAXNR2leohogYaE68RNn7LvwFwQK5Ybo&libraries=places";
    private final RestTemplate restTemplate = new RestTemplate();
    
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
}
