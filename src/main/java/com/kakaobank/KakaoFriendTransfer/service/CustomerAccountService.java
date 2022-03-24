package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.KakaoFriend;

public interface CustomerAccountService {
    CustomerAccount findByKakaoFriend(KakaoFriend kakaoFriend);
}
