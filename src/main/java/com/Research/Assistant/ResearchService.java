package com.Research.Assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService {
//    Spring reads application.properties at startup.
//   It finds:
//
//    gemini.api.url = ....
//    gemini.api.key = ....
//
//
//    - It automatically puts those values into:
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    // webclient:
    //A WebClient is a tool/library that allows
    // your Java program to call another web API or website over HTTP.
    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClient , ObjectMapper objectMapper ){
        this.webClient = webClient.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest researchRequest) {
        //1. Build the prompt
        String prompt = buildPrompt(researchRequest);

        //2. Query the AI model API
        Map<String, Object> requestBody = Map.of(
            "constents", new Object[] {
                    Map.of("parts", new Object[]{
                            Map.of("text", prompt)
                    })

        });

        // basically used for call other web api like(gemini api)
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)// Set the API URL where request will go.
                .bodyValue(requestBody) // Send data in request body (JSON / object).
                .retrieve() //Send the request and start receiving the response
                .bodyToMono(String.class)
                .block();//Wait until response is received and return actual value.


        //3. parse the response
        // after getting response we have a lot of content like body
        // so we have only important content is "text" -> extra text from response

        // by calling extractTextFromResponse
        //4. Return response


        // pass above response
        return extractTextFromResponse(response);
    }

    //3. // after getting response we have a lot of content like body
    //        // so we have only important content is "text" -> extra text from response


    private String extractTextFromResponse(String response) {
        try {
            // Convert JSON response to GeminiResponse object
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);

            // Check if candidates exist
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);

                // Check if content parts exist
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {

                    // Return the first part's text
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: " + e.getMessage());
        }

        // Return null or empty string if no text found
        return "No Content found in response!";
    }


    //1. Build the prompts
    private String buildPrompt(ResearchRequest researchRequest){

        StringBuilder prompt = new StringBuilder();

        switch (researchRequest.getOperation()) {

            case "summarize":
                prompt.append(
                        "Provide a clear and concise summary of the following text in a few sentences:\n\n"
                );
                break;

            case "suggest":
                prompt.append(
                        "Based on the following content, suggest related topics and further reading. " +
                                "Format the response with clear headings and bullet points:\n\n"
                );
                break;

            case "explain":
                prompt.append(
                        "Explain the following content in simple terms with examples:\n\n"
                );
                break;

            case "translate":
                prompt.append(
                        "Translate the following content into English clearly and accurately:\n\n"
                );
                break;

            case "keywords":
                prompt.append(
                        "Extract important keywords and key phrases from the following content:\n\n"
                );
                break;

            case "qa":
                prompt.append(
                        "Generate 5 question-and-answer pairs based on the following content:\n\n"
                );
                break;

            case "compare":
                prompt.append(
                        "Compare the main concepts in the following content in a table format:\n\n"
                );
                break;

            case "steps":
                prompt.append(
                        "Convert the following content into step-by-step instructions:\n\n"
                );
                break;

            case "sentiment":
                prompt.append(
                        "Analyze the sentiment (positive, negative, or neutral) of the following text:\n\n"
                );
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown Operation: " + researchRequest.getOperation()
                );
        }

        prompt.append(researchRequest.getContent());

        return prompt.toString();

    }
}
