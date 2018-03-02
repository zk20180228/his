package cn.honry.inner.outpatient.inpatNumber.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.inpatNumber.dao.InpatNumInnerDao;

@Repository("inpatNumInnerDao")
@SuppressWarnings({"all"})
public class InpatNumInnerDaoImpl extends HibernateEntityDao<InpatientNumber> implements InpatNumInnerDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * @Description：  根据住院流水号查询住院次数表
	 * @Author：zhangjin
	 * @CreateDate：2016-11-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public InpatientNumber getInpatientNo(String inpatientNo) {
		String hql ="from InpatientNumber where stop_flg=0 and del_flg=0 and inpatientNo='"+inpatientNo+"'";
		List<InpatientNumber> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
