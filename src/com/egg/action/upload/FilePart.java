package com.egg.action.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FilePart {

	private String formName;
	private String contentType;
	private String fileName;
	private long size;
	private InputStream inputStream;

	public FilePart(String formName, String contentType, String fileName, long size, InputStream inputStream) {
		this.formName = formName;
		this.contentType = contentType;
		this.fileName = fileName;
		this.size = size;
		this.inputStream = inputStream;
	}

	public void write(File file) throws Exception {
		FileOutputStream out = null;
		try {
			file.getParentFile().mkdirs();
			out = new FileOutputStream(file);
			byte[] bs = new byte[4096];
			int len = 0;
			while ((len = inputStream.read(bs)) > 0) {
				out.write(bs, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public String getFormName() {
		return formName;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public long getSize() {
		return size;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
