package com.wwwhynot3.im.SseEmitter.SseResponse;


import com.huanglb.common.Mongodb.Entity.ImMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseResponse {
    public static SseResponse KEEP_ALIVE = new SseResponse(SseResponseType.KEEP_ALIVE, null);
    public static SseResponse CONNECTION_SYNC = new SseResponse(SseResponseType.CONNECTION_SYNC, null);
    private SseResponseType type;
    private ImMessage message;

    public static SseResponse message(ImMessage message) {
        return new SseResponse(SseResponseType.STRING_MESSAGE, message);
    }

    public static SseResponse keepAlive() {
        return KEEP_ALIVE;
    }

    public static SseResponse connectionSync() {
        return CONNECTION_SYNC;
    }
}
