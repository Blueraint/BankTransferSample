package com.kakaobank.KakaoFriendTransfer;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoMessage;
import com.kakaobank.KakaoFriendTransfer.service.KakaoMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class KafkaTest {
    @Autowired
    private KakaoMessageService kakaoMessageService;

    @Test
    public void kakaoMessageTest() {
        IntStream.range(0,10).forEach(i -> {
            String message = "This Message for [" + String.valueOf(i) + "]";

            kakaoMessageService.publish(new KakaoMessage(String.valueOf(i), message));
        });
    }
}
