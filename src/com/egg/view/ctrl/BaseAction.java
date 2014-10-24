package com.egg.view.ctrl;

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

}
