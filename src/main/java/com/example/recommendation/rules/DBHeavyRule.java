package com.example.recommendation.rules;

import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;
import org.springframework.stereotype.Component;

@Component
public class DBHeavyRule implements RecommendationRule {

    @Override
    public RecommendationResponse apply(ApiUsageContext context) {

        if (context.isDatabaseHeavy()) {
            return RecommendationResponse.builder()
                    .apiEndpoint(context.getEndpoint())
                    .recommendationType("RULE")
                    .message("Enable caching for DB-heavy API")
                    .estimatedCostSaving(20.0)
                    .build();
        }
        return null;
    }
}