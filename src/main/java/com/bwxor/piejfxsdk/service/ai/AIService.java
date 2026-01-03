package com.bwxor.piejfxsdk.service.ai;

import com.bwxor.piejfxsdk.type.Model;

public interface AIService {
    Model getModel();
    String generateContent(String apiKey, String prompt);
    void reset();
}
