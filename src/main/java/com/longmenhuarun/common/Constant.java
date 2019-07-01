package com.longmenhuarun.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Constant {

    public static String ORGID;

    public static String TRANTYPE;

    public static String ORIGBANK;

    public static String ORIGACC;

    public static String txnType;

    public static String areaCode;

    @Value("${ORGID}")
    public  void setORGID(String ORGID) {
        Constant.ORGID = ORGID;
    }
    @Value("${TRANTYPE}")
    public  void setTRANTYPE(String TRANTYPE) {
        Constant.TRANTYPE = TRANTYPE;
    }
    @Value("${ORIGBANK}")
    public  void setORIGBANK(String ORIGBANK) {
        Constant.ORIGBANK = ORIGBANK;
    }
    @Value("${ORIGACC}")
    public  void setORIGACC(String ORIGACC) {
        Constant.ORIGACC = ORIGACC;
    }
    @Value("${TXNTYPE}")
    public void setTxnType(String txnType) {
        Constant.txnType = txnType;
    }
    @Value("${AREACODE}")
    public  void setAreaCode(String areaCode) {
        Constant.areaCode = areaCode;
    }

    public static String getProtNoPre() {
       return txnType+areaCode+ORGID+TRANTYPE;
    }

    public static List<String> SUCCESSRETCD= Arrays.asList("CP0I1000","CP1I1000","CP2I1000","CT0I1000","CT1I1000");
    public static String getPrepldsFileName(){
       return  "TXNTYP" + "1202" + "105301" + "00" + ORGID;
    }
    public static Map<String,String> bankMap=new HashMap<>();
    static {
        bankMap.put("B101", "102581099996");
        bankMap.put("B102", "103581099992");
        bankMap.put("B103", "105581000018");
        bankMap.put("B104", "104581003017");
        bankMap.put("TEST", "201581000018");
    }
}
