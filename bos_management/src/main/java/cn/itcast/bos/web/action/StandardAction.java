package cn.itcast.bos.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
/**
 * 收派标准Action
 *
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
	private static final long serialVersionUID = 1L;
	
	//模型驱动
	private Standard standard = new Standard();
	@Override
	public Standard getModel() {
		return standard;
	}
	
	@Action(value="standard_save",
			results={@Result(name="success",type="redirect",
			location="/pages/base/standard.html")})
	public String save(){
		System.out.println("成功");
		return SUCCESS;
	}

	
}
