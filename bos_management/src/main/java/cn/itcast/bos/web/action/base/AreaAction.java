package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area>{

	private static final long serialVersionUID = 1L;
	
	//模型驱动
	private Area area = new Area();
	@Override
	public Area getModel() {
		return area;
	}

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
				
				areaList.add(area);
			}
			//调用业务层
			areaService.batchImport(areaList);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return NONE;
	}
}
