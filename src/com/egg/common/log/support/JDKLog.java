package com.egg.common.log.support;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.egg.common.log.Log;

public class JDKLog implements Log {

	private Logger log;

	public JDKLog() {
		log = Logger.getLogger(JDKLog.class.getName());
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isLoggable(Level.FINE);
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isLoggable(Level.INFO);
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isLoggable(Level.WARNING);
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isLoggable(Level.SEVERE);
	}

	@Override
	public void debug(String msg) {
		log.logp(Level.FINE, "", null, msg);
	}

	@Override
	public void debug(String msg, Throwable e) {
		log.logp(Level.FINE, "", null, msg, e);
	}

	@Override
	public void info(String msg) {
		log.logp(Level.INFO, "", null, msg);
	}

	@Override
	public void info(String msg, Throwable e) {
		log.logp(Level.INFO, "", null, msg, e);
	}

	@Override
	public void warn(String msg) {
		log.logp(Level.WARNING, "", null, msg);
	}

	@Override
	public void warn(String msg, Throwable e) {
		log.logp(Level.WARNING, "", null, msg, e);
	}

	@Override
	public void error(String msg) {
		log.logp(Level.SEVERE, "", null, msg);
	}

	@Override
	public void error(String msg, Throwable e) {
		log.logp(Level.SEVERE, "", null, msg, e);
	}

}
