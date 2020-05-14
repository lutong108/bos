package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion promotion) {
		promotionRepository.save(promotion);
	}

	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

}
