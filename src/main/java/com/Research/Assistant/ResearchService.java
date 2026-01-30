package com.Research.Assistant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClient, ObjectMapper objectMapper) {
        this.webClient = webClient.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest researchRequest) {

        // 1️ Build the prompt
        String prompt = buildPrompt(researchRequest);

        // 2 Prepare request body
        Map<String, Object> requestBody = Map.of(
                "model", "openai/gpt-4o-mini",
                "max_tokens", 500,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        // 3 API Call with debug
        try {
            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Debug: see raw response
            System.out.println("Raw AI response:\n" + response);

            // Check if response is JSON
            if (response == null || !response.trim().startsWith("{")) {
                return "Invalid response from OpenRouter API (check API key or URL).";
            }

            // 4️ Extract AI text
            return extractTextFromResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling OpenRouter API: " + e.getMessage();
        }
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            System.out.println("Error Parsing: " + e.getMessage());
            return "Error reading AI response.";
        }
    }

    private String buildPrompt(ResearchRequest researchRequest) {
        StringBuilder prompt = new StringBuilder();
        switch (researchRequest.getOperation()) {
            case "summarize": prompt.append("Provide a clear and concise summary of the following text:\n\n"); break;
            case "suggest": prompt.append("Based on the following content, suggest related topics and further reading:\n\n"); break;
            case "explain": prompt.append("Explain the following content in simple terms with examples:\n\n"); break;
            case "translate": prompt.append("Translate the following content into English:\n\n"); break;
            case "keywords": prompt.append("Extract important keywords and key phrases from the following content:\n\n"); break;
            case "qa": prompt.append("Generate 5 question-and-answer pairs based on the following content:\n\n"); break;
            case "compare": prompt.append("Compare the main concepts in the following content in a table format:\n\n"); break;
            case "steps": prompt.append("Convert the following content into step-by-step instructions:\n\n"); break;
            case "sentiment": prompt.append("Analyze the sentiment (positive, negative, or neutral) of the following text:\n\n"); break;
            default: throw new IllegalArgumentException("Unknown Operation: " + researchRequest.getOperation());
        }
        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }
}
