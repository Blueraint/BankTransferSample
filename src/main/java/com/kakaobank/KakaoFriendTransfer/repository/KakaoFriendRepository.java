package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoFriendRepository extends JpaRepository<KakaoFriend, Long> {
    KakaoFriend findByUserId(String userId);
}
