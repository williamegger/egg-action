package com.egg.common.log;

public interface Log {

	public boolean isDebugEnabled();

	public boolean isInfoEnabled();

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();

	public void debug(String msg);

	public void debug(String msg, Throwable e);

	public void info(String msg);

	public void info(String msg, Throwable e);

	public void warn(String msg);

	public void warn(String msg, Throwable e);

	public void error(String msg);

	public void error(String msg, Throwable e);

}
