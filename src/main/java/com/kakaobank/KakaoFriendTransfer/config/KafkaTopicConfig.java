package com.kakaobank.KakaoFriendTransfer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    /**
     * - KakaoMessage 발송의 Channel(Managed by Topic) 연동이 필요할 경우를 가정해서 구현함
     */

    @Bean
    public NewTopic kakaoMessageTopic() {
        return TopicBuilder.name("KakaoMessage")
                .build();
    }
}