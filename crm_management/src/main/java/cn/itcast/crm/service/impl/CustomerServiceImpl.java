package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRespory;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRespory customerRespory;

	/**
	 * 查询所有未关联客户列表
	 */
	@Override
	public List<Customer> findNoAssociationCustoimers() {
		return customerRespory.findByFixedAreaIdIsNull();
	}

	/**
	 * 查询所有已关联客户列表
	 */
	@Override
	public List<Customer> findHadAssociationFixedAreaCustoimers(String fixedAreaId) {
		return customerRespory.findByFixedAreaId(fixedAreaId);
	}

	/**
	 * 将客户关联到定区上
	 */
	@Override
	public void associationCustoimersToFixedArea(String custoimerIds, String fixedAreaId) {
		String[] ids = custoimerIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			Integer id = Integer.parseInt(ids[i]);
			customerRespory.updateFixedAreaId(fixedAreaId,id);
		}
	}

}
