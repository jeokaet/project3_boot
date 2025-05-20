package com.kedu.home.services;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class GeminiService {

	private final OkHttpClient client = new OkHttpClient.Builder()
		    .connectTimeout(30, TimeUnit.MINUTES)
		    .readTimeout(30, TimeUnit.MINUTES)
		    .writeTimeout(30, TimeUnit.MINUTES)
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

            JsonNode json = mapper.readTree(body);  // 여기서 에러나는 중
            String content = json.at("/candidates/0/content/parts/0/text").asText();
            System.out.println("🟢 추출된 LLM 텍스트:\n" + content);
            
            if (content == null) {
                throw new IllegalArgumentException("Gemini 응답이 null입니다.");
            }

            // ✅ 마크다운 블록 제거
            String cleaned = content.replaceAll("(?s)```json\\s*|```", "").trim();
            
            cleaned = cleaned.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", ""); // 콘트롤 문자 중 \r\n\t 제외하고 제거
            cleaned = cleaned.replaceAll("[\\r\\n\\t]", " ");
            
            
            

            return cleaned;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Gemini 호출 중 예외 발생", e);
        }
    }
}
