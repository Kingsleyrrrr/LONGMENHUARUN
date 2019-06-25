package com.longmenhuarun.Controller;

import com.longmenhuarun.Service.SsdsService;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.model.SsdsMsg;
import com.sun.jmx.snmp.tasks.TaskServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/lmhr/ssds")
public class SsdsController  {
    @Autowired
    SsdsService ssdsService;

    @GetMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        Sort sort =  new Sort(Sort.Direction.ASC, "createDate").and(new Sort(Sort.Direction.ASC, "createTime"));
        PageRequest request = PageRequest.of(page - 1, size,sort);
        Page<SsdsVo> ssdsVoPage = ssdsService.findSsdsList(request);
        map.put("ssdsVoPage", ssdsVoPage);
        map.put("currentpage", page);
        map.put("size", size);
        return new ModelAndView("/ssds/list",map);
    }

    @GetMapping(value = "/invoke")
    public ModelAndView invoke(){
        return new ModelAndView("/ssds/invoke");
    }

    @PostMapping(value = "/sendSsdsMsg")
    public ModelAndView sendssdsMsg(@Valid SsdsMsg ssdsMsg,
                                   BindingResult bindingResult ,
                                   Map<String,Object> map) {
        map.put("ssdsMsg", ssdsMsg);
        //格式检查
        if (bindingResult.hasErrors()) {
            map.put("Msg", bindingResult.getFieldError().getDefaultMessage());
            return new ModelAndView("ssds/invoke", map);
        }
        //分配一个id
        String ReqMsgNo=MsgUtil.getReqMsgNo();
        ssdsMsg.setReqMsgNo(ReqMsgNo);
        log.info("收到机构实时代收请求[" + ssdsMsg + "]");
        //创建报文
        String ssReqMsg = ssdsService.createSsdsMsg(ssdsMsg);
        //验证协议
        if(!ssdsService.checkProtocol(ssReqMsg)){
            log.error("请求[" + ReqMsgNo + "]协议验证失败");
            map.put("Msg", "协议验证失败");
            return new ModelAndView("ssds/invoke", map);
        }
        //加密并发送报文
        boolean flag = ssdsService.sendSsdsMsg(ssReqMsg);
        if (!flag) {
            map.put("Msg", "发送失败！！");
        } else {
            map.put("Msg", "发送成功！！");
            log.info("发送实时代收报文[" + ssReqMsg + "]");
            //入库
            ssdsService.insertDB(ssReqMsg);
            }
        return new ModelAndView("ssds/invoke", map);
    }
}
