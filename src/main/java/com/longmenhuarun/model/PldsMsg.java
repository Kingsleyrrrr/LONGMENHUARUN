package com.longmenhuarun.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cfbs.api.CFBSConstant;
import cfbs.api.CFBSMsgUtil;
import lombok.Data;

/**
 * 批量交易对象（代收代付通用） 
 */
@Data
public class PldsMsg {

	private String fileName;//文件名
	private String refFileName = "";//参考文件名
	private String orgId;//发起方代码
	private String origBank;//发起方开户行
	private String origAcc;//发起方开户账号
	private String outBank;//接收方代码
	private String txnType;//交易类型
	private String tranType;//业务种类
	private String msgType;//报文类型
	private String currency;//币种
	private String totalCount;//总笔数
	private String totalAmount;//总金额
	private String failCount;//失败总笔数
	private String failAmount;//失败总金额
	private String retCd = "";//返回码
	private String customLength = "0";//变长定制信息长度
	private String customContent = "";//变长定制信息

	private List<PldsMsgDetail> details = new ArrayList<>();
	@Data
	public class PldsMsgDetail {

		private String txnSeq;//交易序号
		private String entrustDate;//委托日期
		private String txnDate;//清算日期
		private String userNo;//用户编号

		private String orgId;//发起方代码
		private String origBank = "";//发起方开户行
		private String outAccBank = "";//接收方分支代码

		private String payerAcc;//付款人账号
		private String payerName;//付款人名称
		private String payerContact = "";//付款人联系方式

		private String payeeAcc;//收款人账号
		private String payeeName;//收款人名称
		private String payeeContact = "";//收款人联系方式

		private String contractId = "";//合同协议号
		private String amount;//金额
		private String charge = "0";//手续费
		private String remark = "";//附言
		private String workReturnNo = "";//业务处理方流水
		private String retCode = "";//返回码
		private String customLength = "0";//变成定制信息长度
		private String customContent = "";//变长定制信息
	}


	/**
	 * 批量代收明文文件转对象
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static PldsMsg procRespPldsFile(String filePath, String fileName) throws Exception {
		
		BufferedReader breader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName), "GBK"));
		
		PldsMsg pldsMsg = new PldsMsg();
		
		/* 1、解析文件头，获取请求时文件名*/
		String temp = breader.readLine();
		int beginIndex = 0;
		
		String replyFileName = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 60);// 文件名
		pldsMsg.setFileName(replyFileName);
		beginIndex += 60;
		
		String cfbsReqFileName = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 60);// 参考文件名，既请求时文件名
		pldsMsg.setRefFileName(cfbsReqFileName);
		beginIndex += 60;
		
		String orgId = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 12);// 发起方代码
		pldsMsg.setOrgId(orgId);
		beginIndex += 12;
		
		String origBank = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 12);// 发起方开户行行号 	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setOrigBank(origBank);
		beginIndex += 12;
		
		String origAcc = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 35);// 发起方开户账号	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setOrigAcc(origAcc);
		beginIndex += 35;
		
		String outBank = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 12);// 接收方代码	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setOutBank(outBank);
		beginIndex += 12;
		
		String tranType = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 5);// 业务种类	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setTranType(tranType);
		beginIndex += 5;
		
		String txnType = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 6);// 交易种类	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setTxnType(txnType);
		beginIndex += 6;
		
		String msgType = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 4);// 报文类型	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setMsgType(msgType);
		beginIndex += 4;
		
		String currency = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 3);// 交易货币代码	【无需使用，不进行截取，请勿删除此代码】
		pldsMsg.setCurrency(currency);
		beginIndex += 3;
		
		String totalCount = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 10);// 交易总笔数	
		pldsMsg.setTotalCount(totalCount);
		beginIndex += 10;
		
		String totalAmount = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 16);// 交易总金额	
		pldsMsg.setTotalAmount(totalAmount);
		beginIndex += 16;
		
		String totalFailCount = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 10);// 失败总笔数
		pldsMsg.setFailCount(totalFailCount);
		beginIndex += 10;
		
		String totalFailAmount = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 16);// 失败总金额
		pldsMsg.setFailAmount(totalFailAmount);
		beginIndex += 16;
		
		String retCd = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 8);// 返回码
		pldsMsg.setRetCd(retCd);
		beginIndex += 8;
		
		/* 2、逐行读取文件，并解析文件体 */
		while ((temp = breader.readLine()) != null) {
			
			PldsMsgDetail detail = pldsMsg.new PldsMsgDetail();
			
			beginIndex = 0;
			
			String msgId = CFBSMsgUtil.getGBKStrCut(temp, beginIndex, 16);// 交易序号
			detail.setTxnSeq(msgId);
			beginIndex += 16;

			String entrustDate = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 8);// 委托日期
			detail.setEntrustDate(entrustDate);
			beginIndex += 8;
			
			String txnDate = CFBSMsgUtil.getGBKStrCut(temp, beginIndex, 8);// 清算日期
			detail.setTxnDate(txnDate);
			beginIndex += 8;

			String userNo = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 18);// 用户编号
			detail.setUserNo(userNo);
			beginIndex += 18;
			
			String detailOrgId = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 12);// 发起方分支代码
			detail.setOrgId(detailOrgId);
			beginIndex += 12;
			
			String outAccBank = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 12);// 接收方分支代码（付款人开户行行号）
			detail.setOutAccBank(outAccBank);
			beginIndex += 12;
			
			String payerAcc = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 35);// 付款人账号
			detail.setPayerAcc(payerAcc);
			beginIndex += 35;
			
			String payerName = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 60);// 付款人名称
			detail.setPayerName(payerName);
			beginIndex += 60;
			
			String payerContact = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 60);// 付款人联系方式
			detail.setPayerContact(payerContact);
			beginIndex += 60;
			
			String contractId = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 60);// 合同（协议）号
			detail.setContractId(contractId);
			beginIndex += 60;
			
			String amount = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 15);// 金额
			detail.setAmount(amount);
			beginIndex += 15;
			
			String charge = CFBSMsgUtil.subStringAndTrimWithByte(temp, beginIndex, 15);// 手续费
			detail.setCharge(charge);
			beginIndex += 15;
			
			String remark = CFBSMsgUtil.getGBKStrCut(temp, beginIndex, 60);// 附言
			detail.setRemark(remark);
			beginIndex += 60;

			String workReturnNo = CFBSMsgUtil.getGBKStrCut(temp, beginIndex, 16);// 业务处理方流水
			detail.setWorkReturnNo(workReturnNo);
			beginIndex += 16;
			
			String retCode = CFBSMsgUtil.getGBKStrCut(temp, beginIndex, 8);// 返回码
			detail.setRetCode(retCode);
			beginIndex += 8;
			
			pldsMsg.getDetails().add(detail);
		}
		
		breader.close();
		
		return pldsMsg;
	}
	
	/**
	 * 批量代收对象转明文文件
	 * @param msg
	 * @param path
	 * @throws Exception
	 */
	public static String genReqPldsFile(PldsMsg msg, String path) throws Exception {
		
		//不要用println，一定要手工录入回车换行
		char[] chrLineEnd = new char[2];
		chrLineEnd[0] = 0x0d;
		chrLineEnd[1] = 0x0a;
		
		PrintWriter cfbsFileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + msg.getFileName()), "GBK"));
		
		//文件头
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 60, msg.getFileName()));// 文件名 
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 60, ""));// 参考文件名 O 
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 12, msg.getOrgId()));// 发起方代码，应该填发起业务的真实机构号，而非代理的机构号
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 12, msg.getOrigBank()));// 发起方开户行行号
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 35, msg.getOrigAcc()));// 发起方开户账号
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 12, msg.getOutBank()));// 接收方代码
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 5, msg.getTranType()));// 业务种类
		cfbsFileWriter.write("105301");// 交易种类
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 4, msg.getMsgType()));// 报文类型
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 3, msg.getCurrency()));// 交易货币代码
		cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 10, msg.getTotalCount()));// 交易总笔数
		cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 16, msg.getTotalAmount()));// 交易总金额
		cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 10, "0"));// 失败总笔数 O
		cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 16, "0"));// 失败总金额 O
		cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 8, ""));// 返回码 O
		cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 8, msg.getCustomLength()));// 变长定制信息长度 
		cfbsFileWriter.write(msg.getCustomContent());// 变长定制信息内容 O
		cfbsFileWriter.write(CFBSConstant.CUSTOM_MESSAGE);
		cfbsFileWriter.write(chrLineEnd);

		//文件体
		for(PldsMsgDetail detail : msg.getDetails()) {
			cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 16, detail.getTxnSeq()));// 交易序号
			cfbsFileWriter.write(detail.getEntrustDate());// 委托日期
			cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 8, "0"));// 清算日期 00000000
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 18, detail.getUserNo()));// 用户编号
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 12, detail.getOrgId()));// 发起方分支代码 O
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 12, detail.getOutAccBank()));// 接收方分支代码（付款人开户行行号） O
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 35, detail.getPayerAcc()));// 付款人账号
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 60, detail.getPayerName()));// 付款人名称
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 60, detail.getPayerContact()));// 付款人联系方式 O
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE,60, detail.getContractId()));// 合同（协议）号
			cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 15, detail.getAmount()));// 金额
			cfbsFileWriter.write(CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 15, detail.getCharge()));// 手续费
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 60, detail.getRemark()));// 附言 O
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_ZERO, 16, "0"));// 业务处理方流水 0000
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_BLANK_SPACE, 8, ""));// 返回码 0000
			cfbsFileWriter.write(CFBSMsgUtil.rightFill(CFBSConstant.FILL_WORD_ZERO, 8, detail.getCustomLength()));// 变长定制信息长度
			cfbsFileWriter.write(detail.getCustomContent());// 变长定制信息
			cfbsFileWriter.write(CFBSConstant.CUSTOM_MESSAGE);
			cfbsFileWriter.write(chrLineEnd);
		}

		cfbsFileWriter.flush();
		cfbsFileWriter.close();
		
		File file = new File(path + msg.getFileName());
		if(file.exists()) {
			
			File newFile = new File(path + msg.getFileName() + (CFBSMsgUtil.leftFill(CFBSConstant.FILL_WORD_ZERO, 8, "" + file.length())));
			file.renameTo(newFile);
			
			return newFile.getName();
		}
		
		return null;
	}

}
