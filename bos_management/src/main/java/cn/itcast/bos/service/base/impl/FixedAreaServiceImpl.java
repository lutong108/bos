package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRespory;
import cn.itcast.bos.dao.base.FixedAreaRespory;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRespory fixedAreaRespory;
	
	/**
	 * 插入定区信息
	 */
	@Override
	public void save(FixedArea model) {
		fixedAreaRespory.save(model);
	}

	/**
	 * 定区信息分页查询
	 */
	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
		return fixedAreaRespory.findAll(specification, pageable);
	}

	@Autowired
	private CourierRespory courierRespory;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	/**
	 * 定区关联快递员
	 */
	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId) {
		//利用hibernate的持久态对象自动更新插入数据
		//查出对象
		FixedArea fixedAreaBean = fixedAreaRespory.findOne(fixedArea.getId());
		Courier courierBean = courierRespory.findOne(courierId);
		TakeTime takeTimeBean = takeTimeRepository.findOne(takeTimeId);
		//谁维护外键，插入谁
		courierBean.setTakeTime(takeTimeBean);
		fixedAreaBean.getCouriers().add(courierBean);
	}

}
