package com.longmenhuarun.model;

import cfbs.api.CFBSConstant;
import cfbs.api.CFBSMsgUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 机构发起实时协议报文
 * @author xujj
 * @date 2018年8月8日 上午10:29:02
 */
@Data
public class SsxyMsg {
	
	private String txnType;//交易种类，默认400101
	private String msgType;//报文类型，请求1202，回应1212
	private String reqMsgNo;//M交易序号
	private String entrustDate;//M委托日期

	private String protActType;//协议操作标识，新增100，撤销102
	private String orgId;//M收费机构代码
	//机构分支代码，略
	private String workReturnNo;//业务处理方流水
	private String retCd;//返回码
	private String protNo;//M合同协议号
	@NotBlank(message = "必填用户号")
	@Length(min = 1,max=18,message = "用户号长度错误")
	@Pattern(regexp = "^[A-Za-z0-9]+$",message ="用户号有非法字符" )
	private String userNo;//M用户编号
	@NotBlank(message = "必填用户名称")
	@Length(min = 1,max=60,message = "用户名长度错误")
	@Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$",message ="用户名有非法字符" )
	private String userName;//M用户名称
	
	private String contactName;//联系人名称
	private String contactAddr;//联系人地址
	private String contactAddrPost;//联系人地址邮编
	@NotBlank(message = "必填电话")
	@Pattern(regexp ="(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}",message ="电话格式错误" )
	private String contactNo;//M联系人电话
	
	private String tranType;//M业务种类
	@NotBlank(message = "必填行号")
	@Length(min = 12,max=12,message = "行号长度错误")
	@Pattern(regexp = "^[A-Za-z0-9]+$",message ="行号有非法字符" )
	private String payerBank;//M付款行行号
	@NotBlank(message = "必填账号")
	@Length(min = 1,max=35,message = "帐号长度错误")
	private String payerAcc;//M付款人账号
	@NotBlank(message = "必填户名")
	@Length(min = 1,max=60,message = "付款人户名长度错误")
	@Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z]+$",message ="付款人户名有非法字符" )
	private String payerName;//M付款人名称
	
	private String payerCreditType;//付款人证件类型
	private String payerCreditNo;//付款人证件号码
	private String payerMobile;//付款人手机号码
	private String payerEmail;//付款人邮箱
	private String remark;//附言

	public String toAddMsg() {
		return this.toMsg("100");
	}
	
	public String toDelMsg() {
		return this.toMsg("102");
	}
	
	private String toMsg(String oprType) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(CFBSConstant.TXNTYPE_SSXY);//交易种类
		sb.append(CFBSConstant.MSGTYPE_REQUEST);//报文类型
		sb.append(CFBSMsgUtil.leftFill("0", 16, this.getReqMsgNo()));//交易序号
		sb.append(CFBSMsgUtil.leftFill(" ", 8, this.getEntrustDate()));//委托日期
		sb.append(oprType);//协议操作标识，新增100，撤销102
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getOrgId()));//发起方机构代码
		sb.append(CFBSMsgUtil.rightFill(" ", 12, ""));//发起方分支代码(选填)
		sb.append("0000000000000000");//业务处理方流水
		sb.append("        ");//返回码
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getProtNo()));//合同协议号
		sb.append(CFBSMsgUtil.rightFill(" ", 18, this.getUserNo()));//用户编号
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getUserName()));//用户名称
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getContactName()));
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getContactAddr()));
		sb.append(CFBSMsgUtil.leftFill(" ", 6, this.getContactAddrPost()));
		sb.append(CFBSMsgUtil.rightFill(" ", 25, this.getContactNo()));
		
		sb.append(CFBSMsgUtil.leftFill(" ", 5, this.getTranType()));//业务种类
		sb.append(CFBSMsgUtil.rightFill(" ", 12, this.getPayerBank()));//付款行行号
		sb.append(CFBSMsgUtil.rightFill(" ", 32, this.getPayerAcc()));//付款人账号
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getPayerName()));//付款人姓名
		
		sb.append(CFBSMsgUtil.rightFill(" ", 1, this.getPayerCreditType()));
		sb.append(CFBSMsgUtil.rightFill(" ", 18, this.getPayerCreditNo()));
		sb.append(CFBSMsgUtil.rightFill(" ", 16, this.getPayerMobile()));
		sb.append(CFBSMsgUtil.rightFill(" ", 30, this.getPayerEmail()));
		sb.append(CFBSMsgUtil.rightFill(" ", 60, this.getRemark()));//附言
		
		return sb.toString();
	}
	
	public static SsxyMsg fromMsg(String rspMsg) {
		
		SsxyMsg obj = new SsxyMsg();
			
		obj.setTxnType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, 0, 6));
		int beginIndex = 6;
		
		obj.setMsgType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 4));
		beginIndex += 4;
		
		obj.setReqMsgNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setEntrustDate(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setProtActType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 3));
		beginIndex += 3;
		
		obj.setOrgId(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		//CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12)
		//分支机构代码，暂不设置
		beginIndex += 12;
		
		obj.setWorkReturnNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setRetCd(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 8));
		beginIndex += 8;
		
		obj.setProtNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		beginIndex += 60;
		
		obj.setUserNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 18));
		beginIndex += 18;
		
		obj.setUserName(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setContactName(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setContactAddr(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setContactAddrPost(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 6));
		beginIndex += 6;
		
		obj.setContactNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 25));
		beginIndex += 25;
		
		obj.setTranType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 5));
		beginIndex += 5;
		
		obj.setPayerBank(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 12));
		beginIndex += 12;
		
		obj.setPayerAcc(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 32));
		beginIndex += 32;
		
		obj.setPayerName(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60).trim());
		beginIndex += 60;
		
		obj.setPayerCreditType(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 1));
		beginIndex += 1;
		
		obj.setPayerCreditNo(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 18));
		beginIndex += 18;
		
		obj.setPayerMobile(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 16));
		beginIndex += 16;
		
		obj.setPayerEmail(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 30));
		beginIndex += 30;
		
		obj.setRemark(CFBSMsgUtil.subStringAndTrimWithByte(rspMsg, beginIndex, 60));
		
		//...
		
		return obj;
	}

	
	
}
