package com.xp.medshare.model.vomodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response <T> {
    int code;
    String msg;
    T data;

    public final static Response FAIL = Response.of(ResponseCode.UNKNOWN);
    public final static Response SUCCESS = Response.of(ResponseCode.SUCCESS);

    public static Response of(ResponseCode code) {
        return new Response(code.code, code.msg, null);
    }

    public static <T> Response of(ResponseCode code, T data) {
        return new Response<T>(code.code, code.msg, data);
    }
}
