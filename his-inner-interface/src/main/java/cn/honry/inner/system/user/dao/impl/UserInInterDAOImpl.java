package cn.honry.inner.system.user.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * 内部接口：用户
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("userInInterDAO")
@SuppressWarnings({ "all" })
public class UserInInterDAOImpl extends HibernateEntityDao<User> implements UserInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	

	/**  
	 *  
	 * @Description： 角色对用户的添加-从科室查找用户
	 * @Author：wujiao
	 * @CreateDate：2015-8-11 下午03:11:49  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-8-11下午03:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<User> findUserByDept(String deptId) {
		String hql="from User u where u.id in " +
				   "(select e.userId from SysEmployee e where e.deptId ='"+deptId+"')";
		List<User> userList = super.findByObjectProperty(hql, null);
		if(userList!=null&&userList.size()>0){
			return userList;
		}
		return new ArrayList<User>();
	}
	
	@Override
	public List<User> getPage(String page, String rows, User entity) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}
	
	public String joint(User entity){
		String hql="FROM User u WHERE  u.del_flg = 0 AND u.stop_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getName())){
				hql = hql+" AND ( u.account LIKE '%"+entity.getName()+"%' OR u.name LIKE '%"+entity.getName()+"%'OR u.nickName LIKE '%"+entity.getName()+"%')";
			}
		}
		return hql;
	}
	
	@Override
	public int getTotal(User entity) {
		String hql= joint(entity);
		return super.getTotal(hql);
	}
	
	@Override
	public int getTotalList(String hql) {
		return super.getTotal(hql);
	}
	
	/**
	 * 根据编码查询用户信息
	 * @param code 系统编码
	 * @return
	 */
	public User getByCode(String code){
		User user = findUniqueBy("account", code);
		return user;
	}

	/**
	 * @Description:获取所有员工信息
	 * @Author: zhangjin
	 * @CreateDate: 2016年11月8日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysEmployee> getuserMap() {
		String hql=" from SysEmployee where stop_flg=0 and del_flg=0";
		List<SysEmployee> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	@Override
	public List<User> getAllUser() {
		String hql=" from User where stop_flg=0 and del_flg=0";
		List<User> list = super.find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}


	@Override
	public User getByAccount(String account) {
		String hql="from User u where u.del_flg = 0 and u.stop_flg = 0 and u.account='"+account+"'";
		List<User> userList = super.findByObjectProperty(hql, null);
		if(userList!=null&&userList.size()>0){
			return userList.get(0);
		}
		return null;
	}

	@Override
	public List<SysDepartment> getRelatedDeptAccount(String account) {
		StringBuffer sb=new StringBuffer();
		sb.append("FROM SysDepartment t where  t.deptCode in (SELECT d.deptId FROM UserLoginDept d WHERE ");
		sb.append(" d.userId = (SELECT u.id FROM User u WHERE  u.account='"+account+"' and u.del_flg = 0 and u.stop_flg = 0 )  ");
		sb.append("   ) and t.stop_flg=0 and t.del_flg=0 ");
		List<SysDepartment> list= getSession().createQuery(sb.toString()).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
}
