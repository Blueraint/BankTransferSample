package com.kakaobank.KakaoFriendTransfer.listener;

import com.kakaobank.KakaoFriendTransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    private TransferService transferService;

    private final static String HashKey = "transferFind";
    private final static String ExpireHashKey = "transferExpire";

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param redisMessageListenerContainer must not be {@literal null}.
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }
    /*
    * redis.conf -> notify-keyspace-events Ex
    * or redis-cli> config set notify-keyspace-events Ex
    */

    /*
    * Redis Expire Listener에 의해 event subscribe 한 후 Auto Cancel 수행
    * 수억건의 Redis Data가 Expire되는데 이걸 모두 listen하여 처리하긴 어렵다 -> 실무에서 쓰기는 쉽지 않을듯
    */
    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        String expirePrefixKey = CacheKeyPrefix.simple().compute(ExpireHashKey);
        String keyBody = new String(message.getBody());

        if(!keyBody.startsWith(expirePrefixKey)) {
            log.info("### This is not expire target key");
            return;
        }

        log.info("### Redis Key Expire Listener ### Channel : {}. {}", message.getChannel(), new String(message.getChannel()));
        log.info("### Redis Key Expire Listener ### Body : {}, {}", message.getBody(), keyBody);
        log.info("### Redis Key Expire Listener ### String : {}", message.toString());
        log.info("### Cachekey Prefix : {}", CacheKeyPrefix.simple().compute(ExpireHashKey));

        String keyStr = StringUtils.substringAfterLast(keyBody, expirePrefixKey);

        log.info("### Expired Key Value : {}", keyStr);
        transferService.cancelTransfer(Long.valueOf(keyStr));

        super.onMessage(message, pattern);
    }
}
