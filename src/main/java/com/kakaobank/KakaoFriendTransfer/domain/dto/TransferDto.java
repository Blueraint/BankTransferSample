package com.kakaobank.KakaoFriendTransfer.domain.dto;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferDto {
    // Api find Object
    private CustomerAccount sendCustomerAccount;

    // Api find Object
    private CustomerAccount receiveCustomerAccount;

    private Long transferAmt;

    private TransferStatus transferStatus;

    private LocalDateTime regDate;
}
