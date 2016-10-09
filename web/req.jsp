<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	StringBuffer sb = new StringBuffer();

	sb.append("<table>");
	
	sb.append("<tr><th colspan=\"2\">属性</th></tr>");
	// getMethod
	sb.append("<tr>");
	sb.append("<td>").append("getMethod").append("</td>");
	sb.append("<td>").append(request.getMethod()).append("</td>");
	sb.append("</tr>");
	// getRequestedSessionId
	sb.append("<tr>");
	sb.append("<td>").append("getRequestedSessionId").append("</td>");
	sb.append("<td>").append(request.getRequestedSessionId()).append("</td>");
	sb.append("</tr>");
	// isRequestedSessionIdValid
	sb.append("<tr>");
	sb.append("<td>").append("isRequestedSessionIdValid").append("</td>");
	sb.append("<td>").append(request.isRequestedSessionIdValid()).append("</td>");
	sb.append("</tr>");
	// getPathTranslated
	sb.append("<tr>");
	sb.append("<td>").append("getPathTranslated").append("</td>");
	sb.append("<td>").append(request.getPathTranslated()).append("</td>");
	sb.append("</tr>");
	// getAuthType
	sb.append("<tr>");
	sb.append("<td>").append("getAuthType").append("</td>");
	sb.append("<td>").append(request.getAuthType()).append("</td>");
	sb.append("</tr>");
	// getPathInfo
	sb.append("<tr>");
	sb.append("<td>").append("getPathInfo").append("</td>");
	sb.append("<td>").append(request.getPathInfo()).append("</td>");
	sb.append("</tr>");
	// getContextPath
	sb.append("<tr>");
	sb.append("<td>").append("getContextPath").append("</td>");
	sb.append("<td>").append(request.getContextPath()).append("</td>");
	sb.append("</tr>");
	// getQueryString
	sb.append("<tr>");
	sb.append("<td>").append("getQueryString").append("</td>");
	sb.append("<td>").append(request.getQueryString()).append("</td>");
	sb.append("</tr>");
	// getRemoteUser
	sb.append("<tr>");
	sb.append("<td>").append("getRemoteUser").append("</td>");
	sb.append("<td>").append(request.getRemoteUser()).append("</td>");
	sb.append("</tr>");
	// getRequestURI
	sb.append("<tr>");
	sb.append("<td>").append("getRequestURI").append("</td>");
	sb.append("<td>").append(request.getRequestURI()).append("</td>");
	sb.append("</tr>");
	// getRequestURL
	sb.append("<tr>");
	sb.append("<td>").append("getRequestURL").append("</td>");
	sb.append("<td>").append(request.getRequestURL()).append("</td>");
	sb.append("</tr>");
	// getServletPath
	sb.append("<tr>");
	sb.append("<td>").append("getServletPath").append("</td>");
	sb.append("<td>").append(request.getServletPath()).append("</td>");
	sb.append("</tr>");
	// isRequestedSessionIdFromCookie
	sb.append("<tr>");
	sb.append("<td>").append("isRequestedSessionIdFromCookie").append("</td>");
	sb.append("<td>").append(request.isRequestedSessionIdFromCookie()).append("</td>");
	sb.append("</tr>");
	// isRequestedSessionIdFromURL
	sb.append("<tr>");
	sb.append("<td>").append("isRequestedSessionIdFromURL").append("</td>");
	sb.append("<td>").append(request.isRequestedSessionIdFromURL()).append("</td>");
	sb.append("</tr>");
	// isRequestedSessionIdFromUrl
	sb.append("<tr>");
	sb.append("<td>").append("isRequestedSessionIdFromUrl").append("</td>");
	sb.append("<td>").append(request.isRequestedSessionIdFromUrl()).append("</td>");
	sb.append("</tr>");
	// getScheme
	sb.append("<tr>");
	sb.append("<td>").append("getScheme").append("</td>");
	sb.append("<td>").append(request.getScheme()).append("</td>");
	sb.append("</tr>");
	// getProtocol
	sb.append("<tr>");
	sb.append("<td>").append("getProtocol").append("</td>");
	sb.append("<td>").append(request.getProtocol()).append("</td>");
	sb.append("</tr>");
	// getContentLength
	sb.append("<tr>");
	sb.append("<td>").append("getContentLength").append("</td>");
	sb.append("<td>").append(request.getContentLength()).append("</td>");
	sb.append("</tr>");
	// getContentType
	sb.append("<tr>");
	sb.append("<td>").append("getContentType").append("</td>");
	sb.append("<td>").append(request.getContentType()).append("</td>");
	sb.append("</tr>");
	// getCharacterEncoding
	sb.append("<tr>");
	sb.append("<td>").append("getCharacterEncoding").append("</td>");
	sb.append("<td>").append(request.getCharacterEncoding()).append("</td>");
	sb.append("</tr>");
	// getServerName
	sb.append("<tr>");
	sb.append("<td>").append("getServerName").append("</td>");
	sb.append("<td>").append(request.getServerName()).append("</td>");
	sb.append("</tr>");
	// getServerPort
	sb.append("<tr>");
	sb.append("<td>").append("getServerPort").append("</td>");
	sb.append("<td>").append(request.getServerPort()).append("</td>");
	sb.append("</tr>");
	// getRemoteAddr
	sb.append("<tr>");
	sb.append("<td>").append("getRemoteAddr").append("</td>");
	sb.append("<td>").append(request.getRemoteAddr()).append("</td>");
	sb.append("</tr>");
	// getRemoteHost
	sb.append("<tr>");
	sb.append("<td>").append("getRemoteHost").append("</td>");
	sb.append("<td>").append(request.getRemoteHost()).append("</td>");
	sb.append("</tr>");
	// getRemotePort
	sb.append("<tr>");
	sb.append("<td>").append("getRemotePort").append("</td>");
	sb.append("<td>").append(request.getRemotePort()).append("</td>");
	sb.append("</tr>");
	// getLocalName
	sb.append("<tr>");
	sb.append("<td>").append("getLocalName").append("</td>");
	sb.append("<td>").append(request.getLocalName()).append("</td>");
	sb.append("</tr>");
	// getLocalAddr
	sb.append("<tr>");
	sb.append("<td>").append("getLocalAddr").append("</td>");
	sb.append("<td>").append(request.getLocalAddr()).append("</td>");
	sb.append("</tr>");
	// getLocalPort
	sb.append("<tr>");
	sb.append("<td>").append("getLocalPort").append("</td>");
	sb.append("<td>").append(request.getLocalPort()).append("</td>");
	sb.append("</tr>");
	// isAsyncStarted
	sb.append("<tr>");
	sb.append("<td>").append("isAsyncStarted").append("</td>");
	sb.append("<td>").append(request.isAsyncStarted()).append("</td>");
	sb.append("</tr>");
	// isAsyncSupported
	sb.append("<tr>");
	sb.append("<td>").append("isAsyncSupported").append("</td>");
	sb.append("<td>").append(request.isAsyncSupported()).append("</td>");
	sb.append("</tr>");
	// isSecure
	sb.append("<tr>");
	sb.append("<td>").append("isSecure").append("</td>");
	sb.append("<td>").append(request.isSecure()).append("</td>");
	sb.append("</tr>");

	// getHeader
	sb.append("<tr><th colspan=\"2\">header</th></tr>");
	Enumeration<String> headerNames = request.getHeaderNames();
	String k, v;
	while(headerNames.hasMoreElements()) {
		k = headerNames.nextElement();
		v = request.getHeader(k);
		sb.append("<tr>");
		sb.append("<td>").append(k).append("</td>");
		sb.append("<td>").append(v).append("</td>");
		sb.append("</tr>");
	}
	
	sb.append("</table>");
	
	pageContext.setAttribute("html", sb.toString());

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<title>JSP</title>
<style type="text/css">
body {
	padding: 0;
	margin: 0;
	background-color: #FEFEFE;
	font-family: consolas;
}
.wrap {
}
</style>
</head>
<body>
	<div class="wrap">
		${html}
	</div>

</body>
</html>
