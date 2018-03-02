package cn.honry.inner.drug.supply.dao;

import java.util.Map;

import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface SupplyInnerDao extends EntityDao<DrugSupplycompany>{
	Map<String, Object> findAllMap();
}
