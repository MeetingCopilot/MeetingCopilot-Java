package io.github.meeting.copilot.config.nls;

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
public record NlsConfigInfo(String accessKeyId,
                            String accessKey,
                            String accessSecret,
                            String url) {

    public static NlsConfigInfo ofEmpty() {
        return new NlsConfigInfo(null, null, null, null);
    }

    public static NlsConfigInfo ofDefault() {
        Properties properties = new Properties();

        try (InputStream inputStream = NlsConfigInfo.class.getClassLoader()
                .getResourceAsStream(Constant.Aliyun.DEFAULT_PROPERTIES_FILE_PATH)) {
            if (Objects.isNull(inputStream)) {
                log.error("Properties file not found, path: {}", Constant.Aliyun.DEFAULT_PROPERTIES_FILE_PATH);
                return ofEmpty();
            }

            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Failed to load aliyun.properties", e);
        }

        return new NlsConfigInfo(
                properties.getProperty(Constant.Aliyun.ACCESS_KEY_ID, Constant.Strings.EMPTY),
                properties.getProperty(Constant.Aliyun.ACCESS_KEY, Constant.Strings.EMPTY),
                properties.getProperty(Constant.Aliyun.ACCESS_SECRET, Constant.Strings.EMPTY),
                properties.getProperty(Constant.Aliyun.URL, Constant.Strings.EMPTY)
        );
    }
}
