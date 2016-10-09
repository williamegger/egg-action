package com.egg.common.log;

import java.lang.reflect.Modifier;

import com.egg.common.log.support.SLF4JLog;

public class LogFactory {

	private static volatile Log LOG = null;

	public static Log getLog() {
		if (LOG == null) {
			synchronized (LogFactory.class) {
				if (LOG == null) {
					LOG = new SLF4JLog();
				}
			}
		}
		return LOG;
	}

	public static void setLog(String className) {
		try {
			if (className == null || className.isEmpty()) {
				return;
			}
			Class<?> logImplClass = Class.forName(className);
			int mod = logImplClass.getModifiers();
			if(Modifier.isPublic(mod)
					&& !Modifier.isAbstract(mod)
					&& !Modifier.isInterface(mod)
					&& Log.class.isAssignableFrom(logImplClass)) {
				setLog((Log)logImplClass.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setLog(Log log) {
		synchronized (LogFactory.class) {
			LOG = log;
		}
	}

}
