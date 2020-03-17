package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRespory;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService{

	@Autowired
	private StandardRespory standardRespory;
	/**
	 * 保存订单标准
	 */
	@Override
	public void save(Standard standard) {
		standardRespory.save(standard);
	}
	
	/**
	 * 订单数据分页查询
	 */
	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		return standardRespory.findAll(pageable);
	}

}
