package com.longmenhuarun.model;

import cfbs.api.CFBSMsgUtil;
import com.longmenhuarun.common.MsgUtil;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/24 10:51
 * 4
 */
@Data
public class DzMsg {
    public String msgId;
    public String amount;
    public static List<DzMsg> procDzFile(String filePath, String fileName) throws Exception {

        BufferedReader breader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName), "GBK"));

        List<DzMsg> dzList = new ArrayList<>();
        /* 1、解析文件头，获取请求时文件名*/
        String temp = breader.readLine();
        while ((temp = breader.readLine()) != null) {
            String msgId = CFBSMsgUtil.getGBKStrCut(temp, 0, 16);// 交易序号
            String amount = CFBSMsgUtil.getGBKStrCut(temp, 258, 15);// 交易序号
            DzMsg dzMsg=new DzMsg();
            dzMsg.setMsgId(msgId);
            dzMsg.setAmount(CFBSMsgUtil.removeLeftZero(amount));
            dzList.add(dzMsg);
        }
        return dzList;
    }
}
