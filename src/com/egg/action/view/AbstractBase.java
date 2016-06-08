package com.egg.action.view;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBase {

	protected abstract HttpServletRequest getRequest();

	// ------------
	// User
	// ------------
	protected void loginUser(Map user) {
		LoginTool.loginUser(getRequest().getSession(), user);
	}

	protected void logoutUser() {
		LoginTool.logoutUser(getRequest().getSession());
	}

	protected Map getLoginUser() {
		return LoginTool.getLoginUser(getRequest().getSession());
	}

	protected boolean isLoginUser() {
		return LoginTool.isLoginUser(getRequest().getSession());
	}

	protected Integer getLoginUserId() {
		return LoginTool.getLoginUserId(getRequest().getSession());
	}

	// ------------
	// Commons
	// ------------
	public <T> T form2Bean(Class<T> beanClass) {
		try {
			T bean = beanClass.newInstance();
			BeanUtils.populate(bean, getRequest().getParameterMap());
			return bean;
		} catch (Exception e) {
			return null;
		}
	}

	protected boolean isBlank(CharSequence c) {
		return (c == null || c.toString().trim().isEmpty());
	}

	protected boolean isBlank(Collection<?> c) {
		return (c == null || c.isEmpty());
	}

	protected boolean isBlank(Map<?, ?> m) {
		return (m == null || m.isEmpty());
	}

	protected boolean isBlank(Object[] array) {
		return (array == null || array.length == 0);
	}

	protected boolean isNotBlank(Object[] array) {
		return (!isBlank(array));
	}

	protected boolean isNotBlank(CharSequence c) {
		return (!isBlank(c));
	}

	protected boolean isNotBlank(Collection<?> c) {
		return (!isBlank(c));
	}

	protected boolean isNotBlank(Map<?, ?> m) {
		return (!isBlank(m));
	}

	protected String substring(String str, int max) {
		if (str == null) {
			return null;
		}
		if (max > 0 && str.length() > max) {
			str = str.substring(0, max);
		}
		return str;
	}

	// ------------
	// LOG
	// ------------
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	protected void logE(String msg, Throwable e) {
		log.error(msg, e);
	}

	protected void logI(Object msg) {
		log.info(msg == null ? null : msg.toString());
	}
}
