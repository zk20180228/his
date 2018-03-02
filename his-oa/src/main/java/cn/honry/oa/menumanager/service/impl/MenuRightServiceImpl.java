package cn.honry.oa.menumanager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.oa.menumanager.dao.MenuRightDao;
import cn.honry.oa.menumanager.service.MenuRightService;
import cn.honry.oa.menumanager.vo.MenuVo;

@Service("menuRightService")
@Transactional
public class MenuRightServiceImpl implements MenuRightService {
	
	@Autowired
	@Qualifier(value="menuRightDao")
	private MenuRightDao menuRightDao;


	@Override
	public List<OaMenuRight> findAllByMenuid(String code) {
		return menuRightDao.findAllByMenuid(code);
	}

}
