package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{

	private static final long serialVersionUID = 1L;
	//模型驱动
	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		return courier;
	}

	@Autowired
	private CourierService courierService;
	
	/**
	 * 保存快递员信息
	 */
	@Action(value="courier_save",results={@Result(name="success",type="redirect",
			location="/pages/base/courier.html")})
	public String save(){
		courierService.save(courier);
		return SUCCESS;
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

	/**
	 * 快递员数据分页查询
	 */
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//封装pageAble对象
		Pageable pageAble = new PageRequest(page-1, rows);
		//封装条件查询对象
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				//本表查询
				//构造courierNum = ?
				if(StringUtils.isNotBlank(courier.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					predicateList.add(p1);
				}
				//构造company like %?%
				if(StringUtils.isNotBlank(courier.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					predicateList.add(p2);
				}
				//构造type = ?
				if(StringUtils.isNotBlank(courier.getType())){
					Predicate p3 = cb.equal(root.get("type"), courier.getType());
					predicateList.add(p3);
				}
				//关联表查询
				//构造standard.name like %?%
				if(courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())){
					Join<Object, Object> standardRoot = root.join("standard",JoinType.INNER);
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
							"%"+courier.getStandard().getName()+"%");
					predicateList.add(p4);
				}
				
				return cb.and(predicateList.toArray(new Predicate[0]));
			}
		};
		//调用查询方法
		Page<Courier> pageData = courierService.pageQuery(specification,pageAble);
		//分离查询数据，并放入值栈
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
}
