package pers.cjr.chatbot.nb.biz.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * @author Boll
 */
public class ReflectUtil {
	// ================================================
	// 					field相关
	// ================================================
	
	
	/**
	 * 获得所有的field
	 * @param clazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		try {
			return clazz.getDeclaredFields();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据属性名获得指定类的属性对象
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByName(Class<?> clazz,String fieldName){
		if(clazz == null || StringUtil.isEmpty(fieldName)){
			return null;
		}
		
		Field field = null;
		
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return field;
	}
	
	
	/**
	 * 获取指定类的指定名称属性的所有注解
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Annotation[] getAnnosByFieldName(Class<?> clazz, String fieldName){
		Field field = getFieldByName(clazz, fieldName);
		if(field == null){
			return null;
		}
		
		try {
			return field.getAnnotations();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取指定类的指定名称属性的所有注解
	 * @param clazz
	 * @param fieldName
	 * @param annoClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getAnnoByFieldName(Class<?> clazz, String fieldName,Class<? extends Annotation> annoClass){
		Field field = getFieldByName(clazz, fieldName);
		if(field == null){
			return null;
		}
		
		try {
			Annotation anno = field.getAnnotation(annoClass);
			if(anno == null){
				return null;
			}else{
				return (T)anno;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// ================================================
	// 					constructor相关
	// ================================================
	/**
	 * 以默认构造函数创建对象
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getDefaultInstance(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		
		 try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		 
		 return null;
	}
	
	
	// ================================================
	// 					method相关
	// ================================================
	
	/**
	 * 获得指定类的所有方法
	 * @param clazz
	 * @return
	 */
	public static Method[] getMehtods(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		
		try {
			return clazz.getDeclaredMethods();
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 执行指定字段名称对应getter方法
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Method getGetterByFieldName(Class<?> clazz, String fieldName){
		if(clazz == null || StringUtil.isEmpty(fieldName)){
			return null;
		}
		
		Method method = null;
		try {
			// getXxx类型
			String methodLowerName = "get"+fieldName.toLowerCase();
			Method[] mehtods = getMehtods(clazz);
			for (Method methodTmp : mehtods) {
				if(methodLowerName.equals(methodTmp.getName().toLowerCase())){
					return methodTmp;
				}
			}
			if(method == null){
				// isXxx类型
				methodLowerName = "is"+fieldName.toLowerCase();
				for (Method methodTmp : mehtods) {
					if(methodLowerName.equals(methodTmp.getName())){
						return methodTmp;
					}
				}
			}
			
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 执行指定字段名称对应setter方法
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Method getSetterByFieldName(Class<?> clazz, String fieldName){
		if(clazz == null || StringUtil.isEmpty(fieldName)){
			return null;
		}
		
		Method method = null;
		try {
			// getXxx类型
			String methodLowerName = "set"+fieldName.toLowerCase();
			Method[] mehtods = getMehtods(clazz);
			for (Method methodTmp : mehtods) {
				if(methodLowerName.equals(methodTmp.getName().toLowerCase())){
					return methodTmp;
				}
			}
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	} 
	
	/**
	 * 指定getter方法
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T invokeGetter(Object obj,String fieldName){
		if(obj == null || StringUtil.isEmpty(fieldName)){
			return null;
		}
		
		Method method = getGetterByFieldName(obj.getClass(), fieldName);
		if(method == null){
			return null;
		}
			
		try {
			return (T) method.invoke(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		return null;
	}
	
	/**
	 * 执行setter方法
	 * @param obj
	 * @param fieldName
	 * @param param
	 * @return
	 */
	public static<T> boolean invokeSetter(Object obj,String fieldName,Object param){
		if(obj == null || StringUtil.isEmpty(fieldName)){
			return false;
		}
		
		Method method = getSetterByFieldName(obj.getClass(), fieldName);
		if(method == null){
			return false;
		}
		
		try {
			method.invoke(obj, param);
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
}
