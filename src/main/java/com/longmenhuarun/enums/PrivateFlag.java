package com.longmenhuarun.enums;

import lombok.Getter;
@Getter
public enum PrivateFlag {
     G("G","对公"),
     S("S","对私");
        private String  Code;
        private String Msg;

    PrivateFlag(String code, String msg) {
            Code = code;
            Msg = msg;
        }

}
