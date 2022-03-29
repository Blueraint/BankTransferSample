package com.kakaobank.KakaoFriendTransfer.domain.dto;

import lombok.Data;

@Data
public class CustomerAccountDto {
    private String bankCode;

    private String bankName;

    private String accountNumber;

    private String customerName;

    private Long balance;

    private boolean isCertified;
}
