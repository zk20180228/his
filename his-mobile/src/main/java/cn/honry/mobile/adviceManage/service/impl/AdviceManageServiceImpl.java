package cn.honry.mobile.adviceManage.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MAdviceManage;
import cn.honry.base.bean.model.User;
import cn.honry.mobile.adviceManage.dao.AdviceManageDao;
import cn.honry.mobile.adviceManage.service.AdviceManageService;
import cn.honry.utils.ShiroSessionUtils;

@Service("adviceManageService")
public class AdviceManageServiceImpl implements AdviceManageService {

	@Resource
	private AdviceManageDao adviceManageDao;

	@Override
	public MAdviceManage get(String id) {
		return adviceManageDao.get(id);
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(MAdviceManage mAdviceManage) {
		
	}

	@Override
	public List<MAdviceManage> getMAdviceManageList(String rows, String page,
			String queryName) throws Exception {
		return adviceManageDao.getMAdviceManageList(rows,page,queryName);
	}

	@Override
	public Integer getMAdviceManageCount(String queryName) throws Exception {
		return adviceManageDao.getMAdviceManageCount(queryName);
	}

	@Override
	public void delAdviceManage(String ids)  throws Exception{
		adviceManageDao.delAdviceManage(ids);
		
	}

	@Override
	public List<User> getUserManageList(String rows, String page,String queryUser) throws Exception {
		return adviceManageDao.getUserManageList(rows,page,queryUser);
	}

	@Override
	public Integer getUserManageCount(String queryUser) throws Exception {
		return adviceManageDao.getUserManageCount(queryUser);
	}

	@Override
	public void saveAdviceManage(String userStr) {
		String userAccount=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String deptCode=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		if(StringUtils.isNotBlank(userStr)){
			List<String> userList=Arrays.asList(userStr.split(","));
			for(int i=0;i<userList.size();i++){
				String [] userStrs=userList.get(i).split("_");
				MAdviceManage mAdviceManage=new MAdviceManage();
				mAdviceManage.setUserAccount(userStrs[0]);
				mAdviceManage.setEmpName(userStrs[1]);
				mAdviceManage.setCreateUser(userAccount);
				mAdviceManage.setCreateDept(deptCode);
				mAdviceManage.setCreateTime(new Date());
				mAdviceManage.setDel_flg(0);
				mAdviceManage.setStop_flg(0);
				adviceManageDao.save(mAdviceManage);
			}
		}
	}
}
