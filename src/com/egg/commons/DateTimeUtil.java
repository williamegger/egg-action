package com.egg.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

	private static final String DATE = "yyyy-MM-dd";
	private static final String DATETIME = "yyyy-MM-dd HH:mm";
	private static final String TIME = "HH:mm";

	/**
	 * yyyy-MM-dd
	 */
	public static String formatDate(Long times) {
		if (times == null || times <= 0) {
			return "";
		}
		return new SimpleDateFormat(DATE).format(times);
	}

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static String formatDateTime(Long times) {
		if (times == null || times <= 0) {
			return "";
		}
		return new SimpleDateFormat(DATETIME).format(times);
	}

	/**
	 * HH:mm
	 */
	public static String formatTime(Long times) {
		if (times == null || times <= 0) {
			return "";
		}
		return new SimpleDateFormat(TIME).format(times);
	}

	/**
	 * 按pattern格式化
	 */
	public static String format(Long times, String pattern) {
		try {
			if (times == null || times <= 0) {
				return "";
			}
			if (pattern == null || pattern.trim().length() == 0) {
				return new SimpleDateFormat(DATETIME).format(times);
			} else {
				return new SimpleDateFormat(pattern).format(times);
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到“xx之前”
	 * 
	 * <pre>
	 * 如：
	 * 5分钟前
	 * 5小时前
	 * 5天前
	 * 5月前
	 * 5年前
	 * </pre>
	 */
	public static String getAgo(Long times) {
		if (times == null || times <= 0) {
			return "";
		}
		long now = System.currentTimeMillis();
		long s = (now - times) / 1000; // 与现在相差的时间（秒）

		// < 1分钟：1分钟前
		if (s < 60) {
			return "1分钟前";
		}
		// < 1小时：x分钟前
		else if (s < 60 * 60) {
			return (s / 60) + "分钟前";
		}
		// < 1天：x小时前
		else if (s < 60 * 60 * 24) {
			return (s / 60 / 60) + "小时前";
		}
		// < 1月（30天）：x天前
		else if (s < 60 * 60 * 24 * 30) {
			return (s / 60 / 60 / 24) + "天前";
		}
		// < 1年（365天）：x月前
		else if (s < 60 * 60 * 24 * 365) {
			return (s / 60 / 60 / 24 / 30) + "月前";
		}
		// > 1年（365天）：x年前
		else {
			return (s / 60 / 60 / 24 / 365) + "年前";
		}
	}

	/**
	 * yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return formatDate(date.getTime());
	}

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return formatDateTime(date.getTime());
	}

	/**
	 * HH:mm
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return formatTime(date.getTime());
	}

	/**
	 * 按pattern格式化
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		return format(date.getTime(), pattern);
	}

	public static boolean isToday(Date d) {
		return isSameday(d, new Date());
	}

	public static boolean isSameday(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		}

		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		int y1 = c1.get(Calendar.YEAR);
		int m1 = c1.get(Calendar.MONTH);
		int dd1 = c1.get(Calendar.DATE);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		int y2 = c2.get(Calendar.YEAR);
		int m2 = c2.get(Calendar.MONTH);
		int dd2 = c2.get(Calendar.DATE);

		return (y1 == y2 && m1 == m2 && dd1 == dd2);
	}

}
