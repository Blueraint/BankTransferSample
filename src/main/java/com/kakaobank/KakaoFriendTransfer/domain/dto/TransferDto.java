package com.kakaobank.KakaoFriendTransfer.domain.dto;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferDto {
    // Api find Object
//    private CustomerAccount sendCustomerAccount;
    private String sendBankCode;
    private String sendAccountNumber;

    // Api find Object
//    private CustomerAccount receiveCustomerAccount;
    private String receiveBankCode;
    private String receiveAccountNumber;

    private Long transferAmt;

    private TransferStatus transferStatus;

    private LocalDateTime regDate;
}
