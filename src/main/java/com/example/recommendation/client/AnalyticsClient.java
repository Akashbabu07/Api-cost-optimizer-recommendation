package com.example.recommendation.client;

import com.example.recommendation.dto.ApiUsageContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "analytics-service",
        url = "${analytics.service.url}",
        fallback = AnalyticsClientFallback.class
)
public interface AnalyticsClient {

    @GetMapping("/analytics/context")
    ApiUsageContext getContext(@RequestParam String apiId);
}