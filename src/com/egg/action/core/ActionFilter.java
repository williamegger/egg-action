package com.egg.action.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.egg.action.handler.Handler;
import com.egg.action.render.VelocityRender;
import com.egg.common.log.LogFactory;
import com.egg.common.log.LogKit;
import com.egg.common.utils.PropUtil;

public class ActionFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        LogKit.info("===========================");
        LogKit.info("Start init ActionFilter");
        LogKit.info("===========================");
        try {
            Config.init(config);
            LogKit.info("Config init            [OK]");
            LogFactory.setLog(Config.getLog());
            LogKit.info("Log init               [OK]");
            ActionMapping.init(Config.getScanPackage());
            LogKit.info("ActionMap init         [OK]");
            PropUtil.load(Config.getPropertiesName());
            LogKit.info("PropUtil init          [OK]");
            VelocityRender.init(config.getServletContext());
            LogKit.info("VelocityRender init    [OK]");
        } catch (Exception e) {
            LogKit.error(null, e);
        }
        LogKit.info("===========================");
        LogKit.info("Init ActionFilter OVER");
        LogKit.info("===========================");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding(Config.getCharset());
        resp.setCharacterEncoding(Config.getCharset());

        if (isStaticRes(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

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

    private boolean isStaticRes(String uri) {
        if (uri == null || uri.isEmpty()) {
            return false;
        }
        uri = uri.toLowerCase();
        return (uri.endsWith(".jpg")
                || uri.endsWith(".gif")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".png")
                || uri.endsWith(".bmp")
                || uri.endsWith(".ico")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".html")
                || uri.endsWith(".htm")
        );
    }
}
