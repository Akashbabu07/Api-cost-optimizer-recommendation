package com.example.recommendation.controller;

import com.example.recommendation.dto.RecommendationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.recommendation.service.RecommendationService;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping("/**")
    public RecommendationResponse get(
            HttpServletRequest request) {

        String fullPath = request.getRequestURI()
                .replaceFirst("/recommendations/", "");

        return service.getRecommendation(fullPath);
    }
}