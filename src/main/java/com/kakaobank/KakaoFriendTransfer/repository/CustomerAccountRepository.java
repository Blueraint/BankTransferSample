package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.Customer;
import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    @EntityGraph(attributePaths = {"customer"})
    List<CustomerAccount> findByCustomer(Customer customer);
    Optional<CustomerAccount> findByAccountNumber(String accountNumber);
}
