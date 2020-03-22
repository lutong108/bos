package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRespory;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRespory courierRespory;
	
	@Override
	public void save(Courier courier) {
		courierRespory.save(courier);
	}

	/**
	 * 快递员信息分页查询
	 */
	@Override
	public Page<Courier> pageQuery(Specification<Courier> specification,Pageable pageAble) {
		return courierRespory.findAll(specification,pageAble);
	}

}
