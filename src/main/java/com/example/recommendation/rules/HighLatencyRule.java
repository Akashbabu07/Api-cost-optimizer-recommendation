package com.example.recommendation.rules;

import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;
import org.springframework.stereotype.Component;

@Component
public class HighLatencyRule implements RecommendationRule {

    @Override
    public RecommendationResponse apply(ApiUsageContext context) {

        if (context.getAvgResponseTime() > 500) {
            return RecommendationResponse.builder()
                    .apiEndpoint(context.getEndpoint())
                    .recommendationType("RULE")
                    .message("Optimize DB / caching / async processing")
                    .estimatedCostSaving(25.0)
                    .build();
        }
        return null;
    }
}