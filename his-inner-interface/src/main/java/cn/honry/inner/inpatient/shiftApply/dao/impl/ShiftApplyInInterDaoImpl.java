package cn.honry.inner.inpatient.shiftApply.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.shiftApply.dao.ShiftApplyInInterDao;


@Repository("shiftApplyInInterDao")
@SuppressWarnings({"all"})
public class ShiftApplyInInterDaoImpl extends HibernateEntityDao<InpatientShiftApply> implements ShiftApplyInInterDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public InpatientShiftApply getApply(String inpatientno, String oldDeptCode) {
		String hql=" from InpatientShiftApply i where i.inpatientNo='"+inpatientno+"' and oldDeptCode='"+oldDeptCode+"'"
				+" and i.shiftState in ('0','1')";//当前状态,0未生效,1转科申请,2确认,3取消申请
		List<InpatientShiftApply> list=super.find(hql,null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientShiftApply();
	}

	@Override
	public Integer getHappenNo(String inpatientNo) {
		String hql="select nvl(max(happenNo)+1,0) from InpatientShiftApplyNow i where i.inpatientNo='"+inpatientNo+"'"
				+" and i.shiftState in ('0','1')";//当前状态,0未生效,1转科申请,2确认,3取消申请
		Object happenNo=this.getSession().createQuery(hql).uniqueResult();
		return Integer.valueOf(happenNo.toString());
	}
}
