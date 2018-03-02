package cn.honry.oa.activiti.modeler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.activiti.modeler.dao.ModelerPageDao;
import cn.honry.oa.activiti.modeler.service.ModelerPageService;
import cn.honry.oa.activiti.modeler.vo.ModelerVO;
@Service("modelerPageService")
@Transactional
public class ModelerPageServiceImpl implements ModelerPageService {
	
	@Autowired
	@Qualifier("modelerPageDao")
	private ModelerPageDao modelerPageDao;
	@Override
	public List<ModelerVO> getModeler(String name, Integer page, Integer rows) {
		return modelerPageDao.getModeler(name, page, rows);
	}

	@Override
	public int getModelerTotal(String name) {
		return modelerPageDao.getModelerTotal(name);
	}

}
