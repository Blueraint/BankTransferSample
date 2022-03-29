package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jpaKakaoFriendRepository")
public interface KakaoFriendRepository extends JpaRepository<KakaoFriend, Long> {
    KakaoFriend findByUserId(String userId);
}
