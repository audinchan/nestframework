package ${hss_base_package}.service.ext;

import java.io.File;
import java.util.Map;

import org.nestframework.action.FileItem;

public interface IUploadPhotoManagerExt {
	/**
	 * 验证相片的属性.
	 * 
	 * @return Map<String, String>.
	 */
	public Map<String, String> checkPhoto(FileItem fileItem, String minphotosize,
			String photosize,String photopattern);

	/**
	 * 保存相片到文件系统
	 * 
	 * @param photo
	 *            相片文件.
	 * @param oldPhotoPath
	 *            原有相片的路径.
	 * @return String 相片保存的路径.
	 */
	public String savePhoto(File photoFile, String oldPhotoPath,
			String photoDirInPar,String generatePath);
	
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
			String photoDirInPar,String generatePath);
	
	/**
	 * 返回照片url
	 * @param sc
	 * @param photo_url_prefix
	 * @param PhotoPath
	 * @return
	 */
	public String loadPhotoRootPath(String photo_url_prefix,String PhotoPath);
}
