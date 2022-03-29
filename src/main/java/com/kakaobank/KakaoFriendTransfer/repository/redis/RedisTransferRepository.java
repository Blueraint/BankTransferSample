package com.kakaobank.KakaoFriendTransfer.repository.redis;

import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("redisTransferRepository")
@RequiredArgsConstructor
public class RedisTransferRepository {
    private static final String HASH_KEY = "Transfer";
    private final RedisTemplate redisTemplate;

    public void save(Transfer transfer) {
        // Hashkey must equals
        redisTemplate.opsForHash().put(HASH_KEY, transfer.getId(), transfer);

        return;
    }

    public List<Transfer> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Transfer findById(Long id) {
        return (Transfer) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public Transfer delete(Long id) {
        Transfer deleteTransfer = findById(id);
        redisTemplate.opsForHash().delete(HASH_KEY, id);


        return deleteTransfer;
    }
}