package io.github.meeting.copilot.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Strings {
        public static final String EMPTY = "";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Aliyun {

        public static final String DEFAULT_PROPERTIES_FILE_PATH = "config/aliyun.properties";

        public static final String ACCESS_KEY_ID = "accessKeyId";

        public static final String ACCESS_KEY = "accessKey";

        public static final String ACCESS_SECRET = "accessSecret";

        public static final String URL = "url";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Gemini {
        public static final String DEFAULT_PROPERTIES_FILE_PATH = "config/geminiPro.properties";

        public static final String KEY = "key";

        public static final String BASE_URL = "baseUrl";

        public static final String PROJECT_ID = "projectId";

        public static final String REGION = "region";

        public static final String MODEL_NAME = "modelName";
    }
}
