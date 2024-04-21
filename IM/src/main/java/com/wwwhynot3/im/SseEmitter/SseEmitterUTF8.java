package com.wwwhynot3.im.SseEmitter;

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;

public class SseEmitterUTF8 extends SseEmitter {
    public SseEmitterUTF8() {
        super();
    }

    public SseEmitterUTF8(Long timeout) {
        super(timeout);
    }

    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);
        outputMessage.getHeaders().setContentType(new MediaType("text", "event-stream", StandardCharsets.UTF_8));
    }
}
