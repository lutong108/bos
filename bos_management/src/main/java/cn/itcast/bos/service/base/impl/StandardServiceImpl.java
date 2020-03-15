package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

}
