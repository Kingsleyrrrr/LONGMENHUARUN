package com.longmenhuarun.common;

import java.util.*;

public class Constant {
//    public static String ORGID="058594569";
//    public static String TRANTYPE="90301";
//    public static String ORIGBANK="103595424808";
//    public static String ORIGACC="44248001040036998";
//    public static String txnType="01";
//    public static String areaCode="5950";
//    public static String getProtNoPre=txnType+areaCode+ORGID+TRANTYPE;
    public static String ORGID="726501402";
    public static String TRANTYPE="01000";
    public static String ORIGBANK="201581000018";
    public static String ORIGACC="6227020181127144569";
    public static String txnType="01";
    public static String areaCode="5810";
    public static String getProtNoPre=txnType+areaCode+ORGID+TRANTYPE;
    public static List<String> SUCCESSRETCD= Arrays.asList("CP0I1000","CP1I1000","CP2I1000","CT0I1000","CT1I1000");
    public static String prepldsFileName="TXNTYP" + "1202" + "105301" + "00" + ORGID;
    public static Map<String,String> bankMap=new HashMap<>();
    static {
        bankMap.put("B101", "102581099996");
        bankMap.put("B102", "103581099992");
        bankMap.put("B103", "105581000018");
        bankMap.put("B104", "104581003017");
    }
}
