package com.example.recommendation.rules;

import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;
import org.springframework.stereotype.Component;

@Component
public class HighTrafficRule implements RecommendationRule {

    @Override
    public RecommendationResponse apply(ApiUsageContext context) {

        if (context.getHitsPerMinute() > 1000) {
            return RecommendationResponse.builder()
                    .apiEndpoint(context.getEndpoint())
                    .recommendationType("RULE")
                    .message("Enable caching or rate limiting")
                    .estimatedCostSaving(30.0)
                    .build();
        }
        return null;
    }
}