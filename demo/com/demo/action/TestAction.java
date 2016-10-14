package com.demo.action;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.egg.action.core.ActionAnno.Action;
import com.egg.action.core.ActionAnno.POST;
import com.egg.action.core.RequestContext;
import com.egg.action.upload.FilePart;
import com.egg.common.log.LogKit;
import com.egg.common.utils.FileUtil;

@Action("test")
// @Action(value = "test", isSingle = false)
public class TestAction {

	private int count;

	public void index() {
		RequestContext.get().write("This's TestAction Index");
	}

	public void count() {
		RequestContext.get().write("Count = " + (count++));
	}

	public void params(RequestContext ctx) {
		ctx.writeHTML(getParamsHtml());
	}

	@POST
	public void mustBePost(RequestContext ctx) {
		ctx.write("执行mustBePost()方法。method=" + ctx.method());
	}

	public void upload(RequestContext ctx) {
		try {
			StringBuilder html = getParamsHtml();
			FilePart filePart = ctx.filePart();
			if (filePart == null) {
				html.append("<h3>上传文件为空</h3>");
			} else {
				String fileName = System.currentTimeMillis() + FileUtil.getExt(filePart.getFileName());
				String filePath = ctx.realPath() + "upload/" + fileName;
				FileUtil.save(filePart.getInputStream(), filePath);
				
				html.append("<table>");
				html.append("<tr><th colspan=\"2\">Upload File</th></tr>");
				html.append("<tr><td>FileName</td>");
				html.append("<td>").append(filePart.getFileName()).append("</td></tr>");
				html.append("<tr><td>ContentType</td>");
				html.append("<td>").append(filePart.getContentType()).append("</td></tr>");
				html.append("<tr><td>Size</td>");
				html.append("<td>").append(filePart.getSize()).append("</td></tr>");
				html.append("<tr><td>FormName</td>");
				html.append("<td>").append(filePart.getFormName()).append("</td></tr>");
				html.append("<tr><td colspan=\"2\"><img src=\"" + ctx.contextPath() + "/upload/" + fileName
						+ "\" style=\"max-width:100%;\" /></td></tr>");
				html.append("</table>");
			}
			ctx.writeHTML(html);
		} catch (Exception e) {
			LogKit.error(null, e);
		}
	}

	private StringBuilder getParamsHtml() {
		RequestContext ctx = RequestContext.get();
		StringBuilder html = new StringBuilder();
		Map<String, String[]> map = ctx.getParameterMap();
		Set<Entry<String, String[]>> set = map.entrySet();

		html.append("<table>");
		html.append("<tr><th colspan=\"2\">Parameters</th></tr>");
		for (Entry<String, String[]> entry : set) {
			html.append("<tr>");
			html.append("<td>").append(entry.getKey()).append("</td>");
			html.append("<td>").append(Arrays.toString(entry.getValue())).append("</td>");
			html.append("</tr>");
		}
		html.append("</table>");
		return html;
	}

}
