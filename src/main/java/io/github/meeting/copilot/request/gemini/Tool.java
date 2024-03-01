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
public class Tool {
    private List<FunctionDeclaration> functionDeclarations;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FunctionDeclaration {
        private String name;

        private String description;

        private Parameter parameters;

        @Data
        @Builder
        public static class Parameter {
        }
    }
}
