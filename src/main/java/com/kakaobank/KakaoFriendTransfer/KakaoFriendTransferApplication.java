package com.kakaobank.KakaoFriendTransfer;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoMessage;
import com.kakaobank.KakaoFriendTransfer.service.KakaoMessageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.stream.IntStream;

//@EnableRedisRepositories(basePackages = "com.kakaobank.KakaoFriendTransfer.repository.redis")
//@EnableJpaRepositories(basePackages = "com.kakaobank.KakaoFriendTransfer.repository.jpa")
@EnableCaching
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class KakaoFriendTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaoFriendTransferApplication.class, args);
	}
}
