package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.dto.CustomerAccountDto;

public interface CustomerAccountService {
    CustomerAccount findByBankCodeAndAccountNumber(String bankCode, String accountNumber);
    CustomerAccountDto findByKakaoUserId(String kakaoUserId);
}
