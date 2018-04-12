package pers.cjr.chatbot.nb.biz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志工具类
 */
public class LoggerUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

	private static String msgPrefix= "-------------=============";
	
	private static Boolean actionTimeMillisRecord = false;
	
	private static int mapTimeout = 1000*60*5;	//日志超时时间5分钟
	private static int mapMaxSize = 100;		//最大日志队列
	private static List<String> loggerTimeKeyList = new ArrayList<String>();	//日志记录key列表
	private static Map<String,RecordTiming> loggerTimeMap = new HashMap<String, RecordTiming>();	//日志记录表
	
	//行为开始
	public static String startAction(String message) {
		return startAction(message,null);
	}
	public static <T> String startAction(String message,Class<T> cls) {
		String key = message;
		if(StringUtil.isEmpty(message)) {
			logger.debug(msgPrefix+"【{}】开始,时间戳:{}","未设置行为名称",System.currentTimeMillis());
		}else {

			if(actionTimeMillisRecord) {
				logger.debug(msgPrefix+"【{}】开始,时间戳:{}",message,System.currentTimeMillis());
			}
			
			if(cls != null) {
				key += "_" + cls.toString();
			}
			RecordTiming timing = new RecordTiming(System.currentTimeMillis(), message);
			if(loggerTimeMap.get(key) != null) {
				key += "_" + System.currentTimeMillis();
				logger.debug(msgPrefix+"【{}】行为已存在,改key为:{}",message,key);
			}
			
			if(loggerTimeMap.size() >= mapMaxSize) {	//释放队列
				releaseMapMemory();	
			}
			
			loggerTimeMap.put(key, timing);
			loggerTimeKeyList.add(key);
		}
		return key;
	}

	//行为结束
	public static void endAction(String key) {
		endAction(key,true);
	}
	
	public static void endAction(String key,boolean destoryFlag) {
		
		RecordTiming timing = loggerTimeMap.get(key);
		
		if(timing == null || timing.getStartTime() <=0 ) {
			logger.debug(msgPrefix+"【{}】未找到或未开始,时间戳:{}",key,System.currentTimeMillis());
		}else{
			
			timing.setEndTime(System.currentTimeMillis());
			timing.setCostTime(((double)(timing.getEndTime() - timing.getStartTime())) / 1000);
			if(actionTimeMillisRecord) {
				logger.debug(msgPrefix+"【{}】结束,时间戳:{}",timing.getMessage(),System.currentTimeMillis());
			}
			
			logger.debug(msgPrefix+"【{}】行为共耗时:{}",timing.getMessage(),timing.getCostTime());
			
			if(destoryFlag) {
				loggerTimeMap.remove(key);
				loggerTimeKeyList.remove(key);
			}
		}
	}
	
	//判断释放需要释放内存
	public static void releaseMapMemory() {
		
		List<String> destoryKeyList = new ArrayList<String>();
		//判断是否有超过5分钟的,存在则销毁
		for (String key : loggerTimeMap.keySet()) {
			long currTime = System.currentTimeMillis();
			RecordTiming timing = loggerTimeMap.get(key);
			if(currTime - timing.getStartTime() > mapTimeout) {
				destoryKeyList.add(key);
			}
		}
		
		if(destoryKeyList.size() > 0) {	//仍然队列超出
			logger.debug(msgPrefix+"【日志计时队列超出,开始销毁{}秒前日志...】,剩余队列数量:{},销毁列表:{}",mapTimeout,loggerTimeMap.size()-(loggerTimeMap.size()-destoryKeyList.size()),JSONUtil.objectToJsonStr(destoryKeyList));
		}else {
			int destorySize = (int) (mapMaxSize * 0.2  < 1 ? 1 : mapMaxSize * 0.2);
			for (int i = 0; i < loggerTimeKeyList.size(); i++) {
				if(i < destorySize) {
					String key = loggerTimeKeyList.get(i);
					destoryKeyList.add(key);
				}
			}
			logger.debug(msgPrefix+"【日志计时队列仍超出,开始销毁前{}条日志...】,剩余队列数量:{},销毁列表:{}",destorySize,loggerTimeMap.size()-destorySize,JSONUtil.objectToJsonStr(destoryKeyList));
		}
		//销毁
		for (int i = 0; i < destoryKeyList.size(); i++) {
			loggerTimeMap.remove(destoryKeyList.get(i));
			loggerTimeKeyList.remove(destoryKeyList.get(i));
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		LoggerUtil.startAction("行为1");
		LoggerUtil.startAction("行为2");
		Thread.sleep(1000);
		LoggerUtil.startAction("行为3");
		LoggerUtil.startAction("行为4");
		LoggerUtil.startAction("行为5");
	}
}

class RecordTiming{
	private long startTime = 0;		//开始时间戳
	private long endTime = 0;		//结束时间戳
	private double costTime = 0;		//总耗时(秒)
	private String message = "";	//行为描述
	
	public RecordTiming(long startTime,String message) {
		this.startTime = startTime;
		this.message = message;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public double getCostTime() {
		return costTime;
	}

	public void setCostTime(double costTime) {
		this.costTime = costTime;
	}
	
}