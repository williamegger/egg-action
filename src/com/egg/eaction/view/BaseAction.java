package com.egg.eaction.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.egg.eaction.RequestContext;

public class BaseAction extends AbstractBase {

	private static final Log LOG = LogFactory.getLog(BaseAction.class);

	@Override
	protected HttpServletRequest getRequest() {
		return  RequestContext.get().request();
	}

	// ------------
	// Commons
	// ------------
	protected JSONObject paramJSON(String name) {
		RequestContext ctx = RequestContext.get();
		String param = ctx.param(name);

		if (isBlank(param)) {
			String url = ctx.request().getRequestURL().toString();
			String query = ctx.request().getQueryString();
			query = (query == null) ? "" : "?" + query;
			String method = ctx.method();
			String ip = ctx.ip();

			StringBuffer sb = new StringBuffer();
			sb.append(BaseAction.class.getName() + ".paramJSON(String) : 方法异常，参数不能为空：");
			sb.append("[ IP : ").append(ip).append(" ] ");
			sb.append("[ ParameterName : ").append(name).append(" ] ");
			sb.append("[ ").append(method).append(" ] ");
			sb.append("[ ").append(url).append(query).append(" ] ");
			NullPointerException e = new NullPointerException(sb.toString());
			LOG.error(e);

			return null;
		}
		return JSONObject.parseObject(param);
	}

}
