package org.nestframework.action;

import java.io.File;
import java.io.InputStream;

import org.nestframework.utils.NestUtil;

public class DownloadItem {
	private File file;

	private String filename;

	private String contentType;

	private long contentLength = 0;

	private InputStream inputStream;

	private byte[] data;

	public DownloadItem(File file) {
		this.file = file;
		setFilename(NestUtil.getFilename(file.getName()));
		setContentLength(file.length());
	}

	public DownloadItem(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public DownloadItem(InputStream inputStream, long contentLength) {
		this.inputStream = inputStream;
		setContentLength(contentLength);
	}

	public DownloadItem(byte[] data) {
		this.data = data;
		setContentLength(data.length);
	}


	public File getFile() {
		return file;
	}

	public String getFilename() {
		return filename;
	}

	public DownloadItem setFilename(String filename) {
		this.filename = filename;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public DownloadItem setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public long getContentLength() {
		return contentLength;
	}

	public DownloadItem setContentLength(long size) {
		this.contentLength = size;
		return this;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public byte[] getData() {
		return data;
	}
}
