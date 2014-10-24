package com.egg.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.egg.servlet.ActionAnn.POST;

public class ActionServlet extends HttpServlet {

	private static final long serialVersionUID = -6191626884139403356L;

	private static final Logger LOG = LoggerFactory.getLogger(ActionServlet.class);

	private static final Map<String, String> PKS = new HashMap<String, String>();
	private static final Map<String, Object> ACTIONS = new HashMap<String, Object>();
	private static final Map<String, Method> METHODS = new HashMap<String, Method>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		if (PKS.isEmpty()) {
			Enumeration<String> names = config.getInitParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				PKS.put(name, config.getInitParameter(name));
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, false);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, true);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp, boolean isPost) {
		RequestContext context = RequestContext.create(req, resp);

		try {
			String contextPath = context.contextPath();
			String uri = context.uri();
			if (contextPath.length() > 1) {
				uri = uri.substring(contextPath.length());
			}

			String[] parts = StringUtils.split(uri, '/');
			if (parts.length < 2) {
				context.gotoHome();
				return;
			}

			Object action = loadAction(parts[0], parts[1]);
			if (action == null) {
				context.gotoHome();
				return;
			}

			Method method = loadMethod(action, parts.length > 2 ? parts[2] : "index");
			if (method == null) {
				context.gotoHome();
				return;
			}

			if (!isPost) {
				POST must_post = method.getAnnotation(ActionAnn.POST.class);
				if (must_post != null) {
					LOG.error("Error. [" + uri + "] must be POST method.");
					return;
				}
			}

			int len = method.getParameterTypes().length;
			method.setAccessible(true);
			switch (len) {
			case 0:
				method.invoke(action);
				break;
			case 1:
				method.invoke(action, context);
				break;
			default:
				context.gotoHome();
				break;
			}
		} catch (IllegalArgumentException e) {
			LOG.error(".process", e);
		} catch (IllegalAccessException e) {
			LOG.error(".process", e);
		} catch (InvocationTargetException e) {
			LOG.error(".process", e);
		} catch (Exception e) {
			LOG.error(".process", e);
		} finally {
			RequestContext.end();
		}
	}

	protected Object loadAction(String shortPKName, String shortClassName) {
		String key = shortPKName + "." + shortClassName;
		Object ctrl = ACTIONS.get(key);
		if (ctrl == null && PKS.containsKey(shortPKName)) {
			String className = PKS.get(shortPKName) + StringUtils.capitalize(shortClassName) + "Action";
			try {
				ctrl = Class.forName(className).newInstance();
				if (!ACTIONS.containsKey(key)) {
					synchronized (ACTIONS) {
						ACTIONS.put(key, ctrl);
					}
				}
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
				LOG.error(".loadAction", e);
			} catch (IllegalAccessException e) {
				LOG.error(".loadAction", e);
			}
		}
		return ctrl;
	}

	protected Method loadMethod(Object ctrl, String methodName) {
		String key = ctrl.getClass().getName() + "." + methodName;
		Method m = METHODS.get(key);
		if (m == null) {
			Method[] methods = ctrl.getClass().getMethods();
			for (Method method : methods) {
				if (method.getModifiers() == Modifier.PUBLIC && method.getName().equals(methodName)) {
					if (!METHODS.containsKey(key)) {
						synchronized (METHODS) {
							METHODS.put(key, method);
						}
					}
					return method;
				}
			}
		}
		return m;
	}

}
