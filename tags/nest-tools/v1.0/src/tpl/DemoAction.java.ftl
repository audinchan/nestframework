package ${hss_base_package}.webapp.action.demo;

import javax.servlet.http.HttpServletRequest;

import org.nestframework.action.FileItem;
import org.nestframework.addons.spring.Spring;
import org.nestframework.annotation.Before;
import org.nestframework.annotation.DefaultAction;
import org.nestframework.validation.Param;
import org.nestframework.validation.Validate;
import org.nestframework.validation.Validations;

import ${hss_service_package}.IDemoManager;


@Validate
public class DemoAction {
	
	//////////// Action����������ǰ�� //////////////////

	// ��ʾ�����Actionִ��ǰ����ʼ������
	@Before
	public void init() {
		username = "����";
		age = 20;
		date = "2005-10-10";
	}

	// Ĭ��Action����
	@DefaultAction
	public Object doDefault() {
		username = "aaa";
		msg = demoManager.getMsg();
		return "/demo/demo.jsp";
	}
	
	/**
	 * �ļ��ϴ�.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public Object doSave(HttpServletRequest req) throws Exception {
		if (file != null && file.isUploaded()) {
			fileContent = new String(file.getBytes());
		}
		if (file2 != null && file2.isUploaded()) {
			fileContent += new String(file2.getBytes());
		}
		return "/demo/demo.jsp";
	}
	
	////////// ҵ��ӿ� //////////
	@Spring
	private IDemoManager demoManager;
	
	
	@Validations({
		@Validate(label="user.username", type="required", on="doSave", client=false),
		@Validate(label="user.username", type="mask", on="doSave", msg="�û���ֻ������Сд��ĸ�����֡�", msgFromResource=false, params= {
				@Param(name="mask", value="^[a-z0-9]+$")
		})
	})
	private String username;

	@Validate(label="����", labelFromResource=false, type="intRange", on="doSave", params= {
			@Param(name="min", value="1"),
			@Param(name="max", value="100")
	})
	private int age;

	@Validate(label="����", labelFromResource=false, type="date", on="doSave", params= {
			@Param(name="datePattern", value="yyyy-MM-dd"),
			@Param(name="strict", value="false")
	})
	private String date;
	
	@Validate(label="ʱ��", labelFromResource=false, type="mask", on="doSave", msg="������Ч��ʱ���ʽ��Сʱ:���ӣ���", msgFromResource=false, params= {
				@Param(name="mask", value="^[0-9]{1,2}:[0-9]{1,2}$")
		})
	private String time;
	
	// test indexed values
	private String[] items = {};
	
	private FileItem file;
	
	private FileItem file2;
	
	private String fileContent;
	
	private String msg;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public FileItem getFile() {
		return file;
	}

	public void setFile(FileItem file) {
		this.file = file;
	}

	public FileItem getFile2() {
		return file2;
	}

	public void setFile2(FileItem file2) {
		this.file2 = file2;
	}

	public String getFileContent() {
		return fileContent;
	}

	public String getMsg() {
		return msg;
	}

}
