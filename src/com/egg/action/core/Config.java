package com.egg.action.core;

import com.egg.action.upload.UploadFactory;

import javax.servlet.FilterConfig;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取ActionServlet的初始化参数
 */
public final class Config {

	private static final String KEY_SCAN_PACKAGE = "scanPackage";
	private static final String KEY_UPLOAD_HANDLER = "uploadHandler";
	private static final String KEY_PROPERTIES_NAME = "propertiesName";
	private static final String KEY_CHARSET = "charset";
	private static final String KEY_LOG = "log";

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final Map<String, String> conf = new HashMap<String, String>();

	/**
	 * 初始化配置信息
	 */
	protected static void init(FilterConfig config) {
		Enumeration<String> names = config.getInitParameterNames();
		String k, v;
		while (names.hasMoreElements()) {
			k = names.nextElement();
			v = config.getInitParameter(k).trim();
			conf.put(k, v);
		}
	}

	/**
	 * 扫描Action的包名，默认扫描所有包
	 */
	public static String getScanPackage() {
		return get(KEY_SCAN_PACKAGE, "");
	}

	/**
	 * 得到上传文件处理类的类名，默认值"com.egg.action.upload.support.CommonsUploadHandler"
	 * 
	 * <pre>
	 * 可选值：
	 * 1.{@link com.egg.action.upload.support.CommonsUploadHandler
	 * com.egg.action.upload.support.CommonsUploadHandler}
	 * 使用commons.uploadfile包。默认该类为上传处理类。
	 * 2.{@link com.egg.action.upload.support.Servlet3UploadHandler
	 * com.egg.action.upload.support.Servlet3UploadHandler}
	 * 使用HttpServlet中的Part。
	 * 1)需要Servlet3+
	 * 2)在web.xml的servlet中配置multipart-config
	 * </pre>
	 */
	public static String getUploadHandler() {
		return get(KEY_UPLOAD_HANDLER, UploadFactory.DEFAULT_UPLOAD_HANDLER);
	}

	/**
	 * 得到要加载的.properties文件名
	 */
	public static String getPropertiesName() {
		return get(KEY_PROPERTIES_NAME, "");
	}

	/**
	 * 得到字符编码，默认值"UTF-8"
	 */
	public static String getCharset() {
		return get(KEY_CHARSET, DEFAULT_CHARSET);
	}

	public static String getLog() {
		return get(KEY_LOG, "");
	}

	private static String get(String key) {
		return conf.get(key);
	}

	private static String get(String key, String def) {
		String value = get(key);
		return (value == null || value.isEmpty() ? def : value);
	}
}
