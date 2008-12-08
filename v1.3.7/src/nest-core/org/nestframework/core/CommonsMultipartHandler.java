/**
 * 
 */
package org.nestframework.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.nestframework.action.ActionException;
import org.nestframework.action.FileUploadLimitExceededException;
import org.nestframework.config.IConfiguration;
import org.nestframework.utils.NestUtil;

/**
 * Multipart handler using commons-fileupload.
 * 
 * @author audin
 * 
 */
@SuppressWarnings("unchecked")
public class CommonsMultipartHandler implements IMultipartHandler, IInitable {
	private static final String FILENAME_SEPERATOR = "\\$";
	
	private boolean monitorStatus = false;

	public void init(IConfiguration config) {
		String monitor = NestUtil.trimAll(config.getProperties().get("monitorUploadStatus"));
		if (NestUtil.isNotEmpty(monitor)) {
			monitorStatus = "true".equalsIgnoreCase(monitor);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nestframework.core.IMultipartHandler#processMultipart(org.nestframework.core.ExecuteContext,
	 *      java.io.File, int, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void processMultipart(ExecuteContext context, File tmpDir,
			int maxPostSize, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(tmpDir);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxPostSize);
			if (monitorStatus) {
				upload.setProgressListener(new UploadProcessListener(context));
			}
			List<FileItem> items = upload.parseRequest(req);
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			Map<String, List<org.nestframework.action.FileItem>> files = new HashMap<String, List<org.nestframework.action.FileItem>>();

			for (FileItem item : items) {
				// If it's a form field, add the string value to the list
				if (item.isFormField()) {
					List<String> values = params.get(item.getFieldName());
					if (values == null) {
						values = new ArrayList<String>();
						params.put(item.getFieldName(), values);
					}
					values.add(item.getString());
				}
				// Else store the file param
				else {
					final FileItem it = item;
					String[] fNames = item.getFieldName().split(FILENAME_SEPERATOR);
					List<org.nestframework.action.FileItem> fList = files
							.get(fNames[0]);
					if (fList == null) {
						fList = new ArrayList<org.nestframework.action.FileItem>();
						files.put(fNames[0], fList);
					}
					String origName = item.getName();
					int p1 = origName.lastIndexOf('/');
					int p2 = origName.lastIndexOf('\\');
					if (p1 < p2) {
						p1 = p2;
					}
					origName = origName.substring(p1 + 1);
				
					fList.add(new org.nestframework.action.FileItem(null, item
							.getContentType(), origName) {

						@Override
						public void delete() throws IOException {
							it.delete();
						}

						@Override
						public byte[] getBytes() throws IOException {
							return it.get();

						}

						@Override
						public File getFile() {
							throw new ActionException(
									"Commons-Fileupload doesn't support this operation.");
						}

						@Override
						public InputStream getInputStream() throws IOException {
							return it.getInputStream();
						}

						@Override
						public long getSize() {
							return it.getSize();
						}

						@Override
						public boolean isUploaded() {
							return this.getFileName().length() != 0;
						}

						@Override
						public void save(File toFile) throws IOException {
							try {
								File parent = toFile.getAbsoluteFile().getParentFile();
						        if (toFile.exists() && !toFile.canWrite()) {
						            throw new IOException("Cannot overwrite existing file at "+ toFile.getAbsolutePath());
						        }
						        else if (!parent.exists() && !parent.mkdirs()) {
						            throw new IOException("Parent directory of specified file does not exist and cannot " +
						                " be created. File location supplied: " + toFile.getAbsolutePath());
						        }
						        else if (!toFile.exists() && !parent.canWrite()) {
						            throw new IOException("Cannot create new file at location: " + toFile.getAbsolutePath());
						        }
								it.write(toFile);
							} catch (Exception e) {
								if (e instanceof IOException) {
									throw (IOException) e;
								} else {
									IOException ioe = new IOException(
											"Problem saving uploaded file.");
									ioe.initCause(e);
									throw ioe;
								}
							}
						}

						@Override
						protected void saveViaCopy(File toFile)
								throws IOException {
							save(toFile);
						}

					});
				}
			}
			
			Map<String, String[]> _params = new HashMap<String, String[]>();
			for (String paramName: params.keySet()) {
				_params.put(paramName, params.get(paramName).toArray(new String[0]));
			}
			context.setParams(_params);
			
			Map<String, org.nestframework.action.FileItem[]> _files = new HashMap<String, org.nestframework.action.FileItem[]>();
			for (String fileName: files.keySet()) {
				_files.put(fileName, files.get(fileName).toArray(new org.nestframework.action.FileItem[0]));
			}
			context.setUploadedFiles(_files);
			
		} catch (FileUploadBase.SizeLimitExceededException slee) {
			throw new FileUploadLimitExceededException(maxPostSize, slee
					.getActualSize());
		} catch (FileUploadException fue) {
			IOException ioe = new IOException(
					"Could not parse and cache file upload data.");
			ioe.initCause(fue);
			throw ioe;
		}
	}

}
