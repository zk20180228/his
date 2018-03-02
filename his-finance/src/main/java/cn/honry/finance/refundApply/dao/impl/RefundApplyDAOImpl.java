package cn.honry.finance.refundApply.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.refundApply.dao.RefundApplyDAO;

@SuppressWarnings("all")
@Repository("refundApplyDAO")
public class RefundApplyDAOImpl extends HibernateEntityDao<InpatientCancelitem> implements RefundApplyDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public RegistrationNow findInfo(String clinicCode) {
		String hql = "from RegistrationNow where clinicCode = ? and del_flg=0 and stop_flg=0 and transType=1";
		List<RegistrationNow> infoList = super.find(hql, clinicCode);
		if(infoList==null||infoList.size()<=0){
			return new RegistrationNow();
		}
		return infoList.get(0);
	}

	@Override
	public OutpatientFeedetailNow queryFeedetail(String id) {
		String hql = "from OutpatientFeedetailNow where id = ? and  del_flg=0 and stop_flg=0 ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, id);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}
	
}
