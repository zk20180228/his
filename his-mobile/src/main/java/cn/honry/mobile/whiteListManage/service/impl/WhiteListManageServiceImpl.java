package cn.honry.mobile.whiteListManage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.mobile.whiteListManage.dao.WhiteListManageDao;
import cn.honry.mobile.whiteListManage.service.WhiteListManageService;
import cn.honry.utils.ShiroSessionUtils;

@Service("whiteListManageService")
public class WhiteListManageServiceImpl implements WhiteListManageService{

	@Resource
	private WhiteListManageDao whiteListManageDao;
	
	@Override
	public MMobileTypeManage get(String arg0) {
		return whiteListManageDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(MMobileTypeManage arg0) {
	}

	@Override
	public List<MMobileTypeManage> getWhiteManageList(String rows, String page,
			String mobileCategory) throws Exception {
		return whiteListManageDao.getWhiteManageList(rows, page, mobileCategory);
	}

	@Override
	public Integer getWhiteManageCount(String mobileCategory) throws Exception {
		return whiteListManageDao.getWhiteManageCount(mobileCategory);
	}

	@Override
	public void delWhiteManage(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MMobileTypeManage mobileTypeManage  = null;
		for (String mobileId : id) {
			mobileTypeManage = whiteListManageDao.get(mobileId);
			mobileTypeManage.setDel_flg(1);
			mobileTypeManage.setDeleteTime(new Date());
			mobileTypeManage.setDeleteUser(account);
			whiteListManageDao.update(mobileTypeManage);
		}
		
	}

	@Override
	public List<MMobileTypeManage> checkExist(String mobileCategory,String type)
			throws Exception {
		return whiteListManageDao.checkExist(mobileCategory,type);
	}

	@Override
	public void save(MMobileTypeManage mobileTypeManage, String flg) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if("1".equals(flg)){
			List<MMobileTypeManage> mBlackList = whiteListManageDao.checkExist(mobileTypeManage.getMobileCategory(),"2");
			if(mBlackList!=null){
				for(int i=0;i<mBlackList.size();i++){
					MMobileTypeManage mBlack=mBlackList.get(i);
					mBlack.setDel_flg(1);
					mBlack.setUpdateTime(new Date());
					mBlack.setUpdateUser(account);
					whiteListManageDao.update(mBlack);
				}
			}
		}
		if(StringUtils.isNotBlank(mobileTypeManage.getId())){
			MMobileTypeManage mWhite = whiteListManageDao.get(mobileTypeManage.getId());
			mWhite.setMobileCategory(mobileTypeManage.getMobileCategory());
			mWhite.setMobileRemark(mobileTypeManage.getMobileRemark());
			whiteListManageDao.update(mWhite);
		}else{
			//白名单添加
			mobileTypeManage.setId(null);
			mobileTypeManage.setType("1");
			mobileTypeManage.setCreateTime(new Date());
			mobileTypeManage.setCreateUser(account);
			mobileTypeManage.setCreateDept(dept);
			mobileTypeManage.setDel_flg(0);
			mobileTypeManage.setStop_flg(0);
			whiteListManageDao.save(mobileTypeManage);
		
		}
	}

	@Override
	public void initData() throws Exception {
		List<String> list =whiteListManageDao.getInitData();
		whiteListManageDao.clearData();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		for(int i=0;i<list.size();i++){
			MMobileTypeManage  mManage=new MMobileTypeManage();
			mManage.setId(null);
			mManage.setMobileCategory(list.get(i));
			mManage.setType("1");
			mManage.setDel_flg(0);
			mManage.setStop_flg(0);
			mManage.setCreateDept(dept);
			mManage.setCreateUser(account);
			mManage.setCreateTime(new Date());
			whiteListManageDao.save(mManage);
		}
	}

	@Override
	public void moveBlack(String ids) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MMobileTypeManage mobileTypeManage  = null;
		for (String mobileId : id) {
			mobileTypeManage = whiteListManageDao.get(mobileId);
			mobileTypeManage.setType("2");
			mobileTypeManage.setUpdateTime(new Date());
			mobileTypeManage.setUpdateUser(account);
			whiteListManageDao.update(mobileTypeManage);
		}
		
	}

}
