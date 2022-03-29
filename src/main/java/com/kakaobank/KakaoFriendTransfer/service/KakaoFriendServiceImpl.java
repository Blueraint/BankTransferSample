package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.KakaoFriendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoFriendServiceImpl implements KakaoFriendService {
    private final KakaoFriendRepository kakaoFriendRepository;

    @Override
    public KakaoFriend findByUserId(String userId) {
        return kakaoFriendRepository.findByUserId(userId);
    }

    // External Service Impl
    @Override
    public List<KakaoFriend> findAll() {
        log.info("### Find KakaoFriends List ###");

        List<KakaoFriend> resultList = new ArrayList<>();

        return resultList;
    }

    // External Service Impl
    @Override
    public ResponseEntity sendKakaoMessage(KakaoFriend fromKakaoFriend, KakaoFriend toKakaoFriend, String message) {
        log.info("### Kakao Message Send : (from) {}, (to) {}, (msg) {}",fromKakaoFriend.getUserId(), toKakaoFriend.getUserId(), message);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        return responseEntity;
    }
}
