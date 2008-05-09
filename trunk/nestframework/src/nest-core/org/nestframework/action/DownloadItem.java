package org.nestframework.action;

import java.io.File;
import java.io.InputStream;

public class DownloadItem {
	private File file;

	private String filename;

	private String contentType;

	private long size;

	private InputStream inputStream;

	private byte[] data;

	public DownloadItem(File file) {
		this.file = file;
		String name = file.getName();
		int pos = name.lastIndexOf(File.separatorChar);
		setFilename(name.substring(pos + 1));
		setSize(file.length());
	}

	public DownloadItem(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public DownloadItem(byte[] data) {
		this.data = data;
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

	public long getSize() {
		return size;
	}

	public DownloadItem setSize(long size) {
		this.size = size;
		return this;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public byte[] getData() {
		return data;
	}
}
