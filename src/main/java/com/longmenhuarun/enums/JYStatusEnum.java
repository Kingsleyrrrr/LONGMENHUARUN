package com.longmenhuarun.enums;

import lombok.Getter;

@Getter
public enum JYStatusEnum {
    ANSWER("A","待回应"),
    SUCCESS("S","成功"),
    FAIL("F","失败"),
    ERROR("E","错误"),
    RESET("R","已冲正");


    private String  Code;
    private String Msg;

    JYStatusEnum(String code, String msg) {
        Code = code;
        Msg = msg;
    }

}
