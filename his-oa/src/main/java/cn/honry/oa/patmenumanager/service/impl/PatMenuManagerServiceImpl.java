package cn.honry.oa.patmenumanager.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaMenu;
import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.patmenumanager.dao.PatMenuManagerDao;
import cn.honry.oa.patmenumanager.dao.PatMenuRightDao;
import cn.honry.oa.patmenumanager.service.PatMenuManagerService;
import cn.honry.oa.patmenumanager.vo.MenuVo;
import cn.honry.utils.ShiroSessionUtils;

@Service("patMenuManagerService")
@Transactional
public class PatMenuManagerServiceImpl implements PatMenuManagerService {

	@Autowired
	@Qualifier(value = "patMenuManagerDao")
	private PatMenuManagerDao patMenuManagerDao;

	@Autowired
	@Qualifier(value = "patMenuRightDao")
	private PatMenuRightDao patMenuRightDao;

	@Override
	public MenuVo showTree(String pid) throws Exception {
		MenuVo root = new MenuVo();
		List<MenuVo> parentnodes = new ArrayList<MenuVo>();// parentnodes存放所有的父节点
		if ("".equals(pid)) {// 所用父级节点
			List<MenuVo> list = patMenuManagerDao.findAllChild("1");
			parentnodes = digui(list);
		}

		root.setId("1");
		root.setName("患者服务");
		root.setChildren(parentnodes);
		return root;
	}

	// 递归形成专业树
	public List<MenuVo> digui(List<MenuVo> tree) throws Exception {
		for (MenuVo academy : tree) {
			if (academy.getPath() != null) {
				List<MenuVo> _tree = patMenuManagerDao.findAllChild(academy
						.getPath());
				academy.setChildren(_tree);
				digui(_tree);
			}
		}
		return tree;
	}

	// 递归形成专业树
	public List<MenuVo> diguiWithCode(List<MenuVo> tree) throws Exception {
		for (MenuVo academy : tree) {
			if (academy.getPath() != null) {
				List<MenuVo> _tree = patMenuManagerDao
						.findAllChildWithCode(academy.getPath());
				academy.setChildren(_tree);
				diguiWithCode(_tree);
			}
		}
		return tree;
	}

	@Override
	public MenuVo showTreeWithCode(String pid) throws Exception {
		MenuVo root = new MenuVo();
		List<MenuVo> parentnodes = new ArrayList<MenuVo>();// parentnodes存放所有的父节点
		if ("".equals(pid)) {// 所用父级节点
			List<MenuVo> list = patMenuManagerDao.findAllChild("1");
			parentnodes = diguiWithCode(list);
		}

		root.setId("1");
		root.setName("栏目管理");
		root.setState("close");
		root.setChildren(parentnodes);
		return root;
	}

	@Override
	public void delMenu(MenuVo menu) throws Exception {
		// 获取当前节点下的所有子级节点
		List<MenuVo> voList = patMenuManagerDao.queryMenuById(menu.getId());
		String code = voList.get(0).getCode();
		List<MenuVo> list = patMenuManagerDao.findAllChilds(code);
		String ids = menu.getId();
		if (list != null && list.size() > 0) {
			for (MenuVo menuVo : list) {
				ids += "," + menuVo.getId();
			}
		}
		String codes = menu.getCode();
		if (list != null && list.size() > 0) {
			for (MenuVo menuVo : list) {
				codes += "," + menuVo.getCode();
			}
		}
//		patMenuManagerDao.delChildMenu(code);
//		patMenuManagerDao.delMenu(code);
		patMenuManagerDao.del(ids, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		// 删除栏目对应的权限
		String[] idarr = codes.split(",");
		for (String sid : idarr) {
			List<OaMenuRight> listRight = patMenuRightDao.findAllByMenuid(sid);
			Iterator iterList = listRight.iterator();// List接口实现了Iterable接口
			while (iterList.hasNext()) {
				patMenuRightDao.remove(iterList.next());
			}
		}
	}

	@Override
	public List<MenuVo> queryMenuByParentcode(String code) {

		List<MenuVo> listVo = patMenuManagerDao.queryMenuByParentcode(code);
		List<MenuVo> list = null;
		if (listVo != null && listVo.size() > 0) {
			list = listVo;
		} else {
			list = patMenuManagerDao.queryMenuByCode(code);
		}
		return list;
	}

	@Override
	public void save(MenuVo menu) throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptId = dept == null ? "" : dept.getDeptCode();
		if (StringUtils.isNotBlank(menu.getId())) {
			// id不为空 走修改方法
			List<MenuVo> listvo = patMenuManagerDao.queryMenuById(menu.getId());
			//栏目code没有修改
			String newCode = menu.getCode();
			String oldCode = listvo.get(0).getCode();
			if(newCode.equals(oldCode)){
				//父级修改
				if (!menu.getParent().equals(menu.getParentcode())){
					List<MenuVo> volist = patMenuManagerDao.queryMenuByCode(menu.getParent());
					MenuVo vo = volist.get(0);
					// 当前节点下的所有子节点
					List<OaMenu> list = patMenuManagerDao.queryAllChildren(menu.getPath());
					for (OaMenu oamenu : list) {
						oamenu.setParentpath(oamenu.getParentpath().replace(
								menu.getPath(), vo.getPath() + "," + menu.getCode()));
						oamenu.setPath(oamenu.getPath().replace(menu.getPath(),
								vo.getPath() + "," + menu.getCode()));
					}
					if ("1".equals(menu.getParent())) {
						menu.setParentpath("1");
						menu.setParentcode("1");
						menu.setPath("1," + menu.getCode());
					} else {
						menu.setParentpath(vo.getPath());
						menu.setParentcode(vo.getCode());
						menu.setPath(vo.getPath() + "," + menu.getCode());
					}
					patMenuManagerDao.saveOrUpdateList(list);
					OaMenu newMenu = patMenuManagerDao.get(menu.getId());
					newMenu.setName(menu.getName());
					newMenu.setDept(menu.getDept());
					newMenu.setId(menu.getId());
					newMenu.setCode(menu.getCode());
					newMenu.setDel_flg(0);
					newMenu.setPath(menu.getPath());
					newMenu.setParentpath(vo.getPath());
					newMenu.setParentcode(menu.getParentcode());
					newMenu.setStop_flag(menu.getStop_flag());
					newMenu.setExplain(menu.getExplain());
					newMenu.setMcomment(menu.getMcomment());
					newMenu.setPublishdirt(menu.getPublishdirt());
					newMenu.setMorder(menu.getMorder());
					newMenu.setUpdateTime(new Date());
					newMenu.setUpdateUser(userId);
					newMenu.setType("1");
					patMenuManagerDao.save(newMenu);
					OperationUtils.getInstance().conserve(menu.getId(),"OA栏目管理","UPDATE","T_OA_MENU",OperationUtils.LOGACTIONUPDATE);
				}else{// 父级没有修改
					OaMenu newMenu = patMenuManagerDao.get(menu.getId());
					newMenu.setName(menu.getName());
					newMenu.setDept(menu.getDept());
					newMenu.setId(menu.getId());
					newMenu.setCode(menu.getCode());
					newMenu.setDel_flg(0);
					newMenu.setStop_flag(menu.getStop_flag());
					newMenu.setExplain(menu.getExplain());
					newMenu.setMcomment(menu.getMcomment());
					newMenu.setPublishdirt(menu.getPublishdirt());
					newMenu.setMorder(menu.getMorder());
					newMenu.setParentpath(menu.getParentpath());
					newMenu.setParentcode(menu.getParentcode());
					newMenu.setPath(menu.getPath());
					newMenu.setUpdateTime(new Date());
					newMenu.setUpdateUser(userId);
					newMenu.setType("1");
					patMenuManagerDao.save(newMenu);
					OperationUtils.getInstance().conserve(menu.getId(),"OA栏目管理","UPDATE","T_OA_MENU",OperationUtils.LOGACTIONUPDATE);
				}
				// 修改添加权限
				List<OaMenuRight> list = patMenuRightDao.findAllByMenuid(menu
						.getCode());
				Iterator iterList = list.iterator();// List接口实现了Iterable接口
				while (iterList.hasNext()) {
					patMenuRightDao.remove(iterList.next());
				}

				OaMenuRight menuViewRight = new OaMenuRight();
				OaMenuRight menuPublishRight = new OaMenuRight();
				OaMenuRight menuCheckRight = new OaMenuRight();
				menuViewRight.setMenuId(menu.getCode());
				menuPublishRight.setMenuId(menu.getCode());
				menuCheckRight.setMenuId(menu.getCode());
				menuPublishRight.setId(null);
				menuCheckRight.setId(null);
				menuViewRight.setId(null);

				String view = menu.getMview();
				String publish = menu.getMpublish();

				String check = menu.getMcheck();
				fixString(menuCheckRight, check, "2");
				fixString(menuPublishRight, publish, "1");
				fixString(menuViewRight, "0", "3");
			}else{
				//栏目修改
				//父级修改
				if (!menu.getParent().equals(menu.getParentcode())){
					List<MenuVo> volist = patMenuManagerDao.queryMenuByCode(menu.getParent());
					MenuVo vo = volist.get(0);
					// 当前节点下的所有子节点
					List<OaMenu> list = patMenuManagerDao.findNoFirst(menu.getPath());
					for (OaMenu oamenu : list) {
						vo.setParentcode(newCode);
						oamenu.setParentpath(oamenu.getParentpath().replace(menu.getPath(), vo.getPath() + "," + menu.getCode()));
						oamenu.setPath(oamenu.getPath().replace(menu.getPath(),vo.getPath() + "," + menu.getCode()));
					}
					patMenuManagerDao.saveOrUpdateList(list);
					List<OaMenu> list2 = patMenuManagerDao.findNoFirst(menu.getPath());
					for (OaMenu oamenu : list2) {
						oamenu.setParentpath(oamenu.getParentpath().replace(menu.getPath(), vo.getPath() + "," + menu.getCode()));
						oamenu.setPath(oamenu.getPath().replace(menu.getPath(),vo.getPath() + "," + menu.getCode()));
					}
					patMenuManagerDao.saveOrUpdateList(list2);
					
					if ("1".equals(menu.getParent())) {
						menu.setParentpath("1");
						menu.setParentcode("1");
						menu.setPath("1," + menu.getCode());
					} else {
						menu.setParentpath(vo.getPath());
						menu.setParentcode(vo.getCode());
						menu.setPath(vo.getPath() + "," + menu.getCode());
					}
					OaMenu newMenu = patMenuManagerDao.get(menu.getId());
					newMenu.setName(menu.getName());
					newMenu.setDept(menu.getDept());
					newMenu.setId(menu.getId());
					newMenu.setCode(menu.getCode());
					newMenu.setDel_flg(0);
					newMenu.setPath(menu.getPath());
					newMenu.setParentpath(menu.getParentpath());
					newMenu.setParentcode(menu.getParentcode());
					newMenu.setStop_flag(menu.getStop_flag());
					newMenu.setExplain(menu.getExplain());
					newMenu.setMcomment(menu.getMcomment());
					newMenu.setPublishdirt(menu.getPublishdirt());
					newMenu.setMorder(menu.getMorder());
					newMenu.setUpdateTime(new Date());
					newMenu.setUpdateUser(userId);
					newMenu.setType("1");
					patMenuManagerDao.save(newMenu);
					OperationUtils.getInstance().conserve(menu.getId(),"OA栏目管理","UPDATE","T_OA_MENU",OperationUtils.LOGACTIONUPDATE);
				}else{// 父级没有修改
					// 当前节点下的所有子节点
					List<OaMenu> list = patMenuManagerDao.findFirst(menu.getPath());
					for (OaMenu vo : list) {
						vo.setParentcode(newCode);
						vo.setPath(vo.getPath().replace(oldCode, newCode));
						vo.setParentpath(vo.getParentpath().replace(oldCode, newCode));
					}
					patMenuManagerDao.saveOrUpdateList(list);
					List<OaMenu> lists = patMenuManagerDao.findNoFirst(menu.getPath());
					for (OaMenu vo : lists) {
						vo.setPath(vo.getPath().replace(oldCode, newCode));
						vo.setParentpath(vo.getParentpath().replace(oldCode, newCode));
					}
					patMenuManagerDao.saveOrUpdateList(lists);
					OaMenu newMenu = patMenuManagerDao.get(menu.getId());
					newMenu.setName(menu.getName());
					newMenu.setDept(menu.getDept());
					newMenu.setId(menu.getId());
					newMenu.setCode(menu.getCode());
					newMenu.setDel_flg(0);
					newMenu.setStop_flag(menu.getStop_flag());
					newMenu.setExplain(menu.getExplain());
					newMenu.setMcomment(menu.getMcomment());
					newMenu.setPublishdirt(menu.getPublishdirt());
					newMenu.setMorder(menu.getMorder());
					newMenu.setParentpath(menu.getParentpath());
					newMenu.setParentcode(menu.getParentcode());
					newMenu.setPath(menu.getPath().replace(oldCode, newCode));
					newMenu.setUpdateTime(new Date());
					newMenu.setUpdateUser(userId);
					newMenu.setType("1");
					patMenuManagerDao.save(newMenu);
					OperationUtils.getInstance().conserve(menu.getId(),"OA栏目管理","UPDATE","T_OA_MENU",OperationUtils.LOGACTIONUPDATE);
				}
				// 修改添加权限
				List<OaMenuRight> list = patMenuRightDao.findAllByMenuid(oldCode);
				Iterator iterList = list.iterator();// List接口实现了Iterable接口
				while (iterList.hasNext()) {
					patMenuRightDao.remove(iterList.next());
				}

				OaMenuRight menuViewRight = new OaMenuRight();
				OaMenuRight menuPublishRight = new OaMenuRight();
				OaMenuRight menuCheckRight = new OaMenuRight();
				menuViewRight.setMenuId(menu.getCode());
				menuPublishRight.setMenuId(menu.getCode());
				menuCheckRight.setMenuId(menu.getCode());
				menuPublishRight.setId(null);
				menuCheckRight.setId(null);
				menuViewRight.setId(null);

				String view = menu.getMview();
				String publish = menu.getMpublish();

				String check = menu.getMcheck();
				fixString(menuCheckRight, check, "2");
				fixString(menuPublishRight, publish, "1");
				fixString(menuViewRight, "0", "3");
			}

		} else {
			// 如果id为空 走添加方法
			OaMenu newMenu = new OaMenu();
			// 如果父级为根节点
			if ("1".equals(menu.getParent())) {
				newMenu.setName(menu.getName());
				newMenu.setParentpath("1");
				newMenu.setParentcode("1");
				newMenu.setDept(menu.getDept());
				newMenu.setId(menu.getId());
				newMenu.setStop_flag(menu.getStop_flag());
				newMenu.setExplain(menu.getExplain());
				newMenu.setMcomment(menu.getMcomment());
				newMenu.setDel_flg(0);
				newMenu.setPublishdirt(menu.getPublishdirt());
				Integer max = patMenuManagerDao.getMaxOrder();
				newMenu.setMorder(max + 1);
				newMenu.setId(null);
				newMenu.setCode(menu.getCode());
				newMenu.setCreateTime(new Date());
				newMenu.setCreateDept(deptId);
				newMenu.setType("1");
				newMenu.setCreateUser(userId);
				patMenuManagerDao.save(newMenu);

				newMenu.setCode(newMenu.getCode());
				newMenu.setPath("1," + newMenu.getCode());
			} else {
				List<MenuVo> voList = patMenuManagerDao.queryMenuByCode(menu
						.getParent());
				MenuVo vo = null;
				if (voList != null && voList.size() > 0) {
					vo = voList.get(0);
					newMenu.setName(menu.getName());
					newMenu.setParentpath(vo.getPath());
					newMenu.setParentcode(vo.getCode());
					newMenu.setDept(menu.getDept());
					newMenu.setId(menu.getId());
					newMenu.setStop_flag(menu.getStop_flag());
					newMenu.setExplain(menu.getExplain());
					newMenu.setMcomment(menu.getMcomment());
					newMenu.setPublishdirt(menu.getPublishdirt());
					newMenu.setCode(menu.getCode());
					Integer max = patMenuManagerDao.getMaxOrder();
					newMenu.setMorder(max + 1);
					newMenu.setDel_flg(0);
					newMenu.setId(null);
					newMenu.setCreateUser(userId);
					newMenu.setCreateDept(deptId);
					newMenu.setCreateTime(new Date());
					newMenu.setType("1");
					patMenuManagerDao.save(newMenu);
					newMenu.setPath(vo.getPath() + "," + newMenu.getCode());
				}
			}
			
			newMenu.setType("1");
			patMenuManagerDao.save(newMenu);
			OperationUtils.getInstance().conserve(menu.getId(),"OA栏目管理","INSERT INTO","T_OA_MENU",OperationUtils.LOGACTIONUPDATE);
			// 添加权限
			OaMenuRight menuViewRight = new OaMenuRight();
			OaMenuRight menuPublishRight = new OaMenuRight();
			OaMenuRight menuCheckRight = new OaMenuRight();

			menuViewRight.setMenuId(newMenu.getCode());
			menuPublishRight.setMenuId(newMenu.getCode());
			menuCheckRight.setMenuId(newMenu.getCode());

			menuViewRight.setId(null);
			menuPublishRight.setId(null);
			menuCheckRight.setId(null);

			String view = menu.getMview();
			String publish = menu.getMpublish();
			String check = menu.getMcheck();
			fixString(menuCheckRight, check, "2");
			fixString(menuPublishRight, publish, "1");
			fixString(menuViewRight, "0", "3");

		}
	}

	/**
	 * <p>
	 * 解析前台传递的字符串,并存到数据库
	 * </p>
	 * 
	 * @Author: yuke
	 * @CreateDate: 2017年7月23日 下午5:45:40
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月23日 下午5:45:40
	 * @ModifyRmk:
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 *
	 */
	private void fixString(OaMenuRight right, String view, String type)
			throws IllegalAccessException, InvocationTargetException {
		String[] varr = view.split("#");
		for (String s : varr) {
			String[] s1 = s.split("_");
			if ("0".equals(s1[0]) && s1.length > 1) {
				OaMenuRight menuRight = new OaMenuRight();
				BeanUtils.copyProperties(menuRight, right);
				menuRight.setRightType(type);
				menuRight.setRight("0");
				menuRight.setCode("all");
				patMenuRightDao.save(menuRight);
			}
			if ("1".equals(s1[0]) && s1.length > 1) {
				String[] s2 = s1[1].split(",");
				for (String ss : s2) {
					OaMenuRight menuRight = new OaMenuRight();
					BeanUtils.copyProperties(menuRight, right);
					menuRight.setRightType(type);
					menuRight.setRight("1");
					String[] sss = ss.split(":");
					menuRight.setCode(sss[1]);
					menuRight.setCodeName(sss[0]);
					patMenuRightDao.save(menuRight);
				}
			}
			if ("2".equals(s1[0]) && s1.length > 1) {
				String[] s2 = s1[1].split(",");
				for (String ss : s2) {
					OaMenuRight menuRight = new OaMenuRight();
					BeanUtils.copyProperties(menuRight, right);
					menuRight.setRightType(type);
					menuRight.setRight("2");
					String[] sss = ss.split(":");
					menuRight.setCode(sss[1]);
					menuRight.setCodeName(sss[0]);
					patMenuRightDao.save(menuRight);
				}
			}
			if ("3".equals(s1[0]) && s1.length > 1) {
				String[] s2 = s1[1].split(",");
				for (String ss : s2) {
					OaMenuRight menuRight = new OaMenuRight();
					BeanUtils.copyProperties(menuRight, right);
					menuRight.setRightType(type);
					menuRight.setRight("3");
					String[] sss = ss.split(":");
					menuRight.setCode(sss[1]);
					menuRight.setCodeName(sss[0]);
					patMenuRightDao.save(menuRight);
				}
			}
			if ("4".equals(s1[0]) && s1.length > 1) {
				String[] s2 = s1[1].split(",");
				for (String ss : s2) {
					OaMenuRight menuRight = new OaMenuRight();
					BeanUtils.copyProperties(menuRight, right);
					menuRight.setRightType(type);
					menuRight.setRight("4");
					String[] sss = ss.split(":");
					menuRight.setCode(sss[1]);
					menuRight.setCodeName(sss[0]);
					patMenuRightDao.save(menuRight);
				}
			}
		}
	}

	@Override
	public MenuVo getById(String id) {
		return null;
	}

	@Override
	public List<MenuVo> queryMenuByCode(String code) {
		return patMenuManagerDao.queryMenuByCode(code);
	}

	@Override
	public List<MenuVo> queryMenuById(String id) {
		return patMenuManagerDao.queryMenuById(id);
	}

	@Override
	public MenuVo showTreeTwo(String pid) throws Exception {
		MenuVo root = new MenuVo();
		List<MenuVo> parentnodes = new ArrayList<MenuVo>();// parentnodes存放所有的父节点
		if ("".equals(pid)) {// 所用父级节点
			List<MenuVo> list = patMenuManagerDao.findAllChildWithOutStop("1");
			parentnodes = diguiTreeTwo(list);
		}

		root.setId("1");
		root.setName("患者服务");
		root.setChildren(parentnodes);
		return root;
	}
	// 递归形成专业树
		public List<MenuVo> diguiTreeTwo(List<MenuVo> tree) throws Exception {
			for (MenuVo academy : tree) {
				if (academy.getPath() != null) {
					List<MenuVo> _tree = patMenuManagerDao.findAllChildWithOutStop(academy
							.getPath());
					academy.setChildren(_tree);
					diguiTreeTwo(_tree);
				}
			}
			return tree;
		}

		@Override
		public void moveUp(MenuVo menu) {
			MenuVo vo = patMenuManagerDao.getLast(menu.getMorder(),menu.getParentcode());
			OaMenu lastMenu = patMenuManagerDao.get(vo.getId());
			OaMenu thisMenu = patMenuManagerDao.get(menu.getId());
			Integer lsatOrder = lastMenu.getMorder();
			Integer thisOrer = menu.getMorder();
			lastMenu.setMorder(thisOrer);
			thisMenu.setMorder(lsatOrder);
			patMenuManagerDao.save(lastMenu);
			patMenuManagerDao.save(thisMenu);
		}

		@Override
		public void moveDown(MenuVo menu) {
			MenuVo vo = patMenuManagerDao.getNext(menu.getMorder(),menu.getParentcode());
			OaMenu nextMenu = patMenuManagerDao.get(vo.getId());
			OaMenu thisMenu = patMenuManagerDao.get(menu.getId());
			Integer nextOrder = nextMenu.getMorder();
			Integer thisOrer = menu.getMorder();
			nextMenu.setMorder(thisOrer);
			thisMenu.setMorder(nextOrder);
			patMenuManagerDao.save(nextMenu);
			patMenuManagerDao.save(thisMenu);
			
		}


}
