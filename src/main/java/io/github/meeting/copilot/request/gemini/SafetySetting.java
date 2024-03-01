package io.github.meeting.copilot.request.gemini;

import io.github.meeting.copilot.enums.gemini.safety.HarmBlockThreshold;
import io.github.meeting.copilot.enums.gemini.safety.HarmCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafetySetting {
    private HarmCategory harmCategory;

    private HarmBlockThreshold harmBlockThreshold;
}
