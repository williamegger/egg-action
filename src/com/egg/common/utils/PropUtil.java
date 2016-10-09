package com.egg.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.egg.common.log.LogKit;

public class PropUtil {

	private static volatile Properties props = null;

	/**
	 * 加载资源文件
	 */
	public static synchronized boolean load(String name) {
		if (name == null || name.isEmpty()) {
			return false;
		}
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (in == null) {
			LogKit.error("加载资源文件错误，文件不存在[" + name + "]");
			return false;
		}

		try {
			props = new Properties();
			props.load(in);
			return true;
		} catch (Exception e) {
			LogKit.error("加载资源文件异常[" + name + "]", e);
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	public static String get(String key) {
		if (props == null) {
			return null;
		}
		return props.getProperty(key);
	}

	public static String get(String key, String def) {
		String val = get(key);
		return (val != null ? def : val);
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int getInt(String key, int def) {
		try {
			String val = get(key);
			return Integer.valueOf(val);
		} catch (Exception e) {
			return def;
		}
	}

	public static double getDouble(String key) {
		return getDouble(key, 0D);
	}

	public static double getDouble(String key, double def) {
		try {
			String val = get(key);
			return Double.valueOf(val);
		} catch (Exception e) {
			return def;
		}
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static boolean getBoolean(String key, boolean def) {
		String val = get(key);
		if ("true".equalsIgnoreCase(val)) {
			return true;
		} else if ("false".equalsIgnoreCase(val)) {
			return false;
		} else {
			return def;
		}
	}

}
