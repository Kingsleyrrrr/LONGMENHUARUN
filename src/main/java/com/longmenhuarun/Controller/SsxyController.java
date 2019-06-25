package com.longmenhuarun.Controller;

import com.longmenhuarun.Service.SsxyService;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.Vo.SsxyVo;
import com.longmenhuarun.common.MsgUtil;
import com.longmenhuarun.model.SsxyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/lmhr/ssxy")
public class SsxyController {
    @Autowired
    SsxyService ssxyService;
    @GetMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                              Map<String, Object> map,
                             @ModelAttribute("Msg") String Msg ){
        Sort sort =  new Sort(Sort.Direction.ASC, "createDate").and(new Sort(Sort.Direction.ASC, "createTime"));
        PageRequest request = PageRequest.of(page - 1, size,sort);
        Page<SsxyVo> ssxyVoPage = ssxyService.findSsxyList(request);
        map.put("ssxyVoPage", ssxyVoPage);
        map.put("currentpage", page);
        map.put("size", size);
        if("".equals(Msg) ) map.remove("Msg");
        return new ModelAndView("/ssxy/list",map);
    }

    @GetMapping(value = "/create")
    public ModelAndView create(){
        return new ModelAndView("/ssxy/create");
    }


    @PostMapping(value = "/sendSsxy")
    public ModelAndView sendSsxy(@Valid SsxyMsg ssxyMsg,
                                   BindingResult bindingResult ,
                                  Map<String,Object> map) {
        map.put("ssxyMsg", ssxyMsg);
        if (bindingResult.hasErrors()) {
            map.put("Msg", bindingResult.getFieldError().getDefaultMessage());
            return new ModelAndView("/ssxy/create", map);
        }
        //分配一个id
        String ReqMsgNo= MsgUtil.getReqMsgNo();
        ssxyMsg.setReqMsgNo(ReqMsgNo);
        log.info("收到机构实时协议请求[" + ssxyMsg + "]");
        //创建报文
        String ssReqMsg = ssxyService.createSsxyMsg(ssxyMsg);
        //加密并发送报文
        boolean flag = ssxyService.sendSsxyMsg(ssReqMsg);
        if (!flag) {
            map.put("Msg", "发送失败！！");
        } else {
            map.put("Msg", "发送成功！！");
            log.info("发送实时协议报文[" + ssReqMsg + "]");
            //入库
            ssxyService.insertDB(ssReqMsg);
        }
        return new ModelAndView("/ssxy/create", map);
    }
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam String msgId,
                              RedirectAttributes attributes) {
        log.info("收到机构撤销协议请求[" + msgId + "]");
        //创建报文
        String ssReqMsg = ssxyService.cancelSsxyMsg(msgId);
        log.info("发送撤销协议报文[" + ssReqMsg + "]");
        boolean flag = ssxyService.sendSsxyMsg(ssReqMsg);
        if (!flag) {
            attributes.addFlashAttribute("Msg", "发送失败！！");
        } else {
            attributes.addFlashAttribute("Msg", "发送成功！！");
            //入库
            ssxyService.insertDB(ssReqMsg);
        }
        return new ModelAndView("redirect:list");
    }
}
