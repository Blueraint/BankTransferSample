package com.kakaobank.KakaoFriendTransfer.domain.dto;

import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferSearchParam {
    private String sendAccountNumber;

    private String receiveAccountNumber;

    private TransferStatus transferStatus;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
