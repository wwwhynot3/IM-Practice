package com.wwwhynot3.im.Controller;

import com.huanglb.common.Authorization.AuthorizationAuthenticator;
import com.huanglb.common.JsonUtil;
import com.huanglb.common.Mongodb.Entity.GroupMessage;
import com.huanglb.common.Mongodb.Entity.UserMessage;
import com.huanglb.common.Mongodb.Service.ImMessageService;
import com.huanglb.common.Msg.MapperMsg;
import com.huanglb.common.Msg.MqMsg;
import com.huanglb.common.ResponseData;
import com.huanglb.common.SnowFlakeIdGenerator;
import com.huanglb.common.TimeCache;
import com.wwwhynot3.im.SseEmitter.ImMessageEmitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/im")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImController {

    /**
     * 两分钟内可以撤回
     */

    private final ImMessageService imMessageService;
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final RocketMQTemplate rocketMQTemplate;
    private final ImMessageEmitter imMessageEmitter;
    private final JsonUtil jsonUtil;
    @Value("${im.withdraw-timeout}")
    private long WITHDRAW_TIME_LIMIT;
    @Value("${rocketmq.user-message-topic}")
    private String USER_MESSAGE_TOPIC;
    @Value("${rocketmq.group-message-topic}")
    private String GROUP_MESSAGE_TOPIC;
    @Value("${rocketmq.producer.group}")
    private String IM_MESSAGE_PRODUCER_GROUP;
    @Value("${im.message-pull-limit}")
    private Integer IM_LIMIT;

    @GetMapping("/user")
    @AuthorizationAuthenticator
    public ResponseData<List<UserMessage>> getUserMessage(@RequestAttribute("userId") Long userId,
                                                          @RequestParam("destUserId") Long destUserId,
                                                          @RequestParam("committedMessageId") ObjectId committedMessageId) {
        log.info("userId:{}, destUserId:{}, committedMessageId: {}", userId, destUserId, committedMessageId);
        List<UserMessage> userMessageSince = imMessageService.getUserMessageSince(userId, destUserId, committedMessageId, IM_LIMIT);
        return userMessageSince == null
                ? ResponseData.build(MapperMsg.QUERY_FAILURE, null)
                : ResponseData.build(MapperMsg.QUERY_SUCCESS, userMessageSince);
    }

    @GetMapping("/group")
    @AuthorizationAuthenticator
    public ResponseData<List<GroupMessage>> getGroupMessage(@RequestAttribute("userId") Long userId,
                                                            @RequestParam("destGroupId") Long destGroupId,
                                                            @RequestParam("committedMessageId") ObjectId committedMessageId) {
        List<GroupMessage> groupMessageSince = imMessageService.getGroupMessageSince(destGroupId, committedMessageId, IM_LIMIT);
        return groupMessageSince == null
                ? ResponseData.build(MapperMsg.QUERY_FAILURE, null)
                : ResponseData.build(MapperMsg.QUERY_SUCCESS, groupMessageSince);
    }

    @PostMapping("/user")
    @AuthorizationAuthenticator
    public ResponseData<UserMessage> sendMessageReceive(@RequestAttribute("userId") Long userId,
                                                        @RequestBody UserMessage userMessage) throws IOException {
        userMessage.setMessageId(null);
        userMessage.setTimestamp(TimeCache.getCurrentTimeStamp());
        log.info("userMessage: {}", userMessage);
        userMessage = rocketMQTemplate.sendAndReceive(this.USER_MESSAGE_TOPIC, userMessage, UserMessage.class);
        imMessageEmitter.pushMessage(userMessage.getDestUserId(), userMessage);
        return ResponseData.build(MqMsg.SEND_SUCCESS, userMessage);
    }

    @PostMapping("/group")
    @AuthorizationAuthenticator
    public ResponseData<GroupMessage> sendGroupMessageReceive(@RequestAttribute("userId") Long userId,
                                                              @RequestBody GroupMessage groupMessage) {
        groupMessage.setMessageId(null);
        groupMessage.setTimestamp(TimeCache.getCurrentTimeStamp());
        return ResponseData.build(MqMsg.SEND_SUCCESS,
                rocketMQTemplate.sendAndReceive(this.GROUP_MESSAGE_TOPIC, groupMessage, GroupMessage.class));
    }

    /**
     * 撤回用户消息，在客户端和服务端进行双重检查
     *
     * @param userId
     * @param destUserId
     * @param messageId
     * @return
     */
    @DeleteMapping("/user")
    @AuthorizationAuthenticator
    public ResponseData<String> withdrawUserMessage(@RequestAttribute("userId") Long userId,
                                                    @RequestParam("destUserId") Long destUserId,
                                                    @RequestParam("messageId") ObjectId messageId) {
        return imMessageService.withdrawUserMessageTimed(userId, destUserId, messageId, TimeCache.getCurrentTimeStamp(), WITHDRAW_TIME_LIMIT)
                ? ResponseData.build(MapperMsg.DELETE_SUCCESS, "撤回成功")
                : ResponseData.build(MapperMsg.DELETE_FAILURE, "撤回失败");
    }

    @DeleteMapping("/group")
    @AuthorizationAuthenticator
    public ResponseData<String> withdrawGroupMessage(@RequestAttribute("userId") Long userId,
                                                     @RequestParam("destGroupId") Long destGroupId,
                                                     @RequestParam("messageId") ObjectId messageId) {
        return imMessageService.withdrawGroupMessageTimed(destGroupId, messageId, TimeCache.getCurrentTimeStamp(), WITHDRAW_TIME_LIMIT)
                ? ResponseData.build(MapperMsg.DELETE_SUCCESS, "撤回成功")
                : ResponseData.build(MapperMsg.DELETE_FAILURE, "撤回失败");
    }
}
