package com.Research.Assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResearchService {


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

        //3. parse the response
        //4. Return response



        return "";
    }

    //1. Build the prompt
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
