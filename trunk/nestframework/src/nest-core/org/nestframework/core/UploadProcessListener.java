/**
 * 
 */
package org.nestframework.core;

import org.apache.commons.fileupload.ProgressListener;
import org.nestframework.action.IUploadStatus;

/**
 * @author audin
 *
 */
public class UploadProcessListener implements ProgressListener {
	private UploadStatus status;
	
	public UploadProcessListener(ExecuteContext ctx) {
		status = new UploadStatus();
		ctx.getRequest().getSession().setAttribute(ctx.getPath() + "$UploadStatus", status);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.fileupload.ProgressListener#update(long, long, int)
	 */
	public void update(long bytesRead, long contentLength, int items) {
		status.bytesRead = bytesRead;
		status.contentLength = contentLength;
		status.items = items;
	}
	
	protected class UploadStatus implements IUploadStatus {
		long bytesRead;
		long contentLength;
		int items;

		public long getBytesRead() {
			return bytesRead;
		}

		public long getContentLength() {
			return contentLength;
		}
		
		public int getItems() {
			return items;
		}
	}

}
