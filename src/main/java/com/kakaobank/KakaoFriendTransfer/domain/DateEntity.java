package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class DateEntity {
    @CreatedDate
    @Column(
            name = "REG_DATE",
            updatable = false
    )
    LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "MODIFY_DATE")
    LocalDateTime modifyDate;
}