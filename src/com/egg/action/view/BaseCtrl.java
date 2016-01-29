package com.egg.action.view;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.egg.action.FacesTool;

public class BaseCtrl extends AbstractBase implements Serializable {

	protected FacesTool faces = FacesTool.getInstance();

	@Override
	protected HttpServletRequest getRequest() {
		return faces.request();
	}

	// ------------
	// Commons
	// ------------
	/**
	 * 断言字符串非空
	 * 
	 * @param str 字符串
	 * @param msg 错误提示
	 */
	protected boolean assertNotBlank(String str, String msg) {
		if (isNotBlank(str)) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertNotNull(Object obj, String msg) {
		if (obj != null) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMax(String str, int max, String msg) {
		if (str == null || str.length() <= max) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMax(int num, int max, String msg) {
		if (num <= max) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMax(double num, double max, String msg) {
		if (num <= max) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMin(String str, int min, String msg) {
		if (str == null || str.length() >= min) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMin(int num, int min, String msg) {
		if (num >= min) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertMin(double num, double min, String msg) {
		if (num >= min) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertEquals(String str, String str1, String msg) {
		str = (str == null) ? "" : str;
		str1 = (str1 == null) ? "" : str1;

		if (str.equals(str1)) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertEqualsIg(String str, String str1, String msg) {
		str = (str == null) ? "" : str;
		str1 = (str1 == null) ? "" : str1;

		if (str.equalsIgnoreCase(str1)) {
			return true;
		} else {
			faces.error(msg);
			return false;
		}
	}

	protected boolean assertTrue(boolean b, String msg) {
		if (!b) {
			faces.error(msg);
		}
		return b;
	}

}
