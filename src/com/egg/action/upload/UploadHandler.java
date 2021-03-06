package com.egg.action.upload;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理上传文件的接口
 */
public interface UploadHandler {

	public boolean isMultipart(HttpServletRequest req);

	public List<FilePart> parseFiles(HttpServletRequest req);

	public Map<String, String[]> parseParams(HttpServletRequest req);

}
