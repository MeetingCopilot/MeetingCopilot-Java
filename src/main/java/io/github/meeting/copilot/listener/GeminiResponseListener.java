package io.github.meeting.copilot.listener;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class GeminiResponseListener extends EventSourceListener {
    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("关闭 sse 连接...");
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        log.info("返回数据：{}", data);
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        if (Objects.isNull(response)) {
            log.error("sse 连接异常", t);
            eventSource.cancel();
            return;
        }

        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            String string = null;
            try {
                string = body.string();
            } catch (IOException e) {
                log.error("sse 连接异常 data：{}，异常：", response, e);
            }
            log.error("sse 连接异常 data：{}，异常：{}", string, t);
        } else {
            log.error("sse 连接异常 data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.info("建立 sse 连接...");
    }
}
