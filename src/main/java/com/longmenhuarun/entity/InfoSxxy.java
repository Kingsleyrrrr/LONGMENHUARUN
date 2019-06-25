package com.longmenhuarun.entity;

import lombok.Data;
import org.hibernate.mapping.PrimaryKey;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/14 16:21
 * 4
 */
@Entity
@Data
public class InfoSxxy {
    private String orgId;
    private String protNo;
    @Id
    private String userNo;
    private String userName;
    private String entrustDate;
    private String contactName;
    private String contactAddr;
    private String activeDate;
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
}
