package com.example.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse  implements Serializable {
    private String apiEndpoint;
    private String recommendationType;
    private String message;
    private double estimatedCostSaving;
}