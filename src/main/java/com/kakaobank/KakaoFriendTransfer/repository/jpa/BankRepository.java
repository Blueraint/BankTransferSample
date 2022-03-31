package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaBankRepository")
public interface BankRepository extends JpaRepository<Bank, Long> {
    // 20220331 Optional을 왜 썻는가? 옵셔널을 쓰면 안되는 상황에 대해서
    Optional<Bank> findByBankCode(String bankCode);
}
