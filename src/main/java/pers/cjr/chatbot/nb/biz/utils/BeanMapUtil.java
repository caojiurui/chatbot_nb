package pers.cjr.chatbot.nb.biz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

public class BeanMapUtil {
	public static final char UNDERLINE = '_';
	private static Logger logger = LoggerFactory.getLogger(BeanMapUtil.class);

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean 要转化的JavaBean 对象
	 */
	public static Map<String, Object> convertBean(Object bean) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Class<?> type = bean.getClass();
			BeanInfo beanInfo = Introspector.getBeanInfo(type);

			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
//					if (result != null) {
						returnMap.put(propertyName, result);
//					} else {
//						returnMap.put(propertyName, "");
//					}
				}
			}
		} catch (IntrospectionException e) {
			logger.error("如果分析类属性失败");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("如果实例化 JavaBean 失败");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("如果调用属性的 setter 方法失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 将一个 JavaBean 对象转化为一个 Map 并把Date类型字段format成字符串
	 * 
	 * @param bean 要转化的JavaBean 对象
	 */
	public static Map<String, Object> toMap(Object bean, SimpleDateFormat sdf) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		if (bean == null) {
			return rtn;
		}
		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			String key = field.getName();
			Object value = null;
			try {
				value = field.get(bean);
			} catch (Exception e) {
				logger.warn(key + "属性未能正确取到值");
				e.printStackTrace();
			}
			if (value instanceof Date && sdf != null) {
				value = sdf.format(value);
			}
			rtn.put(key, value);
		}
		return rtn;
	}
	
	/**
	 * 将 mybatis查询返回的map key 转成驼峰命名
	 *
	 * @param map
	 * @return
	 */
	public static Map<String, Object> convertMap(Map<String, Object> map) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		for (String key : map.keySet()) {
			rtn.put(underlineToCamel(key), map.get(key));
		}
		return rtn;
	}

	/**
	 * 将 mybatis查询返回的map key 转成驼峰命名 Date类型的值format成字符串
	 *
	 * @param map
	 * @return
	 */
	public static void convertMap(Map<String, Object> map, SimpleDateFormat sdf) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value instanceof Date && sdf != null) {
				value = sdf.format(value);
			}
			rtn.put(underlineToCamel(key), value);
		}
		map.clear();
		map.putAll(rtn);
	}

	/**
	 * 将 map 转为 javabean
	 *
	 * @param map
	 *            驼峰命名key的map
	 * @param cls
	 *            javabean
	 * @param sdf
	 *            日期格式
	 * @param writeNullNumberAsZero
	 *            是否将 数字类型null转为0
	 */
	public static <T> T toBean(Map<String, Object> map, Class<T> cls, final SimpleDateFormat sdf, boolean writeNullNumberAsZero) {
		T bean = null;
		try {
			bean = cls.newInstance();
		} catch (Exception e) {
			logger.error("创建实例失败");
			throw new RuntimeException(e);
		}

		if (map == null) {
			return bean;
		}

		try {
			ConvertRegister(sdf, writeNullNumberAsZero);
			BeanUtils.populate(bean, map);
		} catch (Exception e) {
			logger.error("toBean Error 转换失败 " + e);
			throw new RuntimeException(e);
		}
		return bean;
	}

	/**
	 * 将 map 转为 javabean 内部调用toBean(map, cls, sdf, false);
	 *
	 * @param map
	 *            驼峰命名key的map
	 * @param cls
	 *            javabean
	 * @param sdf
	 *            日期格式
	 */
	public static <T> T toBean(Map<String, Object> map, Class<T> cls, SimpleDateFormat sdf) {
		return toBean(map, cls, sdf, false);
	}

	/**
	 * 将 map 转为 javabean 内部调用toBean(map, cls, new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), false);
	 *
	 * @param map
	 *            驼峰命名key的map
	 * @param cls
	 *            javabean
	 */
	public static <T> T toBean(Map<String, Object> map, Class<T> cls) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return toBean(map, cls, sdf, false);
	}

	private static void ConvertRegister(final SimpleDateFormat sdf, boolean writeNullNumberAsZero) {
		ConvertUtils.deregister();
		if (sdf != null) {
			Converter converter = new Converter() {
				@Override
				public Object convert(Class type, Object value) {
					if (value == null)
						return null;
					if (value instanceof Long) {
						try {
							return new Date((Long) value);
						} catch (Exception e) {
							throw new RuntimeException(e);// 异常链不能断
						}
					} else if (value instanceof String) {
						try {
							return sdf.parse((String) value);
						} catch (ParseException e) {
							return null;
//							throw new RuntimeException(e);// 异常链不能断
						}
					} else if (value instanceof Date) {
						return value;
					} else {
						logger.error("不是日期类型");
						System.out.println("不是日期类型");
					}
                    return value;
				}
			};
			ConvertUtils.register(converter, Date.class);
		}
		;
		if (!writeNullNumberAsZero) {
			Converter converter = new Converter() {
				@Override
				public Object convert(Class type, Object value) {
					if (value == null || "".equals(value))
						return null;
					try {
						value = String.valueOf(value);
						if (type.equals(Integer.class) 
								|| type.equals(Long.class) 
								|| type.equals(Short.class) 
								|| type.equals(Byte.class)
								) {
							value = ((String)value).replaceFirst("\\.\\d*", "");
						}
						value = type.getConstructor(String.class).newInstance(value);
					} catch (Exception e) {
						logger.error("不是数字类型");
						System.out.println("不是数字类型");
					}
					return value;
				}
			};
			ConvertUtils.register(converter, Integer.class);
			ConvertUtils.register(converter, Long.class);
			ConvertUtils.register(converter, Short.class);
			ConvertUtils.register(converter, Byte.class);
			ConvertUtils.register(converter, Float.class);
			ConvertUtils.register(converter, Double.class);
			ConvertUtils.register(converter, BigDecimal.class);
		}
		;
	}

	/**
	 * 驼峰命名 to 下划线命名
	 *
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 下划线命名 to 驼峰命名
	 *
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
		}
		return sb.toString();
	}

}
