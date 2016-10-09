package com.egg.action.upload.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.egg.action.upload.FilePart;
import com.egg.action.upload.UploadHandler;
import com.egg.common.log.LogKit;

/**
 * 使用Serlvet3的Part对象
 */
public class Servlet3UploadHandler implements UploadHandler {

	private Boolean isMultipart = null;
	private List<FilePart> fileParts = null;
	private boolean isInitFinish = false;

	public Servlet3UploadHandler() {
	}

	@Override
	public boolean isMultipart(HttpServletRequest req) {
		if (isMultipart == null) {
			isMultipart = ("POST".equalsIgnoreCase(req.getMethod())
					&& req.getContentType() != null
					&& req.getContentType().startsWith("multipart/"));
		}
		return isMultipart;
	}

	@Override
	public List<FilePart> parseFiles(HttpServletRequest req) {
		if (!isInitFinish) {
			initFileParts(req);
		}
		return fileParts;
	}

	@Override
	public Map<String, String[]> parseParams(HttpServletRequest req) {
		return null;
	}

	private void initFileParts(HttpServletRequest req) {
		isInitFinish = true;
		if (!isMultipart(req)) {
			return;
		}

		try {
			Collection<Part> parts = req.getParts();
			if (parts != null && !parts.isEmpty()) {
				fileParts = new ArrayList<FilePart>(parts.size());
				FilePart filePart = null;
				for (Part part : parts) {
					filePart = this.build(part);
					if (filePart != null) {
						fileParts.add(filePart);
					}
				}
			}
		} catch (Exception e) {
			LogKit.error("Parse Upload Files Error.", e);
		}
	}

	private FilePart build(Part part) throws IOException {
		if (part == null || part.getSize() == 0) {
			return null;
		}
		String disposition = part.getHeader("content-disposition");
		if (disposition == null || !disposition.contains("; filename=")) {
			return null;
		}

		String inputName = part.getName();
		String contentType = part.getContentType();
		String name = disposition.substring(disposition.lastIndexOf("=") + 2, disposition.length() - 1);
		long size = part.getSize();
		InputStream in = part.getInputStream();
		return new FilePart(inputName, contentType, name, size, in);
	}

}
