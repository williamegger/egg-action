package com.demo.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.egg.action.core.ActionFilter;

public class MyActionFilter extends ActionFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		// config something
	}

}
