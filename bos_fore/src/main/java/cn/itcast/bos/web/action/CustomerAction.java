package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.utils.MailUtils;
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
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
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
		//向客户绑定邮箱发激活邮件
		//产生激活码
		String activeCode = RandomStringUtils.randomNumeric(32);
		//在radis中设置过期时间24小时
		redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
		//发邮件
		String content = "尊敬的用户，您好！请于24小时内激活.</br><a href='"
				+MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&activeCode="+activeCode+"'>速运快递激活码</a>";
		MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
		return SUCCESS;
	}
	
	//属性驱动
	private String activeCode;
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	/**
	 * 邮箱注册
	 */
	@Action(value="customer_activeMail")
	public String activeMail(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		//判断激活码是否有效
		String activeCodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
		if(activeCodeRedis == null || !activeCodeRedis.equals(activeCode)){
			//无效
			try {
				response.getWriter().println("激活码无效，请登录重新注册！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//有效，防止重复绑定
			//调用crm系统查询当前用户时候已经绑定
			Customer customer = WebClient.create("http://localhost:8081/crm_management"
					+ "/services/customerService//customer/telephone/"+model.getTelephone())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if(customer.getType() == null || customer.getType() != 1){
				//未绑定
				WebClient.create("http://localhost:8081/crm_management"
					+ "/services/customerService//customer/updatetype/"+model.getTelephone()).get();
				try {
					response.getWriter().println("邮箱绑定成功！");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				//已绑定
				try {
					response.getWriter().println("邮箱已绑定，无需重复绑定！");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//删除redis激活码
			 redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}
}
