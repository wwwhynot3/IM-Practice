package com.huanglb.datatransfer.Service;

import com.huanglb.common.Mongodb.Entity.ImMessage;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 不能反序列化为接口
 */
@Slf4j
@Deprecated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(topic = "${rocketmq.im-message-topic}", consumerGroup = "${rocketmq.consumer.im-message-group}")
public class ImMessageConsumer implements RocketMQReplyListener<ImMessage, ImMessage> {
    private final ImMessageService imMessageService;

    @Override
    public ImMessage onMessage(ImMessage imMessage) {
        imMessageService.saveMessage(imMessage);
        log.info("receive im-message:{}", imMessage);
        return imMessage;
    }
}