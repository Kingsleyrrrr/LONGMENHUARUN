package com.longmenhuarun.Exception;

import com.longmenhuarun.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Kingsley
 * @Date: 2019/2/24 22:20
 * @Version 1.0
 */
@Slf4j
public class PldsFileException extends RuntimeException{
    private Integer code;

    public PldsFileException(String fileName,ErrorEnum errorEnum) {
       super(errorEnum.getMsg());
        this.code=errorEnum.getCode();
        log.error(fileName+errorEnum.getMsg());
    }

}
