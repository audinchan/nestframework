/**
 * 
 */
package org.nestframework.action;

/**
 * Upload Status.
 * 
 * @author audin
 *
 */
public interface IUploadStatus {
	/**
	 * The total number of bytes, which have been read so far.
	 * @return
	 */
	public long getBytesRead();
	
	/**
	 * The total number of bytes, which are being read. May be -1, if this number is unknown.
	 * @return
	 */
	public long getContentLength();
	
	/**
	 * The number of the field, which is currently being read. (0 = no item so far, 1 = first item is being read, ...)
	 * @return
	 */
	public int getItems();
}
