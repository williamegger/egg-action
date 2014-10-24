package com.egg.view.ctrl;

import org.apache.commons.fileupload.FileItem;

import com.egg.commons.ImageUtil;
import com.egg.commons.Res;
import com.egg.servlet.ActionAnn.POST;
import com.egg.servlet.RequestContext;

public class UserAction extends BaseAction {

	public void index(RequestContext ctx) {
		ctx.write("Hello");
	}

	public void view(RequestContext ctx) {
		int uid = ctx.param("uid", 1);

		StringBuffer sb = new StringBuffer();
		sb.append("方法：view()").append("\r\n");
		sb.append("参数：").append("\r\n");
		sb.append("uid=" + uid).append("\r\n");

		ctx.write(sb);
	}

	@POST
	public void mustPost(RequestContext ctx) {
		StringBuffer sb = new StringBuffer();
		sb.append("方法：mustPost()").append("\r\n");
		sb.append("POST提交").append("\r\n");

		ctx.write(sb);
	}

	@POST
	public void setHead(RequestContext ctx) {
		try {
			FileItem item = ctx.formFile();
			if (item == null) {
				ctx.write("error,请选择图片文件");
				return;
			}
			if (item.getSize() == 0) {
				ctx.write("error,请选择图片文件");
				return;
			}

			if (!ImageUtil.isImage(item.getName())) {
				ctx.write("error,请上传图片文件");
				return;
			}

			String name = System.currentTimeMillis() + ImageUtil.ext(item.getName());
			String filepath = ctx.realPath() + "/upload/img/" + name;
			boolean b = ImageUtil.zoomFix(item.getInputStream(), filepath, 200, 200);
			if (!b) {
				ctx.write("error,图片上传失败");
				return;
			}

			ctx.redirect(Res.WEB_URL + "/upload/img/" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
