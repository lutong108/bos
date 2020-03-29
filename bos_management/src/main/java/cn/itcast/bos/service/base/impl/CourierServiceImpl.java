package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	/**
	 * 快递员批量作废
	 */
	@Override
	public void delBatch(String[] idArr) {
		for (int i = 0; i < idArr.length; i++) {
			courierRespory.delBatch(Integer.parseInt(idArr[i]));
		}
	}

	/**
	 * 查询未关联快递员
	 */
	@Override
	public List<Courier> findNoassociationCourier() {
		//构造条件表达式
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		return courierRespory.findAll(specification);
	}
}
