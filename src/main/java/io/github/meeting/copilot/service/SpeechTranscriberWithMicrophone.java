package io.github.meeting.copilot.service;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import io.github.meeting.copilot.listener.RealTimeSpeechTranscriberListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author HydroCarbon
 * @since 2024-03-01
 */
@Slf4j
public class SpeechTranscriberWithMicrophone {

    private final String accessKey;

    private NlsClient nlsClient;

    public SpeechTranscriberWithMicrophone(String accessKeyId,
                                           String accessKey,
                                           String accessSecret,
                                           String url) {
        this.accessKey = accessKey;
        var accessToken = new AccessToken(accessKeyId, accessSecret);

        try {
            accessToken.apply();
        } catch (IOException e) {
            log.error("Failed to apply access token", e);
            return;
        }

        if (Objects.isNull(url) || url.isBlank()) {
            log.warn("URL is not set, using default URL");
            nlsClient = new NlsClient(accessToken.getToken());
        } else {
            nlsClient = new NlsClient(url, accessToken.getToken());
        }
    }

    @SneakyThrows
    public void process() {
        SpeechTranscriber transcriber = null;

        try {
            transcriber = new SpeechTranscriber(nlsClient, new RealTimeSpeechTranscriberListener());
            // 设置 AppKey
            transcriber.setAppKey(accessKey);
            // 设置音频编码格式
            transcriber.setFormat(InputFormatEnum.PCM);
            // 设置音频采样率
            transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置是否返回中间识别结果
            transcriber.setEnableIntermediateResult(false);
            // 设置是否生成并返回标点符号
            transcriber.setEnablePunctuation(true);
            // 设置是否将返回结果规整化，比如将一百返回为 100
            transcriber.setEnableITN(false);

            // 启动录音和识别
            transcriber.start();

            AudioFormat audioFormat = new AudioFormat(16000.0F, 16,
                    1, true, false);

            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

            Mixer.Info firstInfo = Arrays.stream(mixerInfos)
                    .filter(info -> info.getName().equals("BlackHole 2ch"))
                    .findFirst().orElse(null);

            if (Objects.isNull(firstInfo)) {
                log.error("No microphone found with the name 'BlackHole 2ch'");
                transcriber.stop();
                return;
            }

            Mixer mixer = AudioSystem.getMixer(firstInfo);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            Line line = mixer.getLine(info);
            if (Objects.nonNull(line) && line instanceof TargetDataLine targetDataLine) {
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                log.info("Start recording");

                int nByte = 0;
                final int bufSize = 6400;
                byte[] buffer = new byte[bufSize];
                while ((nByte = targetDataLine.read(buffer, 0, bufSize)) > 0) {
                    transcriber.send(buffer, nByte);
                }
                transcriber.stop();
            }
        } catch (LineUnavailableException e) {
            log.error("Failed to open audio line", e);
        } catch (Exception e) {
            log.error("Failed to start transcriber", e);
        }
    }

    public void shutdown() {
        if (Objects.nonNull(nlsClient)) {
            nlsClient.shutdown();
        }
    }
}
