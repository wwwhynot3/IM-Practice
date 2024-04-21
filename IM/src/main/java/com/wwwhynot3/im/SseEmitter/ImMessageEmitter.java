package com.wwwhynot3.im.SseEmitter;

import com.huanglb.common.JsonUtil;
import com.huanglb.common.Mongodb.Entity.ImMessage;
import com.wwwhynot3.im.SseEmitter.SseResponse.SseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ImMessageEmitter {
    private final Map<Long, SseEmitter> pool;
    private final JsonUtil jsonUtil;

    @Autowired

    public ImMessageEmitter(JsonUtil jsonUtil) {
        this.pool = new HashMap<>();
        this.jsonUtil = jsonUtil;
    }

    public boolean isOnline(Long userId) {
        return pool.containsKey(userId);
    }

    /**
     * @param userId
     * @param emitter
     * @return return the user's previous emitter if the user is already online otherwise null
     */
    public SseEmitter addOnlineUser(Long userId, SseEmitter emitter) {
        return pool.put(userId, emitter);
    }

    /**
     * @param userId
     * @return return the user's emitter if the user is online otherwise null
     */
    public SseEmitter removeOnlineUser(Long userId) {
        return pool.remove(userId);
    }

    /**
     * @param destUserId
     * @param message
     * @return false if the user is offline
     * @throws IOException
     */

    public boolean pushMessage(Long destUserId, ImMessage message) throws IOException {
        SseEmitter emitter = pool.get(destUserId);
        if (emitter == null) {
            return false;
        }
        log.info("push message to user {} : {}", destUserId, message);
        emitter.send(jsonUtil.object2Json(SseResponse.message(message)));
        return true;
    }

    public boolean keepAlive(Long userId) throws IOException {
        SseEmitter emitter = pool.get(userId);
        if (emitter == null) {
            return false;
        }
        emitter.send(jsonUtil.object2Json(SseResponse.keepAlive()));
        return true;
    }
}
