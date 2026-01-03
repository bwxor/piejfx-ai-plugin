package com.bwxor.piejfxsdk.type;

public enum Model {
    GEMINI("gemini"), CHATGPT("chatgpt"), CLAUDE("claude");

    private String value;

    Model(String value) {
        this.value = value;
    }

    public static Model parse(String input) {
        for(Model m : values()) {
            if (m.value.equals(input)) {
                return m;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }
}
