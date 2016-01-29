package com.egg.action.view;

import java.util.Map;

import javax.servlet.http.HttpSession;

public class LoginTool {

	public static final String KEY_USER = "LOGIN_USER";

	// ------------
	// User
	// ------------
	public static void loginUser(HttpSession session, Map user) {
		setSessionAttr(session, KEY_USER, user);
	}

	public static void logoutUser(HttpSession session) {
		setSessionAttr(session, KEY_USER, null);
	}

	public static Map getLoginUser(HttpSession session) {
		return (Map) session.getAttribute(KEY_USER);
	}

	public static boolean isLoginUser(HttpSession session) {
		return (getLoginUser(session) != null);
	}

	public static Integer getLoginUserId(HttpSession session) {
		Map user = getLoginUser(session);
		return (user == null) ? null : (Integer) user.get("userId");
	}

	// ------------
	// get set
	// ------------
	private static void setSessionAttr(HttpSession session, String key, Object val) {
		if (session == null) {
			return;
		}
		session.setAttribute(key, val);
		if (val == null) {
			session.removeAttribute(key);
		}
	}
}
