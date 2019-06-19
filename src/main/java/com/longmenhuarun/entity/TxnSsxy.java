package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/14 16:21
 * 4
 */
@Entity
@Data
public class TxnSsxy {
    @Id
    private String msgId;
    private String reqMsgId;
    private String entrustDate;
    private String protActType;
    private String txnType;
    private String orgId;
    private String protNo;
    private String userNo;
    private String userName;
    private String contactName;
    private String contactAddr;
    private String contactAddrPost;
    private String contactNo;
    private String tranType;
    private String payerBank;
    private String payerAcc;
    private String payerName;
    private String payerCreditType;
    private String payerCreditNo;
    private String payerMobile;
    private String payerEmail;
    private String remark;
    private String status;
    private String retCd;
    private String retCdRemark;
    private String workReturnNo;
    private String createDate;
    private String createTime;
    private String rspDate;
    private String rspTime;
}
