package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ClaudeService implements AIService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final List<JSONObject> history = new ArrayList<>();

    @Override
    public Model getModel() {
        return Model.CLAUDE_3_5_SONNET;
    }

    @Override
    public String generateContent(String apiKey, String prompt) {
        history.add(new JSONObject().put("role", "user").put("content", prompt));

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "claude-3-5-sonnet-20241022");
        requestBody.put("max_tokens", 1024);
        requestBody.put("messages", new JSONArray(history));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01") // Required by Anthropic
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .timeout(Duration.ofSeconds(60))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (response.statusCode() != 200) {
                return jsonResponse.getJSONObject("error").getString("message");
            }

            String responseText = jsonResponse.getJSONArray("content")
                    .getJSONObject(0)
                    .getString("text");

            history.add(new JSONObject().put("role", "assistant").put("content", responseText));

            return responseText;

        } catch (Exception e) {
            return "Fatal Error: " + e.getMessage();
        }
    }

    @Override
    public void reset() {
        history.clear();
    }
}