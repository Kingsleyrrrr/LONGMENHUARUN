package com.longmenhuarun.Vo;

import com.longmenhuarun.enums.DZStatusEnum;
import com.longmenhuarun.enums.JYStatusEnum;
import lombok.Data;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/11 11:28
 * 4
 */
@Data
public class PldsVo {
    private String msgId;
    private String batchId;
    private String rspfileName;
    private String sendfileName;
    private String totCnt;
    private String totAmt;
    private String sucCnt;
    private String sucAmt;
    private String failCnt;
    private String failAmt;
    private String status;
    private String createTime;
    private String rspTime;

   public String getTypeMsg(){
       return "燃气费";
   }

   public String getJYStatus() {
        for (JYStatusEnum statusEnum : JYStatusEnum.values()) {
            if (statusEnum.getCode().equals(this.status)) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }
}