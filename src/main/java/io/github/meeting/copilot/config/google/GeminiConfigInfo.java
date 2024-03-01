package io.github.meeting.copilot.config.google;

import io.github.meeting.copilot.config.nls.NlsConfigInfo;
import io.github.meeting.copilot.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public record GeminiConfigInfo(String key,
                               String baseUrl) {

    public static GeminiConfigInfo ofEmpty() {
        return new GeminiConfigInfo(null, null);
    }

    public static GeminiConfigInfo ofDefault() {
        Properties properties = new Properties();

        try (InputStream inputStream = NlsConfigInfo.class.getClassLoader()
                .getResourceAsStream(Constant.Gemini.DEFAULT_PROPERTIES_FILE_PATH)) {
            if (Objects.isNull(inputStream)) {
                log.error("Properties file not found, path: {}", Constant.Gemini.DEFAULT_PROPERTIES_FILE_PATH);
                return ofEmpty();
            }

            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Failed to load properties file", e);
        }
        return new GeminiConfigInfo(properties.getProperty(Constant.Gemini.KEY, Constant.Strings.EMPTY),
                properties.getProperty(Constant.Gemini.BASE_URL, Constant.Strings.EMPTY));
    }
}
