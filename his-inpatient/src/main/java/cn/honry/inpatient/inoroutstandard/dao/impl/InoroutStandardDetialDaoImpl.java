package cn.honry.inpatient.inoroutstandard.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InoroutStandardDetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.inoroutstandard.dao.InoroutStandardDetialDao;
@Repository("inoroutStandardDetialDao")
@SuppressWarnings({"all"})
public class InoroutStandardDetialDaoImpl extends HibernateEntityDao<InoroutStandardDetail>
		implements InoroutStandardDetialDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InoroutStandardDetail> getStandardDetialList(String code,
			String versionNO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" From InoroutStandardDetail where standCode = ? and standVersionNo = ? and del_flg=0 and stop_flg=0 ");
		List<InoroutStandardDetail> list = super.find(sb.toString(), code,versionNO);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<InoroutStandardDetail>();
	}
	
}
