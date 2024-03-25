package com.wwwhynot3.manager.Config;


import com.huanglb.common.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Unknown RuntimeException
     *
     * @param e RuntimeException
     * @return ResponseData
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseData<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error(String.format("统一异常处理 RuntimeException:\n\t请求地址:%s\n\t Error:%s\n\t", request.getRequestURI(), e.getMessage()), e);
        return ResponseData.Error(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseData<String> handleException(Exception e) {
        log.error(String.format("统一异常处理 RuntimeException:\n\tError:%s\n", e.getMessage()), e);
        return ResponseData.Error(e.getMessage());
    }
}
