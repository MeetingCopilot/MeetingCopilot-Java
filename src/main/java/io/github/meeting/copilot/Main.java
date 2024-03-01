package io.github.meeting.copilot;

import io.github.meeting.copilot.config.nls.NlsConfigInfo;
import io.github.meeting.copilot.service.GeminiProProcessService;
import io.github.meeting.copilot.service.SpeechTranscriberWithMicrophone;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
//        NlsConfigInfo info = NlsConfigInfo.ofDefault();
//
//        SpeechTranscriberWithMicrophone transcriber = new SpeechTranscriberWithMicrophone(info.accessKeyId(),
//                info.accessKey(),
//                info.accessSecret(),
//                info.url());
//        transcriber.process();
//        transcriber.shutdown();

        GeminiProProcessService geminiProProcessService = new GeminiProProcessService();

        geminiProProcessService.process("Redis 集群模式和哨兵模式的区别是什么？");
    }
}
