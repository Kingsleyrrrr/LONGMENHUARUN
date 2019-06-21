package com.longmenhuarun.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {

	/**
	 * 格式为:yyyyMMddHHmmss
	 */
	public static final String DATE_FORMAT_yyyyMMddHHmm = "yyyyMMddHHmm";
	/**
	 * 格式为:yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 格式为:HHmmss
	 */
	public static final String DATE_FORMAT_HHmmss = "HHmmss";
	/**
	 * 格式为:HH:mm:ss
	 */
	public static final String DATE_FORMAT_HH_mm_ss = "HH:mm:ss";
	/**
	 * 格式为:yyyyMMdd
	 */
	public static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";
	/**
	 * 格式为：yyyyMM
	 */
	public static final String DATE_FORMAT_yyyyMM = "yyyyMM";
	/**
	 * 格式为:yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
	
	private static final ThreadLocal<Map<String, SimpleDateFormat>> innerVariables = new ThreadLocal<Map<String, SimpleDateFormat>>();
	
	private static SimpleDateFormat getSimpleDateFormatInstance(String pattern) {
		Map<String, SimpleDateFormat> sdfMap = innerVariables.get();
		if(sdfMap == null) {
			sdfMap = new HashMap<String, SimpleDateFormat>();
			innerVariables.set(sdfMap);
		}
		SimpleDateFormat sdf = sdfMap.get(pattern);
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			sdfMap.put(pattern, sdf);
		}
		return sdf;
	}

	public static String dateFormat(Date date, String format) {
		String result = null;
		try {
			if(date == null)
				result = "";
			else{
				SimpleDateFormat sdf = getSimpleDateFormatInstance(format);
				result = sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 字符串日期格式转换
	 * 
	 * @param date
	 * @param oldFormat
	 * @param newFormat
	 * @return
	 */
	public static String changeStrTimeFormat(String date, String oldFormat,
			String newFormat) {
		String result = null;
		try {
			if (date == null || date.equals(""))
				return "";
			else {
				SimpleDateFormat sdf = getSimpleDateFormatInstance(oldFormat);
				Date tmp = sdf.parse(date);
				sdf.applyPattern(newFormat);
				result = sdf.format(tmp);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if (result == null) {
			return "";
		}
		return result;
	}
	
	public static Date getDateRelateToDate(Date date, int dateCnt){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dateCnt);
		return calendar.getTime();
	}
	
	/**
     * 获取时间date1与date2相差的秒数
     * 
     * @param date1
     *            起始时间
     * @param date2
     *            结束时间
     * @return 返回相差的秒数
     */
    public static long getOffsetSeconds(Date date1, Date date2) {
        long seconds = (long) ((date2.getTime() - date1.getTime()) / 1000);
        return seconds;
    }
    
    /**
     * 获取时间date1与date2相差的分钟数
     * 
     * @param date1
     *            起始时间
     * @param date2
     *            结束时间
     * @return 返回相差的分钟数
     */
    public static long getOffsetMinutes(Date date1, Date date2) {
        return getOffsetSeconds(date1, date2) / 60;
    }
    
    /**
     * 获取时间date1与date2相差的小时数
     * 
     * @param date1
     *            起始时间
     * @param date2
     *            结束时间
     * @return 返回相差的小时数
     */
    public static long getOffsetHours(Date date1, Date date2) {
        return getOffsetMinutes(date1, date2) / 60;
    }
 
    /**
     * 获取时间date1与date2相差的天数数
     * 
     * @param date1
     *            起始时间
     * @param date2
     *            结束时间
     * @return 返回相差的天数
     */
    public static long getOffsetDays(Date date1, Date date2) {
        return getOffsetHours(date1, date2) / 24;
    }
    
    /**
     * 获取时间date1与date2相差的周数
     * 
     * @param date1
     *            起始时间
     * @param date2
     *            结束时间
     * @return 返回相差的周数
     */
    public static long getOffsetWeeks(Date date1, Date date2) {
        return getOffsetDays(date1, date2) / 7;
    }
    
    /**
     * 获取指定日期累加年月日后的时间
     * 
     * @param date
     *            指定日期
     * @param year
     *            指定年数
     * @param month
     *            指定月数
     * @param day
     *            指定天数
     * @return 返回累加年月日后的时间
     */
    public static Date rollDate(Date date, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }
    
    /**
     * 获取指定日期累加指定月数后的时间
     * 
     * @param date
     *            指定日期
     * @param month
     *            指定月数
     * @return 返回累加月数后的时间
     */
    public static Date rollMonth(Date date, int month) {
        return rollDate(date, 0, month, 0);
    }
 
    /**
     * 获取指定日期累加指定天数后的时间
     * 
     * @param date
     *            指定日期
     * @param day
     *            指定天数
     * @return 返回累加天数后的时间
     */
    public static Date rollDay(Date date, int day) {
        return rollDate(date, 0, 0, day);
    }
 
    /**
     * 返回当前日期
     * @return yyyyMMdd
     */
    public static String getCurDateStr() {
    
    	return dateFormat(new Date(), DATE_FORMAT_yyyyMMdd);
    }
    
    /**
     * 返回当前时间
     * @return HHmmss
     */
    public static String getCurTimeStr() {
    
    	return dateFormat(new Date(), DATE_FORMAT_HHmmss);
    }
    
    /**
     * 返回当前月份
     * @return yyyyMM
     */
    public static String getCurMonthStr() {
    	
    	return dateFormat(new Date(), DATE_FORMAT_yyyyMM);
    }
	/**
	 * 返回当前年月日时间
	 * @return yyyyMMddHHmm
	 */
	public static String getCurDateTimeStr() {

		return dateFormat(new Date(), DATE_FORMAT_yyyyMMddHHmm);
	}
}
