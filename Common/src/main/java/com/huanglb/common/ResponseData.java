package com.huanglb.common;


import com.huanglb.common.Msg.Msg;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ResponseData<T> {
    public Msg msg;
    public T data;

    public static <T> ResponseData<T> build(Msg msg, T data) {
        return new ResponseData<>(msg, data);
    }

    public static <T> ResponseData<T> Ok(T data) {
        return new ResponseData<>(Msg.OK, data);
    }

    public static <T> ResponseData<T> Error(T data) {
        return new ResponseData<>(Msg.ERROR, data);
    }
}
