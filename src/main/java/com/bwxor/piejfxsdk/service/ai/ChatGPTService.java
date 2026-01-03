package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTService implements AIService {
    private OpenAiService service;
    private final List<ChatMessage> history = new ArrayList<>();

    @Override
    public Model getModel() {
        return Model.CHATGPT_4O;
    }

    @Override
    public String generateContent(String apiKey, String prompt) {
        if (service == null) {
            service = new OpenAiService(apiKey, Duration.ofSeconds(60));

            history.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant inside a code editor."));
        }

        history.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o")
                .messages(history)
                .n(1)
                .maxTokens(2000)
                .build();

        try {
            ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage();

            history.add(responseMessage);

            return responseMessage.getContent();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public void reset() {
        service = null;
        history.clear();
    }
}