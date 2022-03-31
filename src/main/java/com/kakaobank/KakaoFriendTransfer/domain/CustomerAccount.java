package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  Account Entity
 *  동시성 제어를 위한 OptimisticLocking(낙관적 잠금) 적용 - 외부 API 가 없이 DB Transaction 내에서 Service 진행 시 사용
 *  - 현재의 Application 에서는 외부 API 연동은 KakaoMessage, Friend목록 조회기 때문에 경합 발생하는 Dataset 은 내부 API로만 동작됨
 *  - 또한 계좌의 발급은 외부 서비스에서 이루어지고, 계좌 발급 이후에 조회에 대한 서비스만 이루어짐  -> Optimistic ?
 */

@Entity
/*
@SequenceGenerator(
        name = "ACCOUNT_ID_GENERATOR",
        sequenceName = "ACCOUNT_ID_SEQ",
        initialValue = 1,
        allocationSize = 100
)
 */
@Getter
@Setter
@NoArgsConstructor
@OptimisticLocking(type = OptimisticLockType.ALL)
@DynamicUpdate
@Table(name="ACCOUNT")
public class CustomerAccount extends DateEntity implements Serializable {
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "BANK_ID",
            nullable = false
    )
    private Bank bank;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KAKAO_FRIEND_SEQ")
    private KakaoFriend kakaoFriend;

    @Column(name = "BALANCE")
    private Long balance;

    @Column(name = "IS_CERTIFIED")
    private boolean isCertified;

    public CustomerAccount(Bank bank, String accountNumber, Customer customer, Long balance, boolean isCertified) {
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
        this.isCertified = isCertified;
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.modifyDate = regDate;
    }

    public CustomerAccount(Bank bank, String accountNumber) {
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

//    @Override
//    public String toString() {
//        return "CustomerAccount{" +
//                "id=" + id +
//                ", bankCode=" + bank.getBankCode() +
//                ", accountNumber='" + accountNumber + '\'' +
////                ", customerCi=" + customer.getCi() +
////                ", kakaoFriendUserId=" + ((kakaoFriend!=null)?kakaoFriend.getUserId():null) +
//                ", balance=" + balance +
//                ", isCertified=" + isCertified +
//                '}';
//    }
}
