package com.kakaobank.KakaoFriendTransfer.service;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalExceptionFactory;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.mapper.TransferMapper;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.CustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.TransferRepository;
import com.kakaobank.KakaoFriendTransfer.repository.redis.RedisTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService{
    private final TransferRepository transferRepository;
    private final CustomerAccountRepository customerAccountRepository;
    private final GlobalExceptionFactory globalExceptionFactory;
    private final TransferMapper transferMapper;
    private final CacheManager cacheManager;

    private final static String HashKey = "transferFind";
    private final static String ExpireHashKey = "transferExpire";


    /*
    * RedisCache 관련
    * https://daddyprogrammer.org/post/3217/redis-springboot2-spring-data-redis-cacheable-cacheput-cacheevict/
    * https://sundries-in-myidea.tistory.com/110
    * https://www.baeldung.com/spring-boot-redis-cache
    * */
    @Override
    @Cacheable(key = "#id", value=HashKey, unless = "#result == null")
    public Transfer findTransfer(Long id) {
        // 1. Find by Redis
        /*
        Transfer redisTransfer = redisTransferRepository.findById(id);

        if(redisTransfer != null) {
            log.info("(Transfer)Search for Redis : " + redisTransfer.toString());
            return redisTransfer;
        }
         */

        // 2. Find by Database
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        // 2-1. Save(Update) Redis

        if(!optionalTransfer.isEmpty()) {
            Transfer transfer = optionalTransfer.get();
//            redisTransferRepository.save(transfer);
            return transfer;
        }
        return null;
    }

    // not used
    @Override
    @Cacheable(key = "#id", value="transferDto", unless = "#result == null")
    public TransferDto findTransferDto(Long id) {
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        // 2-1. Save(Update) Redis

        if(!optionalTransfer.isEmpty()) {
            Transfer transfer = optionalTransfer.get();
//            redisTransferRepository.save(transfer);
            return transferMapper.entityToDto(transfer);
        }
        return null;
    }

    @Override
    public List<Transfer> findTransferListBySender(String sendKakaoUserId) {
        return transferRepository.findBySendCustomerAccountKakaoFriendUserId(sendKakaoUserId);
    }

    @Override
    public List<Transfer> findTransferListByReceiver(String receiveKakaoUserId) {
        return transferRepository.findByReceiveCustomerAccountKakaoFriendUserId(receiveKakaoUserId);
    }

    @Override
    public boolean transferAccountValidation(Transfer transfer) {
        // 인증되지 않은 문제 있는 계좌인 경우
        if(!transfer.getSendCustomerAccount().isCertified() || !transfer.getReceiveCustomerAccount().isCertified())
            return false;
        else return true;
    }

//    @CachePut(key = "#id", value = ExpireHashKey)
    public Transfer saveNew(Transfer transfer) {
        //  Save Database
        transferRepository.save(transfer);

        log.info("### saveNew Entity : {}",transfer.toString());
        log.info("### saveNew Redis Serializer : {}",new JdkSerializationRedisSerializer().serialize(transfer));
        log.info("### saveNew Redis Serializer to Deserializer : {}",new JdkSerializationRedisSerializer().deserialize(new JdkSerializationRedisSerializer().serialize(transfer)));

        // 저장 후 반드시 캐시에 넣는다
        cacheManager.getCache(ExpireHashKey).put(transfer.getId(), transfer);
        cacheManager.getCache(HashKey).put(transfer.getId(), transfer);
        return transfer;
    }

    @Override
    @Transactional
//    @Caching(evict = {@CacheEvict(key = "#id", value = "transfer")}, put = {@CachePut(key = "#id", value = "transfer")})
    @CachePut(key = "#transferId", value = HashKey)
    public Transfer save(Long transferId, Transfer transfer) {
        if(transferId != null) {
            log.info("### CacheManager Data : {}", cacheManager.getCache("transfer").get(transferId).get().toString());
            cacheManager.getCache(HashKey).evict(transferId);
        }
        //  Save Database
        transferRepository.save(transfer);

        log.info("### Entity : {}",transfer.toString());
        log.info("### Redis Serializer : {}",new JdkSerializationRedisSerializer().serialize(transfer));
        log.info("### Redis Serializer to Deserializer : {}",new JdkSerializationRedisSerializer().deserialize(new JdkSerializationRedisSerializer().serialize(transfer)));

        // 저장 후 반드시 캐시에 넣는다
        cacheManager.getCache(HashKey).put(transfer.getId(), transfer);
        return transfer;
    }

    /*
     * 동시성 제어를 위한 Pessimistic Lock(Database 수준에서 Account Lock을 건다!)
     * Test 를 위해서 Thread Sleep 주입 후 동시성 제어 수행 -> DeadLock 방지
     * 그러나 Locking 수준에 대해서 좀 더 생각해야 함!!!
     * 참고 : https://isntyet.github.io/jpa/JPA-%EB%B9%84%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88(Pessimistic-Lock)/ , https://velog.io/@lsb156/JPA-Optimistic-Lock-Pessimistic-Lock
     */
    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void saveTransfer(Transfer transfer) throws GlobalException {
        if(!transferAccountValidation(transfer))
//            throw new IllegalStateException("Invalid Sender/Receiver Account.");
            throw globalExceptionFactory.globalExceptionBuilder("100");

        // 계좌 잔액이 전송금액보다 작은 경우
        if(transfer.getSendCustomerAccount().getBalance() < transfer.getTransferAmt())
//            throw new IllegalArgumentException("TransferAmt must lower than balance in sender's account.");
            throw globalExceptionFactory.globalExceptionBuilder("200");

        // 송신자 계좌 잔액 감액
        transfer.decreaseSenderBalance();

        // 수신자 계좌 잔액 증액은 이체를 승인한 시점에 수행

        log.debug("Create New Transfer : {}", transfer.toString());
        // 이체내역 저장
//        transferRepository.save(transfer);

        // 신규저장은 Expire를 위해 따로 탄다
        saveNew(transfer);
//        save(transfer.getId(),transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getSendCustomerAccount());
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Transfer cancelTransfer(Long id) {
        /* 존재하는 거래내역 찾는 경우 Redis를 통해서 대기중인 이체내역을 Cache에서 찾아온다 */
        Transfer transfer = findTransfer(id);
        if(transfer == null)
            throw globalExceptionFactory.globalExceptionBuilder("400");

        // transfer search
        /*
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);

        if(optionalTransfer.isEmpty())
            throw globalExceptionFactory.globalExceptionBuilder("400");

        Transfer transfer = optionalTransfer.get();
        */

        // 상태가 이미 변경된 이체내역인 경우 에러 발생
        if(!transfer.getTransferStatus().equals(TransferStatus.WAITING))
            throw globalExceptionFactory.globalExceptionBuilder("500");

        // 감액되었던 송신자 계좌 잔액 복구
        transfer.increaseSenderBalance();

        // 이체내역 상태값 변경
        transfer.setTransferStatus(TransferStatus.CANCEL);
        transfer.setModifyDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        log.debug("Cancel Current Transfer : {}", transfer.toString());
        // 이체내역 update
//        transferRepository.save(transfer);
        save(transfer.getId(), transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getSendCustomerAccount());

        return transfer;
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Transfer confirmTransfer(Long id) {
        /* 존재하는 거래내역 찾는 경우 Redis를 통해서 대기중인 이체내역을 Cache에서 찾아온다 */
        Transfer transfer = findTransfer(id);
        if(transfer == null)
            throw globalExceptionFactory.globalExceptionBuilder("400");

        // transfer search
        /*
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);

        if(optionalTransfer.isEmpty())
            throw globalExceptionFactory.globalExceptionBuilder("400");

        Transfer transfer = optionalTransfer.get();
         */

        // 상태가 이미 변경된 이체내역인 경우 에러 발생
        if(!transfer.getTransferStatus().equals(TransferStatus.WAITING))
            throw globalExceptionFactory.globalExceptionBuilder("500");

        // 승인에 따른 수신자 계좌 잔액 증액
        transfer.increaseReceiverBalance();

        // 이체내역 상태값 변경
        transfer.setTransferStatus(TransferStatus.SUCCESS);
        transfer.setModifyDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        log.debug("Confirm Transfer : {}", transfer.toString());
        // 이체내역 update
//        transferRepository.save(transfer);
        save(transfer.getId(), transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getReceiveCustomerAccount());

        return transfer;
    }
}
