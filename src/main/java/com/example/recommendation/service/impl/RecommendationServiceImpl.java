package com.example.recommendation.service.impl;

import com.example.recommendation.ai.GroqService;
import com.example.recommendation.cache.RecommendationCacheService;
import com.example.recommendation.client.AnalyticsClient;
import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;
import com.example.recommendation.rules.RecommendationRule;
import com.example.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final List<RecommendationRule> rules;
    private final GroqService groqService;
    private final AnalyticsClient analyticsClient;
    private final RecommendationCacheService cache;

    @Override
    public RecommendationResponse getRecommendation(String apiId) {

        String key = "rec:v1:api:" + apiId;

        RecommendationResponse cached = cache.get(key);

        if (cached != null) return cached;

        ApiUsageContext context;

        try {
            context = analyticsClient.getContext(apiId);
        } catch (Exception ex) {
            return RecommendationResponse.builder()
                    .apiEndpoint(apiId)
                    .recommendationType("FALLBACK")
                    .message("Analytics service unavailable")
                    .estimatedCostSaving(0)
                    .build();
        }

        // Rule engine first
        for (RecommendationRule rule : rules) {
            RecommendationResponse response = rule.apply(context);
            if (response != null) {
                cache.put(key, response);
                return response;
            }
        }

        // AI call (Groq)
        RecommendationResponse ai = groqService.generateRecommendation(context);

        if (!"FALLBACK".equals(ai.getRecommendationType())) {
            cache.put(key, ai);
        }

        return ai;
    }
}
