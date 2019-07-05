package com.longmenhuarun.common;

import cfbs.api.CFBSMsgUtil;

import java.util.UUID;

public class MsgUtil {
    public static String getReqMsgNo() {

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        String random = CFBSMsgUtil.rightFill("0", 8,Integer.toString(hashCodeV));
        return TimeUtil.getCurDateStr()+random.substring(0,8);
    }
    public static String getPlpackNo() {

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        return Integer.toString(hashCodeV).substring(0,8);
    }
}
