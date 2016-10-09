package com.egg.action.upload;

import com.egg.action.core.Config;
import com.egg.common.log.LogKit;

/**
 * 上传控制类创建工厂
 */
public class UploadFactory {

	public static final String DEFAULT_UPLOAD_HANDLER = "com.egg.action.upload.support.CommonsUploadHandler";

	/**
	 * 创建上传处理类
	 * 
	 * <pre>
	 * 使用Config.getUploadHandler()返回值创建上传处理类
	 * </pre>
	 */
	public static UploadHandler buildUploadHandler() {
		UploadHandler handler = null;

		try {
			handler = (UploadHandler) Class.forName(Config.getUploadHandler()).newInstance();
		} catch (Exception e) {
			LogKit.error("创建UploadHandler实例异常，web.xml中的uploadHandler参数配置错误：[" + Config.getUploadHandler() + "]", e);
			handler = buildDefaultUploadHandler();
		}

		return handler;
	}

	/**
	 * 创建默认上传处理类
	 * 
	 * <pre>
	 * 使用{@link com.egg.action.upload.support.CommonsUploadHandler CommonsUploadHandler}类
	 * </pre>
	 */
	public static UploadHandler buildDefaultUploadHandler() {
		UploadHandler handler = null;
		try {
			handler = (UploadHandler) Class.forName(DEFAULT_UPLOAD_HANDLER).newInstance();
		} catch (Exception e) {
			LogKit.error("创建默认UploadHandler实例异常：[" + DEFAULT_UPLOAD_HANDLER + "]", e);
		}
		return handler;
	}

}
