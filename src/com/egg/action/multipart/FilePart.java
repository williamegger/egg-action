package com.egg.action.multipart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FilePart {

	private String inputName;
	private String contentType;
	private String name;
	private long size;
	private InputStream in;

	public FilePart(String inputName, String contentType, String name, long size, InputStream in) {
		this.inputName = inputName;
		this.contentType = contentType;
		this.name = name;
		this.size = size;
		this.in = in;
	}

	public void write(File file) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] bs = new byte[4096];
			int len = 0;
			while ((len = in.read(bs)) > 0) {
				out.write(bs, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	public String getInputName() {
		return inputName;
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public InputStream getIn() {
		return in;
	}

}
