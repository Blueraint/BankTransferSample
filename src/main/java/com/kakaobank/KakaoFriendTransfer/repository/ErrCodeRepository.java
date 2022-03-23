package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.ErrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrCodeRepository extends JpaRepository<ErrCode, String> {
}
