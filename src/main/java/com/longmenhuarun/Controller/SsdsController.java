package com.longmenhuarun.Controller;

import com.longmenhuarun.Service.SsdsService;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.model.SsdsMsg;
import com.sun.jmx.snmp.tasks.TaskServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
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
    //过滤空字符串
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    @GetMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "15") Integer size,
                              SsdsMsg ssdsMsg,
                             Map<String, Object> map){
        Sort sort =  new Sort(Sort.Direction.ASC, "createDate").and(new Sort(Sort.Direction.ASC, "createTime"));
        PageRequest request = PageRequest.of(page - 1, size,sort);
        Page<SsdsVo> ssdsVoPage = ssdsService.findSsdsList(ssdsMsg,request);
        map.put("ssdsVoPage", ssdsVoPage);
        map.put("currentpage", page);
        map.put("size", size);
        return new ModelAndView("/ssds/list",map);
    }
    @PostMapping(value = "/list")
    public ModelAndView slist(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "15") Integer size,
                             SsdsMsg ssdsMsg,
                             Map<String, Object> map){
        Sort sort =  new Sort(Sort.Direction.ASC, "createDate").and(new Sort(Sort.Direction.ASC, "createTime"));
        PageRequest request = PageRequest.of(page - 1, size,sort);
        Page<SsdsVo> ssdsVoPage = ssdsService.findSsdsList(ssdsMsg,request);
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
        log.info("交易序号:"+ReqMsgNo+"收到机构实时代收请求[" + ssdsMsg + "]");
        //验证行号
        String bankId=ssdsMsg.getOutBank();
        if(!ssdsService.checkBankId(bankId)){
            log.error("交易序号:"+ReqMsgNo+"行号不存在");
            map.put("Msg", "行号不存在");
            return new ModelAndView("ssds/invoke", map);
        }
        //查找协议(机构号+业务种类+用户编号)
        String protNo=ssdsService.checkProtocol(ssdsMsg);
        if(protNo==null){
            log.error("交易序号:"+ReqMsgNo+"协议不存在");
            map.put("Msg", "协议不存在");
            return new ModelAndView("ssds/invoke", map);
        }
        ssdsMsg.setProtocolNo(protNo);
        //创建报文
        String ssReqMsg = ssdsService.createSsdsMsg(ssdsMsg);
        //加密并发送报文
        boolean flag = ssdsService.sendSsdsMsg(ssReqMsg);
        if (!flag) {
            map.put("Msg", "发送失败！！");
        } else {
            map.put("Msg", "发送成功！！");
            log.info("交易序号:"+ReqMsgNo+"发送实时代收报文[" + ssReqMsg + "]");
            //入库
            ssdsService.insertDB(ssReqMsg);
            }
        return new ModelAndView("ssds/invoke", map);
    }
}
