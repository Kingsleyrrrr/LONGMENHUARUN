package com.longmenhuarun.Vo;


import com.longmenhuarun.enums.XYStatusEnum;
import lombok.Data;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/11 11:28
 * 4
 */
@Data
public class SsxyVo {
    private String msgId;
    private String protNo;
    private String tranType;
    private String userNo;
    private String userName;
    private String payerBank;
    private String payerAcc;
    private String payerName;
    private String remark;
    private String workReturnNo;
    private String retCd;
    private String retCdRemark;
    private String status;
    private String protActType;
    private String createTime;
    private String activeDate;
   public String getTypeMsg(){
       return "燃气费";
   }

   public String getXYStatus() {
        for (XYStatusEnum statusEnum : XYStatusEnum.values()) {
            if (statusEnum.getCode().equals(this.status)) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }
}