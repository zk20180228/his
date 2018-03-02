package cn.honry.mobile.personMenu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.bean.model.SysUserMenuFunjuris;
import cn.honry.inner.system.menu.dao.MenuInInterDAO;
import cn.honry.inner.system.menu.vo.ParentMenuVo;
import cn.honry.mobile.personMenu.dao.PersonMenuDAO;
import cn.honry.mobile.personMenu.service.PersonMenuService;
import cn.honry.mobile.personMenu.vo.MenuVo;
import cn.honry.mobile.personMenu.vo.UserVo;
import cn.honry.utils.JSONUtils;

import com.google.gson.reflect.TypeToken;

/**  
 *  
 * @className：UserMenuFunJurisServiceImpl
 * @Description：  用户栏目功能权限管理（移动端）
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("personMenuService")
@Transactional
@SuppressWarnings({ "all" })
public class PersonMenuServiceImpl implements PersonMenuService{
	@Autowired
	@Qualifier(value = "personMenuDAO")
	PersonMenuDAO personMenuDAO;
	
	@Autowired
	@Qualifier(value = "menuInInterDAO")
	MenuInInterDAO menuInInterDAO;
	
	@Override
	public SysUserMenuFunjuris get(String arg0) {
		return personMenuDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(SysUserMenuFunjuris arg0) {
		
	}

	@Override
	public int getUserTotal(String q) {
		return personMenuDAO.getUserTotal(q);
	}

	@Override
	public List<UserVo> getUserRows(String page, String rows, String q) {
		return personMenuDAO.getUserRows(page, rows, q);
	}

	@Override
	public List<ParentMenuVo> queryMenuCombo() {
		return personMenuDAO.queryMenuCombo();
	}

	@Override
	public List<MenuVo> queryMenuList(String parentId, String userAccount) {
		return personMenuDAO.queryMenuList(parentId, userAccount);
	}

	@Override
	public void saveMenus(String infoJson, String userAccount, String parentId) {
		List<SysUserMenuFunjuris> menuFunjuris = null;
		try {
			menuFunjuris = JSONUtils.fromJson(infoJson, new TypeToken<List<SysUserMenuFunjuris>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		SysUserMenuFunjuris funjuris = null;
		Map<String, String> map = new HashMap<String, String>();
		
		for (SysUserMenuFunjuris sysUserMenuFunjuris : menuFunjuris) {
			funjuris = personMenuDAO.getByMenuIdUserId(sysUserMenuFunjuris.getMenuAlias(), sysUserMenuFunjuris.getUserAcc());
			funjuris.setRmMenuOrder(sysUserMenuFunjuris.getRmMenuOrder());
			funjuris.setRmIsVisible(sysUserMenuFunjuris.getRmIsVisible());
			personMenuDAO.update(funjuris);
		}
		
//		/**
//		 * 消息推送  新需求不让推送    杜天亮 2017年7月31日15:55:15
//		 */
//		Map<String, Object> bodyMap = new HashMap<String, Object>();
//		OFBatchPush ofBatchPush = new OFBatchPush();
//		bodyMap.put("jid", userAccount);
//		bodyMap.put("msgType", "msg_type_column_permission");
//		String msgBody = JSONUtils.toJson(bodyMap);
//		ofBatchPush.setBody(msgBody);
//		personMenuDAO.save(ofBatchPush);
		
	}
	
}
