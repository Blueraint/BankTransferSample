package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KakaoFriendService extends KakaoFriendExternalService{
    KakaoFriend findByUserId(String userId);
}
