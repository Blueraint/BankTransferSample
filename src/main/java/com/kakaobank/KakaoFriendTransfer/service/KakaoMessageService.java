package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoMessageService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(KakaoMessage message) {
        kafkaTemplate.send("KakaoMessage", message);
    }
}
