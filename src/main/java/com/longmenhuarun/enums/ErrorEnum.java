package com.longmenhuarun.enums;

import lombok.Getter;

/**
 * @Author: Kingsley
 * @Date: 2019/2/24 22:21
 * @Version 1.0
 */
@Getter
public enum ErrorEnum {
    PROTNOERROR(06,"协议号长度错误"),
    ONCEAMOUNTERROR(021,"单笔金额错误"),
    ACCERROR(05,"银行账号长度错误"),
    USERNOERROR(04,"用户编号长度错误"),
    SEQERROR(03,"流水号长度错误"),
    AMOUNTERROR(02,"文件总金额错误"),
    COUNTERROR(01,"文件总笔数错误");
    private Integer code;
    private String msg;

    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
