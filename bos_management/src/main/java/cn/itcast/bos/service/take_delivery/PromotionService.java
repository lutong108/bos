package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	// 保存宣传任务
	void save(Promotion promotion);

	// 分页查询
	Page<Promotion> findPageData(Pageable pageable);

}
