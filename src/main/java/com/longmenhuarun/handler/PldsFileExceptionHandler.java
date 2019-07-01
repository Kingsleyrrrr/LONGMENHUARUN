package com.longmenhuarun.handler;

import com.longmenhuarun.Exception.PldsFileException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Kingsley
 * @Date: 2019/3/2 13:47
 * @Version 1.0
 */
@ControllerAdvice
public class PldsFileExceptionHandler {

    @ExceptionHandler(value = PldsFileException.class)
    public ModelAndView handlerPldsFileException(PldsFileException ex){
        Map<String, Object> map=new HashMap<>();
        map.put("Msg",ex.getMessage());
        return new ModelAndView("plds/invoke", map);

    }
}
