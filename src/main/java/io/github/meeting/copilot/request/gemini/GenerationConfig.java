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
public class GenerationConfig {
    private Double temperature;

    private Double topP;

    private Double topK;

    private Integer candidateCount;

    private Integer maxOutputTokens;

    private List<String> stopSequences;
}
