package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;

public class ClaudeService implements AIService {
    @Override
    public Model getModel() {
        return Model.CLAUDE;
    }

    @Override
    public String generateContent(String apiKey, String prompt) {
        return "";
    }

    @Override
    public void reset() {

    }
}
