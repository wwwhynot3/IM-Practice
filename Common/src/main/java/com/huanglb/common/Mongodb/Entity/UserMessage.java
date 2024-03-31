package com.huanglb.common.Mongodb.Entity;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class UserMessage implements ImMessage {
    /**
     * 据网上描述，MongoDB在主键为null时会自动生成一个ObjectId作为主键，据描述不指定ObjectId插入的速率较高
     * todo 验证不同Long(SnowFlakeId)和ObjectId的插入速率和查询速率
     * 可以考虑换成Long类型的主键
     */
    @Id
    private ObjectId messageId;
    //    private ObjectId messageId;
    private ObjectId quoteMessageId;
    private Long srcUserId;
    private Long destUserId;
    private String messageContent;
    private Long timestamp;
}
