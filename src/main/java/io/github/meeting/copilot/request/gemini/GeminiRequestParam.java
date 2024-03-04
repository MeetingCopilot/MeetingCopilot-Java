package io.github.meeting.copilot.request.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeminiRequestParam {

    private List<Content> contents;

    private List<Tool> tools;

    private List<SafetySetting> safetySettings;

    private GenerationConfig generationConfig;


}
