package pers.cjr.chatbot.nb.biz.utils;


import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class MathUtil {

	public static void main(String[] args) {
		double[] testData = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		System.out.println("最大值：" + getMax(testData));
		System.out.println("最小值：" + getMin(testData));
		System.out.println("计数：" + getCount(testData));
		System.out.println("求和：" + getSum(testData));
		System.out.println("求平均：" + getAverage(testData));
		System.out.println("方差：" + getVariance(testData));
		System.out.println("标准差：" + getStandardDiviation(testData));

	}

	/**
	 * 求给定双精度数组中值的最大值
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果,如果输入值不合法，返回为-1
	 */
	public static double getMax(double[] inputData) {
		if (inputData == null || inputData.length == 0)
			return 0;
		int len = inputData.length;
		double max = inputData[0];
		for (int i = 0; i < len; i++) {
			if (max < inputData[i])
				max = inputData[i];
		}
		return max;
	}

	/**
	 * 求求给定双精度数组中值的最小值
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果,如果输入值不合法，返回为-1
	 */
	public static double getMin(double[] inputData) {
		if (inputData == null || inputData.length == 0)
			return 0;
		int len = inputData.length;
		double min = inputData[0];
		for (int i = 0; i < len; i++) {
			if (min > inputData[i])
				min = inputData[i];
		}
		return min;
	}

	/**
	 * 求给定双精度数组中值的和
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getSum(double[] inputData) {
		if (inputData == null || inputData.length == 0)
			return 0;
		int len = inputData.length;
		double sum = 0;
		for (int i = 0; i < len; i++) {
			sum = sum + inputData[i];
		}

		return sum;
	}
	
	/**
	 * 求给定双精度数组中值的和
	 *
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getSum(List<Double> inputData) {
		if (CollectionUtils.isEmpty(inputData))
			return 0;
		int len = inputData.size();
		double sum = 0;
		for (int i = 0; i < len; i++) {
			sum = sum + inputData.get(i);
		}

		return sum;

	}


	/**
	 * 求给定双精度数组中值的数目
	 * @param inputData
	 * @return
	 */
	public static int getCount(double[] inputData) {
		if (inputData == null)
			return 0;

		return inputData.length;
	}

	/**
	 * 求给定双精度数组中值的平均值
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getAverage(double[] inputData) {
		if (inputData == null || inputData.length == 0)
			return 0;
		int len = inputData.length;
		double result;
		result = getSum(inputData) / len;

		return result;
	}

	/**
	 * 求给定双精度数组中值的平均值
	 *
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getAverage(List<Double> inputData) {
		if(CollectionUtils.isEmpty(inputData)){
			return 0;
		}
		int len = inputData.size();
		double result;
		result = getSum(inputData) / len;
		return result;
	}

	/**
	 * 求给定双精度数组中值的平方和
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getSquareSum(double[] inputData) {
		if (inputData == null || inputData.length == 0)
			return 0;
		int len = inputData.length;
		double sqrsum = 0.0;
		for (int i = 0; i < len; i++) {
			sqrsum = sqrsum + inputData[i] * inputData[i];
		}

		return sqrsum;
	}

	/**
	 * 求给定双精度数组中值的方差
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getVariance(double[] inputData) {
		int count = getCount(inputData);
		if (count == 0) return 0; 
		double sqrsum = getSquareSum(inputData);
		double average = getAverage(inputData);
		double result;
		result = (sqrsum - count * average * average) / count;

		return result;
	}

	/**
	 * 求给定双精度数组中值的标准差
	 * 
	 * @param inputData
	 *            输入数据数组
	 * @return 运算结果
	 */
	public static double getStandardDiviation(double[] inputData) {
		double result;
		// 绝对值化很重要
		result = Math.sqrt(Math.abs(getVariance(inputData)));

		return result;

	}
	
	public static double getStandardDiviation(List<Double> inputData) {
		double[] arr = new double[inputData.size()];
		for (int i = 0, len = inputData.size(); i < len; i++) {
			arr[i] = inputData.get(i);
		}
		return getStandardDiviation(arr);
	}

	/**
	 * 计算区分度
	 * @param inputData
	 * @param totalScore
	 * @return
	 */
	public static double getDiscrimination(double[] inputData, double totalScore) {
		if (totalScore == 0) {
			return 0;
		}

		double highGroupData = 0;
		double lowGroupData = 0;

		int count = inputData.length;

		int highGroupOffset = (int) (count * 0.27);
		if (highGroupOffset == 0) {
			return 0;
		}
		int lowGroupOffset = (int) (count * 0.73);

		for (int i = 0; i < count; i++) {

			double data = inputData[i];

			if (i <= highGroupOffset) {
				highGroupData += data;
			} else if (i >= lowGroupOffset) {
				lowGroupData += data;
			}
		}

		return (highGroupData / highGroupOffset - lowGroupData / (count - lowGroupOffset)) / totalScore;

	}

	/**
	 * 计算难度
	 * 
	 * @param inputData
	 * @param totalScore
	 * @return
	 */
	public static double getDifficulty(double[] inputData, double totalScore) {
		if (totalScore == 0) {
			return 0;
		}
		return 1 - (getAverage(inputData) / totalScore);
	}

	/**
	 * 计算得分率
	 * 
	 * @param inputData
	 * @param totalScore
	 * @return
	 */
	public static double getScoreRate(double[] inputData, double totalScore) {
		if (totalScore == 0) {
			return 0;
		}
		return getAverage(inputData) / totalScore;
	}

	/**
	 * 阿拉伯数字转化为中文数字
	 * 
	 * @param n
	 * @param big_case
	 * @return
	 */
	public static String numberChinese(long n, boolean big_case) {
		final char big_units[] = { '万', '亿', '兆', '京', '垓', '秭', '穰', '沟', '涧', '正', '载' },
				BigCase_SmallUnits[] = { '拾', '佰', '仟' },
				BigCase_HanNumbers[] = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' },
				SmallCase_SmallUnits[] = { '十', '百', '千' },
				SmallCase_HanNumbers[] = { '〇', '一', '二', '三', '四', '五', '六', '七', '八', '九' },
				small_units[] = big_case ? BigCase_SmallUnits : SmallCase_SmallUnits,
				han_numbers[] = big_case ? BigCase_HanNumbers : SmallCase_HanNumbers;
		boolean sign = true;
		StringBuffer buf = new StringBuffer();
		if (n < 0) {
			sign = false;
			n = -n;
		}
		StringBuffer buf_wan = new StringBuffer();
		byte bu_count = 0;
		while (n > 0) {
			short wan = (short) (n % 10000);
			if (bu_count > 0 && wan > 0)
				buf.insert(0, big_units[bu_count - 1]);
			byte su_count = 0;
			buf_wan.setLength(0);
			boolean zero_written = true;
			boolean full_thou = wan >= 1000;
			while (wan > 0) {
				byte d = (byte) (wan % 10);
				if (su_count > 0 && d > 0)
					buf_wan.insert(0, small_units[su_count - 1]);
				if (d > 0) {
					buf_wan.insert(0, han_numbers[d]);
					zero_written = false;
				} else if (!zero_written) {
					buf_wan.insert(0, han_numbers[0]);
					zero_written = true;
				}
				su_count++;
				wan /= 10;
			}
			n /= 10000;
			if (!zero_written && n > 0 && !full_thou)
				buf_wan.insert(0, han_numbers[0]);
			buf.insert(0, buf_wan);
			bu_count++;
		}
		// buf.insert(0,sign?'正':'负');
		// buf.append('整');
		return buf.toString();
	}

	/**
	 * 掌握程度
	 * 
	 * @param rate
	 * @return
	 */
	public static double getMasterLevel(double rate) {
		if (rate > 0.9) {
			return 10;
		} else if (rate > 0.8) {
			return 9;
		} else if (rate > 0.7) {
			return 8;
		} else if (rate > 0.6) {
			return 7;
		} else if (rate > 0.5) {
			return 6;
		} else if (rate > 0.4) {
			return 5;
		} else if (rate > 0.3) {
			return 4;
		} else if (rate > 0.2) {
			return 3;
		} else if (rate > 0.1) {
			return 2;
		} else if (rate > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
	 */

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		} 
		if (v2 == 0){
			return 0;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 四舍五入double类型，如果为小数位0就舍去小数点
	 * @param value
	 * @param scal 保留的小数位
	 * @return
	 */
	public  static  String format(double value,int scal){
		BigDecimal b=new BigDecimal(value);
		double d=  b.setScale(scal, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (d%1.0==0){
			return String.valueOf((long)d);
		}else {
			return String.valueOf(d);
		}
	}

	
	
	/**
	 * 最大值
	 * @param inputData
	 * @return 
	 */
	public static<T> BigDecimal getMaxToBigDecimal(List<T> inputData) {
		if(inputData.isEmpty())
			return new BigDecimal(0);
		int len = inputData.size();
		
		T t = inputData.get(0);
		BigDecimal max = new BigDecimal(t.toString());
		
		
		for (int i = 0; i < len; i++) {
			BigDecimal value = new BigDecimal(inputData.get(i).toString());
			if (max.compareTo(value) == -1){
				max = value;
			}
		}
		return max;
	}
	
	

	/**
	 * 最小值
	 * @param inputData
	 * @return
	 */
	public static<T> BigDecimal getSumToBigDecimal(List<T> inputData) {
		if (inputData.isEmpty())
			return new BigDecimal(0);
		
		BigDecimal sum = new BigDecimal(0);
		for (T t : inputData) {
			sum = sum.add(new BigDecimal(t.toString()));
		}

		return sum;
	}
	
	


	/**
	 * 平均值
	 * @param inputData
	 * @param scale	保留几位小数，四舍五入
	 * @return
	 */
	public static<T> BigDecimal getAverageToBigDecimal(List<T> inputData, int scale) {
		if(CollectionUtils.isEmpty(inputData)){
			return new BigDecimal(0);
		}
		BigDecimal result = new BigDecimal(0);
		
		result = getSumToBigDecimal(inputData).divide(new BigDecimal(inputData.size()), scale, BigDecimal.ROUND_HALF_UP);
		
		return result;
	}
}
