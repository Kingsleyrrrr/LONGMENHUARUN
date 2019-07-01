package com.longmenhuarun.Controller;

import cfbs.model.CfbsSsdfMsg;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import com.longmenhuarun.Exception.PldsFileException;
import com.longmenhuarun.Service.PldsService;
import com.longmenhuarun.Vo.PldsVo;
import com.longmenhuarun.common.EncodeUtil;
import com.longmenhuarun.common.TimeUtil;
import com.longmenhuarun.enums.ErrorEnum;
import com.longmenhuarun.model.PldsMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/lmhr/plds")
public class PldsController {

    @Value("${LOCAL_FILE_PATH}")
    private String LOCAL_FILE_PATH;
    @Autowired
    PldsService pldsService;
    @GetMapping(value = "/list")
    public ModelAndView ssds(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        Sort sort =  new Sort(Sort.Direction.ASC, "createDate").and(new Sort(Sort.Direction.ASC, "createTime"));
        PageRequest request = PageRequest.of(page - 1, size,sort);
        Page<PldsVo> pldsVoPage = pldsService.findPldsList(request);
        map.put("pldsVoPage", pldsVoPage);
        map.put("currentpage", page);
        map.put("size", size);
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
        String filename=file.getOriginalFilename();
        String filepath = LOCAL_FILE_PATH;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filepath + filename));
            String code = EncodeUtil.getEncode(bis, false);
            if(!"UTF-8".equals(code)) {
                map.put("Msg", "文件编码格式错误！！");
                return new ModelAndView("/plds/invoke", map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("收到机构发起批量文件"+filename);
            //转存文件
            file.transferTo(new File(filepath + filename));
        //解析
         PldsMsg pldsMsg=pldsService.anaCustomFile(filepath,filename);
        //生成明文文件 密文文件
        Map<String,String> filemap=pldsService.genReqPldsFile(pldsMsg);
        //发送
        boolean flag =pldsService.sendFile(filemap.get("encFileName"));
        if (!flag) {
            map.put("Msg", "发送失败！！");
        } else {
            map.put("Msg", "发送成功！！");
            log.info("发送批量加密文件"+filemap.get("encFileName"));
            //入库
            pldsMsg.setFileName(filemap.get("fileName"));
           pldsService.insertDB(pldsMsg,filename);
        }
        return new ModelAndView("/plds/invoke", map);
    }


}
