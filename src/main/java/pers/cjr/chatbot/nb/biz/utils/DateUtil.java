package pers.cjr.chatbot.nb.biz.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具
 * @author Boll
 * 2014下午4:57:36
 */
public class DateUtil {
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm:ss";
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE2 = "yyyy/MM/dd";
	public static final String PATTERN_DATE3 = "yyyy年MM月dd日";

	/**
	 * 生成日期格式化对象
	 * @param datePattern
	 */
	public static SimpleDateFormat generateFormat(String datePattern){
		if(StringUtil.isEmpty(datePattern)){
			datePattern = PATTERN_DATETIME;
		}
		return new SimpleDateFormat(datePattern);
	}
	/**
	 * 得到时间日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat generateFormatDateTime() {
		return generateFormat(DateUtil.PATTERN_DATETIME);
	}
	
	
	/**
	 * 快捷方式 @see DateUtil.timestampToDate(SimpleDateFormat simpleDateFormat, long timestamp, String dateFormat);
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String timestampToDate(long timestamp) {
		return timestampToDate(new SimpleDateFormat(PATTERN_DATETIME), timestamp);
	}
	
	/**
	 * 快捷方式 @see DateUtil.timestampToDate(SimpleDateFormat simpleDateFormat, long timestamp);
	 * 
	 * @param timestamp
	 * @param dateFormat 指定的结果日期字符串格式
	 * @return
	 */
	public static String timestampToDate(long timestamp, String dateFormat) {
		return timestampToDate(new SimpleDateFormat(dateFormat), timestamp);
	}
	
	/**
	 * 将时间戳转换为指定格式的日期字符串
	 * 
	 * @param simpleDateFormat @link{SimpleDateFormat}
	 * @param timestamp 1970 年至现在的毫秒数
	 * @return 返回转换为指定格式的字符串
	 */
	public static String timestampToDate(SimpleDateFormat simpleDateFormat, long timestamp) {
	    Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return simpleDateFormat.format(calendar.getTime());
	}
	
	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		if(date == null){
			return null;
		}
		try {
			return generateFormat(null).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date,String datePattern){
		if(date == null){
			return null;
		}
		try {
			return generateFormat(datePattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();
	
	private static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = formatMap.get(pattern);
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			formatMap.put(pattern, sdf);
		}
		return sdf;
	}
	
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = getSimpleDateFormat(pattern);
		synchronized(sdf) {
			return sdf.format(date);
		}
	}
	
	/**
	 * 按yyyy-MM-dd HH:mm:ss格式format
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, PATTERN_DATETIME);
	}
	
	
	/**
	 * yyyy/MM/dd
	 * @param date
	 * @return
	 */
	public static String format2(Date date) {
		return format(date, PATTERN_DATE2);
	}
	
	/**
	 * yyyy年MM月dd日
	 * @param date
	 * @return
	 */
	public static String format3(Date date) {
		return format(date, PATTERN_DATE3);
	}

	/**
	 * yyyy/MM/dd
	 * @param date
	 * @return
	 */
	public static String format4(Date date) {
		return format(date, PATTERN_DATE);
	}
	
	public static Date parse(String dateStr, String pattern) {
		SimpleDateFormat sdf = getSimpleDateFormat(pattern);
		try {
			synchronized(sdf) {
				return sdf.parse(dateStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 长度10 按yyyy-MM-dd格式parse
	 * 长度16按yyyy-MM-dd HH:mm格式parse
	 * 长度19按yyyy-MM-dd HH:mm:ss格式parse
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr) {
		String pattern = null;
		switch (dateStr.length()) {
		case 10: pattern = "yyyy-MM-dd"; break;
		case 16: pattern = "yyyy-MM-dd HH:mm"; break;
		case 19: pattern = "yyyy-MM-dd HH:mm:ss"; break;
		}
		return parse(dateStr, pattern);
	}
	
	/**
	 * 距离当前时间为多久前
	 * @param timeStamp
	 * @return
	 */
	public static String beforDate(Long timeStamp) {
		long minute = 1000 * 60;
		long hour = minute * 60;
		long day = hour * 24;
		long month = day * 30;
		long diffValue = System.currentTimeMillis() - timeStamp;
		if (diffValue < 0) {
			return DateUtil.timestampToDate(timeStamp, DateUtil.PATTERN_DATE3);
		}
		long monthC = diffValue / month;
		long weekC = diffValue / (7 * day);
		long dayC = diffValue / day;
		long hourC = diffValue / hour;
		long minC = diffValue / minute;

		String result = "刚刚";
		if (monthC >= 12) {
			return DateUtil.timestampToDate(timeStamp, DateUtil.PATTERN_DATE3);
		}
		if (monthC >= 1) {
			result = "" + monthC + "个月前";
		} else if (weekC >= 1) {
			result = "" + weekC + "周前";
		} else if (dayC >= 1) {
			result = "" + dayC + "天前";
		} else if (hourC >= 1) {
			result = "" + hourC + "小时前";
		} else if (minC >= 1) {
			result = "" + minC + "分钟前";
		}

		return result;
	}

	/**
	 * @param date1
	 * @param date2
	 * @return
	 * @see Calendar
	 */
	public static int compare(Date date1,Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return calendar1.compareTo(calendar2);
	}
	
	/**
	 * 两个时间差
	 * @param date
	 * @param date2
	 * @param isSG 是否为标点符号格式
	 * @return
	 */
	public static String dateDiff(Date date, Date date2, boolean isSG) {
		long mixdate = date.getTime();
		long maxdate = date2.getTime();
		if(mixdate > maxdate){
			maxdate = date.getTime();
			mixdate = date2.getTime();
		}
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		//获得两个时间的毫秒时间差异
		long diff = maxdate - mixdate;
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
		
		String dayStr = "天", hourStr = "小时", minStr = "分钟", secStr = "秒";
		if(isSG){
			dayStr = "d"; hourStr = "h"; minStr = "'"; secStr = "\"";
//			day = 0;//计算差多少天
//			hour = diff/nh;//计算差多少小时
		}
		
		String result = "";
		if(day != 0){
			result = day + dayStr;
		}
		
		if(hour != 0){
			result = result + hour + hourStr;
		}
		
		if(min != 0){
			result = result + min + minStr;
		}
		
		if(sec != 0){
			result = result + sec + secStr;
		}
		return result;
	}

	public static void main(String[] args) {
		long time = DateUtil.parse("2018-02-01").getTime();
		System.out.println(DateUtil.beforDate(time));
		
		System.out.println(DateUtil.dateDiff(DateUtil.parse("2018-03-01 15:00:12") , DateUtil.parse("2018-02-01 15:25:44"), true));
	}
	
}
