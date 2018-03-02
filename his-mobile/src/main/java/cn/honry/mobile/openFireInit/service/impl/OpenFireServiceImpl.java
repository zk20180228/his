package cn.honry.mobile.openFireInit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.Ofuser;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.mobile.openFireInit.dao.OpenFireInitDao;
import cn.honry.mobile.openFireInit.service.OpenFireInitService;

@Service("openFireInitService")
public class OpenFireServiceImpl implements OpenFireInitService{
	@Resource
	private OpenFireInitDao  openFireInitDao;
	
	@Resource
	private UserInInterDAO userInInterDAO;

	@Override
	public void delOfUserAccount(String account1, String account2) throws Exception {
		openFireInitDao.delOfUserAccount(account1,account2);
	}

	@Override
	public Ofuser get(String userAoounnt) {
		return openFireInitDao.get(userAoounnt);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(Ofuser arg0) {
		if(StringUtils.isNotBlank(arg0.getUserName())){
			openFireInitDao.update(arg0);
		}else {
			openFireInitDao.save(arg0);
		}
		
	}

	@Override
	public List<User> findAllUsers() {
		return userInInterDAO.getAll();
	}

	@Override
	public List<User> findUsers() {
		return openFireInitDao.findUsers();
	}

}
