package com.cont.spring.utils;

import lombok.Data;

@Data
public class ResponseUtils {
    private int code = 200;
    private String msg = "成功";
    private Object data;

    public static ResponseUtils success() {
        return new ResponseUtils();
    }

    public static ResponseUtils success(Object data) {
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.data = data;
        return responseUtils;
    }
}
