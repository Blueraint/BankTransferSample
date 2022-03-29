package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.ErrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jpaErrCodeRepository")
@Transactional(readOnly = true)
public interface ErrCodeRepository extends JpaRepository<ErrCode, String> {
}
