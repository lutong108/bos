package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
