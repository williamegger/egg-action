package com.egg.action.handler;

import com.egg.action.core.ActionAnno.POST;
import com.egg.action.core.RequestContext;
import com.egg.action.upload.UploadFactory;
import com.egg.common.log.LogKit;
import com.egg.common.utils.MethodUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ActionHandler implements Handler {

	private Class<?> actionClass;
	private Object single;
	private Method method;

	private int methodParamCount = 0;
	private boolean isMustPOST = false;

	public ActionHandler(Class<?> actionClass, Object single, Method method) {
		this.actionClass = actionClass;
		this.single = single;
		this.method = method;
		methodParamCount = method.getParameterCount();
		isMustPOST = method.getAnnotation(POST.class) != null;
	}

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (LogKit.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(req.getRequestURI()).append("]");
			sb.append(" ---> ");
			sb.append("[").append(actionClass.getName()).append(".").append(method.getName()).append("()]");
			LogKit.debug(sb.toString());
		}

		try {
			RequestContext.create(req, resp, UploadFactory.buildUploadHandler());

			if (isMustPOST && !req.getMethod().equalsIgnoreCase("POST")) {
				RequestContext.get().write(req.getRequestURI() + " 必须以POST方式提交！");
				return;
			}

			MethodUtil.invoke(getActionInstance(), method, getMethodParameters(req, resp));
		} catch (Exception e) {
			LogKit.error(actionClass.getName() + "." + method.getName() + "()方法调用异常");
			throw e;
		} finally {
			RequestContext.end();
		}
	}

	private Object getActionInstance() throws InstantiationException, IllegalAccessException {
		if (single == null) {
			return actionClass.newInstance();
		} else {
			return single;
		}
	}

	private Object[] getMethodParameters(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Object[] result = null;
		if (methodParamCount > 0) {
			result = new Object[methodParamCount];

			Parameter[] params = method.getParameters();
			Class<?> paramClass;
			for (int i = 0; i < methodParamCount; i++) {
				paramClass = params[i].getType();
				if (paramClass == HttpServletRequest.class) {
					result[i] = req;
				} else if (paramClass == HttpServletResponse.class) {
					result[i] = resp;
				} else if (paramClass == RequestContext.class) {
					result[i] = RequestContext.get();
				} else {
					throw new Exception(actionClass.getName() + "." + method.getName() + "()方法参数错误");
				}
			}
		}
		return result;
	}
}
