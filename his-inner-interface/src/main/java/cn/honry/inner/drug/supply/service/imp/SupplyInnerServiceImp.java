package cn.honry.inner.drug.supply.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.inner.drug.supply.dao.SupplyInnerDao;
import cn.honry.inner.drug.supply.service.SupplyInnerService;

/**
 * @author sgt
 *
 */
@Service("supplyInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class SupplyInnerServiceImp implements SupplyInnerService{
	@Autowired
	private SupplyInnerDao supplyInnerDao;
	@Override
	public Map<String, Object> findAllMap() {
		return supplyInnerDao.findAllMap();
	}
	@Override
	public DrugSupplycompany get(String arg0) {
		return supplyInnerDao.get(arg0);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}
	@Override
	public void saveOrUpdate(DrugSupplycompany arg0) {
		supplyInnerDao.save(arg0);
	}

}
