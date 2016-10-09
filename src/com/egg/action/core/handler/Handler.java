package com.egg.action.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

	public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
