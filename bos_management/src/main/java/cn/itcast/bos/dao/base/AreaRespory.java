package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.Area;

public interface AreaRespory extends JpaRepository<Area, String> {

}
