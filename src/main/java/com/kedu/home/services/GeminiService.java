package com.kedu.home.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;

@Service
public class GeminiService {

	private final OkHttpClient client = new OkHttpClient.Builder()
		    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)     // 연결 시도 제한 시간
		    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)        // 서버 응답 기다리는 시간
		    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)       // 요청 바디 전송 제한 시간
		    .build();


    @Value("${gemini.api.key}")
    private String apiKey;

    private static final MediaType JSON = MediaType.parse("application/json");

    public String call(String prompt) throws IOException {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=" + apiKey;

        ObjectMapper mapper = new ObjectMapper();
        String bodyJson = mapper.writeValueAsString(
                Map.of("contents", new Object[] {
                        Map.of("parts", new Object[] { Map.of("text", prompt) })
                })
        );
        

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(bodyJson, JSON))
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("❌ Gemini 응답 실패 - 코드: " + response.code());
                System.out.println("❌ 응답 바디: " + response.body());
                throw new IOException("Gemini 응답 실패: " + response.code());
            }

            String body = response.body().string();

            JsonNode json = mapper.readTree(body);
            String content = json.at("/candidates/0/content/parts/0/text").asText();

            // ✅ 마크다운 블록 제거
            String cleaned = content.replaceAll("(?s)```json\\s*|```", "").trim();
            
            
            

            return cleaned;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Gemini 호출 중 예외 발생", e);
        }
    }
}
