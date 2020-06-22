package com.fengtu.data.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author yls
 * @Description 时间工具类
 * @Date 2020/3/26 14:16
 * @return
 **/
public class DateUtils {

	/**
	 * @Author yls
	 * @Description 获取当前时间  yyyy-MM-dd
	 * @Date 2020/4/14 12:03
	 * @param
	 * @return java.lang.String
	 **/
	public static String getNowDateTime(){
		return toString(new Date(), null);
	}
	
	/** 
	 * @Description 获取当前系统时间
	 * @return java.util.Date 日期类型
	 */
	public static Date getCurrentTime() {
		return new Date();
	}
	
	/** 
	 * @Description 获取当前系统时间字符串
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String currTimeToString() {
		return toString(getCurrentTime(), null);
	}
	
	/**
	 * @Description 将日期类型数据转换成字符串
	 * @param d java.util.Date 日期类型
	 * @param format String 转换格式(如:yyyy-MM-dd HH:mm:ss)
	 * @return String 日期字符串
	 */
	public static String toString(Date d, String format) {
		if(format==null || "".equals(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
	/**
	 * @Description 将java.util.Date转换成java.sql.Date
	 * @param d java.util.Date 日期类型数据
	 * @return java.sql.Date 数据库日期类型数据
	 */
	public static java.sql.Date toSqlDate(Date d) {
		if(d != null) {
			return new java.sql.Date(d.getTime());
		}
		return null;
	}
	/**
	 * @Description 将java.sql.Date转为java.util.Date类型
	 * @param sqlDate java.sql.Date 时间类型
	 * @return java.util.Date 时间类型
	 */
	public static Date sqlToUtil(Date sqlDate) {
		Date utilDate = null;
		if (sqlDate != null) {
			utilDate = new Date(sqlDate.getTime());
		}
		return utilDate;
	}
	/**
	 * @Description 将java.util.Date转换成java.sql.Timestamp
	 * @param d java.util.Date 日期类型数据
	 * @return java.sql.Timestamp 数据库日期时间类型数据
	 */
	public static Timestamp toTimestamp(Date d) {
		if(d != null) {
			return new Timestamp(d.getTime());
		}
		return null;
	}
	
	/**
	 * @Description 将字符串转换成日期类型数据
	 * @param s String 日期字符串
	 * @param format String 转换格式(如:yyyy-MM-dd HH:mm:ss)
	 * @return java.util.Date 日期类型
	 * @throws ParseException
	 */
	public static Date parseDate(String s, String format) throws ParseException {
		if(format==null || "".equals(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(s);
	}
	
	/**
	 * @Description 将字符串转换成数据库日期类型数据
	 * @param s String 日期字符串
	 * @param format String 转换格式(如:yyyy-MM-dd HH:mm:ss)
	 * @return java.sql.Date 数据库日期类型
	 * @throws ParseException
	 */
	public static java.sql.Date parseSqlDate(String s, String format) throws ParseException {
		Date d = parseDate(s, format);
		return new java.sql.Date(d.getTime());
	}
	
	/**
	 * @Description 将字符串转换成数据库日期时间类型数据
	 * @param s String 日期字符串
	 * @param format String 转换格式(如:yyyy-MM-dd HH:mm:ss)
	 * @return java.sql.Timestamp 数据库日期时间类型数据
	 * @throws ParseException
	 */
	public static Timestamp parseTimestamp(String s, String format) throws ParseException {
		Date d = parseDate(s, format);
		return new Timestamp(d.getTime());
	}
	
	/**
	 * @Description 获取指定格式的日期字符串
	 * @param d java.util.Date 日期类型
	 * @return String 日期字符串（格式:yyyy-MM-dd）
	 */
	public static String getDate(Date d) {
		return toString(d, "yyyy-MM-dd");
	}
	
	/**
	 * @Description 获取指定格式的时间字符串
	 * @param d java.util.Date 日期类型
	 * @return String 时间字符串（格式:HH:mm:ss）
	 */
	public static String getTime(Date d) {
		return toString(d, "HH:mm:ss");
	}

	/**
	 * @Description 获取指定格式的日期时间字符串
	 * @param d java.util.Date 日期类型
	 * @return String 日期时间字符串（格式:yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime(Date d) {
		return toString(d, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * @Description 返回日期中的年份
	 * @param date java.util.Date 日期
	 * @return int 年份
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * @Description 返回日期中的月份
	 * @param date java.util.Date 日期
	 * @return int 月份
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * @Description 返回日期中的日份
	 * @param date java.util.Date 日期
	 * @return int 日份
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * @Description 返回日期中的小时
	 * @param date java.util.Date 日期
	 * @return int 小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @Description 返回日期中的分钟
	 * @param date java.util.Date 日期
	 * @return int 分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @Description 返回日期中的秒钟
	 * @param date java.util.Date 日期
	 * @return int 秒钟
	 */
	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}
	
	/**
	 * @Description 返回日期中的毫秒
	 * @param date java.util.Date 日期
	 * @return int 毫秒
	 */
	public static long getMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * @Description 指定日期加上天数
	 * @param d java.util.Date 日期类型数据
	 * @param day int 天数
	 * @return java.util.Date 日期类型数据
	 */
	public static Date addDate(Date d, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getMillis(d) + ((long)day) * 24 * 3600 * 1000);
		return calendar.getTime();
	}


	/**
	 * @Description 指定日期加上天数
	 * @param d java.util.Date 日期类型数据
	 * @param day int 天数
	 * @return java.util.Date 日期类型数据
	 */
	public static Date subtractDate(Date d, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getMillis(d) - ((long)day) * 24 * 3600 * 1000);
		return calendar.getTime();
	}


	/**
	 * @Description 将两个日期相减,得到相差的天数
	 * @param start java.util.Date 开始日期
	 * @param end java.util.Date 结束日期
	 * @return int 相差的天数
	 */
	public static int diffDate(Date start, Date end) {
		return (int)((getMillis(start) - getMillis(end)) / (24 * 3600 * 1000));
	}


	public static void main(String[] args) {
		System.err.println(getNowDateTime());
	}
	
}