package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	
	/**
	 * 新增注册用户
	 */
	@Path("/registCustomer")
	@POST
	@Consumes({"application/json","application/xml"})
	public void regist(Customer customer);
	
	/**
	 * 查询用户是否已注册
	 */
	@Path("/customer/telephone/{telephone}")
	@GET
	@Produces({"application/json","application/xml"})
	public Customer findByTelephone(@PathParam("telephone")String telephone);
	
	/**
	 * 更新用户注册类型
	 */
	@Path("/customer/updatetype/{telephone}")
	@GET
	public void updateType(@PathParam("telephone")String telephone);
	
	/**
	 * 用户登录
	 * @param telephone
	 * @param password
	 * @return
	 */
	@Path("customer/login")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public Customer login(@QueryParam("telephone") String telephone,
			@QueryParam("password") String password);
}
