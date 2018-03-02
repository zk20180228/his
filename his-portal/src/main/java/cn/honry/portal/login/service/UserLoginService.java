package cn.honry.portal.login.service;

import java.util.List;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.base.service.BaseService;


@SuppressWarnings({"all"})
public interface UserLoginService extends BaseService<UserLogin>{
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-17
	 * @version 1.0
	 * @return
	 */
	void saveuserlogin(UserLogin userLogin);
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  wujiao
	 * @date 2015-08-17
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	int getTotal(UserLogin userLogin);
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
	List<UserLogin> getPage(String page, String rows, UserLogin userLogin);
}
