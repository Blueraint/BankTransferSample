package com.kakaobank.KakaoFriendTransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoMessage {
    private String userId;

    private String message;
}
