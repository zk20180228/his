package cn.honry.mobile.mobileBlackList.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.bean.model.MMobileBlackList;
import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.base.bean.model.MWhiteList;
import cn.honry.base.bean.model.MenuIcon;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.mobileBlackList.dao.MobileBlackListDao;
import cn.honry.mobile.mobileBlackList.service.MobileBlackListService;
import cn.honry.utils.ShiroSessionUtils;

@Service("mobileBlackListService")
public class MobileBlackListServiceImpl  implements MobileBlackListService {

	@Resource
	private MobileBlackListDao mobileBlackListDao;
	
	@Override
	public MMobileBlackList get(String arg0) {
		return mobileBlackListDao.get(arg0);
	}
	
	@Resource
	private MoOperateLogDao moOperateLogDao;

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(MMobileBlackList arg0) {
	}

	@Override
	public List<MMobileBlackList> getCellPhoneBlack(String rows, String page,
			String queryName, String type) throws Exception {
		return mobileBlackListDao.getCellPhoneBlack(rows, page, queryName, type);
	}

	@Override
	public Integer getCellPhoneBlackCount(String queryName, String type) throws Exception  {
		return mobileBlackListDao.getCellPhoneBlackCount(queryName, type);
	}

	@Override
	public void save(MMobileBlackList mobileBlackList) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		if(StringUtils.isNotBlank(mobileBlackList.getId())){
			MMobileBlackList mobileBlack  = mobileBlackListDao.get(mobileBlackList.getId());
			mobileBlack.setMobileNum(mobileBlackList.getMobileNum());
			mobileBlack.setMobileRemark(mobileBlackList.getMobileRemark());
			mobileBlack.setType(mobileBlackList.getType());
			mobileBlack.setUpdateUser(name);
			mobileBlack.setUpdateTime(new Date());
			mobileBlackListDao.update(mobileBlack);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("更新","手机号（短信）黑名单管理",account,"UPDATE","M_MOBILEBLACKLIST","",""));
		}else{
			mobileBlackList.setId(null);
			mobileBlackList.setCreateTime(new Date());
			mobileBlackList.setCreateUser(name);
			mobileBlackList.setDel_flg(0);
			mobileBlackList.setStop_flg(0);
			mobileBlackListDao.save(mobileBlackList);
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("添加","手机号（短信）黑名单管理",account,"INSERT INTO","M_MOBILEBLACKLIST","",""));
		}
		
	}

	@Override
	public List<MMobileBlackList> checkExist(String mobileNum, String type) throws Exception {
		return mobileBlackListDao.checkExist(mobileNum, type);
	}

	@Override
	public void delCellPhoneBlack(String ids) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] id = ids.split(",");
		MMobileBlackList mobileBlack  = null;
		for (String mobileId : id) {
			mobileBlack = mobileBlackListDao.get(mobileId);
			mobileBlack.setDel_flg(1);
			mobileBlack.setDeleteTime(new Date());
			mobileBlack.setDeleteUser(account);
			mobileBlackListDao.update(mobileBlack);
		}
		
	}

	@Override
	public List<String> synCach(String str) throws Exception{
		return mobileBlackListDao.synCach(str);
	}

}
