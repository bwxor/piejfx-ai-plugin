package com.bwxor.piejfxsdk.type;

public enum Model {
    GEMINI_2_5_FLASH_LITE("gemini-2.5-flash-lite"), CHATGPT_4O("chatgpt-4o"), CLAUDE_3_5_SONNET("claude-3.5-sonnet");

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
