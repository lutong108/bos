package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		if(StringUtils.isBlank(fixedAreaId)){
			return;
		}
		//清空传入的对应定区的已关联数据
		customerRespory.cleanFixedAreaId(fixedAreaId);
		//重新关联
		if(StringUtils.isNotBlank(custoimerIds)){
			String[] ids = custoimerIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				Integer id = Integer.parseInt(ids[i]);
				customerRespory.updateFixedAreaId(fixedAreaId,id);
			}
		}
	}

	/**
	 * 新增注册用户
	 */
	@Override
	public void regist(Customer customer) {
		customerRespory.save(customer);
	}

	/**
	 * 查询用户是否已注册
	 */
	@Override
	public Customer findByTelephone(String telephone) {
		return customerRespory.findByTelephone(telephone);
	}

	/**
	 * 更新用户注册类型
	 */
	@Override
	public void updateType(String telephone) {
		customerRespory.updateType(telephone);
	}

	/**
	 * 用户登录
	 */
	@Override
	public Customer login(String telephone, String password) {
		return customerRespory.findByTelephoneAndPassword(telephone,
				password);
	}

}
