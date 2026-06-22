package com.example.recommendation.rules;

import com.example.recommendation.dto.ApiUsageContext;
import com.example.recommendation.dto.RecommendationResponse;

public interface RecommendationRule {
    RecommendationResponse apply(ApiUsageContext context);
}