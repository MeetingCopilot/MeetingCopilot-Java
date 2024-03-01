package io.github.meeting.copilot.enums;

import lombok.Getter;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Getter
public enum Models {
    CHAT_BISON_001("chat-bison-001", ""),
    TEXT_BISON_001("text-bison-001", ""),
    EMBEDDING_GECKO_001("embedding-gecko-001", ""),
    GEMINI_1_0_PRO("gemini-1.0-pro", ""),
    GEMINI_1_0_PRO_001("gemini-1.0-pro-001", ""),
    GEMINI_1_0_PRO_LATEST("gemini-1.0-pro-latest", ""),
    GEMINI_1_0_PRO_VISION_LATEST("gemini-1.0-pro-vision-latest", ""),
    GEMINI_PRO("gemini-pro", ""),
    GEMINI_PRO_VISION("gemini-pro-vision", ""),
    EMBEDDING_001("embedding-001", ""),
    AQA("aqa", "");

    private final String name;

    private final String desc;

    Models(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
