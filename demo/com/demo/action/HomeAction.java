package com.demo.action;

import com.egg.action.core.ActionAnno.Action;
import com.egg.action.core.RequestContext;

@Action(value = "/")
public class HomeAction {

	public void index(RequestContext ctx) {
		ctx.writeHTML("<h1>Hello Home !</h1>");
	}

}
