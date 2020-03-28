package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

/**
 * 客户信息cxf接口
 * @author Administrator
 *
 */
public interface CustomerService {

	/**
	 * 查询所有未关联客户列表
	 */
	@Path("/noAssociationCustoimers")
	@GET
	@Produces({"application/json","application/xml"})
	public List<Customer> findNoAssociationCustoimers();
	
	/**
	 * 查询所有已关联客户列表
	 */
	@Path("/hadAssociationFixedAreaCustoimers/{fixedAreaId}")
	@GET
	@Produces({"application/json","application/xml"})
	public List<Customer> findHadAssociationFixedAreaCustoimers(
			@PathParam("fixedAreaId")String fixedAreaId);
	
	
	/**
	 * 将客户关联到定区上
	 */
	@Path("/associationCustoimersToFixedArea")
	@PUT
	public void associationCustoimersToFixedArea(
			@QueryParam("custoimerIds")String custoimerIds,
			@QueryParam("fixedAreaId")String fixedAreaId);
	
}
