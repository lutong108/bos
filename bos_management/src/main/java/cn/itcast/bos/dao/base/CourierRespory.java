package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRespory extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {

	@Query("update Courier t set t.deltag = '1' where t.id = ?")
	@Modifying
	public void delBatch(Integer id);
	
}
