package cn.honry.inner.system.menuButton.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysMenuButton;
import cn.honry.inner.system.menuButton.dao.HisbuttonInInterDao;
import cn.honry.inner.system.menuButton.service.HisbuttonInInterService;


@Service("hisbuttonInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class HisbuttonInInterServiceImpl implements HisbuttonInInterService{
	

	@Autowired
	@Qualifier(value="hisbuttonInInterDao")
	private HisbuttonInInterDao hisbuttonInInterDao;
	/**  
	 *  
	 * @Description：    获得该栏目拥有的按钮
	 * @Author：zhangjin
	 * @CreateDate：2016-7-1  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysMenuButton> getListByMenuId(String id) {
		return hisbuttonInInterDao.getListByMenuId(id);
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public SysMenuButton get(String id) {
		return null;
	}
	@Override
	public void saveOrUpdate(SysMenuButton entity) {
		
	}
	
	
}
