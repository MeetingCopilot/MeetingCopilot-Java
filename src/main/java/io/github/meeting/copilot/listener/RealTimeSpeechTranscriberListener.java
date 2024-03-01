package io.github.meeting.copilot.listener;

import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class RealTimeSpeechTranscriberListener extends SpeechTranscriberListener {

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
        log.info("Sentence end: {}", endText);

        // get the final result

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
