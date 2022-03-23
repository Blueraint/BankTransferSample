package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCi(String ci);
}
