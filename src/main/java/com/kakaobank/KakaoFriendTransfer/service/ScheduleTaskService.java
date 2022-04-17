package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.repository.jpa.CustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTaskService {
    @Value("${schedule.timeout.day}")
    private long timeoutDay;

    private final TransferRepository transferRepository;
    private final CustomerAccountRepository customerAccountRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cancelTransferByTimeout() {
        log.info("SCHEDULING EXECUTE START");

        String timeoutCriteriaDate = LocalDateTime.now().minusDays(timeoutDay).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        /*
         * 추가 구현사항
         * Cancel 이전 대상되는 계좌에 대해서 이체내역을 원복해야 함
         * In절로 서브쿼리가 들어가서 성능을 해치는 경우, Slice로 조회해서 Chunk(Partitioning)방식의 업데이트(transferService.cancelTransfer)를 돌리는 것도 생각해봐야 함
         * */

        // 하루가 지난 대상에 대해서 Account 원상복구 수행
//        int countAccount = customerAccountRepository.bulkUpdateAccountForTransferTimeoutCancel(timeoutCriteriaDate);
        int countAccount = 0;
        // 하루가 지난 대상에 대해서 벌크 업데이트(CANCEL)수행
        int countTransfer = transferRepository.bulkTransferCancel(timeoutCriteriaDate);

        log.info("SCHEDULING EXECUTE [TIMEOUT DATE] : {}, [UPDATE ACCOUNT COUNT] : {}, [UPDATE TRANSFER COUNT] : {}",timeoutCriteriaDate, countAccount, countTransfer);
    }
}
