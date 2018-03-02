package cn.honry.inpatient.doctorAdvice.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.doctorAdvice.dao.InpatientDrugbilldetailDAO;
@Repository("inpatientDrugbilldetailDAO")
@SuppressWarnings({ "all" })
public class InpatientDrugbilldetailDAOImpl extends HibernateEntityDao<InpatientDrugbilldetail> implements InpatientDrugbilldetailDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientDrugbilldetail> queryInpatientDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) {
		String hql = "from InpatientDrugbilldetail t where t.billNo='"+inpatientDrugbilldetail.getBillNo()+"' "
				+ "and t.stop_flg = 0 and t.del_flg = 0 ";
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
		List<InpatientDrugbilldetail> drugbilldetailList=this.getSession().createQuery(hql).list();
		if(drugbilldetailList!=null && drugbilldetailList.size()>0){
			return drugbilldetailList;
		}		
		return new ArrayList<InpatientDrugbilldetail>();
	}

	@Override
	public List<InpatientDrugbilldetail> queryDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail,
			String page,String rows) {
		String hql = jointDrugbilldetail(inpatientDrugbilldetail);
		return super.getPage(hql, page, rows);
	}
	
	public String jointDrugbilldetail(InpatientDrugbilldetail inpatientDrugbilldetail){
		String hql = "from InpatientDrugbilldetail t where t.billNo='"+inpatientDrugbilldetail.getBillNo()+"' "
				+ "and t.stop_flg = 0 and t.del_flg = 0 ";
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
		return hql;
	}

	@Override
	public int getTotalBilldetail(InpatientDrugbilldetail inpatientDrugbilldetail) {
		String hql = jointDrugbilldetail(inpatientDrugbilldetail);
		return super.getTotal(hql);
	}

}
