package io.github.meeting.copilot.listener;

import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import io.github.meeting.copilot.service.GeminiProProcessService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class RealTimeSpeechTranscriberListener extends SpeechTranscriberListener {

    private final GeminiProProcessService geminiProProcessService = new GeminiProProcessService();

    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void onTranscriberStart(SpeechTranscriberResponse speechTranscriberResponse) {
        log.info("TaskId: {}, name: {}, status: {}",
                speechTranscriberResponse.getTaskId(),
                speechTranscriberResponse.getName(),
                speechTranscriberResponse.getStatus());
    }

    @Override
    public void onSentenceBegin(SpeechTranscriberResponse speechTranscriberResponse) {
        log.info("Sentence begin, taskId: {}, name: {}, status: {}",
                speechTranscriberResponse.getTaskId(),
                speechTranscriberResponse.getName(),
                speechTranscriberResponse.getStatus());
    }

    @Override
    public void onSentenceEnd(SpeechTranscriberResponse speechTranscriberResponse) {
        String endText = speechTranscriberResponse.getTransSentenceText();
        // get the final result

        executorService.submit(() -> geminiProProcessService.process(endText));
        //geminiProProcessService.process(endText);
    }

    @Override
    public void onTranscriptionResultChange(SpeechTranscriberResponse speechTranscriberResponse) {
        // 中间结果
        log.info("Transcription change result: {}", speechTranscriberResponse.getTransSentenceText());
    }

    @Override
    public void onTranscriptionComplete(SpeechTranscriberResponse speechTranscriberResponse) {
        log.info("Transcription complete: {}", speechTranscriberResponse.getTransSentenceText());
    }

    @Override
    public void onFail(SpeechTranscriberResponse speechTranscriberResponse) {
        log.error("Transcription failed: {}, taskId: {}, status: {}, message: {}",
                speechTranscriberResponse.getName(),
                speechTranscriberResponse.getTaskId(),
                speechTranscriberResponse.getStatus(),
                speechTranscriberResponse.getStatusText());
    }
}
