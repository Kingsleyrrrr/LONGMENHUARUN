package com.longmenhuarun.common;

import java.util.UUID;

public class MsgUtil {
    public static String getReqMsgNo() {

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        return TimeUtil.getCurDateStr()+Integer.toString(hashCodeV).substring(0,8);
    }
}
