package com.longmenhuarun.enums;

import lombok.Getter;

@Getter
public enum DZStatusEnum {
    RECONCILED("R","已对账"),
    CHECK("C","待核查");


    private String  Code;
    private String Msg;

    DZStatusEnum(String code, String msg) {
        Code = code;
        Msg = msg;
    }

}
