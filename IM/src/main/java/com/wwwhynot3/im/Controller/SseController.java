package com.wwwhynot3.im.Controller;

import com.huanglb.common.Authorization.AuthorizationAuthenticator;
import com.huanglb.common.JsonUtil;
import com.huanglb.common.Msg.SseMsg;
import com.huanglb.common.ResponseData;
import com.wwwhynot3.im.SseEmitter.ImMessageEmitter;
import com.wwwhynot3.im.SseEmitter.SseEmitterUTF8;
import com.wwwhynot3.im.SseEmitter.SseResponse.SseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SseController {
    private final ImMessageEmitter imMessageEmitter;
    private final JsonUtil jsonUtil;
    @Value("${im.sse-timeout}")
    private Long SSE_TIMEOUT;

    @GetMapping("/test")
    public SseEmitter test() {
        SseEmitter sseEmitter = new SseEmitter();
        Thread.startVirtualThread(() -> {
            try {
                while (true) {
                    sseEmitter.send("fuck you");
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                sseEmitter.completeWithError(e);
                log.error("SseEmitter error: {}", e.getMessage());
            }
        });
        return sseEmitter;
    }

    /**
     * sse 不能被包装在其他类中，SpringMVC会知道如何处理
     *
     * @param userId
     * @return 返回值不能为SseEmitter
     * @throws IOException
     */
    @GetMapping
    @AuthorizationAuthenticator
    public SseEmitter createEmitter(@RequestAttribute("userId") Long userId) throws IOException {
        SseEmitter sseEmitter = new SseEmitterUTF8(SSE_TIMEOUT);

        SseEmitter previousEmitter = imMessageEmitter.addOnlineUser(userId, sseEmitter);
        if (previousEmitter != null) {
            previousEmitter.send(jsonUtil.object2Json(new ResponseData<>(SseMsg.BLOCKED_ERROR, "被顶号啦")));
            previousEmitter.complete();
            return sseEmitter;
        }
        sseEmitter.onCompletion(() -> imMessageEmitter.removeOnlineUser(userId));
        sseEmitter.send(jsonUtil.object2Json(SseResponse.connectionSync()));
        sseEmitter.send("连接成功");
        return sseEmitter;
    }

    @DeleteMapping
    @AuthorizationAuthenticator
    public ResponseData<String> closeEmitter(@RequestAttribute("userId") Long userId) {
        SseEmitter sseEmitter = imMessageEmitter.removeOnlineUser(userId);
        if (sseEmitter == null) {
            return new ResponseData<>(SseMsg.NOT_ONLINE_ERROR, "用户不在线，下线失败");
        }
        sseEmitter.complete();
        return new ResponseData<>(SseMsg.CLOSE_SUCCESS, "关闭成功");
    }
}
