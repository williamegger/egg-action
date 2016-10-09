package com.egg.common.log;

public class LogKit {

	public static boolean isDebugEnabled() {
		return LogFactory.getLog().isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return LogFactory.getLog().isInfoEnabled();
	}

	public static boolean isWarnEnabled() {
		return LogFactory.getLog().isWarnEnabled();
	}

	public static boolean isErrorEnabled() {
		return LogFactory.getLog().isErrorEnabled();
	}

	public static void debug(String msg) {
		LogFactory.getLog().debug(buildMessage(Thread.currentThread().getStackTrace()[2], msg));
	}

	public static void debug(String msg, Throwable e) {
		LogFactory.getLog().debug(buildMessage(Thread.currentThread().getStackTrace()[2], msg), e);
	}

	public static void info(String msg) {
		LogFactory.getLog().info(buildMessage(Thread.currentThread().getStackTrace()[2], msg));
	}

	public static void info(String msg, Throwable e) {
		LogFactory.getLog().info(buildMessage(Thread.currentThread().getStackTrace()[2], msg), e);
	}

	public static void warn(String msg) {
		LogFactory.getLog().warn(buildMessage(Thread.currentThread().getStackTrace()[2], msg));
	}

	public static void warn(String msg, Throwable e) {
		LogFactory.getLog().warn(buildMessage(Thread.currentThread().getStackTrace()[2], msg), e);
	}

	public static void error(String msg) {
		LogFactory.getLog().error(buildMessage(Thread.currentThread().getStackTrace()[2], msg));
	}

	public static void error(String msg, Throwable e) {
		LogFactory.getLog().error(buildMessage(Thread.currentThread().getStackTrace()[2], msg), e);
	}

	private static String buildMessage(StackTraceElement ele, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(ele.getClassName());
		sb.append(".");
		sb.append(ele.getMethodName());
		sb.append("(");
		sb.append(ele.getFileName());
		sb.append(":");
		sb.append(ele.getLineNumber());
		sb.append(") - ");
		if (msg != null) {
			sb.append(msg);
		}
		return sb.toString();
	}
}
