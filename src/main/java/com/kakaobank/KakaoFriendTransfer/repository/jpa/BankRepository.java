package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaBankRepository")
public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByBankCode(String bankCode);
}
