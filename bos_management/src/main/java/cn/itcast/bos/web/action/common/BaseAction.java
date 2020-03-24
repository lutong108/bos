package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * Action功能抽取
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	//抽取模型驱动
private static final long serialVersionUID = 1L;
	
	/**
	 * 模型驱动
	 */
	protected T model;
	@Override
	public T getModel() {
		System.out.println("===============2==============");
		return model;
	}
	public BaseAction(){
		System.out.println("===============1==============");
		try {
			//1,获得子类对象继承的父类对象的泛型
			Type genericSuperclass = this.getClass().getGenericSuperclass();
			//2,拿到父类具体的泛型的字节码对象
			ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
			@SuppressWarnings("unchecked")
			Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("模型构造失败...");
		}
	}
	

	/**
	 *  接收分页查询参数
	 */
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 将分页查询结果数据，压入值栈的方法
	 * @param pageData
	 */
	protected void pushPageDataToValueStack(Page<T> pageData) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());

		ActionContext.getContext().getValueStack().push(result);
	}
}
