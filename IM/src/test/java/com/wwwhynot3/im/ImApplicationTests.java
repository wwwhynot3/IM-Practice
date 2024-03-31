package com.wwwhynot3.im;

import com.huanglb.common.Mongodb.Entity.GroupMessage;
import com.huanglb.common.Mongodb.Entity.UserMessage;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ImApplicationTests {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ImMessageService imMessageService;
    @Value("${rocketmq.im-message-topic}")
    private String IM_MESSAGE_TOPIC;
    @Value("${rocketmq.user-message-topic}")
    private String USER_MESSAGE_TOPIC;
    @Value("${rocketmq.group-message-topic}")
    private String GROUP_MESSAGE_TOPIC;
    @Value("${rocketmq.producer.group}")
    private String IM_MESSAGE_PRODUCER_GROUP;

    @Test
    void contextLoadsReceive() {
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageId(null);
        userMessage.setSrcUserId(0L);
        userMessage.setDestUserId(0L);
        userMessage.setQuoteMessageId(null);
        userMessage.setTimestamp(System.currentTimeMillis());
        userMessage.setMessageContent("嘻嘻");
        GroupMessage groupMessage = new GroupMessage();
        userMessage.setMessageId(null);
        userMessage.setSrcUserId(0L);
        userMessage.setDestUserId(0L);
        userMessage.setQuoteMessageId(null);
        userMessage.setTimestamp(System.currentTimeMillis());
        userMessage.setMessageContent("哈哈");
        log.info(rocketMQTemplate.sendAndReceive(this.IM_MESSAGE_TOPIC, userMessage, UserMessage.class).toString());
        log.info(rocketMQTemplate.sendAndReceive(this.IM_MESSAGE_TOPIC, groupMessage, GroupMessage.class).toString());


    }
}
