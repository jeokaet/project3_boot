package com.kedu.home.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherUtils {

    @Value("${weather.api.key}")
    private String serviceKey;

    // 위경도 → 기상청 격자 변환 (초간단 버전)
    public static class GridXY {
        public int nx;
        public int ny;
        public GridXY(int nx, int ny) {
            this.nx = nx;
            this.ny = ny;
        }
    }

    // 위경도 → 격자
    public GridXY convertToGrid(double lat, double lon) {
        // 서울 기준 (위도: 37.5, 경도: 126.5 → nx:60, ny:127)
        int nx = (int)Math.round((lon - 126.0) * 5) + 60;
        int ny = (int)Math.round((lat - 37.0) * 5) + 127;
        return new GridXY(nx, ny);
    }

    public String getTodayWeatherDescription(int nx, int ny, String dateStr){
        try {
            GridXY grid = convertToGrid(nx, ny);

            String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = "0500"; // 정시 발표 기준 (예: 05시 예보)

            String url = String.format(
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
                "?serviceKey=%s&nx=%d&ny=%d&base_date=%s&base_time=%s&dataType=JSON&pageNo=1&numOfRows=100",
                serviceKey, grid.nx, grid.ny, baseDate, baseTime
            );
            System.out.printf("📍 nx: %d, ny: %d\n", grid.nx, grid.ny);
            
            RestTemplate restTemplate = new RestTemplate();
            String json = restTemplate.getForObject(url, String.class);
            System.out.println("📦 응답 내용: " + json);
            JsonNode root = new ObjectMapper().readTree(json);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                String value = item.path("fcstValue").asText();

                if ("PTY".equals(category)) {
                    switch (value) {
                        case "0": return "맑음";
                        case "1": return "비";
                        case "2": return "비/눈";
                        case "3": return "눈";
                        default: return "알 수 없음";
                    }
                }

                if ("SKY".equals(category)) {
                    switch (value) {
                        case "1": return "맑음";
                        case "3": return "구름많음";
                        case "4": return "흐림";
                        default: return "알 수 없음";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "날씨 정보 없음";
    }
}
