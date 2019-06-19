package com.longmenhuarun.Controller;

import cfbs.model.CfbsSsdfMsg;
import com.longmenhuarun.Service.PldsService;
import com.longmenhuarun.common.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/lmhr/plds")
public class PldsController {

    @Value("${LOCAL_FILE_PATH}")
    private String localpath;
    @Autowired
    PldsService pldsService;
    @GetMapping(value = "/list")
    public ModelAndView ssds(){
        return new ModelAndView("/plds/list");
    }

    @GetMapping(value = "/invoke")
    public ModelAndView invoke(){
        return new ModelAndView("/plds/invoke");
    }


    @PostMapping(value = "/sendFile")
    public ModelAndView sendFile( MultipartFile file, Map<String,Object> map)throws Exception{
        if(file.isEmpty()) {
            map.put("Msg", "文件为空！！");
            return new ModelAndView("/plds/invoke", map);
        }
            //转存文件
            String filename=file.getOriginalFilename();
            String filepath = localpath;
            file.transferTo(new File(filepath+filename));

        //解析
        pldsService.anaCustomFile(filepath,filename);
        //生成明文文件 密文文件

        //发送
        boolean flag =true;
                //ssdsService.sendSsdsMsg(ssReqMsg);
        if (!flag) {
            map.put("Msg", "发送失败！！");
        } else {
            map.put("Msg", "发送成功！！");
            //入库
        }
        return new ModelAndView("/plds/invoke", map);
    }

}
