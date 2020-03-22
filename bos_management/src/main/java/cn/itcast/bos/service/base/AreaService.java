package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.Area;

public interface AreaService {

	void batchImport(List<Area> areaList);

}
