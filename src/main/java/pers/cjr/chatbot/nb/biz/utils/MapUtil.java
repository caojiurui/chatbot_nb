package pers.cjr.chatbot.nb.biz.utils;


import org.springframework.util.CollectionUtils;
import pers.cjr.chatbot.nb.biz.utils.page.PageData;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Map工具类
 * @author Boll
 */
public class MapUtil {
	
	/**
	 * 判断是否为空(map为null或者其中没有数据)
	 * @param map
	 * @return
	 * Boll
	 * 2014上午10:50:07
	 */
	public static boolean isEmpty(Map<?,?> map){
		if(map == null || map.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否不为空
	 * @param map
	 * @return
	 * Boll
	 * 2014上午10:50:54
	 */
	public static boolean isNotEmpty(Map<?,?> map){
		return !isEmpty(map);
	}
	
	/**
	 * 数组转为Map
	 * 	此数组中的元素必须是同一类型的对象(不能使基本类型)
	 * @param arary
	 * @param fieldName
	 * @return
	 */
	public static<K,V> Map<K,V> arrayToMap(V[] array, String fieldName){
		Map<K,V> map = new HashMap<K, V>();
		
		if(array == null 
				|| array.length == 0
				|| StringUtil.isEmpty(fieldName)){
			return map;
		}
		
		for (V v : array) {
			if(v == null){
				continue;
			}
			Field field = ReflectUtil.getFieldByName(v.getClass(), fieldName);
			if(field != null){
				 K k = ReflectUtil.<K>invokeGetter(v, fieldName);
				 if(k == null){
					 continue;
				 }
				 map.put(k,v);
			}
		}
		return map;
	}
	
	/**
	 * List转为Map
	 * 	此数组中的元素必须是同一类型的对象(不能使基本类型)
	 * @param arary
	 * @param fieldName
	 * @return
	 */
	public static<K,V> Map<K,V> listToMap(List<V> list, String fieldName){
		Map<K,V> map = new HashMap<K, V>();
		
		if(CollectionUtils.isEmpty(list) || StringUtil.isEmpty(fieldName)){
			return map;
		}
		
		for (V v : list) {
			if(v == null){
				continue;
			}
			if (v instanceof Map) {
				Map temp = (Map) v;
				if (temp.containsKey(fieldName)) {
					K k = (K) temp.get(fieldName);
					if (k == null) {
						continue;
					}
					map.put(k,v);
				}
			} else {
				Field field = ReflectUtil.getFieldByName(v.getClass(), fieldName);
				if(field != null){
					K k = ReflectUtil.<K>invokeGetter(v, fieldName);
					if(k == null){
						continue;
					}
					map.put(k,v);
				}
			}
		}
		return map;
	}
	
	/**
	 * 数组转换成map
	 * @param array
	 * @param keyName
	 * @return
	 */
	public static Map<Object,Map<String,Object>> arrayToMap(List<Map<String,Object>> array,String keyName) {
		Map<Object,Map<String,Object>> returnMap = new HashMap<Object, Map<String,Object>>();
		for (Map<String, Object> map : array) {
			returnMap.put(map.get(keyName), map);
		}
		return returnMap;
	}
	
	/**
	 * 去除map中的null对象和空字符串
	 * @param map
	 */
	public static<K,V> void removeNullAndEmpty(Map<K,V> map){
		if(map == null){
			return;
		}
		
		Iterator<Entry<K,V>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<K,V> entry = iterator.next();
			if(entry.getValue() == null 
					|| (entry.getValue() instanceof String && String.valueOf(entry.getValue()).isEmpty())){
				iterator.remove();
			}
		}
	}

	/**
	 * map中某个key转成string
	 * @param map
	 * @param keys
	 * @return
	 */
	public static Map<String,Object> mapKeyToString(Map<String,Object> map,List<String> keys) {
		if(map != null && keys != null){
			for (String key : keys) {
				if( map.get(key) != null){
					map.put(key, map.get(key).toString());
				}
			}
		}
		return map;
	}
	
	/**
	 * getMapDataToXxx : 字符串强转成某类型 xxx
	 * getXxx :先把某值转成String再转成某值 (防止前台传入数据PageData接收,强转类型失败)
	 */
	
	public static String getMapDataToString(Map<String, Object> map, String key, String defaultValue){
		return map.get(key) == null ? defaultValue : map.get(key).toString();
	}
	
	public static String getString(Map<String, Object> map, String key, String defaultValue){
		return map.get(key) == null ? defaultValue : map.get(key).toString();
	}
	public static String getString(Map<String, Object> map, String key){
		return getString(map, key, "");
	}
	
	public static Number getNumber(Map map, Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                    
                } else if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                        
                    } catch (ParseException e) {
//                    	e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
	
	public static Integer getMapDataToInteger(Map<String, Object> map, String key, Integer defaultValue){
		if (map == null || map.get(key) == null) {
			return defaultValue;
		} else {
			Number value = getNumber(map, key);
			if (value == null) {
				return defaultValue;
			}
			return value.intValue();
		}
	}
	public static Integer getInteger(Map<String, Object> map, String key, Integer defaultValue){
		return getMapDataToInteger(map, key, defaultValue);
	}
	public static Integer getInteger(Map<String, Object> map, String key){
		return getMapDataToInteger(map, key, 0);
	}
	
	public static Long getMapDataToLong(Map<String, Object> map, String key, Long defaultValue){
		if (map == null || map.get(key) == null) {
			return defaultValue;
		} else {
			Number value = getNumber(map, key);
			if (value == null) {
				return defaultValue;
			}
			return value.longValue();
		}
	}
	public static Long getLong(Map<String, Object> map, String key, Long defaultValue){
		return getMapDataToLong(map, key, defaultValue);
	}
	public static Long getLong(Map<String, Object> map, String key){
		return getMapDataToLong(map, key, 0l);
	}

	public static Byte getMapDataToByte(Map<String, Object> map, String key, Byte defaultValue){
		if (map == null || map.get(key) == null) {
			return defaultValue;
		} else {
			Number value = getNumber(map, key);
			if (value == null) {
				return defaultValue;
			}
			return value.byteValue();
		}
	}
	public static Byte getByte(Map<String, Object> map, String key, Byte defaultValue){
		return getMapDataToByte(map, key, defaultValue);
	}
	public static Byte getByte(Map<String, Object> map, String key){
		return getMapDataToByte(map, key, (byte)0);
	}
	
	public static Float getMapDataToFloat(Map<String, Object> map, String key, Float defaultValue){
		if (map == null || map.get(key) == null) {
			return defaultValue;
		} else {
			Number value = getNumber(map, key);
			if (value == null) {
				return defaultValue;
			}
			return value.floatValue();
		}
	}
	public static Float getFloat(Map<String, Object> map, String key, Float defaultValue){
		return getMapDataToFloat(map, key, defaultValue);
	}
	public static Float getFloat(Map<String, Object> map, String key){
		return getMapDataToFloat(map, key, 0f);
	}

	public static Double getMapDataToDouble(Map<String, Object> map, String key, Double defaultValue){
		if (map == null || map.get(key) == null) {
			return defaultValue;
		} else {
			Number value = getNumber(map, key);
			if (value == null) {
				return defaultValue;
			}
			return value.doubleValue();
		}
	}
	public static Double getDouble(Map<String, Object> map, String key, Double defaultValue){
		return getMapDataToDouble(map, key, defaultValue);
	}
	public static Double getDouble(Map<String, Object> map, String key){
		return getMapDataToDouble(map, key, 0d);
	}
	
	public static Date getMapDataToDate(Map<String, Object> map, String key, Date defaultValue){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return map.get(key) == null ? defaultValue : sdf.parse(map.get(key).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date getDate(Map<String, Object> map, String key, Date defaultValue){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return map.get(key) == null ? defaultValue : sdf.parse(map.get(key).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date getDate(Map<String, Object> map, String key){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object value = map.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof Long) {
			return new Date((Long)value);
		}
		if (value instanceof String) {
			try {
				return sdf.parse((String)value);
			} catch (ParseException e) {
				return null;
			}
		}
		if (value instanceof Date) {
			return (Date)value;
		}
		return null;
	}
	
	public static String getDateString(Map<String, Object> map, String key, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Object value = map.get(key);
		if (value == null) {
			return "";
		}
		if (value instanceof Long) {
			return sdf.format(new Date((Long)value));
		}
		if (value instanceof String) {
			return (String)value;
		}
		if (value instanceof Date) {
			return sdf.format((Date)value);
		}
		return "";
	}
	
	public static String getDateString(Map<String, Object> map, String key){
		return getDateString(map, key, "yyyy-MM-dd HH:mm:ss");
	}

	public static Map<String, Map<String, Object>> getListDataToMap(List<Map<String, Object>> list, String key){
		Map<String, Map<String, Object>> rtnMap = new HashMap<String, Map<String, Object>>();
		for(Map<String, Object> map : list) {
			if(map.containsKey(key)) {
				rtnMap.put(map.get(key).toString(), map);
			}
		}
		return rtnMap;
	}

	public static Date getMapDataToDate(PageData map, String key, Date defaultValue){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return map.get(key) == null ? defaultValue : sdf.parse(map.get(key).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Integer getIntegerByBoolean(Map<String, Object> map, String key){
		Integer flag = 0;
		try {
			if(map.get(key) !=null){
				Boolean val = (Boolean)map.get(key);
				flag = val ? 1 : 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * Long类型值转成String,暂时只支持一层
	 * @param map
	 */
	public static void covertLongValue2String(Map map) {
		if (map == null) {
			return ;
		}
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			if (value != null && value instanceof Long) {
				map.put(key, value.toString());
			}
		}
	}
	
	/**
	 * 得到Map中部分数据返回Map    
	 * @param map
	 * @param keys
	 * @return
	 */
	public static Map<String,Object> getMapPartData(Map<String,Object> map ,String... keys) {
		if(map == null || keys == null) return new HashMap();
		List<String> keysArray = Arrays.asList(keys);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		for (String key : map.keySet()) {
			if (keysArray.contains(key)) {
				returnMap.put(key, map.get(key));
			}
		}
		return returnMap;
	}
	
	/**
	 * 删除keys
	 * @param map
	 * @param keys
	 */
	public static void removeMapKeys(Map<String,Object> map ,String... keys) {
		if(map != null && keys != null){
			for (String key : keys) {
				map.remove(key);
			}
		}
	}
	/**
	 * 得到map (keys和values必须一一对应)
	 * @param keys
	 * @param values
	 * @return
	 */
	public static Map<String,String> getNewStringMap(List<String> keys,List<String> values) {
		Map<String,String> map = new HashMap<String, String>();
		if(keys != null && values != null){
			try {
				for (int i = 0; i < values.size(); i++) {
					map.put(keys.get(i), StringUtil.isNotEmpty(values.get(i)) ? values.get(i).toString() : values.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
