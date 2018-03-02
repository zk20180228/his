package cn.honry.inner.drug.storage.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.storage.dao.StorageInInterDAO;
import cn.honry.utils.SessionUtils;

/**  
 *  
 * @className：AddrateDAOImpl 
 * @Description：  物资加价率维护
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("storageInInterDAO")
public class StorageInInterDAOImpl extends HibernateEntityDao<DrugStorage> implements StorageInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<SysDepartment> findTree(int flag) {
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptId = dept.getDeptCode();
		String hql = "";
		if (flag == 1) {
			hql = "from SysDepartment where deptType in ('P','PI') and stop_flg=0 and del_flg=0 order by deptType";
		} else if (flag == 2) {
			hql = "from SysDepartment where deptType ='P' and stop_flg=0 and del_flg=0";
		} else if (flag == 3) {
			hql = "from SysDepartment where deptType ='P' and stop_flg=0 and del_flg=0 and deptCode='" + deptId + "'";
		}
		List<SysDepartment> list = this.getSession().createQuery(hql).list();
		return list.size() != 0 ? list : null;
	}
	
}
