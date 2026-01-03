package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

import java.util.ArrayList;
import java.util.List;

public class GeminiService implements AIService {
    private Client client;
    private List<Content> history = new ArrayList<>();

    @Override
    public Model getModel() {
        return Model.GEMINI;
    }

    @Override
    public String generateContent(String apiKey, String prompt) {
        if (client == null) {
            client = Client.builder().vertexAI(false).apiKey(apiKey).build();
        }

        Content userMessage = Content.builder()
                .role("user")
                .parts(List.of(Part.builder().text(prompt).build()))
                .build();

        history.add(userMessage);

        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-lite", history, null);

        if (response.text() != null) {
            Content modelResponse = Content.builder()
                    .role("model")
                    .parts(List.of(Part.builder().text(response.text()).build()))
                    .build();
            history.add(modelResponse);
        }

        return response.text();
    }

    @Override
    public void reset() {
        client = null;
    }
}
