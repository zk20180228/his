package cn.honry.inner.oa.userSign.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaUserSign;
import cn.honry.inner.oa.userSign.dao.UserSigninnerDao;
import cn.honry.inner.oa.userSign.service.UserSigninnerService;
import cn.honry.inner.operation.arrange.dao.OperationArrangeInnerDAO;

@Service("userSigninnerService")
@Transactional
@SuppressWarnings({ "all" })
public class UserSigninnerServiceImpl implements UserSigninnerService{
	
	@Autowired
	@Qualifier(value = "userSigninnerDao")
	private UserSigninnerDao userSigninnerDao;

	@Override
	public List<OaUserSign> queryOaUserSigns(String account, String signType) {
		return userSigninnerDao.queryOaUserSigns(account, signType);
	}

}
