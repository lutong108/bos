package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
/**
 * 收派标准Action
 *
 */
@ParentPackage("json-default")
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
	//属性驱动
	private int page;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	@Autowired
	private StandardService standardService;
	
	/**
	 * 新增收派标准
	 * @return
	 */
	@Action(value="standard_save",
			results={@Result(name="success",type="redirect",
			location="/pages/base/standard.html")})
	public String save(){
		standardService.save(standard);
		return SUCCESS;
	}

	/**
	 * 收派标准分页查询
	 */
	@Action(value="standard_pageQuery",
			results={@Result(name="success",type="json")})
	public String pageQuery(){
		//使用spring data实现类提供的对象
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> pageData = standardService.findPageData(pageable);
		//用map接收分离出的pageData数据，用于返回前端
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		//将结果压入值栈顶部，struts2自动利用插件将map转json
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
}
