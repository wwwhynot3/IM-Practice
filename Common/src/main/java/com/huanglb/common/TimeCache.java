package com.huanglb.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeCache {
    @Getter
    private static long currentTimeStamp = System.currentTimeMillis();

    static {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1);
                currentTimeStamp = System.currentTimeMillis();
            } catch (InterruptedException e) {
                log.info(e.toString());
            }
        });
        thread.start();
    }

}
