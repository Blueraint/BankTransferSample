package com.kakaobank.KakaoFriendTransfer.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    /**
     * Kafka Consumer by Listener
     */

    /*
    * Kafka 구조, Partition 등 고려해야함
    */
    @KafkaListener(
            topics = "KakaoMessage",
            groupId = "KakaoFriendTransfer")
    public void listener(String data) {
        System.out.println("KakaoFriendTransfer Application Kafka Listener Received : " + data);
    }
}