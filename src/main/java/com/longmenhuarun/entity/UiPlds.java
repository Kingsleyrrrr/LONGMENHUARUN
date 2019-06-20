package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/20 16:29
 * 4
 */
@Entity
@Data
public class UiPlds {
    @Id
    private String msgId;
    private String batchMsgId;
    private String txnDate;
    private String userNo;
    private String orgId;
    private String txnType;
    private String origBank;
    private String origAcc;
    private String outBank;
    private String payerAcc;
    private String payerName;
    private String tranType;
    private String protNo;
    private String privateFlag="S";
    private String currency;
    private String amount;
    private String charge;
    private String remark;
    private String status;
    private String retCd;
    private String retCdRemark;
    private String workReturnNo;
    private String createDate;
    private String createTime;
    private String rspDate;
    private String rspTime;
    private String source="ORG";
}
