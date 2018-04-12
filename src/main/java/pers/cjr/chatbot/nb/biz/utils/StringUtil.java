package pers.cjr.chatbot.nb.biz.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * @author 朱海强
 * createTime: 2014-1-6 下午4:56:56
 */
public class StringUtil {
	/**
	 * 字符串数组中是否有空
	 * @param strs
	 * @return
	 */
	public static boolean containsEmpty(String[] strs){
		if(strs == null)
			return true;
		
		for (String str : strs) {
			if(isEmpty(str))
				return true;
		}
		return false;
	}
	
	/**
	 * 字符串数组中是否不含空
	 * @param strs
	 * @return
	 */
	public static boolean containsNoEmpty(String[] strs){
		return (!containsEmpty(strs));
	}
	
	/**
	 * 字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.isEmpty())
			return true;
		else
			return false;
	}
	
	/**
	 * 字符串是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return (!isEmpty(str));
	}
	
	/**
	 * 得到与匹配规则字符串数组
	 * @param str
	 * @param regex
	 * @return
	 */
	public static List<String> match(String str, String regex){
		List<String> results = new ArrayList<String>();
		
		Pattern pattern=Pattern.compile(regex);  
		Matcher matcher=pattern.matcher(str);  
		
		while(matcher.find())
			results.add(matcher.group());
		
		return results;
	}
	
	/**
	 * 按照正则表达式替换某个字符串
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String replace(String sourceStr, String regex, String replaceStr){
		return sourceStr.replaceAll(regex, replaceStr);
	}
	
	
	/**
	 * 拆分字符串为int数组
	 * @param source
	 * @param seperator
	 * @return
	 * Boll
	 * 2014下午5:03:22
	 */
	public static Integer[] splitToIntArray(String source,String seperator){
		if(isEmpty(source)){
			return null;
		}
		if(isEmpty(seperator)){
			seperator = ",";
		}
		
		String[] split = source.split(seperator);
		if(split == null){
			return null;
		}
		
		Integer[] result = new Integer[split.length];
		for (int i = 0; i < split.length; i++) {
			if(isNumber(split[i])){
				result[i] = Integer.valueOf(split[i]);
			}
		}
		
		return result;
	}
	
	/**
	 * 拆分字符串为Long数组
	 * @author 朱海强
	 * @param source
	 * @param seperator
	 * @return
	 * create time:	2014年7月31日	下午3:33:45
	 */
	public static Long[] splitToLongArray(String source,String seperator){
		if(isEmpty(source)){
			return null;
		}
		if(isEmpty(seperator)){
			seperator = ",";
		}
		
		String[] split = source.split(seperator);
		if(split == null){
			return null;
		}
		
		Long[] result = new Long[split.length];
		for (int i = 0; i < split.length; i++) {
			if(isNumber(split[i])){
				result[i] = Long.valueOf(split[i]);
			}
		}
		
		return result;
	}

	/**
	 * 拆分String类型List为Long数组
	 * @param list
	 * @return
	 */
	public static Long[] splitToLongArray(List<String> list) {
		String[] source = (String[])list.toArray(new String[list.size()]);
		return splitToLongArray(spliceFromArray(source, ","), ",");
	}
	
	/**
	 * 将字符串数组中拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 * Boll
	 * 2014年7月15日上午11:06:09
	 */
	public static String spliceFromArray(String[] array, String separator){
		if(ArrayUtils.isEmpty(array) || isEmpty(separator)){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for (String str : array) {
			if(!StringUtil.isEmpty(str)){
				if(sb.length() == 0){
					sb.append(str);
				}else{
					sb.append(separator+str);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 去除字符串中的重复字符串(以特定分隔符分割的字符串)
	 * @param str
	 * @param separator
	 * Boll
	 * 2014年7月15日上午11:12:29
	 */
	public static String removeRepeatStrFromStrBySep(String str, String separator){
		if(isEmpty(str) || isEmpty(separator)){
			return null;
		}
		
		String[] strArray = str.split(separator);
		Set<String> set = new TreeSet<String>();
		for (String strTmp : strArray) {
			if(strTmp != null){
				set.add(strTmp);
			}
		}
		
		return spliceFromArray(set.toArray(new String[set.size()]), separator);
	}
	
	/**
	 * 去除字符串结尾
	 * @param str
	 * @param endStr
	 * @return
	 */
	public static String removeEnd(String str,String endStr){
		if(str == null || StringUtil.isEmpty(endStr))
			return null;
		if(str.endsWith(endStr))
			return str.substring(0,str.length()-1);
		else
			return str;
	}
	
	/**
	 * 是否是布尔类型变量
	 * @param str
	 * @return
	 */
	public static boolean isBoolean(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		if((str.toLowerCase().equals("true"))
				|| (str.toLowerCase().equals("false")))
			return true;
		else
			return false;
	}
	
	// ***************************************************************
	// 						数字相关
	// ***************************************************************
	/**
	 * 字符串是否为整型数据
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		try {
			Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 字符串是否为长整型数据
	 * @param str
	 * @return
	 */
	public static boolean isLong(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		try {
			Long.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 字符串是否为单精度型数据
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		try {
			Float.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 字符串是否为双精度型数据
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		try {
			Double.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		return ( StringUtil.isLong(str) || StringUtil.isDouble(str) );
	}
	
	/**
	 * 字符串转换为Integer类型
	 * @param str
	 * @return
	 */
	public static Integer stringToInteger(String str){
		if(StringUtil.isEmpty(str))
			return null;
		
		Integer result = null;
		try {
			result = Integer.valueOf(str);
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 字符串转换为Long类型
	 * @param str
	 * @return
	 */
	public static Long stringToLong(String str){
		if(StringUtil.isEmpty(str))
			return null;
		
		Long result = null;
		try {
			result = Long.valueOf(str);
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 字符串转换为Float类型
	 * @param str
	 * @return
	 */
	public static Float stringToFloat(String str){
		if(StringUtil.isEmpty(str))
			return null;
		
		Float result = null;
		try {
			result = Float.valueOf(str);
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 字符串转换为Double类型
	 * @param str
	 * @return
	 */
	public static Double stringToDouble(String str){
		if(StringUtil.isEmpty(str))
			return null;
		
		Double result = null;
		try {
			result = Double.valueOf(str);
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 字符串转换为Boolean类型
	 * @param str
	 * @return
	 */
	public static Boolean stringToBoolean(String str){
		if(StringUtil.isEmpty(str))
			return false;
		
		Boolean result = null;
		try {
			result = Boolean.valueOf(str);
			return result;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	/**
	 * 格式化数字为指定格式字符串
	 * @param number		要格式的数字
	 * @param formatPattern	目标格式
	 * @return
	 */
	public static String formatNumber(Number number,String formatPattern){
		if(number == null || StringUtil.isEmpty(formatPattern))
			return null;
		
		String result = null;
		try {
			DecimalFormat decimalFormat = new DecimalFormat(formatPattern);
			result = decimalFormat.format(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// ###############################################################
	// 						数字相关结束
	// ###############################################################
	
	
	/**
	 * 获得文件拓展名
	 * @param path
	 * Boll
	 * 2014上午9:42:06
	 */
	public static String getExtension(String path){
		if(isEmpty(path)){
			return "";
		}
		
		int dotIndex = path.lastIndexOf(".");
		if(dotIndex < 0){
			return "";
		}
		return path.substring(dotIndex+1).replaceAll("\\)","");
	}
	
	/**
	 * 获得不带拓展名的文件名
	 * @author 朱海强
	 * @param path
	 * @return
	 * create time:	2014年9月19日	下午4:50:02
	 */
	public static String getNameWithoutExt(String path){
		if(isEmpty(path)){
			return "";
		}
		
		path = path.replace("\\", "/");
		int seperatorIndex = path.lastIndexOf("/");
		int startIndex = 0;
		if(seperatorIndex > 0){
			startIndex = seperatorIndex+1;
		}
		int dotIndex = path.lastIndexOf(".");
		if(dotIndex < 0){
			return path.substring(startIndex);
		}else{
			return path.substring(startIndex, dotIndex);
		}
	}
	
	/**
	 * 解析字符串[比如: x1:y1,x2:y2]
	 * @author 朱海强
	 * @param str
	 * @param rootSeperator			第一重分隔符
	 * @param subSeperator		第二重分隔符
	 * @return
	 * create time:	2014年8月22日	上午9:56:20
	 */
	@SuppressWarnings("unchecked")
	public static<K, V> Map<K, List<V>> parseStrToMap(String str, String rootSeperator, String subSeperator){
		if(StringUtil.isEmpty(str) 
				|| StringUtil.isEmpty(rootSeperator)
				|| StringUtil.isEmpty(subSeperator)
				|| (str.indexOf(rootSeperator) < 0 && str.indexOf(subSeperator) < 0)){
			return new TreeMap<K, List<V>>();
		}
		
		String[] firstArray = str.split(rootSeperator);
		if(ArrayUtils.isEmpty(firstArray)){
			return new TreeMap<K, List<V>>();
		}
		
		Map<K, List<V>> resultMap = new TreeMap<K, List<V>>();
		try {
			for (String firstStr : firstArray) {
				if(firstStr.indexOf(subSeperator) < 0){
					continue;
				}
				
				String[] secondArray = firstStr.split(subSeperator);
				if(secondArray.length != 2){
					continue;
				}
				
				if(resultMap.get(secondArray[0]) == null){
					List<V> list  = new ArrayList<V>();
					list.add((V)secondArray[1]);
					resultMap.put((K)secondArray[0], list);
				}else{
					resultMap.get(secondArray[0]).add((V)secondArray[1]);
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new TreeMap<K, List<V>>();
		}
	}
	
	/**
	 * 将字符串转换成指定分隔符分割的字符串
	 * 2015年1月16日 17:58:23 杨先锋
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String join(String str,String separator){
		String rtn = "";
		char[] chs = str.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			if(i == 0){
				rtn += chs[i];
			}else{
				rtn += separator + chs[i];
			}
		}
		return rtn;
	}
	
	
	/**
	 * 将字符串以某个字符拆分
	 * @param str
	 * @return
	 */
	public static List<String> split(String str,String separator) {
		List<String> list = new ArrayList<String>();
		if (str == null || str.length() == 0) {
			return list;
		}
		
		int len = str.length();
		int startOffset = 0;
		for(int i = 0; i < len; i++) {
			String s = str.substring(i, i + 1);
			if (separator.equals(s)) {
				if (i == startOffset) {
					list.add("");
				}
				else {
					list.add(str.substring(startOffset, i));
				}
				startOffset = i + 1;
			}
			else if (i == len - 1) {
				// last
				list.add(str.substring(startOffset, len));
			}
		}
		
		if (separator.equals(str.substring(len - 1, len))) {
			list.add("");
		}
		
		return list;
	}

	/**
	 * 将Long数组中拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 * 杨先锋
	 * 2015年08月19日16:30:41
	 */
	public static String spliceFromArray(Long[] array, String separator){
		if(ArrayUtils.isEmpty(array) || isEmpty(separator)){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (Long str : array) {
			if(!StringUtil.isEmpty(str.toString())){
				if(sb.length() == 0){
					sb.append(str);
				}else{
					sb.append(separator+str);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将Integer集合拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 * 杨先锋
	 * 2015年08月19日16:30:41
	 */
	public static String spliceFromIntegerArray(List<Integer> array, String separator){
		if(null == array || array.size() == 0 || isEmpty(separator)){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (Integer str : array) {
			if(!StringUtil.isEmpty(str.toString())){
				if(sb.length() == 0){
					sb.append(str);
				}else{
					sb.append(separator+str);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将String集合拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 * 杨先锋
	 * 2015年08月19日16:30:41
	 */
	public static String spliceFromStringArray(List<String> array, String separator){
		if(null == array || array.size() == 0 || isEmpty(separator)){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (String str : array) {
			if(!StringUtil.isEmpty(str)){
				if(sb.length() == 0){
					sb.append(str);
				}else{
					sb.append(separator+str);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将Long集合拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 * 杨先锋
	 * 2015年08月19日16:30:41
	 */
	public static String spliceFromLongArray(List<Long> array, String separator){
		if(null == array || array.size() == 0 || isEmpty(separator)){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (Long str : array) {
			if(!StringUtil.isEmpty(str.toString())){
				if(sb.length() == 0){
					sb.append(str);
				}else{
					sb.append(separator+str);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 转换 int 集合 为 String 数组
	 * @param list
	 * @return
	 */
	public static String[] convertIntegerList2StringArray(List<Integer> list) {
		String[] strings = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strings[i] = list.get(i).toString();
		}
		return strings;
	}

	/**
	 * 装换 long 集合 为 long 数组
	 * @param list
	 * @return
	 */
	public static Long[] converLongList2LongArray(List<Long> list) {
		Long[] longs = new Long[list.size()];
		for (int i = 0; i < list.size(); i++) {
			longs[i] = list.get(i);
		}
		return longs;
	}

	public static final String[] SBC_CASE = {"，", "。", "!"};
	public static final String[] DBC_CASE = {",",  ".",  "!"};

	/**
	 * 替换标点符号
	 * @param str
	 * @return
	 */
	public static String replacePunctuation(String str) {
		for (int i = 0; i < StringUtil.SBC_CASE.length; i++) {
			str = str.replaceAll(StringUtil.SBC_CASE[i], StringUtil.DBC_CASE[i]);
		}
		return str;
	}
	
	
	/**
	 * 转义处理
	 * @param string
	 * @param regexs
	 * @return
	 */
	public static String replaceString(String string,String[] regexs){
		if(!isEmpty(string)){
			for (String  regex: regexs) {
				if(string.contains(regex)){
					string.replace(regex, "\\" + regex);
				}
			}
		}
		return string;
	}

    /**
     * 将HTML字符串中的 br p 标签 转换为换行符
     * @param html
     * @return
     */
    public static String br2nl(String html) {
        if(StringUtil.isEmpty(html)) {
            return html;
        }
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n").replaceAll("&nbsp;", "").trim();
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

	/**
	 * 去重复的字符串分割
	 * @param string
	 * @param separator
	 * @return
	 */
    public static List<Long> splitRemoveRepeat(String string, String separator){
    	List<String> strList = StringUtil.split(string, separator);
    	
    	List<Long> list = new ArrayList<Long>();
    	try {
			for (String str : strList) {
				Long longStr = Long.valueOf(str);
				if(!list.contains(longStr)){
					list.add(longStr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	return list;
    }

	public static String toShortString(String message,int maxLogLength) {
		return message.length() > maxLogLength ? message.substring(0,maxLogLength) : message;
	}
}
