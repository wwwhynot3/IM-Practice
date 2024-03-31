package com.huanglb.common.Mongodb.Service;

import com.huanglb.common.Mongodb.Entity.GroupMessage;
import com.huanglb.common.Mongodb.Entity.ImMessage;
import com.huanglb.common.Mongodb.Entity.UserMessage;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImMessageService {
    public static final String IM_MESSAGE_TOPIC = "im-message-topic";
    public static final String USER_MESSAGE_TAG = "user-message";
    public static final String GROUP_MESSAGE_TAG = "group-message";
    private static final Criteria messageId = Criteria.where("messageId");
    private final MongoTemplate mongoTemplate;

    public static String userMessageTopicTag() {
        return IM_MESSAGE_TOPIC + ":" + USER_MESSAGE_TAG;
    }

    public static String groupMessageTopicTag() {
        return IM_MESSAGE_TOPIC + ":" + GROUP_MESSAGE_TAG;
    }

    private static String userCollectionName(Long srcUserId, Long destUserId) {
        return "user_message_" + Math.max(srcUserId, destUserId) + Math.min(srcUserId, destUserId);
    }

    private static String groupCollectionName(Long destGroupId) {
        return "group_message_" + destGroupId;
    }

    public String getIM_MESSAGE_TOPIC() {
        return IM_MESSAGE_TOPIC;
    }

    public ImMessage saveMessage(ImMessage imMessage) {
        //模式变量 ————> jdk17 and later
        if (imMessage instanceof UserMessage userMessage) {
            return mongoTemplate.insert(userMessage,
                    userCollectionName(userMessage.getSrcUserId(), userMessage.getDestUserId())
            );
        } else if (imMessage instanceof GroupMessage groupMessage) {
            return mongoTemplate.insert(groupMessage,
                    groupCollectionName(groupMessage.getDestGroupId())
            );
        } else {
            return null;
        }
    }

    public UserMessage saveUserMessage(UserMessage userMessage) {
        return mongoTemplate.insert(userMessage,
                userCollectionName(userMessage.getSrcUserId(), userMessage.getDestUserId())
        );
    }

    public Collection<UserMessage> saveUserMessages(List<UserMessage> userMessages) {
        UserMessage userMessage = userMessages.getFirst();
        return mongoTemplate.insert(userMessages,
                userCollectionName(userMessage.getSrcUserId(), userMessage.getDestUserId())
        );
    }

    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        return mongoTemplate.insert(groupMessage,
                groupCollectionName(groupMessage.getDestGroupId())
        );
    }

    public Collection<GroupMessage> saveGroupMessages(List<GroupMessage> groupMessages) {
        GroupMessage groupMessage = groupMessages.getFirst();
        return mongoTemplate.insert(groupMessages,
                groupCollectionName(groupMessage.getDestGroupId())
        );
    }

    public List<UserMessage> getUserMessagesAll(Long srcUserId, Long destUserId) {
        return mongoTemplate.findAll(UserMessage.class, userCollectionName(srcUserId, destUserId));
    }

    public List<UserMessage> getUserMessageSince(Long srcUserId, Long destUserId, ObjectId since, int limit) {
        return mongoTemplate.find(
                query(messageId.gt(since)).limit(limit),
                UserMessage.class,
                userCollectionName(srcUserId, destUserId)
        );
    }

    public List<GroupMessage> getGroupMessagesAll(Long destGroupId) {
        return mongoTemplate.findAll(GroupMessage.class, groupCollectionName(destGroupId));
    }

    public List<GroupMessage> getGroupMessageSince(Long destGroupId, ObjectId since, int limit) {
        return mongoTemplate.find(
                Query.query(messageId.gt(since)).limit(limit),
                GroupMessage.class,
                groupCollectionName(destGroupId)
        );
    }

    public void dropGroupMessages(Long destGroupId) {
        mongoTemplate.dropCollection(groupCollectionName(destGroupId));

    }

    public void dropUserMessages(Long srcUserId, Long destUserId) {
        mongoTemplate.dropCollection(userCollectionName(srcUserId, destUserId));
    }

    public boolean withdrawUserMessage(Long srcUserId, Long destUserId, ObjectId messageId) {
        return mongoTemplate.remove(query(ImMessageService.messageId.is(messageId)), UserMessage.class, userCollectionName(srcUserId, destUserId)).getDeletedCount() > 0;
    }

    public boolean withdrawGroupMessage(Long destGroupId, ObjectId messageId) {
        return mongoTemplate.remove(query(ImMessageService.messageId.is(messageId)), GroupMessage.class, groupCollectionName(destGroupId)).getDeletedCount() > 0;
    }

    /**
     * 在消息发送后一段时间内可以撤回消息，可以在客户端选择是否删除或者在服务器双重验证
     *
     * @param srcUserId
     * @param destUserId
     * @param messageId
     * @param currentTime
     * @param timeLimit
     * @return
     */
    public boolean withdrawUserMessageTimed(Long srcUserId, Long destUserId, ObjectId messageId, long currentTime, long timeLimit) {
        UserMessage message = mongoTemplate.findById(messageId, UserMessage.class, userCollectionName(srcUserId, destUserId));
        return message != null
                && currentTime - message.getTimestamp() >= timeLimit
                && mongoTemplate.remove(query(ImMessageService.messageId.is(messageId)), GroupMessage.class, groupCollectionName(destUserId)).getDeletedCount() > 0;

    }

    /**
     * 在消息发送后一段时间内可以撤回消息，可以在客户端选择是否删除或者在服务器双重验证
     *
     * @param destGroupId
     * @param messageId
     * @param currentTime
     * @param timeLimit
     * @return
     */
    public boolean withdrawGroupMessageTimed(Long destGroupId, ObjectId messageId, long currentTime, long timeLimit) {
        GroupMessage message = mongoTemplate.findById(messageId, GroupMessage.class, groupCollectionName(destGroupId));
        return message != null
                && currentTime - message.getTimestamp() >= timeLimit
                && mongoTemplate.remove(query(ImMessageService.messageId.is(messageId)), GroupMessage.class, groupCollectionName(destGroupId)).getDeletedCount() > 0;
    }
}
