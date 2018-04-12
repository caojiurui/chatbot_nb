package pers.cjr.chatbot.nb.biz.utils;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 集合工具类
 * @author 朱海强
 * create time: 	2014年11月13日	上午12:14:00
 */
public class CollectionUtil {
	/**
	 * 从元素为Map<String, Object>类型的集合中获得全部的指定key元素集合
	 * @author 朱海强
	 * @param list
	 * @param key
	 * @return
	 * create time:	2014年11月13日	上午12:23:28
	 */
	@SuppressWarnings("unchecked")
	public static<T> List<T> getAllPartList(List<Map<String, Object>> list, String key){
		if(CollectionUtils.isEmpty(list) || StringUtil.isEmpty(key)){
			return new ArrayList<T>();
		}
		
		ArrayList<T> resultList = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			Object tmpObj = map.get(key);
			if(tmpObj == null){
				resultList.add(null);
			}else{
				try {
					resultList.add((T)tmpObj);
				} catch (Exception e) {
					resultList.add(null);
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 从集合对象中中获得指定key元素集合（不带重复）
	 * @author yanghan
	 * @param list
	 * @param key
	 * @param isRepeat true:可重复，false:不重复
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static<K, V> List<V> getPartList(List<K> list, String key, boolean isRepeat){
		if(CollectionUtils.isEmpty(list) || StringUtil.isEmpty(key)){
			return new ArrayList<V>();
		}
		
		List<V> resultList = new ArrayList<V>();
		for (K k : list) {
			Object obj = null;
			if (k instanceof Map) {
				Map temp = (Map) k;
				if (temp.containsKey(key)) {
					obj = temp.get(key);
				}
			} else {
				Field field = ReflectUtil.getFieldByName(k.getClass(), key);
				if(field != null){
					obj = ReflectUtil.<K>invokeGetter(k, key);
				}
			}
			
			if (obj == null) {
				continue;
			}
			V tObj = (V) obj;
			
			if(!isRepeat){
				if(!resultList.contains(tObj)){
					resultList.add(tObj);
				} 
			} else {
				resultList.add(tObj);
			}
		}
		return resultList;
	}
	
	/**
	 * 从元素为Map<String, Object>类型的集合中获得不为空的指定key元素集合
	 * 注意： 对于Integer，Long之类的类型，可能会在使用Collections.toArray方法报错
	 * @author 朱海强
	 * @param list
	 * @param key
	 * @return
	 * create time:	2014年11月13日	上午12:23:28
	 */
	@SuppressWarnings("unchecked")
	public static<T> List<T> getNotNullPartList(List<Map<String, Object>> list, String key){
		if(CollectionUtils.isEmpty(list) || StringUtil.isEmpty(key)){
			return new ArrayList<T>();
		}
		
		ArrayList<T> resultList = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			Object tmpObj = map.get(key);
			if(tmpObj != null){
				try {
					resultList.add((T)tmpObj);
				} catch (Exception e) {
					
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 从元素为Map<String, Object>类型的集合中获得不为空的指定key元素集合
	 * @author 朱海强
	 * @param list
	 * @param key
	 * @return
	 * create time:	2014年11月13日	上午10:02:11
	 */
	public static List<Integer> getNotNullPartIntList(List<Map<String, Object>> list, String key){
		if(CollectionUtils.isEmpty(list) || StringUtil.isEmpty(key)){
			return new ArrayList<Integer>();
		}
		
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		for (Map<String, Object> map : list) {
			Object tmpObj = map.get(key);
			if(tmpObj != null){
				try {
					resultList.add(Integer.valueOf(tmpObj.toString()));
				} catch (Exception e) {
					
				}
			}
		}
		return resultList;
	}
	/**
	 * 将 list的所有map转成驼峰命名
	 * @param list
	 */
	public static void listMapToCamelCase(List<Map<String,Object>> list,SimpleDateFormat sdf) {
		if(list == null || list.size() == 0) return;
		for (int i = 0; i < list.size(); i++) {  //转换驼峰命名
			BeanMapUtil.convertMap(list.get(i),sdf); 
		}
	}
	/**
	 * 把list的某列转换成toString
	 * @param list
	 * @param keys
	 */
	public static void listValueToStringByKeys(List<Map<String,Object>> list,List<String> keys) {
		if(list == null || keys == null) return;
		for (Map<String,Object> map : list) {
			for (String key : keys) {
				map.put(key, map.get(key) == null ? null : map.get(key).toString());
			}
		}
	}

	/**
	 * 把list的某列转换成toString
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> List<String> listValueToString(List<T> list) {
		List<String> list2 = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			list2.add(list.get(i) == null ? null : list.get(i).toString());
		}
		return list2;
	}
	
	/**
	 * 把list的某列Map移除值
	 * @param list
	 * @param keys
	 */
	public static void listValueRemoveByKeys(List<Map<String,Object>> list,List<String> keys) {
		if(list == null || keys == null) return;
		for (Map<String,Object> map : list) {
			for (String key : keys) {
				if(map.get(key) != null){
					map.remove(key);
				}
			}
		}
	}
	
	/**
	 * 实体类列表转换成Map列表
	 * @param <T>
	 * @param beanList
	 * @return
	 */
	public static <T> List<Map<String,Object>> beanListToMapList(List<T> beanList) {
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		if(beanList == null || beanList.size() == 0) return mapList;
		for (Object bean : beanList) {
			mapList.add(BeanMapUtil.convertBean(bean));
		}
		return mapList;
	}
	
	/**
	 * Set集合转为list
	 * @param keySet
	 * @return
	 */
	public static<T> List<T> setTolist(Set<T> keySet) {
		List<T> list = new ArrayList<T>();
		for (T t : keySet) {
			list.add(t);
		}
		return list;
	}

	/**
	 * Map 转化回 List
	 * @param tagMap Map<T, Map<String, Object>>
	 * @return
	 */
	public static<T> List<Map<String, Object>> MapToList(Map<T, Map<String, Object>> tagMap) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Entry<T, Map<String, Object>> entry : tagMap.entrySet()) {
			Map<String, Object> map = entry.getValue();
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 得到索引集合列表
	 * @param list
	 * @param index
	 * @return
	 */
	public static<T> List<T> getListByIndex(List<T> list,int index) {
		List<T> resultList = new ArrayList<T>();
		if(index >=0 && list.size() > 0){
			if(index >= list.size() ){
				index = list.size()-1;
			}
			for (int i = 0; i < list.size(); i++) {
				if(i <= index){
					resultList.add(list.get(i));
				}
			}
		}
		return resultList;
	}
}
