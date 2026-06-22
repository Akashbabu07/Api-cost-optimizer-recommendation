package com.example.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeminiRequest {

    @JsonProperty("contents")
    private List<Content> contents;

    @Data
    @AllArgsConstructor
    public static class Content {

        @JsonProperty("parts")
        private List<Part> parts;
    }

    @Data
    @AllArgsConstructor
    public static class Part {

        @JsonProperty("text")
        private String text;
    }
}
