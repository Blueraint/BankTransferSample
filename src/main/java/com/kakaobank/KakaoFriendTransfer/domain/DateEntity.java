package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class DateEntity {
//    @CreatedDate
    @Column(
            name = "REG_DATE",
            updatable = false
    )
    String regDate;

//    @LastModifiedDate
    @Column(name = "MODIFY_DATE")
    String modifyDate;

    @PrePersist
    public void onPrePersist(){
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.modifyDate = this.regDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.modifyDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
