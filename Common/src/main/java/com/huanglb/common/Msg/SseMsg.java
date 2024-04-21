package com.huanglb.common.Msg;

public class SseMsg extends Msg {
    public static final SseMsg BUILD_SUCCESS = new SseMsg(200, "建立SSE连接成功");
    public static final SseMsg BUILD_FAILED = new SseMsg(400, "建立SSE连接失败");
    public static final SseMsg CLOSE_SUCCESS = new SseMsg(200, "关闭SSE连接成功");
    public static final SseMsg CLOSE_FAILED = new SseMsg(400, "关闭SSE连接失败");
    public static final SseMsg FINE = new SseMsg(200, "fine");
    public static final SseMsg ALREADY_ONLINE_EXCEPTION = new SseMsg(200, "user is already online, trying to kick off previous one");
    public static final SseMsg BLOCKED_ERROR = new SseMsg(400, "user is blocked(你被顶号啦)");
    public static final SseMsg NOT_ONLINE_ERROR = new SseMsg(400, "user is not online or already offline");
    public static final SseMsg UNKNOWN_ERROR = new SseMsg(400, "unknown error");

    protected SseMsg(int code, String msg) {
        super(code, msg);
    }
}
