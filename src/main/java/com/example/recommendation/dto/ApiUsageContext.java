package com.example.recommendation.dto;

import lombok.Data;

@Data
public class ApiUsageContext {
    private String endpoint;
    private long hitsPerMinute;
    private double avgResponseTime;
    private boolean databaseHeavy;
}