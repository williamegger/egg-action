package com.egg.action.core;

import com.egg.action.handler.Handler;
import com.egg.common.log.LogFactory;
import com.egg.common.log.LogKit;
import com.egg.common.utils.PropUtil;
import com.egg.action.render.VelocityRender;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActionFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        Config.init(config);
        LogFactory.setLog(Config.getLog());
        ActionMapping.init(Config.getScanPackage());
        PropUtil.load(Config.getPropertiesName());
        VelocityRender.init(config.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding(Config.getCharset());
        resp.setCharacterEncoding(Config.getCharset());

        boolean hasHandle = false;
        try {
            Handler handler = ActionMapping.getActionHandler(req);
            if (handler != null) {
                hasHandle = true;
                handler.handle(req, resp);
            }
        } catch (Exception e) {
            LogKit.error(null, e);
        }

        if (!hasHandle) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
