package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaCustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCi(String ci);
}
