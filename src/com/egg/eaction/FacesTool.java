package com.egg.eaction;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FacesTool extends RequestContext {

	private static final Log LOG = LogFactory.getLog(FacesTool.class);
	private static final String KEY_OPT = "optSuccess";

	public static synchronized FacesTool getInstance() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext extContext = facesContext.getExternalContext();
		HttpServletRequest req = (HttpServletRequest) extContext.getRequest();
		HttpServletResponse resp = (HttpServletResponse) extContext.getResponse();
		FacesTool instance = new FacesTool(req, resp);
		return instance;
	}

	public FacesTool(HttpServletRequest req, HttpServletResponse resp) {
		super(req, resp);
	}

	public void addOptStatus(boolean success) {
		addCallbackParam(KEY_OPT, success);
	}

	public void addCallbackParam(String name, Object obj) {
		org.primefaces.context.RequestContext ctx = org.primefaces.context.RequestContext.getCurrentInstance();
		ctx.addCallbackParam(name, obj);
	}

	public void info(String msg) {
		info(null, msg);
	}

	public void info(String clientId, String msg) {
		if (msg != null && !msg.trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(clientId,
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}
	}

	public void error(String msg) {
		error(null, msg);
	}

	public void error(String clientId, String msg) {
		if (msg != null && !msg.trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(clientId,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	@Override
	public void redirect(String uri) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath() + uri);
		} catch (Exception e) {
			String errorMsg = FacesTool.class + ".redirect():方法异常: " + e.getMessage();
			LOG.error(errorMsg, e);
		}
	}

	@Override
	public void gotoHome() {
		try {
			String path = contextPath();
			if (path.length() == 0) {
				path = "/";
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (Exception e) {
			String errorMsg = FacesTool.class + ".gotoHome():方法异常: " + e.getMessage();
			LOG.error(errorMsg, e);
		}
	}

}
