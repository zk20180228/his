package cn.honry.oa.patmenumanager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.oa.patmenumanager.dao.PatMenuRightDao;
import cn.honry.oa.patmenumanager.service.PatMenuRightService;

@Service("patMenuRightService")
@Transactional
public class PatMenuRightServiceImpl implements PatMenuRightService {
	
	@Autowired
	@Qualifier(value="patMenuRightDao")
	private PatMenuRightDao patMenuRightDao;


	@Override
	public List<OaMenuRight> findAllByMenuid(String code) {
		return patMenuRightDao.findAllByMenuid(code);
	}

}
