package com.longmenhuarun.enums;

import lombok.Getter;

@Getter
public enum XYStatusEnum {
    EFFECTIVE("E","已生效"),
    CANCEL("C","已撤销"),
    ERROR("F","失败");

    private String  Code;
    private String Msg;

    XYStatusEnum(String code, String msg) {
        Code = code;
        Msg = msg;
    }

}
