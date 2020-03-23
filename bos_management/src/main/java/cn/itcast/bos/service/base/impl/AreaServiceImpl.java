package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRespory;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRespory areaRespory;
	
	/**
	 * 区域数据批量导入
	 */
	@Override
	public void batchImport(List<Area> areaList) {
		areaRespory.save(areaList);
	}

	/**
	 * 区域信息按条件分页查询
	 */
	@Override
	public Page<Area> pageQuery(Specification<Area> specification, Pageable pageAble) {
		return areaRespory.findAll(specification,pageAble);
	}

}
