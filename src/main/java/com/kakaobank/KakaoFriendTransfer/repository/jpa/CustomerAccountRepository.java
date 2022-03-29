package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.Customer;
import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository("jpaCustomerAccountRepository")
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    @EntityGraph(attributePaths = {"customer"})
    List<CustomerAccount> findByCustomer(Customer customer);

    /* 계좌 내역 찾는 Service 를 Redis 통해서 Caching */
    Optional<CustomerAccount> findByBankBankCodeAndAccountNumber(String bankCode, String accountNumber);

    Optional<CustomerAccount> findByKakaoFriendUserId(String userId);

    /*
     * 동시성 제어를 위한 고려 필요 (계좌 관련 Service 에서 격리 수준 고려)
     * - 특히 계좌는 다른 Service 에서 동시 접근이 가능하다!!!! -> Pessimistic Write 적용
     */
}
