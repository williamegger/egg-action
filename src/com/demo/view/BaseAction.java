package com.demo.view;

import java.util.Collection;
import java.util.Map;

public class BaseAction {

	protected boolean isBlank(CharSequence c) {
		return (c == null || c.toString().trim().isEmpty());
	}

	protected boolean isBlank(Collection<?> c) {
		return (c == null || c.isEmpty());
	}

	protected boolean isBlank(Map<?, ?> m) {
		return (m == null || m.isEmpty());
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

	protected String join(Object[] objs, String split) {
		if (objs == null || objs.length == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		split = (split == null) ? "" : split;
		for (int i = 0, len = objs.length; i < len; i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(objs[i]);
		}

		return sb.toString();
	}

}
