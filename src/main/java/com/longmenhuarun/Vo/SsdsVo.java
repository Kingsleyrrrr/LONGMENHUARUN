package com.longmenhuarun.Vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.longmenhuarun.enums.DZStatusEnum;
import com.longmenhuarun.enums.JYStatusEnum;
import lombok.Data;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/11 11:28
 * 4
 */
@Data
public class SsdsVo {
    private String reqMsgNo;
    private String entrustDate;
    private String txnDate;
    private String tranType;
    private String userNo;
    private String outBank;
    private String payerAcc;
    private String payerName;
    private String currency;
    private String amount;
    private String remark;
    private String workReturnNo;
    private String retCd;
    private String retCdRemark;
    private String status;
    private String dzFlag;
    private String createTime;

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
    public String getDZStatus() {
        for (DZStatusEnum statusEnum : DZStatusEnum.values()) {
            if (statusEnum.getCode().equals(this.dzFlag)) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }
}