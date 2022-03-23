package com.kakaobank.KakaoFriendTransfer.config.exception;

import lombok.RequiredArgsConstructor;

import java.util.Map;

public class GlobalException extends Exception {
    private final String errCode;

    public GlobalException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }
}
