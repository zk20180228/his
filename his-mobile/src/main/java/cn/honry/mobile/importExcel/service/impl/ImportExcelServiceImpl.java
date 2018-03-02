package cn.honry.mobile.importExcel.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.mobile.importExcel.dao.ImportExcelDao;
import cn.honry.mobile.importExcel.service.ImportExcelService;

@Service("importExcelService")
public class ImportExcelServiceImpl implements ImportExcelService{

	@Resource
	private ImportExcelDao  importExcelDao;
	
	@Override
	public void updateEmp(SysEmployee emp) {
		importExcelDao.updateEmp(emp);
		
	}

	@Override
	public SysEmployee get(String arg0) {
		return importExcelDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(SysEmployee arg0) {
		if (StringUtils.isNotBlank(arg0.getId())) {
			importExcelDao.update(arg0);
		}else {
			importExcelDao.save(arg0);
		}
		
	}

}
