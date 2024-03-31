package com.huanglb.datatransfer.Service;

import com.huanglb.common.Mongodb.Entity.UserMessage;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 考虑使用事务消息，但是经过考虑未发现需要使用事务消息
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(topic = "${rocketmq.im-message-topic}", consumerGroup = "${rocketmq.consumer.user-message-group}")
public class UserMessageConsumer implements RocketMQReplyListener<UserMessage, UserMessage> {
    private final ImMessageService imMessageService;

    @Override
    public UserMessage onMessage(UserMessage userMessage) {
        imMessageService.saveUserMessage(userMessage);
        log.info("receive user-message:{}", userMessage);
        return userMessage;
    }
}
