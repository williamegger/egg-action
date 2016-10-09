package com.egg.action.core;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.egg.action.core.handler.Handler;
import com.egg.common.log.LogFactory;
import com.egg.common.log.LogKit;
import com.egg.common.utils.PropUtil;

public class ActionServlet extends HttpServlet {

	private static final long serialVersionUID = 7083381600970856551L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Config.init(config);
		LogFactory.setLog(Config.getLog());
		ActionMapping.init(Config.getScanPackage());
		PropUtil.load(Config.getPropertiesName());
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding(Config.getCharset());
		resp.setCharacterEncoding(Config.getCharset());

		try {
			Handler handler = ActionMapping.getActionHandler(req);
			handler.handle(req, resp);
		} catch (Exception e) {
			LogKit.error(null, e);
		}
	}

}
