package cn.honry.outpatient.registerDeptManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysMenufunction;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.registerDeptManage.dao.RegisterDeptManageDAO;
/**  
 *  
 * @Description：  栏目功能
 * @Author：liudelin
 * @CreateDate：2015-7-28 下午02:00:00
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("registerDeptManageDAO")
@SuppressWarnings({ "all" })
public class RegisterDeptManageDAOImpl extends HibernateEntityDao<SysDepartment> implements RegisterDeptManageDAO{
	/**  
	 *  
	 * @Description：  注册
	 * @Author：zpty
	 * @CreateDate：2015-11-16 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTotal(SysDepartment entity) {
		String hql=" from SysDepartment as hp where hp.del_flg=0 and hp.stop_flg=0 and hp.deptIsforregister=1";
		hql= this.joint(entity,hql);
		int c = super.getTotal(hql);
		return c;
	}
	/**
	 * 列表查询
	 * @param page 页码
	 * @param rows 显示列表数据
	 * @param entity 查询条件封装实体类
	 * @author  zpty
	 * @date 2015-11-16
	 * @version 1.0
	 * @param string2 
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List query(String string, String string2, SysDepartment entity) {
		String hql=" from SysDepartment as hp where hp.del_flg=0 and hp.stop_flg=0 and hp.deptIsforregister=1";
		hql= this.joint(entity,hql);
		List<SysDepartment> sysDepartmentList=super.getPage(hql, string, string2);
		return sysDepartmentList;
	}
	
	/**  
	 *  
	 * @Description： 查询条件拼接
	 * @Author：zpty
	 * @CreateDate：2015-11-16 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String joint(SysDepartment entity,String hql){
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDeptName())){
				hql = hql+" AND (hp.deptName LIKE '%"+entity.getDeptName()+"%' OR hp.deptBrev LIKE '%"+entity.getDeptName()+"%' OR upper(hp.deptPinyin) LIKE '%"+entity.getDeptName().toUpperCase()+"%' OR upper(hp.deptWb) LIKE '%"+entity.getDeptName().toUpperCase()+"%' OR upper(hp.deptInputcode) LIKE '%"+entity.getDeptName().toUpperCase()+"%')";
			}
		}
		hql=hql+" ORDER BY hp.deptRegisterOrder";
		return hql;
	}

}
