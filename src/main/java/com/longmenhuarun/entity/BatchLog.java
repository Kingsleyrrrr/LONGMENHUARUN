package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/20 16:22
 * 4
 */
@Entity
@Data
public class BatchLog {
    @Id
    private String msgId;
    private String batchId;
    private String sendfileName;
    private String rspfileName;
    private String orgId;
    private String totCnt;
    private String totAmt;
    private String sucCnt;
    private String sucAmt;
    private String failCnt;
    private String failAmt;
    private String txnType;
    private String remark;
    private String status;
    private String createDate;
    private String createTime;
    private String rspDate;
    private String rspTime;
    private String source="ORG";
}
