package com.egg.action.upload.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.egg.action.upload.FilePart;
import com.egg.action.upload.UploadHandler;
import com.egg.common.log.LogKit;

/**
 * 使用commons.uploadfile包
 */
public class CommonsUploadHandler implements UploadHandler {

	private static final String TMP_DIR = "/upload/tmp";
	private static final String UTF8 = "UTF-8";

	private Boolean isMultipart = null;
	private List<FilePart> fileParts = null;
	private Map<String, String[]> params = null;
	private boolean isInitFinish = false;

	public CommonsUploadHandler() {
	}

	@Override
	public boolean isMultipart(HttpServletRequest req) {
		if (isMultipart == null) {
			isMultipart = ServletFileUpload.isMultipartContent(req);
		}
		return isMultipart;
	}

	@Override
	public List<FilePart> parseFiles(HttpServletRequest req) {
		if (!isInitFinish) {
			init(req);
		}
		return fileParts;
	}

	@Override
	public Map<String, String[]> parseParams(HttpServletRequest req) {
		if (!isInitFinish) {
			init(req);
		}
		return params;
	}

	@SuppressWarnings("unchecked")
	private void init(HttpServletRequest req) {
		isInitFinish = true;
		if (!isMultipart(req)) {
			return;
		}

		try {
			File tmpFile = new File(req.getSession().getServletContext().getRealPath("/") + TMP_DIR);
			if (!tmpFile.exists()) {
				tmpFile.mkdirs();
			}

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(4096);
			factory.setRepository(tmpFile);

			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> fileItems = upload.parseRequest(req);
			if (fileItems == null || fileItems.isEmpty()) {
				return;
			}

			fileParts = new ArrayList<FilePart>();
			// TODO <string, list> --> <String, String[]>
			params = new HashMap<String, String[]>();
			FilePart filePart = null;
			for (FileItem fileItem : fileItems) {
				if (fileItem.isFormField()) {
					params.put(fileItem.getFieldName(), new String[] { fileItem.getString(UTF8) });
				} else {
					filePart = this.build(fileItem);
					if (filePart != null) {
						fileParts.add(filePart);
					}
				}
			}
		} catch (Exception e) {
			LogKit.error("Initialize Upload Info Error.", e);
		}
	}

	private FilePart build(FileItem fileItem) throws IOException {
		if (fileItem == null || fileItem.getSize() == 0) {
			return null;
		}

		String inputName = fileItem.getFieldName();
		String contentType = fileItem.getContentType();
		String name = fileItem.getName();
		long size = fileItem.getSize();
		InputStream in = fileItem.getInputStream();
		return new FilePart(inputName, contentType, name, size, in);
	}

}
