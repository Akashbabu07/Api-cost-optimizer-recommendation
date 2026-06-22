package com.example.recommendation.ai;

import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroqService {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    public RecommendationResponse generateRecommendation(ApiUsageContext context) {

        try {

            String prompt = """
                    You are an API cost optimization expert.

                    Endpoint: %s
                    Hits Per Minute: %d
                    Average Response Time: %.2f ms
                    Database Heavy: %s

                    Give a short actionable optimization suggestion.
                    Return only the recommendation text.
                    """.formatted(
                    context.getEndpoint(),
                    context.getHitsPerMinute(),
                    context.getAvgResponseTime(),
                    context.isDatabaseHeavy()
            );

            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.1-8b-instant",
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "temperature", 0.7
            );

            Map response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List choices = (List) response.get("choices");
            Map message = (Map) ((Map) choices.get(0)).get("message");

            String result = (String) message.get("content");

            return RecommendationResponse.builder()
                    .apiEndpoint(context.getEndpoint())
                    .recommendationType("AI")
                    .message(result)
                    .estimatedCostSaving(40.0)
                    .build();

        } catch (Exception e) {

            log.error("Groq API failed: {}", e.getMessage());

            return RecommendationResponse.builder()
                    .apiEndpoint(context.getEndpoint())
                    .recommendationType("FALLBACK")
                    .message("AI service unavailable, use caching + optimization")
                    .estimatedCostSaving(10.0)
                    .build();
        }
    }
}