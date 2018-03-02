package cn.honry.inner.inpatient.drugbilldetail.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.drugbilldetail.dao.InpatientDrugbilldetailInnerDAO;
@Repository("inpatientDrugbilldetailInnerDAO")
@SuppressWarnings({ "all" })
public class InpatientDrugbilldetailInnerDAOImpl extends HibernateEntityDao<InpatientDrugbilldetail> implements InpatientDrugbilldetailInnerDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) {
		String hql = "from InpatientDrugbilldetail t where t.billNo='"+inpatientDrugbilldetail.getBillNo()+"' and t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(inpatientDrugbilldetail.getTypeCode())){
			hql =hql+" and t.typeCode='"+inpatientDrugbilldetail.getTypeCode()+"'";
		}
		if(StringUtils.isNotBlank(inpatientDrugbilldetail.getDrugType())){
			hql =hql+" and t.drugType='"+inpatientDrugbilldetail.getDrugType()+"'";
		}
		if(StringUtils.isNotBlank(inpatientDrugbilldetail.getUsageCode())){
			hql =hql+" and t.usageCode='"+inpatientDrugbilldetail.getUsageCode()+"'";
		}
		if(StringUtils.isNotBlank(inpatientDrugbilldetail.getBillType())){
			hql =hql+" and t.billType='"+inpatientDrugbilldetail.getBillType()+"'";
		}
		hql = hql+" ORDER BY t.id";
		List<InpatientDrugbilldetail> drugbilldetailList=this.getSession().createQuery(hql).list();
		if(drugbilldetailList!=null && drugbilldetailList.size()>0){
			return drugbilldetailList;
		}		
		return new ArrayList<InpatientDrugbilldetail>();
	}

}
