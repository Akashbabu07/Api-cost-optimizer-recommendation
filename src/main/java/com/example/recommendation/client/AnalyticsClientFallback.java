package com.example.recommendation.client;

import com.example.recommendation.dto.ApiUsageContext;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsClientFallback implements AnalyticsClient {

    @Override
    public ApiUsageContext getContext(String apiId) {

        ApiUsageContext context = new ApiUsageContext();

        context.setEndpoint(apiId);
        context.setHitsPerMinute(0);
        context.setAvgResponseTime(0);
        context.setDatabaseHeavy(false);

        return context;
    }
}