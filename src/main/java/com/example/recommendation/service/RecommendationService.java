package com.example.recommendation.service;

import com.example.recommendation.dto.RecommendationResponse;

public interface RecommendationService {
    RecommendationResponse getRecommendation(String apiId);
}