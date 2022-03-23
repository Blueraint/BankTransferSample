package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ERR_CODE_LIST")
public class ErrCode {
    @Id
    @Column(name = "CODE_ID")
    private String codeId;

    @Column(name = "MESSAGE")
    private String message;
}
