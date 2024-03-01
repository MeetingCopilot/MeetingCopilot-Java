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
public class Content {

    private String role;

    private List<Part> parts;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Part {
        private String text;

        private InlineDate inlineDate;

        private FileData fileData;

        private VideoMetadata videoMetadata;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class InlineDate {
            private String mimeType;

            private String data;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class FileData {
            private String mimeType;

            private String fileUri;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class VideoMetadata {
            private Offset startOffset;

            private Offset endOffset;

            @Data
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Offset {
                private Integer seconds;

                private Integer nanos;
            }
        }
    }
}
