package cn.honry.inpatient.doctorAdvice.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.doctorAdvice.dao.DoctorAdviceDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("doctorAdviceDAO")
@SuppressWarnings({ "all" })
public class DoctorAdviceDAOImpl extends HibernateEntityDao<InpatientInfo> implements DoctorAdviceDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<BusinessBedward> findNurseCellCode(String inpatientNo) {
		String hql = "from BusinessBedward t where t.id in(select b.businessBedward from BusinessHospitalbed b where b.id in(select h.bedNo from InpatientInfo h where h.inpatientNo = '"+inpatientNo+"')) and t.stop_flg = 0 and t.del_flg = 0";
		hql = hql+" ORDER BY t.id";
		List<BusinessBedward> businessBedward=this.getSession().createQuery(hql).list();
		if(businessBedward!=null && businessBedward.size()>0){
			return businessBedward;
		}		
		return new ArrayList<BusinessBedward>();
	}
	
	@Override
	public List<InpatientConsultation> findInpatientConsultation(String inpatientNo) {
		String hql = "from InpatientConsultation t where t.inpatientNo = '"+inpatientNo+"' and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientConsultation> inpatientConsultation=this.getSession().createQuery(hql).list();
		if(inpatientConsultation!=null && inpatientConsultation.size()>0){
			return inpatientConsultation;
		}		
		return new ArrayList<InpatientConsultation>();
	}
	
	@Override
	public List<InpatientKind> treeDrugExe() {
		String hql = "from InpatientKind t where t.stop_flg = 0 and t.del_flg = 0";
		hql = hql+" ORDER BY t.id";
		List<InpatientKind> inpatientKind=this.getSession().createQuery(hql).list();
		if(inpatientKind!=null && inpatientKind.size()>0){
			return inpatientKind;
		}		
		return new ArrayList<InpatientKind>();
	}
	@Override
	public List<InpatientKind> treeDrugExeByCode(String code) {
		String hql = "from InpatientKind t where t.stop_flg = 0 and t.del_flg = 0 and t.typeCode='"+code+"'";
		List<InpatientKind> inpatientKind=this.getSession().createQuery(hql).list();
		if(inpatientKind!=null && inpatientKind.size()>0){
			return inpatientKind;
		}		
		return new ArrayList<InpatientKind>();
	}
	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids,String billName) {
		//获取当前登录的科室,默认加载当前科室所对应的执行单
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String hql = "from InpatientExecbill t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(dept==null||dept.getDeptCode()==null){
		}else{
			if("N".equals(dept.getDeptType())){
				hql=hql+ " and t.nurseCellCode='"+dept.getDeptCode()+"'";
			}else{
				String hql1 = "select t1.dept_code as deptCode from t_department_contact t1 "
						+ "where t1.id in (select t.pardept_id from t_department_contact t "
						+ "where  t.dept_id='"+dept.getDeptCode()+"' and t.reference_type='03')";
				SQLQuery query = this.getSession().createSQLQuery(hql1.toString());
				query.addScalar("deptCode");	
				query.setResultTransformer(Transformers.aliasToBean(DepartmentContact.class));
				List<DepartmentContact> list = query.list();
				if(list.size()>0){
					hql=hql+ " and t.nurseCellCode='"+list.get(0).getDeptCode()+"'";
				}else{
					hql=hql+ " and t.nurseCellCode='"+dept.getDeptCode()+"'";
				}
			}
		}
		if(StringUtils.isNotBlank(ids)){
			hql =hql+" and t.billNo='"+ids+"'";
		}
		if(StringUtils.isNotBlank(billName)){
			hql =hql+" and t.billName='"+billName+"'";
		}
		List<InpatientExecbill> inpatientExecbill=this.getSession().createQuery(hql).list();
		if(inpatientExecbill!=null && inpatientExecbill.size()>0){
			return inpatientExecbill;
		}		
		return new ArrayList<InpatientExecbill>();
	}
	@Override
	public List<InpatientExecbill> queryDocAdvExeByNo(String billNo) {
		String hql = "from InpatientExecbill t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(billNo)){
			hql =hql+" and t.billNo='"+billNo+"'";
		}
		List<InpatientExecbill> inpatientExecbill=this.getSession().createQuery(hql).list();
		if(inpatientExecbill!=null && inpatientExecbill.size()>0){
			return inpatientExecbill;
		}		
		return new ArrayList<InpatientExecbill>();
	}
	@Override
	public List<DepartmentContact> queryDepartmentContact(String deptCode) {
		String hql1 = "select t1.dept_code as deptCode,t1.dept_name as deptName from t_department_contact t1 "
				+ "where t1.id in (select t.pardept_id from t_department_contact t where  t.dept_id='"+deptCode+"' "
						+ "and t.reference_type='03')";
		SQLQuery query = this.getSession().createSQLQuery(hql1.toString());
		query.addScalar("deptCode").addScalar("deptName");	
		query.setResultTransformer(Transformers.aliasToBean(DepartmentContact.class));
		List<DepartmentContact> list = query.list();
		if(list!=null && list.size()>0){
			return list;
		}		
		return new ArrayList<DepartmentContact>();
	}
  
	@Override
	public void saveDrugBillDetail(
			InpatientDrugbilldetail inpatientDrugbilldetail) {
		this.getHibernateTemplate().save(inpatientDrugbilldetail);
		
	}

	@Override
	public List<InpatientDrugbilldetail> queryAllBillDetail(InpatientDrugbilldetail inpatientDrugbilldetail) {
		String hql="from InpatientDrugbilldetail i where i.billNo="+inpatientDrugbilldetail.getBillNo();
		List<InpatientDrugbilldetail> list = (List<InpatientDrugbilldetail>) this.getHibernateTemplate().find(hql);
		return list;
	}

	@Override
	public InpatientDrugbilldetail findInpatientDrugbilldetailById(String id) {
		String hql="from InpatientDrugbilldetail i where i.id='"+id+"'";
		List<InpatientDrugbilldetail> list = (List<InpatientDrugbilldetail>) this.getHibernateTemplate().find(hql);
		if(list!=null){
			return list.get(0);
		}
		return new InpatientDrugbilldetail();
	}

	@Override
	public void updateDrugBillDetail(
			InpatientDrugbilldetail oInpatientDrugbilldetail) {
		this.getHibernateTemplate().update(oInpatientDrugbilldetail);	
	}

	
}
