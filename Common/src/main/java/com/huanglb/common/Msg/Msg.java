package com.huanglb.common.Msg;

import lombok.ToString;

@ToString
public class Msg {
    public static Msg OK = new Msg(200, "OK");
    public static Msg ERROR = new Msg(400, "ERROR");

    public int code;
    public String msg;

    protected Msg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
