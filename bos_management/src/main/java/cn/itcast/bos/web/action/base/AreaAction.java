package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area>{

	private static final long serialVersionUID = 1L;
	
	//模型驱动
	/*private Area area = new Area();
	@Override
	public Area getModel() {
		return area;
	}*/

	//属性驱动
	private File file;
	public void setFile(File file) {
		this.file = file;
	}

	@Autowired
	private AreaService areaService;
	
	/**
	 * 区域数据批量导入
	 */
	@Action(value="area_batchImport")
	public String batchImport(){
		try {
			//加载excel文件
			@SuppressWarnings("resource")
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
			//读取第一个sheet
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			//读取每行数据
			List<Area> areaList = new ArrayList<Area>();
			for (Row row : sheet) {
				//跳过第一行表头
				if(row.getRowNum() == 0){
					continue;
				}
				//跳过空行
				if(row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
					continue;
				}
				//封装数据
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				// 基于pinyin4j生成城市编码和简码
				String province = area.getProvince();
				String city = area.getCity();
				String district = area.getDistrict();
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				// 简码
				String[] headArray = PinYin4jUtils.getHeadByString(province + city
						+ district);
				StringBuffer buffer = new StringBuffer();
				for (String headStr : headArray) {
					buffer.append(headStr);
				}
				String shortcode = buffer.toString();
				area.setShortcode(shortcode);
				// 城市编码
				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(citycode);
				
				areaList.add(area);
			}
			//调用业务层
			areaService.batchImport(areaList);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return NONE;
	}
	
	/**
	 * 区域信息分页查询
	 */
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//封装pageAble对象
		Pageable pageAble = new PageRequest(page-1, rows);
		//封装条件查询对象
		Specification<Area> specification = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				//本表查询
				//构造province like %?%
				if(StringUtils.isNotBlank(model.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class),"%"+model.getProvince()+"%");
					predicateList.add(p1);
				}
				//构造city like %?%
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					predicateList.add(p2);
				}
				//构造district like %?%
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					predicateList.add(p3);
				}
				
				return cb.and(predicateList.toArray(new Predicate[0]));
			}
		};
		//调用查询方法
		Page<Area> pageData = areaService.pageQuery(specification,pageAble);
		//分离查询数据，并放入值栈
		pushPageDataToValueStack(pageData);
		
		return SUCCESS;
	}
}
