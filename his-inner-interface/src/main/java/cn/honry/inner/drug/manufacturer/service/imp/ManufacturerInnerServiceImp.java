package cn.honry.inner.drug.manufacturer.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugManufacturer;
import cn.honry.inner.drug.manufacturer.dao.ManufacturerInnerDao;
import cn.honry.inner.drug.manufacturer.service.ManufacturerInnerService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/** 供货公司接口
* @ClassName: ManufacturerInnerServiceImp 
* @Description: 供货公司接口
* @author dtl
* @date 2017年1月10日
*  
*/
@Service("manufacturerInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class ManufacturerInnerServiceImp implements ManufacturerInnerService{
	@Autowired
	private ManufacturerInnerDao manufacturerInnerDao;
	@Override
	public Map<String, Object> findAllMap() {
		return manufacturerInnerDao.findAllMap();
	}
	@Override
	public DrugManufacturer get(String arg0) {
		return manufacturerInnerDao.get(arg0);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}
	@Override
	public void saveOrUpdate(DrugManufacturer arg0) {
		manufacturerInnerDao.save(arg0);
	}

}
