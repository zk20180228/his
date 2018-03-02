package cn.honry.inner.drug.manufacturer.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.DrugManufacturer;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface ManufacturerInnerService  extends BaseService<DrugManufacturer> {
	Map<String, Object> findAllMap();
}
