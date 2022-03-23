package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "KAKAO_FRIEND")
public class KakaoFriend extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "KAKAO_FRIEND_SEQ")
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @OneToOne(mappedBy = "kakaoFriend")
    private CustomerAccount customerAccount;

    public KakaoFriend(String userId, CustomerAccount customerAccount) {
        this.userId = userId;
        this.customerAccount = customerAccount;
    }

    public boolean hasAccount() {
        return (customerAccount != null) ? true : false;
    }
}
