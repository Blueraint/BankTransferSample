package com.kakaobank.KakaoFriendTransfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
/*
@SequenceGenerator(
        name = "KAKAO_FRIEND_SEQ_GENERATOR",
        sequenceName = "KAKAO_FRIEND_SEQ",
        initialValue = 1,
        allocationSize = 100
)
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "KAKAO_FRIEND")
public class KakaoFriend extends DateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "KAKAO_FRIEND_SEQ")
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @OneToOne(mappedBy = "kakaoFriend", fetch = FetchType.LAZY)
    @JsonIgnore
    private CustomerAccount customerAccount;

    public KakaoFriend(String userId, CustomerAccount customerAccount) {
        this.userId = userId;
        this.customerAccount = customerAccount;
    }

    public boolean hasAccount() {
        return (customerAccount != null) ? true : false;
    }
}
