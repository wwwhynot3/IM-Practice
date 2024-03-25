package com.huanglb.common.Msg;

import jakarta.servlet.http.HttpServletResponse;

public class UnauthorizedMsg extends Msg {
    public static final String UNAUTHORIZED_MSG = String.format("%d Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
    public static final int UNAUTHORIZED_CODE = HttpServletResponse.SC_UNAUTHORIZED;
    public static UnauthorizedMsg UNAUTHORIZED =
            new UnauthorizedMsg(UNAUTHORIZED_CODE, UNAUTHORIZED_MSG);

    public UnauthorizedMsg(int code, String msg) {
        super(code, msg);
    }
}
