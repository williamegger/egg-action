package com.demo.view.action;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;

import com.demo.commons.Res;
import com.egg.eaction.ActionAnn.POST;
import com.egg.eaction.RequestContext;
import com.egg.eaction.view.BaseAction;

/**
 * 测试类 [/api/demo/方法名]
 */
public class DemoAction extends BaseAction {

	public void index(RequestContext ctx) {
		ctx.write("You using EAction-0.0.1");
	}

	public void view(RequestContext ctx) {
		StringBuffer sb = new StringBuffer();
		Map<String, String[]> map = ctx.request().getParameterMap();
		if (isNotBlank(map)) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				sb.append(key + "=" + join(map.get(key), "&" + key + "=")).append("\r\n");
			}
		}

		ctx.write("[/api/demo/view] 参数：\r\n");
		ctx.write(sb);
	}

	@POST
	public void upload(RequestContext ctx) {
		try {
			FileItem uploadFile = ctx.formFile();
			File file = new File(ctx.realPath() + Res.UPLOAD_PATH + uploadFile.getName());
			file.getParentFile().mkdirs();
			uploadFile.write(file);
			ctx.write("Upload OK [" + file.getPath() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			ctx.write("Upload Error");
		}
	}

	private String join(Object[] objs, String split) {
		if (objs == null || objs.length == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		split = (split == null) ? "" : split;
		for (int i = 0, len = objs.length; i < len; i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(objs[i]);
		}

		return sb.toString();
	}
}
