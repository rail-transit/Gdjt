package com.example.passenger.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    public static final int SYS_ERROR = 10000;
    public static final int KEY_ERROR = 10140;
    public static final int PASSWORD_NOTNULL = 10143;
    public static final int CERTIFICATION_SUCCESS = 10142;
    public static final int CERTIFICATIONCODE_NOTNULL = 10141;

    private static final Map<Integer, String> errorMap = new HashMap<>();

    static {
        errorMap.put(SYS_ERROR, "系统繁忙");
        errorMap.put(KEY_ERROR, "密钥错误");
        errorMap.put(PASSWORD_NOTNULL, "密码不能为空");
        errorMap.put(CERTIFICATION_SUCCESS, "认证成功");
        errorMap.put(CERTIFICATIONCODE_NOTNULL, "认证码不能为空");
    }
    /**
     *
     * @param errorcode
     * @return
     */
    public static String getMsg(int errorcode) {
        if (errorMap.containsKey(errorcode)) {
            return errorMap.get(errorcode);
        }
        return errorMap.get(SYS_ERROR);
    }
}
