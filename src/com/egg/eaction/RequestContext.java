package com.egg.eaction;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestContext implements Serializable {

	private static final long serialVersionUID = -7999141900997547849L;
	private static final Log LOG = LogFactory.getLog(RequestContext.class);
	private static final String UTF8 = "utf-8";

	private static final ThreadLocal<RequestContext> CTX = new ThreadLocal<RequestContext>();

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession session;
	private boolean isMultipart = false;
	private List<FileItem> fileItems;
	private Map<String, Cookie> cookies;
	private String serverUrl = null;

	protected RequestContext(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession(false);
	}

	public static synchronized RequestContext create(HttpServletRequest req, HttpServletResponse resp) {
		LOG.debug("create RequestContext ...");
		RequestContext ctx = CTX.get();
		if (ctx == null) {
			ctx = new RequestContext(req, resp);
			ctx.begin();
			CTX.set(ctx);
		}
		return ctx;
	}

	public static synchronized RequestContext get() {
		return CTX.get();
	}

	private void begin() {
		loadUploadInfo();
		attr("base", contextPath());
	}

	public static synchronized void end() {
		LOG.debug("end RequestContext ...");
		RequestContext ctx = CTX.get();
		if (ctx != null) {
			ctx.req = null;
			ctx.resp = null;
			ctx.session = null;
			ctx.fileItems = null;
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

	public String id() {
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

			StringBuffer sb = new StringBuffer();
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
		return req.getServletContext().getRealPath("/");
	}

	public String method() {
		return req.getMethod();
	}

	public String[] params(String key) {
		return req.getParameterValues(key);
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
		return req.getParameter(key);
	}

	public String param(String key, boolean decode) {
		String val = param(key);
		if (decode) {
			if (StringUtils.isNotBlank(val)) {
				try {
					val = URLDecoder.decode(val, UTF8);
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return val;
	}

	public int param(String key, int def) {
		return NumberUtils.toInt(param(key), def);
	}

	public long param(String key, long def) {
		return NumberUtils.toLong(param(key), def);
	}

	public float param(String key, float def) {
		return NumberUtils.toFloat(param(key), def);
	}

	public double param(String key, double def) {
		return NumberUtils.toDouble(param(key), def);
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
		return resp.getHeader(key);
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
			log(".write", e);
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
			log(".writeHTML", e);
		}
	}

	public void gotoHome() {
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
			log(".forward", e);
		}
	}

	public void forward(String uri) {
		try {
			req.getRequestDispatcher(uri).forward(req, resp);
		} catch (ServletException e) {
			log(".forward", e);
		} catch (IOException e) {
			log(".forward", e);
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
		return (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase(
				"XMLHttpRequest"));
	}

	public boolean isMultipart() {
		return isMultipart;
	}

	public List<FileItem> fileItems() {
		return fileItems;
	}

	public FileItem formFile() {
		if (this.fileItems != null) {
			for (FileItem item : this.fileItems) {
				if (!item.isFormField()) {
					return item;
				}
			}
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
			e.printStackTrace();
		}
		return ip;
	}

	public String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	// ============================================================== private method
	@SuppressWarnings("unchecked")
	private void loadUploadInfo() {
		isMultipart = ServletFileUpload.isMultipartContent(req);
		if (!isMultipart) {
			return;
		}

		File tmpFile = new File(realPath() + "/upload/tmp");
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		factory.setRepository(tmpFile);

		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			fileItems = upload.parseRequest(req);
		} catch (FileUploadException e) {
			log(".loadUploadInfo()", e);
		}
	}

	private void log(String msg, Throwable e) {
		LOG.error(RequestContext.class.getName() + msg + ":方法出现异常:", e);
	}

}
