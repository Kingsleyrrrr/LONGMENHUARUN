package com.longmenhuarun.Service;

import cfbs.api.CFBSMsgUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/19 16:12
 * 4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PldsServiceImplTest {
    @Autowired
    PldsService pldsService;
    @Test
    public void anaRespPldsFile() {
       // pldsService.anaRespPldsFile("TXNTYP121210530100726501402201906190465474200000450");
        String decFileName = CFBSMsgUtil.fileDec("C:/Users/Administrator/Desktop/",
                "TXNTYP120210530100726501402201906191538875200001844");
    }

}