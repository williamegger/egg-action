package com.egg.action.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.egg.action.upload.FilePart;
import com.egg.action.upload.UploadHandler;
import com.egg.common.log.LogKit;

public class RequestContext {

	private static final String UTF8 = "UTF-8";

	private static final ThreadLocal<RequestContext> CTX = new ThreadLocal<RequestContext>();

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession session;
	private UploadHandler uploadHandler = null;
	private Map<String, String[]> parameterMap = null;
	private Map<String, Cookie> cookies;
	private String serverUrl = null;

	protected RequestContext(HttpServletRequest req, HttpServletResponse resp, UploadHandler uploadHandler) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession(false);
		this.uploadHandler = uploadHandler;
		this.parameterMap = new HashMap<String, String[]>();
		this.parameterMap.putAll(req.getParameterMap());
	}

	public static synchronized RequestContext create(HttpServletRequest req, HttpServletResponse resp,
			UploadHandler uploadHandler) {
		RequestContext ctx = CTX.get();
		if (ctx == null) {
			ctx = new RequestContext(req, resp, uploadHandler);
			ctx.begin();
			CTX.set(ctx);
		}
		return ctx;
	}

	public static synchronized RequestContext get() {
		return CTX.get();
	}

	private void begin() {
		if (uploadHandler != null && uploadHandler.isMultipart(req)) {
			Map<String, String[]> multipartParamsMap = uploadHandler.parseParams(req);
			if (multipartParamsMap != null && !multipartParamsMap.isEmpty()) {
				this.parameterMap.putAll(multipartParamsMap);
			}
		}
		attr("base", contextPath());
	}

	public static synchronized void end() {
		RequestContext ctx = CTX.get();
		if (ctx != null) {
			ctx.uploadHandler = null;
			ctx.parameterMap = null;
			ctx.cookies = null;
			ctx = null;
			CTX.set(null);
			CTX.remove();
		}
	}

	public HttpServletRequest request() {
		return req;
	}

	public HttpServletResponse response() {
		return resp;
	}

	public HttpSession session() {
		return session(true);
	}

	public HttpSession session(boolean isCreate) {
		if (session == null && isCreate) {
			session = req.getSession(true);
		}
		return session;
	}

	public String sessionId() {
		return session().getId();
	}

	public String contextPath() {
		return req.getContextPath();
	}

	public String serverUrl() {
		if (serverUrl == null) {
			String scheme = req.getScheme();
			String serverName = req.getServerName();
			int serverPort = req.getServerPort();
			String contextPath = req.getContextPath();

			StringBuilder sb = new StringBuilder();
			sb.append(scheme).append("://");
			sb.append(serverName);
			if (serverPort != 80) {
				sb.append(":").append(serverPort);
			}
			sb.append(contextPath);
			serverUrl = sb.toString();
		}
		return serverUrl;
	}

	public String uri() {
		return req.getRequestURI();
	}

	public String realPath() {
		// 修改为支持tomcat6，得到真实路径
		// return req.getServletContext().getRealPath("/");
		return req.getSession().getServletContext().getRealPath("/");
	}

	public String method() {
		return req.getMethod();
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public String[] params(String key) {
		return parameterMap.get(key);
	}

	public List<Integer> paramsInt(String key) {
		String[] strs = params(key);
		if (strs == null || strs.length == 0) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		Integer i = null;
		for (String str : strs) {
			try {
				i = Integer.parseInt(str);
				list.add(i);
			} catch (Exception e) {
			}
		}
		return list;
	}

	public String param(String key) {
		String result = null;
		if (parameterMap.containsKey(key)) {
			String[] pArray = parameterMap.get(key);
			if (pArray != null && pArray.length > 0) {
				result = pArray[0];
			}
		}
		if (LogKit.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(key).append(":").append(result);
			LogKit.debug(sb.toString());
		}
		return result;
	}

	public String param(String key, boolean decode) {
		String val = param(key);
		if (decode) {
			if (val != null && !val.trim().isEmpty()) {
				try {
					val = URLDecoder.decode(val, UTF8);
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return val;
	}

	public int param(String key, int def) {
		try {
			String val = param(key);
			return (val == null ? def : Integer.valueOf(val));
		} catch (Exception e) {
			return def;
		}
	}

	public long param(String key, long def) {
		try {
			String val = param(key);
			return (val == null ? def : Long.valueOf(val));
		} catch (Exception e) {
			return def;
		}
	}

	public float param(String key, float def) {
		try {
			String val = param(key);
			return (val == null ? def : Float.valueOf(val));
		} catch (Exception e) {
			return def;
		}
	}

	public double param(String key, double def) {
		try {
			String val = param(key);
			return (val == null ? def : Double.valueOf(val));
		} catch (Exception e) {
			return def;
		}
	}

	public Date param(String key, String pattern) {
		try {
			String dateStr = param(key);
			SimpleDateFormat sd = new SimpleDateFormat(pattern);
			return sd.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	public String header(String key) {
		return req.getHeader(key);
	}

	public void header(String key, String value) {
		resp.addHeader(key, value);
	}

	public void header(String key, int value) {
		resp.addIntHeader(key, value);
	}

	public void header(String key, long value) {
		resp.setDateHeader(key, value);
	}

	public void contentTypeHTML() {
		resp.setContentType("text/html; charset=utf-8");
	}

	public void contentTypePlain() {
		resp.setContentType("text/plain; charset=utf-8");
	}

	public void contentType(String type) {
		resp.setContentType(type);
	}

	public void contentLength(int len) {
		resp.setContentLength(len);
	}

	public void noCache() {
		header("Pragma", "no-cache");
		header("Cache-Control", "no-cache");
		header("Expires", 0L);
	}

	public void write(CharSequence str) {
		try {
			if (str == null) {
				return;
			}
			contentTypePlain();
			resp.getWriter().write(str.toString());
		} catch (IOException e) {
			LogKit.error(null, e);
		}
	}

	public void writeHTML(CharSequence str) {
		try {
			if (str == null) {
				return;
			}
			contentTypeHTML();
			resp.getWriter().write(str.toString());
		} catch (IOException e) {
			LogKit.error(null, e);
		}
	}

	public void gotoHome() {
		String path = contextPath();
		if (path.length() == 0) {
			path = "/";
		}
		redirect(contextPath());
	}

	public void goto404() {
		try {
			resp.sendError(404);
		} catch (IOException e) {
		}
	}

	public void redirect(String uri) {
		try {
			resp.sendRedirect(uri);
		} catch (IOException e) {
			LogKit.error("redirect方法异常：[" + uri + "]", e);
		}
	}

	public void forward(String uri) {
		try {
			req.getRequestDispatcher(uri).forward(req, resp);
		} catch (ServletException e) {
			LogKit.error("forward方法异常：[" + uri + "]", e);
		} catch (IOException e) {
			LogKit.error("forward方法异常：[" + uri + "]", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T attr(String key) {
		return (T) req.getAttribute(key);
	}

	public void attr(String key, Object value) {
		req.setAttribute(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T sessionAttr(String key) {
		if (session == null) {
			return null;
		}
		return (T) session.getAttribute(key);
	}

	public void sessionAttr(String key, Object value) {
		if (session == null) {
			session = req.getSession();
		}
		session.setAttribute(key, value);
	}

	public void removeSessionAttr(String key) {
		sessionAttr(key, null);
		session.removeAttribute(key);
	}

	public boolean isAjax() {
		return ("XMLHttpRequest".equalsIgnoreCase(header("x-requested-with")));
	}

	public boolean isMultipart() {
		return (uploadHandler != null ? uploadHandler.isMultipart(req) : false);
	}

	public List<FilePart> fileParts() {
		return (uploadHandler != null ? uploadHandler.parseFiles(req) : null);
	}

	public FilePart filePart() {
		List<FilePart> fileParts = fileParts();
		if (fileParts != null && !fileParts.isEmpty()) {
			return fileParts.get(0);
		}
		return null;
	}

	public Map<String, Cookie> cookie() {
		if (cookies != null) {
			return cookies;
		}
		cookies = new HashMap<String, Cookie>();
		Cookie[] cs = req.getCookies();
		if (null != cs && cs.length > 0) {
			for (Cookie c : cs) {
				cookies.put(c.getName(), c);
			}
		}
		return cookies;
	}

	public Cookie cookie(String name) {
		if (name == null) {
			return null;
		}
		Map<String, Cookie> map = cookie();
		if (null == map || !map.containsKey(name)) {
			return null;
		}
		return map.get(name);
	}

	public void cookie(String name, String value, int seconds, boolean isHttpOnly) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(seconds);
		c.setPath("/");
		c.setHttpOnly(isHttpOnly);
		resp.addCookie(c);

		cookie().put(name, c);

	}

	public void removeCookie(String name) {
		cookie(name, null, 0, false);
	}

	public String ip() {
		String ip = null;
		try {
			ip = req.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = req.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = req.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = req.getRemoteAddr();
			}
		} catch (Exception e) {
		}
		return ip;
	}

}
