package pers.cjr.chatbot.nb.biz.utils;

import org.springframework.stereotype.Service;

/**
 * 唯一ID 生成器 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
 * 
 * 网站使用workerId = 1, 转换服务器使用 workerId = 2
 */
@Service
public class IdGenUtil {
	private final static long idepoch = 1403084147459L;
	// 机器标识位数
	private final static long workerIdBits = 5L;
	// 业务标识位数
	private final static long datacenterIdBits = 5L;
	// 机器ID最大值
	private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 业务ID最大值
	private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	// 毫秒内自增位
	private final static long sequenceBits = 12L;
	// 机器ID偏左移12位
	private final static long workerIdShift = sequenceBits;
	// 业务ID左移17位
	private final static long datacenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒左移22位
	private final static long timestampLeftShift = sequenceBits + workerIdBits
			+ datacenterIdBits;

	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

	private static long lastTimestamp = -1L;

	private long sequence = 0L;
	private final long workerId;
	private final long datacenterId;

	public IdGenUtil(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					"worker Id can't be greater than %d or less than 0");
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					"datacenter Id can't be greater than %d or less than 0");
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}
	
	public IdGenUtil() {
		this.workerId = 1;
		this.datacenterId = 0;
	}

	public IdGenUtil(long workerId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					"worker Id can't be greater than %d or less than 0");
		}
		this.workerId = workerId;
		this.datacenterId = 0;
	}

	public long generate() {
		return this.nextId(false, 0);
	}

	public long generate(long busid) {
		return this.nextId(true, busid);
	}
	
	private synchronized long nextId(boolean isPadding, long busid) {
		long timestamp = timeGen();
		long paddingnum = datacenterId;
		if (isPadding) {
			paddingnum = busid;
		}
		if (timestamp < lastTimestamp) {
			try {
				throw new Exception(
						"Clock moved backwards.  Refusing to generate id for "
								+ (lastTimestamp - timestamp) + " milliseconds");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tailNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		long nextId = ((timestamp - idepoch) << timestampLeftShift)
				| (paddingnum << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;

		return nextId;
	}
	

	private long tailNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}