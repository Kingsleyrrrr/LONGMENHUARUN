package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TxnSsds {
    @Id
    private String msgId;
    private String reqMsgId;
    private String entrustDate;
    private String txnDate;
    private String txnType;
    private String orgId;
    private String userNo;
    private String origBank;
    private String origAcc;
    private String outBank;
    private String outAccBank;
    private String payerAcc;
    private String payerName;
    private String tranType;
    private String privateFlag="S";
    private String currency;
    private String amount;
    private String charge;
    private String remark;
    private String status;
    private String retCd;
    private String retCdRemark;
    private String workReturnNo;
    private String dzFlag;
    private String dzDate;
    private String createDate;
    private String createTime;
    private String rspDate;
    private String rspTime;
    private String protNo;

}
