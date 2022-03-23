package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface KakaoFriendExternalService {
    /*
     * KakaoFriends List API
     */
    List<KakaoFriend> findAll();

    /*
     * Send Message API
     */
    ResponseEntity sendKakaoMessage(KakaoFriend fromKakaoFriend, KakaoFriend toKakaoFriend, String message);
}
