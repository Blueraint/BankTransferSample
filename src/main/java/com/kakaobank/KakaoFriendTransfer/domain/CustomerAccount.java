package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="ACCOUNT")
public class CustomerAccount extends DateEntity {
    @Id
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @OneToOne
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
        this.regDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }

    public CustomerAccount(Bank bank, String accountNumber) {
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "id=" + id +
                ", bankCode=" + bank.getBankCode() +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerCi=" + customer.getCi() +
                ", kakaoFriendUserId=" + ((kakaoFriend!=null)?kakaoFriend.getUserId():null) +
                ", balance=" + balance +
                ", isCertified=" + isCertified +
                '}';
    }
}
