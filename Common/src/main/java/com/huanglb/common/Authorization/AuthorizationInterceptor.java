package com.huanglb.common.Authorization;


import com.huanglb.common.AuthTokenPool;
import com.huanglb.common.JsonUtil;
import com.huanglb.common.Msg.UnauthorizedMsg;
import com.huanglb.common.ResponseData;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationInterceptor implements HandlerInterceptor {
    public static final String AUTHORIZED_USER_ID = "userId";
    public static final String COOKIE_NAME = "Authorization";

    private final AuthTokenPool authTokenPool;
    private final JsonUtil jsonUtil;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // 获取方法上的注解
        if (handlerMethod.getMethod().isAnnotationPresent(AuthorizationAuthenticator.class)) {
            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                log.info("no cookies found in URL {}", request.getRequestURL());
                try {
                    response.getWriter().write(jsonUtil.object2Json(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "没有权限,请检查是否已登录")));
                    //response.getWriter().write(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "没有权限,请检查是否已登录").toString());
                } catch (IOException e) {
                    log.info(e.getMessage(), e);
                }
                return false;
            }
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (token == null) {
                log.info("no token found in URL {}", request.getRequestURL());
                try {
                    response.getWriter().write(jsonUtil.object2Json(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "没有权限,请检查是否已登录")));
                    //response.getWriter().write(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "没有权限,请检查是否已登录").toString());
                } catch (IOException e) {
                    log.info(e.getMessage(), e);
                }
                return false;
            }

            Long userId = authTokenPool.validateToken(token);
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                try {
                    log.info("Invalid :{}", token);
                    response.getWriter().write(jsonUtil.object2Json(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "Token不正确")));
                    response.getWriter().write(new ResponseData<>(UnauthorizedMsg.UNAUTHORIZED, "Token不正确").toString());
                } catch (IOException e) {
                    log.info(e.getMessage(), e);
                }
                return false;
            }
            log.info("get token: {} from URL {}: User:{}", token, request.getRequestURL(), userId);
            request.setAttribute(AUTHORIZED_USER_ID, userId);
            return true;
        }
        return true;
    }
}
