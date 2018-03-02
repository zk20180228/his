package cn.honry.mobile.menuIcon.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MenuIcon;
import cn.honry.mobile.menuIcon.dao.MenuIconDao;
import cn.honry.mobile.menuIcon.service.MenuIconService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Service("menuIconService")
public class MenuIconServiceImpl implements MenuIconService {

	@Resource
	private MenuIconDao menuIconDao;
	
	@Override
	public MenuIcon get(String arg0) {
		return menuIconDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(MenuIcon arg0) {
		
	}

	@Override
	public List<MenuIcon> getMenuIconList(String rows, String page,
			String queryName) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String serverPath = HisParameters.getfileURI(request);
		List<MenuIcon> list=menuIconDao.getMenuIconList( rows,page,queryName);
		for(int i=0;i<list.size();i++){
			if(StringUtils.isNotBlank(list.get(i).getPicPath())){
				list.get(i).setPicShow(serverPath+list.get(i).getPicPath());
			}
		}
		return list;
	}

	@Override
	public Integer getMenuIconCount(String queryName) throws Exception {
		return menuIconDao.getMenuIconCount( queryName);
	}

	@Override
	public void saveMenuIcon(MenuIcon menuIcon)  throws Exception{
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isNotBlank(menuIcon.getId())){
			MenuIcon mIcon =menuIconDao.get(menuIcon.getId());
			mIcon.setPicName(menuIcon.getPicName());
			if(StringUtils.isNotBlank(menuIcon.getPicPath())){
				mIcon.setPicPath(menuIcon.getPicPath());
			}
			mIcon.setUpdateUser(account);
			mIcon.setUpdateTime(new Date());
			menuIconDao.update(mIcon);
		}else{
			menuIcon.setId(null);
			menuIcon.setCreateTime(new Date());
			menuIcon.setCreateDept(deptCode);
			menuIcon.setCreateUser(account);
			menuIcon.setDel_flg(0);
			menuIcon.setStop_flg(0);
			menuIconDao.save(menuIcon);
		}
		
	}

	@Override
	public void delMenuIcon(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MenuIcon mIcon  = null;
		for (String menuId : id) {
			mIcon = menuIconDao.get(menuId);
			mIcon.setDel_flg(1);
			mIcon.setDeleteTime(new Date());
			mIcon.setDeleteUser(account);
			menuIconDao.update(mIcon);
		}
		
		
	}

	@Override
	public String ckeckName(String ckeckName,String id) throws Exception {
		List<MenuIcon> list=menuIconDao.ckeckName(ckeckName);
		if(StringUtils.isNotBlank(id)){
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if(!id.equals(list.get(i).getId())){
						return "error";
					}
				}
			}
		}else{
			if(list!=null&&list.size()>0){
				return "error";
			}
		}
		return "ok";
	}

}
