package com.kakaobank.KakaoFriendTransfer.repository.redis;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("redisCustomerAccountRepository")
@RequiredArgsConstructor
public class RedisCustomerAccountRepository {
    private static final String HASH_KEY = "CustomerAccount";
    private final RedisTemplate redisTemplate;

    /*
     * CustomerAccount Real Key : (BankCode, AccountNumber)
     * Will use HashValue(BankCode_AccountNumber)
     */

    public void save(CustomerAccount customerAccount) {
        redisTemplate.opsForHash().put(HASH_KEY, getHashKey(customerAccount.getBank().getBankCode(), customerAccount.getAccountNumber()), customerAccount);

        return;
    }

    public List<CustomerAccount> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public CustomerAccount findByHashKey(String bankCode, String accountNumber) {
        return (CustomerAccount) redisTemplate.opsForHash().get(HASH_KEY, getHashKey(bankCode, accountNumber));
    }

    public CustomerAccount delete(String bankCode, String accountNumber) {
        CustomerAccount deleteAccount = findByHashKey(bankCode, accountNumber);
        redisTemplate.opsForHash().delete(HASH_KEY, deleteAccount);

        return deleteAccount;
    }

    public String getHashKey(String bankCode, String accountNumber) {
        return bankCode + "_" + accountNumber;
    }
}
