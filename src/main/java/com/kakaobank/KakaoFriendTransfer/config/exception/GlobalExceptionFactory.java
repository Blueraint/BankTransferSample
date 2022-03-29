package com.kakaobank.KakaoFriendTransfer.config.exception;

import com.kakaobank.KakaoFriendTransfer.domain.ErrCode;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.ErrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GlobalExceptionFactory {
    private final ErrCodeRepository errCodeRepository;

//    @DependsOn(value = "errCodeRepository")
    public Map<String, String> errCodeMap() {
        List<ErrCode> errCodeList =  errCodeRepository.findAll();
        Map<String, String> errCodeMap = new HashMap<>();

        errCodeList.forEach(errCode -> {
            errCodeMap.put(errCode.getCodeId(), errCode.getMessage());
        });

        return errCodeMap;
    }

    public GlobalException globalExceptionBuilder(String errCode) {
        return new GlobalException(errCode, errCodeMap().get(errCode));
    }
}
