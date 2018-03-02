package cn.honry.inner.system.menu.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysMenu;
import cn.honry.inner.system.menu.dao.MenuInInterDAO;
import cn.honry.inner.system.menu.service.MenuInInterService;
import cn.honry.inner.system.menu.vo.ParentMenuVo;

/**  
 *  
 * @className：MenuInInterServiceImpl
 * @Description：  栏目接口
 * @Author：aizhonghua
 * @CreateDate：2017-6-23 下午18:59:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-6-23 下午18:59:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("menuInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class MenuInInterServiceImpl implements MenuInInterService{

	private Logger logger=Logger.getLogger(MenuInInterServiceImpl.class);
	
	@Autowired
	@Qualifier(value = "menuInInterDAO")
	private MenuInInterDAO menuInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysMenu get(String id) {
		return menuInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(SysMenu menu) {
		menuInInterDAO.save(menu);
	}

	@Override
	public List<ParentMenuVo> queryMobileParentMenu() {
		return menuInInterDAO.queryMobileParentMenu();
	}
	
}
