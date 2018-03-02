package cn.honry.inner.drug.manufacturer.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.DrugManufacturer;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface ManufacturerInnerDao extends EntityDao<DrugManufacturer>{
	Map<String, Object> findAllMap();
}
