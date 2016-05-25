package com.egg.action.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用Serlvet3的Part对象
 */
public class Servlet3MultipartHandler implements MultipartHandler {

	private static final Logger LOG = LoggerFactory.getLogger(Servlet3MultipartHandler.class);

	private Boolean isMultipart = null;
	
	public Servlet3MultipartHandler() {
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
		List<FilePart> list = null;
		if (!isMultipart(req)) {
			return list;
		}
		try {
			Collection<Part> parts = req.getParts();
			if (parts != null && !parts.isEmpty()) {
				list = new ArrayList<FilePart>();
				FilePart filePart = null;
				for (Part part : parts) {
					filePart = this.build(part);
					if (filePart != null) {
						list.add(filePart);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(Servlet3MultipartHandler.class.getName() + ".parseFiles(): ", e);
		}
		return list;
	}

	@Override
	public Map<String, String> parseParams(HttpServletRequest req) {
		return null;
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
