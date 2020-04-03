package cn.itcast.bos.web.action;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction  extends BaseAction<Customer> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 发送短信验证码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "customer_sendSms")
	public String sendSms() throws IOException {
		// 手机号保存在Customer对象
		// 生成短信验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		// 将短信验证码 保存到session
		ServletActionContext.getRequest().getSession()
				.setAttribute(model.getTelephone(), randomCode);

		System.out.println("生成手机验证码为：" + randomCode);
		// 编辑短信内容
		/*final String msg = "尊敬的用户您好，本次获取的验证码为：" + randomCode
				+ ",服务电话：4006184000";*/
		//调用SMS发短信
		//String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
		String result = "000";
		System.out.println(result);
		return NONE;
	}
	
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	/**
	 * 用户注册
	 * @return
	 * @throws IOException
	 */
	@Action(value="customer_regist",results={
			@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")})
	public String regist(){
		//校验验证码，不通过就返回到注册页面
		String checkCode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute(model.getTelephone());
		if(checkCode==null || !checkCode.equals(checkcode)){
			//校验错误
			return INPUT;
		}
		//把用户信息存入crm系统
		WebClient.create("http://localhost:8081/crm_management"
				+ "/services/customerService/registCustomer")
			.type(MediaType.APPLICATION_JSON).post(model);
		return SUCCESS;
	}

}
