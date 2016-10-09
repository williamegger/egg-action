package com.egg.common.log.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.egg.common.log.Log;

public class SLF4JLog implements Log {

	private Logger log;

	public SLF4JLog() {
		log = LoggerFactory.getLogger(SLF4JLog.class);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	@Override
	public void debug(String msg) {
		log.debug(msg);
	}

	@Override
	public void debug(String msg, Throwable e) {
		log.debug(msg, e);
	}

	@Override
	public void info(String msg) {
		log.info(msg);
	}

	@Override
	public void info(String msg, Throwable e) {
		log.info(msg, e);
	}

	@Override
	public void warn(String msg) {
		log.warn(msg);
	}

	@Override
	public void warn(String msg, Throwable e) {
		log.warn(msg, e);
	}

	@Override
	public void error(String msg) {
		log.error(msg);
	}

	@Override
	public void error(String msg, Throwable e) {
		log.error(msg, e);
	}

}
