package cn.honry.inner.emr.emrMain.service.impl;

import java.sql.Clob;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.EmrMain;
import cn.honry.inner.emr.emrMain.dao.EmrMainInnerDao;
import cn.honry.inner.emr.emrMain.service.EmrMainInnerService;
@Service("emrMainInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class EmrMainInnerServiceImpl implements EmrMainInnerService {
	
	@Autowired
	@Qualifier(value = "emrMainInnerDao")
	private EmrMainInnerDao emrMainInnerDao;
	@Override
	public EmrMain get(String arg0) {
		EmrMain emrMain = emrMainInnerDao.get(arg0);
		emrMain.setStrContent(ClobToString(emrMain.getEmrContent()));
		return emrMainInnerDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(EmrMain arg0) {
		if(StringUtils.isNotBlank(arg0.getId())){
			emrMainInnerDao.update(arg0);
		}else {
			emrMainInnerDao.save(arg0);
		}
	}

	@Override
	public int emrCount(String medicalrecordId, String emrName) {
		
		return emrMainInnerDao.emrCount(medicalrecordId, emrName);
	}

	@Override
	public List<EmrMain> emrList(String medicalrecordId, String emrName,
			String name, String rows, String page) {
		
		return emrMainInnerDao.emrList(medicalrecordId, emrName, name, rows, page);
	}

	@Override
	public String ClobToString(Clob clob) {
		return emrMainInnerDao.ClobToString(clob);
	}

}
