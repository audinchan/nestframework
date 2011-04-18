package ${hss_base_package}.service.ext.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.nestframework.action.FileItem;

import ${hss_base_package}.exception.LoadUpPhotoException;
import ${hss_base_package}.service.ISysParameterManager;
import ${hss_base_package}.service.ext.IUploadPhotoManagerExt;

public class UploadPhotoManagerExt implements IUploadPhotoManagerExt {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(UploadPhotoManagerExt.class);
	
	private ISysParameterManager sysParameterManager;
	
	/**
	 * 验证相片的属性.
	 * 
	 * @return Map<String, String>.
	 */
	public Map<String, String> checkPhoto(FileItem fileItem, String minphotosize,
			String photosize,String photopattern) {
		Map<String, String> ret = new HashMap<String, String>();
		String isValid = "true";
		String msg = "";
		if (fileItem != null && fileItem.getFileName() != null) {
			long filesize = fileItem.getSize();
			if (minphotosize!=null && (filesize / 1024) < new Long(minphotosize)) {
				isValid = "false";
				msg = "对不起，您提供的照片尺寸小于" + minphotosize + "KB，无法上传。请您提供尺寸在"+minphotosize+"—"+photosize+"KB之间的照片！";
			}
			else if (photosize!=null && (filesize / 1024) > new Long(photosize)) {
				isValid = "false";
				msg = "对不起，您提供的照片尺寸大于" + photosize + "KB，无法上传。请您提供尺寸在"+minphotosize+"—"+photosize+"KB之间的照片！";
			}
		}
		if (fileItem != null && fileItem.getFileName() != null) {
			String fileName = fileItem.getFileName();
			// 相片文件的扩展名
			if (photopattern.indexOf(getExtName(fileName)) < 0) {
				isValid = "false";
				msg = "对不起，您提供的照片格式不对，无法上传。请您提供"+ photopattern +"格式的照片文件！";
			}
		}
		ret.put("isValid", isValid);
		ret.put("msg", msg);
		return ret;
	}
	/**
	 * 获取文件扩展名
	 * @param filename
	 * @return
	 */
	private String getExtName(String filename)
	{
		String extName = "";
		if(filename != null)
		{
			extName = filename.substring(filename.lastIndexOf(".")+1);
		}
		return extName;
	}
	
	public String savePhoto(File photoFile, String oldPhotoPath,
			String photoDirInPar,String generatePath) {
		try {
			String filePath = "";
			// 获取图片存储目录.
			String photoDir = sysParameterManager.getParamValue(photoDirInPar);
			if (logger.isDebugEnabled())
			{
				logger.info("存照片的路径photoDir="+photoDir);
			}
			if (photoFile != null && photoFile.getName() != null) {
				String absolutePath = "";
				// 删除以前的相片
				if (null != oldPhotoPath && !"".equals(oldPhotoPath)) {
					absolutePath = photoDir + oldPhotoPath;

					try {
						File tempFile = new File(absolutePath);
						if (tempFile.exists()) {
							tempFile.delete();
						}
					} catch (Exception e) {
						throw new LoadUpPhotoException(e);
					}
				}

				filePath = photoDir + File.separator + generatePath;
				File file = new File(filePath);
				File parent = file.getParentFile();
				
				if (!parent.exists())
				{
					parent.mkdirs();
				}
				
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		        BufferedInputStream   in = new BufferedInputStream(new FileInputStream(photoFile));

		        int b;
		        while ((b = in.read()) != -1) {
		            out.write(b);
		        }

		        in.close();
		        out.close();
		        
				filePath = generatePath;
				if (logger.isDebugEnabled())
				{
					logger.info(filePath);
				}
			}
			return filePath;

		} catch (FileNotFoundException e) {

			logger.error(e.getMessage() + e.fillInStackTrace());

		} catch (IOException ioe) {
			logger.error(ioe.getMessage() + ioe.fillInStackTrace());
		} catch(Exception ep)
		{
			logger.error("保存照片异常", ep);
		}
		return null;
	}
	
	/**
	 * 保存相片到文件系统
	 * 
	 * @param photo
	 *            相片数据.
	 * @param oldPhotoPath
	 *            原有相片的路径.
	 * @return String 相片保存的路径.
	 */
	public String savePhoto(FileItem photo, String oldPhotoPath,
			String photoDirInPar, String generatePath) throws LoadUpPhotoException {

		try {
			String filePath = "";
			// 获取图片存储目录.
			String photoDir = sysParameterManager.getParamValue(photoDirInPar);
			if (logger.isDebugEnabled())
			{
				logger.info("存照片的路径photoDir="+photoDir);
			}
			if (photo != null && photo.getFileName() != null) {
				String absolutePath = "";
				// 删除以前的相片
				if (null != oldPhotoPath && !"".equals(oldPhotoPath)) {
					absolutePath = photoDir + oldPhotoPath;

					try {
						File tempFile = new File(absolutePath);
						if (tempFile.exists()) {
							tempFile.delete();
						}
					} catch (Exception e) {
						throw new LoadUpPhotoException(e);
					}
				}

				filePath = photoDir + File.separator + generatePath;
				File file = new File(filePath);
				
				photo.save(file);
				
				filePath = generatePath;
				if (logger.isDebugEnabled()) {
					logger.info(filePath);
				}
			}
			return filePath;

		} catch (FileNotFoundException e) {

			logger.error(e.getMessage() + e.fillInStackTrace());

		} catch (IOException ioe) {
			logger.error(ioe.getMessage() + ioe.fillInStackTrace());
		} catch(Exception ep)
		{
			logger.error("保存照片异常", ep);
		}
		return null;
	}
	
	public String loadPhotoRootPath(String photo_url_prefix,String PhotoPath) {
		if(null==PhotoPath || PhotoPath.equals(""))
		{
			return "";
		}		
		// 获取考点图片存储目录.
		String mapUrlPrefix = sysParameterManager.getParamValue(photo_url_prefix);	
		if(!mapUrlPrefix.endsWith("/"))
		{
			mapUrlPrefix = mapUrlPrefix + "/";
		}
		// 返回组合串。
		return mapUrlPrefix+PhotoPath;		
	}
}
