package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("jpaTransferRepository")
public interface TransferRepository extends JpaRepository<Transfer, Long>  {
    @Lock(LockModeType.OPTIMISTIC)
    List<Transfer> findBySendCustomerAccount(CustomerAccount sendCustomerAccount);
    @Lock(LockModeType.OPTIMISTIC)
    List<Transfer> findByReceiveCustomerAccount(CustomerAccount receiveCustomerAccount);
    List<Transfer> findByTransferStatus(TransferStatus transferStatus);
    @Lock(LockModeType.OPTIMISTIC)
    List<Transfer> findBySendCustomerAccountKakaoFriendUserId(String userId);
    @Lock(LockModeType.OPTIMISTIC)
    List<Transfer> findByReceiveCustomerAccountKakaoFriendUserId(String userId);

    /**
     * 연관관계(Entity : CustomerAccount) 에 명확하게 Lock이 걸리지 않는다. -> Repository내 명확하게 Cancel Update, Confirm Update 를 위한 Method 를 별도로 만들어야 함
     * findByIdForCancelUpdate(@Query : transfer inner join sendCustomerAccount) : @Pessimistic Lock(scope.EXTENDED)
     * findByIdForConfirmUpdate(@Query : transfer inner join receiverCustomerAccount) : @Pessimistic Lock(scope.EXTENDED)
     *
     * scope 설정이 잘 안먹는거 같다. @JPQL 로 직접 작성 후 Lock 을 거는 것이 현재로써는 최선이다
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(
            value = {
                    @QueryHint(name = "javax.persistence.lock.timeout", value = "1000"),
                    @QueryHint(name = "javax.persistence.lock.scope", value = "EXTENDED")
            })
    Optional<Transfer> findById(Long id);

    /*
    * 동시성 제어를 위한 고려 필요 (이체내역 관련 Service 에서 격리 수준 고려)
    */
//  void transferRepository.save(Transfer transfer);

    /* 동시성 제어를 위한 고려 필요 (Database 수준 격리 실행할 것인지?) */
    @Modifying(clearAutomatically = true)
    @Query("update Transfer t set t.transferStatus = 'CANCEL' where t.transferStatus = 'WAITING' and t.regDate < :regDate")
    int bulkTransferCancel(@Param("regDate") String regDate);
}
