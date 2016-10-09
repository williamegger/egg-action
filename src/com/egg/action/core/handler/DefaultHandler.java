package com.egg.action.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultHandler implements Handler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getServletContext().getNamedDispatcher("default").forward(request, response);
	}

}
