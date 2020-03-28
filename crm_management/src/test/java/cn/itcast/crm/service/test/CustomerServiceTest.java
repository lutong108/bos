package cn.itcast.crm.service.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;
	
	@Test
	public void testFindNoAssociationCustoimers() {
		System.out.println(customerService.findNoAssociationCustoimers());
	}

	@Test
	public void testFindHadAssociationFixedAreaCustoimers() {
		System.out.println(customerService.findHadAssociationFixedAreaCustoimers("dq001"));
	}

	@Test
	public void testAssociationCustoimersToFixedArea() {
		customerService.associationCustoimersToFixedArea("1,2", "dq001");;
	}

}
