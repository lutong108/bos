package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.web.action.common.BaseAction;

/**
 * kindEditor图片的上传和管理
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object>{

	private static final long serialVersionUID = 1L;
	
	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	
	@Action(value="image_upload",
			results={@Result(name="success",type="json")})
	public String upload(){
		//保存上传图片
		//获得绝对路径进行图片存储
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload/");
		//获取相对路径进行访问
		String contextPath = ServletActionContext.getRequest().getContextPath() + "/upload/";
		//图片名
		String uuid = UUID.randomUUID().toString();
		String saveName = uuid + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		//待存储文件
		File saveFile = new File(realPath +"/"+ saveName);
		System.out.println(saveFile.getAbsolutePath());
		//存储
		try {
			FileUtils.copyFile(imgFile, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("url", contextPath+saveName);
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	/**
	 * 图片空间功能
	 * @return
	 */
	@Action(value = "image_manage", results = { @Result(name = "success", type = "json") })
	public String manage() {
		// 根目录路径，可以指定绝对路径，比如 d:/xxx/upload/xxx.jpg
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ "upload/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		// 遍历目录取的文件信息
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		// 当前上传目录
		File currentPathFile = new File(rootPath);
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes)
							.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}
	
}
