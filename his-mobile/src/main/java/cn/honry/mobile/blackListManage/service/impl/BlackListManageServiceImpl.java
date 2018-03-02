package cn.honry.mobile.blackListManage.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.mobile.blackListManage.dao.BlackListManageDao;
import cn.honry.mobile.blackListManage.service.BlackListManageService;
import cn.honry.mobile.whiteListManage.dao.WhiteListManageDao;
import cn.honry.utils.ShiroSessionUtils;

@Service("blackListManageService")
public class BlackListManageServiceImpl  implements BlackListManageService{

	@Resource
	private BlackListManageDao blackListManageDao;
	@Resource
	private WhiteListManageDao whiteListManageDao;
	
	@Override
	public MMobileTypeManage get(String arg0) {
		return blackListManageDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(MMobileTypeManage arg0) {
	}

	@Override
	public List<MMobileTypeManage> getBlackManageList(String rows, String page,
			String mobileCategory) throws Exception {
		return blackListManageDao.getBlackManageList(rows, page, mobileCategory);
	}

	@Override
	public Integer getBlackManageCount(String mobileCategory) throws Exception {
		return blackListManageDao.getBlackManageCount(mobileCategory);
	}
	
	@Override
	public void delBlackManage(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MMobileTypeManage mobileTypeManage  = null;
		for (String mobileId : id) {
			mobileTypeManage = blackListManageDao.get(mobileId);
			mobileTypeManage.setDel_flg(1);
			mobileTypeManage.setDeleteTime(new Date());
			mobileTypeManage.setDeleteUser(account);
			blackListManageDao.update(mobileTypeManage);
		}
		
	}

	@Override
	public void save(MMobileTypeManage mobileTypeManage, String flg) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if("1".equals(flg)){
			List<MMobileTypeManage> mBlackList = whiteListManageDao.checkExist(mobileTypeManage.getMobileCategory(),"1");
			if(mBlackList!=null){
				for(int i=0;i<mBlackList.size();i++){
					MMobileTypeManage mBlack=mBlackList.get(i);
					mBlack.setDel_flg(1);
					mBlack.setUpdateTime(new Date());
					mBlack.setUpdateUser(account);
					blackListManageDao.update(mBlack);
				}
			}
		}
		if(StringUtils.isNotBlank(mobileTypeManage.getId())){
			MMobileTypeManage mBlack = blackListManageDao.get(mobileTypeManage.getId());
			mBlack.setMobileCategory(mobileTypeManage.getMobileCategory());
			mBlack.setMobileRemark(mobileTypeManage.getMobileRemark());
			blackListManageDao.update(mBlack);
		}else{
			//黑名单添加
			mobileTypeManage.setId(null);
			mobileTypeManage.setType("2");
			mobileTypeManage.setCreateTime(new Date());
			mobileTypeManage.setCreateUser(account);
			mobileTypeManage.setCreateDept(dept);
			mobileTypeManage.setDel_flg(0);
			mobileTypeManage.setStop_flg(0);
			blackListManageDao.save(mobileTypeManage);
		
		}
	}
	
	@Override
	public void moveWhite(String ids) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MMobileTypeManage mobileTypeManage  = null;
		for (String mobileId : id) {
			mobileTypeManage = blackListManageDao.get(mobileId);
			mobileTypeManage.setType("1");
			mobileTypeManage.setUpdateTime(new Date());
			mobileTypeManage.setUpdateUser(account);
			blackListManageDao.update(mobileTypeManage);
		}
		
	}

	@Override
	public List<String> synCach() throws Exception {
		return blackListManageDao.getBlackList();
		
	}
}
