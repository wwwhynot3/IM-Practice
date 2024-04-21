package com.huanglb.datatransfer.Service;

import com.huanglb.common.Mongodb.Entity.GroupMessage;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(topic = "${rocketmq.group-message-topic}", consumerGroup = "${rocketmq.consumer.group-message-group}")
public class GroupMessageConsumer implements RocketMQReplyListener<GroupMessage, GroupMessage> {
    private final ImMessageService imMessageService;

    @Override
    public GroupMessage onMessage(GroupMessage groupMessage) {
        imMessageService.saveGroupMessage(groupMessage);
        log.info("receive group-message:{}", groupMessage);
        return groupMessage;
    }
}
