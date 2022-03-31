package com.kakaobank.KakaoFriendTransfer.listener;

import com.kakaobank.KakaoFriendTransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    */
    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        log.info("### Redis Key Expire Listener ### Channel : {}. {}", message.getChannel(), new String(message.getChannel()));
        log.info("### Redis Key Expire Listener ### Body : {}, {}", message.getBody(), new String(message.getBody()));
        log.info("### Redis Key Expire Listener ### String : {}", message.toString());
        log.info("### Cachekey Prefix : {}", CacheKeyPrefix.simple().compute(ExpireHashKey));

        message.toString().

        super.onMessage(message, pattern);
    }
}
