package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
/*
@SequenceGenerator(
        name = "BANK_ID_GENERATOR",
        sequenceName = "BANK_ID_SEQ",
        initialValue = 1,
        allocationSize = 100
)
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "BANK")
public class Bank extends DateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "BANK_ID")
    private Long id;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    public Bank(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.modifyDate = regDate;
    }

    public Bank(String bankCode) {
        this.bankCode = bankCode;
    }
}
