package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	// 保存宣传任务
	void save(Promotion promotion);

	// 分页查询
	Page<Promotion> findPageData(Pageable pageable);

	/*
	 * 活动促销数据查询接口
	 */
	@Path("/pageQuery")
	@GET
	@Produces({ "application/xml", "application/json" })
	PageBean<Promotion> findPageData(@QueryParam("page") int page,
			@QueryParam("rows") int rows);


	/*
	 * 商品详情查询接口
	 */
	@Path("/promotion/{id}")
	@GET
	@Produces({ "application/xml", "application/json" })
	Promotion findById(@PathParam("id") Integer id);
		
		
}
