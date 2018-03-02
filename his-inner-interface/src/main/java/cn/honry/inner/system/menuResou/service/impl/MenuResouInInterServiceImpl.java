package cn.honry.inner.system.menuResou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysMenuResourceCode;
import cn.honry.inner.system.menuResou.dao.MenuResouInInterDao;
import cn.honry.inner.system.menuResou.service.MenuResouInInterService;

/**  
 *  
 * @className：MenuResouInInterServiceImpl
 * @Description：  栏目功能接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("menuResouInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class MenuResouInInterServiceImpl implements MenuResouInInterService{
	

	@Autowired
	@Qualifier(value="menuResouInInterDao")
	private MenuResouInInterDao menuResouInInterDao;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysMenuResourceCode get(String id) {
		return menuResouInInterDao.get(id);
	}

	@Override
	public void saveOrUpdate(SysMenuResourceCode resource) {
		menuResouInInterDao.save(resource);
	}
	
}
