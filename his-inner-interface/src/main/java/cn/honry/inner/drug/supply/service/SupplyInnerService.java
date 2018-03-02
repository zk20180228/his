package cn.honry.inner.drug.supply.service;

import java.util.Map;

import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface SupplyInnerService  extends BaseService<DrugSupplycompany> {
	Map<String, Object> findAllMap();
}
