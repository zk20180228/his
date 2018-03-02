package cn.honry.portal.procedure.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.portal.procedure.dao.ProcedureDao;
import cn.honry.portal.procedure.dao.impl.ProcedureDaoImpl;
import cn.honry.portal.procedure.service.ProcedureService;

@Service("procedureService")
@Transactional
@SuppressWarnings({"all"})
public class ProcedureServiceImpl implements ProcedureService {

	@Autowired
	@Qualifier(value = "procedureDao")
	private ProcedureDao procedureDao;
	@Override
	public Map<String, Object> gettime(String table) {
		return procedureDao.gettime(table);
	}

	@Override
	public void getupdate(String table, String maxDateStr, String minDateStr)throws Exception {
		
		procedureDao.getupdate(table, maxDateStr, minDateStr);
	}

}
