package com.longmenhuarun.model;

import cfbs.api.CFBSConstant;
import cfbs.api.CFBSMsgUtil;
import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * 实时代收报文要素
 */
@Data
public class SsdsMsg {

	private String txnType;
	private String msgType;
	private String reqMsgNo;
	private String entrustDate;
	private String txnDate;
	@NotBlank(message = "必填用户号")
	@Length(min = 1,max=18,message = "用户号长度错误")
	@Pattern(regexp = "^[A-Za-z0-9]+$",message ="用户号有非法字符" )
	private String userNo;
	private String orgId;
	private String origBank;
	private String origAcc;
	@NotBlank(message = "必填行号")
	@Length(min = 12,max=12,message = "行号长度错误")
	@Pattern(regexp = "^[A-Za-z0-9]+$",message ="行号有非法字符" )
	private String outBank;
	private String outAccBank;
	@NotBlank(message = "必填账号")
	@Length(max=35,message = "帐号长度错误")
	private String payerAcc;
	@NotBlank(message = "必填户名")
	@Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z]+$",message ="户名有非法字符" )
	private String payerName;
	private String tranType;
	private String protocolNo;
	private String currency;
	@Pattern(regexp = "^[0-9]+$",message ="金额有非法字符" )
	@Length(min = 1,max=15,message = "金额长度错误")
	@NotBlank(message = "必填金额")
	private String amount;
	private String charge;
	private String remark;

	private String retCd;
	private String workReturnNo;


	public String toMsg() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(CFBSConstant.TXNTYPE_SSDS);//交易种类
		sb.append(CFBSConstant.MSGTYPE_REQUEST);//报文类型
		sb.append(CFBSMsgUtil.leftFill("0", 16, this.getReqMsgNo()));//交易序号
		sb.append(CFBSMsgUtil.rightFill(" ", 8, this.getEntrustDate()));//委托日期
		sb.append("00000000");//清算日期
		sb.append(CFBSMsgUtil.rightFill(" ", 18, this.getUserNo()));//用户编号
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getOrgId()));//发起方机构代码
		sb.append(CFBSMsgUtil.rightFill(" ", 12, ""));//发起方分支代码(选填)
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getOrigBank()));//发起方开户行行号
		sb.append(CFBSMsgUtil.rightFill(" ", 35, this.getOrigAcc()));//发起方开户账号
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getOutBank()));//接收方代码（清算行号）
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getOutAccBank()));//接收方分支代码（集中受理行号）
		sb.append(CFBSMsgUtil.rightFill(" ", 35, this.getPayerAcc()));//付款人账号
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getPayerName()));//付款人姓名
		sb.append(CFBSMsgUtil.rightFill(" ", 5, this.getTranType()));//业务种类
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getProtocolNo()));//合同协议号
		sb.append(CFBSConstant.CURRENCY_CNY);//交易货币
		sb.append(CFBSMsgUtil.leftFill("0", 15, this.getAmount()));//金额
		sb.append(CFBSMsgUtil.leftFill("0", 15, this.getCharge()));//手续费
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getRemark()));//附言
		sb.append("0000000000000000");//业务处理方流水
		sb.append("        ");//返回码
		sb.append("0");//主动缴费标识
		sb.append("00000000");//变长定制信息长度
		
		return sb.toString();
	}
	
	public static SsdsMsg fromMsg(String rspMsg) {
		
		SsdsMsg obj = null;
			
		obj = new SsdsMsg();
		
		obj.setTxnType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, 0, 6));
		int beginIndex = 6;
		
		obj.setMsgType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 4));
		beginIndex += 4;
		
		obj.setReqMsgNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setEntrustDate(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setTxnDate(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setUserNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 18));
		beginIndex += 18;
		
		obj.setOrgId(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		//CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12)
		//分支机构代码，暂不设置
		beginIndex += 12;
		
		obj.setOrigBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setOrigAcc(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 35));
		beginIndex += 35;
		
		obj.setOutBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setOutAccBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setPayerAcc(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 35));
		beginIndex += 35;
		
		obj.setPayerName(CFBSMsgUtil.getGBKStrCut(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setTranType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 5));
		beginIndex += 5;
		
		obj.setProtocolNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		beginIndex += 60;
		
		obj.setCurrency(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 3));
		beginIndex += 3;
		
		obj.setAmount(CFBSMsgUtil.removeLeftZero(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 15)));
		beginIndex += 15;
		
		obj.setCharge(CFBSMsgUtil.removeLeftZero(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 15)));
		beginIndex += 15;
		
		obj.setRemark(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		beginIndex += 60;
		
		obj.setWorkReturnNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setRetCd(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
			
		return obj;
	}
	
	public static SsdsMsg fromMsgWithUTF8(String rspMsg) {
		
		SsdsMsg obj = new SsdsMsg();
		
		obj.setTxnType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, 0, 6));
		int beginIndex = 6;
		
		obj.setMsgType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 4));
		beginIndex += 4;
		
		obj.setReqMsgNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setEntrustDate(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setTxnDate(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setUserNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 18));
		beginIndex += 18;
		
		obj.setOrgId(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		//CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12)
		//分支机构代码，暂不设置
		beginIndex += 12;
		
		obj.setOrigBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setOrigAcc(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 35));
		beginIndex += 35;
		
		obj.setOutBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setOutAccBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setPayerAcc(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 35));
		beginIndex += 35;
		
		obj.setPayerName(CFBSMsgUtil.getGBKStrCut(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setTranType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 5));
		beginIndex += 5;
		
		obj.setProtocolNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		beginIndex += 60;
		
		obj.setCurrency(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 3));
		beginIndex += 3;
		
		obj.setAmount(CFBSMsgUtil.removeLeftZero(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 15)));
		beginIndex += 15;
		
		obj.setCharge(CFBSMsgUtil.removeLeftZero(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 15)));
		beginIndex += 15;
		
		obj.setRemark(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		beginIndex += 60;
		
		/*
		obj.setWorkReturnNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setRetCd(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		*/
		
		obj.setWorkReturnNo(rspMsg.substring(408, 421));
		obj.setRetCd(rspMsg.substring(421, 429));
		
		//...
			
		
		return obj;
	}
}
