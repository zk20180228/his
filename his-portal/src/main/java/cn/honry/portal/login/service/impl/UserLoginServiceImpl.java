package cn.honry.portal.login.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.portal.login.dao.UserLoginDAO;
import cn.honry.portal.login.service.UserLoginService;
import cn.honry.sys.menuButton.dao.MenuButtonDAO;

@Service("userLoginService")
@Transactional
@SuppressWarnings({ "all" })
public class UserLoginServiceImpl implements UserLoginService {
	@Resource
	private UserLoginDAO userLoginDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public UserLogin get(String id) {
		return userLoginDAO.get(id);
	}

	@Override
	public void saveOrUpdate(UserLogin entity) {
		userLoginDAO.save(entity);
	}
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-17
	 * @version 1.0
	 * @return
	 */
	@Override
	public void saveuserlogin(UserLogin userLogin) {
		userLoginDAO.save(userLogin);
	}
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-17
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTotal(UserLogin userLogin) {
		return userLoginDAO.getTotal(userLogin);
	}
	/**
	 * 列表查询
	 * @param page 页码
	 * @param rows 显示列表数据
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-17
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */


	@Override
	public List<UserLogin> getPage(String page, String rows, UserLogin userLogin) {
		return userLoginDAO.getPage(page, rows, userLogin);

	}
}
