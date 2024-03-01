package io.github.meeting.copilot.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.meeting.copilot.config.google.GeminiConfigInfo;
import io.github.meeting.copilot.constant.Constant;
import io.github.meeting.copilot.listener.GeminiResponseListener;
import io.github.meeting.copilot.request.gemini.Content;
import io.github.meeting.copilot.request.gemini.GeminiRequestParam;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class GeminiProProcessService {

    private final GeminiConfigInfo geminiConfigInfo;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1087)))
            .build();

    public GeminiProProcessService() {
        this.geminiConfigInfo = GeminiConfigInfo.ofDefault();

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void process(String sentence) {
        EventSource.Factory factory = EventSources.createFactory(okHttpClient);

        // 请求对象
        Request request = new Request.Builder()
                .url(buildUrl())
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), buildBody(sentence)))
                .build();

        // 自定义监听器
        EventSourceListener eventSourceListener = new GeminiResponseListener();

        // 创建事件
        EventSource eventSource = factory.newEventSource(request, eventSourceListener);

        // 等待线程结束
        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("error", e);
        }
    }

    private String buildBody(String sentence) {
        GeminiRequestParam param = GeminiRequestParam.builder()
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
            body = OBJECT_MAPPER.writeValueAsString(param);
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
