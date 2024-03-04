package io.github.meeting.copilot.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.meeting.copilot.config.google.GeminiConfigInfo;
import io.github.meeting.copilot.constant.Constant;
import io.github.meeting.copilot.request.gemini.Content;
import io.github.meeting.copilot.request.gemini.GeminiRequestParam;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class GeminiProProcessService {

    private final GeminiConfigInfo geminiConfigInfo;

    private GeminiRequestParam param;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build();

    public GeminiProProcessService() {
        this.geminiConfigInfo = GeminiConfigInfo.ofDefault();

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void process(String sentence) {
        Lock lock = new ReentrantLock(true);
        lock.lock();
        RequestBody requestBody = RequestBody.Companion.create(buildChatBody(sentence),
                MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(buildUrl())
                .post(requestBody)
                .build();

        String filePath = "/Users/zcy1/Downloads/qa.md";
        try (Response response = okHttpClient.newCall(request).execute();
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!response.isSuccessful() || Objects.isNull(response.body())) {
                log.error("Request failed, response code: {}", response.code());
                param.setContents(new ArrayList<>());
                return;
            }

            String body = response.body().string();

            List<String> contentList = processBody(body);
            log.info("问题：{}", sentence);

            writer.write("### 问题：" + sentence);
            writer.write("\n");
            writer.write("\n");
            writer.write("答案：" + String.join(Constant.Strings.EMPTY, contentList));

            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
        } catch (Exception e) {
            log.error("Error when request", e);
        }

        lock.unlock();
    }

    private List<String> processBody(String body) {
        if (Objects.isNull(body) || body.isEmpty()) {
            return Collections.emptyList();
        }

        JsonNode jsonNode;
        try {
            jsonNode = OBJECT_MAPPER.readTree(body);
        } catch (JsonProcessingException e) {
            log.error("Error when parse body", e);
            return Collections.emptyList();
        }

        List<Content> contentList = new ArrayList<>();
        jsonNode.get("candidates").forEach(candidate -> {
            JsonNode contentNode = candidate.get("content");
            try {
                contentList.add(OBJECT_MAPPER.treeToValue(contentNode, Content.class));
            } catch (JsonProcessingException e) {
                log.error("Error when process body", e);
            }
        });

        param.getContents().addAll(contentList);

        return contentList.stream()
                .filter(Objects::nonNull)
                .flatMap(content -> content.getParts().stream())
                .filter(Objects::nonNull)
                .map(Content.Part::getText)
                .collect(Collectors.toList());
    }

    private String buildChatBody(String sentence) {
        if (Objects.isNull(param)) {
            List<Content> contentList = new ArrayList<>();
            param = GeminiRequestParam.builder()
                    .contents(contentList)
                    .build();
        }

        param.getContents().add(Content.builder()
                .role("user")
                .parts(List.of(
                        Content.Part.builder()
                                .text(sentence)
                                .build()))
                .build());

        String body;
        try {
            body = OBJECT_MAPPER.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            log.error("Error when build body", e);
            return Constant.Strings.EMPTY;
        }

        return body;
    }

    @SuppressWarnings("unused")
    private String buildBody(String sentence) {
        GeminiRequestParam requestParam = GeminiRequestParam.builder()
                .contents(List.of(
                        Content.builder()
                                .role("user")
                                .parts(List.of(
                                        Content.Part.builder()
                                                .text(sentence)
                                                .build()))
                                .build()

                ))
                .build();

        String body;
        try {
            body = OBJECT_MAPPER.writeValueAsString(requestParam);
        } catch (JsonProcessingException e) {
            log.error("Error when build body", e);
            return Constant.Strings.EMPTY;
        }

        return body;
    }

    private HttpUrl buildUrl() {
        return Objects.requireNonNull(HttpUrl.parse(geminiConfigInfo.baseUrl()))
                .newBuilder()
                .addQueryParameter("key", geminiConfigInfo.key())
                .build();
    }
}
