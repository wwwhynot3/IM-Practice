package com.huanglb.common.Msg;

public class MqMsg extends Msg {
    public static final MqMsg SEND_SUCCESS = new MqMsg(200, "发送至消费队列成功");
    public static final MqMsg SEND_FAILURE = new MqMsg(400, "发送至消费队列失败");

    protected MqMsg(int code, String msg) {
        super(code, msg);
    }
}
