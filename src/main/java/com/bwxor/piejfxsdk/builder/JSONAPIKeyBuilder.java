package com.bwxor.piejfxsdk.builder;

import com.bwxor.piejfxsdk.type.Model;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONAPIKeyBuilder {
    private JSONArray keys = new JSONArray();
    private int currentIndex;

    public JSONAPIKeyBuilder withModel(Model model, String key) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", model.getValue());
        jsonObject.put("key", key);
        keys.put(currentIndex++, jsonObject);
        return this;
    }

    public JSONArray build() {
        return keys;
    }
}
