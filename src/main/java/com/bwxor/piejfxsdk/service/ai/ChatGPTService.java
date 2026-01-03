package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;

public class ChatGPTService implements AIService {

    @Override
    public Model getModel() {
        return Model.CHATGPT;
    }

    @Override
    public String generateContent(String apiKey, String prompt) {
        return "";
    }

    @Override
    public void reset() {

    }
}
