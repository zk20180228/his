package cn.honry.outpatient.newChangeDeptLog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.outpatient.newChangeDeptLog.dao.ChangeDeptDAO;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("changeDeptDAO")
@SuppressWarnings({ "all" })
public class ChangeDeptDAOImpl extends HibernateEntityDao<RegistrationNow> implements ChangeDeptDAO{

	@Autowired
	private KeyvalueInInterDAO keyvalueDAO;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<RegistrationNow> queryRegisterMain(String cardNo,String clinicCode ) {
		String hql = "from RegistrationNow where 1=1 and validFlag = 1 and transType = 1 and inState = 0 and ynsee = 0 and DEL_FLG=0 and STOP_FLG=0 and trunc(regDate,'dd')=to_date(?,'yyyy-MM-dd')  ";
		if(StringUtils.isNotBlank(cardNo)){
			hql = hql + " and cardNo = '"+cardNo+"' ";
		}
		if(StringUtils.isNotBlank(clinicCode)){
			hql = hql + " and clinicCode = '"+clinicCode+"' ";
		}
		List<RegistrationNow> registrationList = super.find(hql, DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		if(registrationList==null||registrationList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return registrationList;
	}

	@Override
	public List<RegisterChangeDeptLog> queryChangeDeptList(String ids) {
		String hql = "from RegisterChangeDeptLog where 1=1 and rigisterId in ('"+ids+"') and DEL_FLG=0 and STOP_FLG=0 ";
		List<RegisterChangeDeptLog> changeDeptLogList = super.find(hql, null);
		if(changeDeptLogList==null||changeDeptLogList.size()<=0){
			return new ArrayList<RegisterChangeDeptLog>();
		}
		return changeDeptLogList;
	}

	@Override
	public RegistrationNow getById(String id) {
		String hql = "from RegistrationNow where 1=1 and id = '"+id+"' and DEL_FLG=0 and STOP_FLG=0";
		List<RegistrationNow> ationList = super.find(hql, null);
		if(ationList==null||ationList.size()<=0){
			return new RegistrationNow();
		}
		return ationList.get(0);
	}

	@Override
	public List<SysDepartment> changeDepartmentCombobox() {
		String hql = "from SysDepartment where 1=1 and deptIsforregister = 1 and DEL_FLG=0 and STOP_FLG=0";
		List<SysDepartment> ationList = super.find(hql, null);
		if(ationList==null||ationList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return ationList;
	}

	@Override
	public List<InfoVo> changeEmployeeCombobox(String gradeX, String newDept) {
		 String hql = "select ((select count(1) from  t_register_main_now t3 where t3.noon_code=t.schedule_midday and t3.doct_code=t.schedule_doctor and to_char(t3.reg_date,'yyyy-MM-dd') ='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and  t3.stop_flg=0 and t3.del_flg=0 and t3.in_state=0 and t3.trans_type=1 )+(select count(1) from  t_register_preregister_now t4 where t4.preregister_midday=t.schedule_midday and t4.preregister_expert=t.schedule_doctor  and t4.preregister_status=1 and to_char(t4.preregister_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t4.stop_flg=0 and t4.del_flg=0)) as infoAlready,"+
				   " t.schedule_doctor as empId,"+
				   " t.schedule_midday as midday,"+
				   " t.schedule_workdept as deptId,"+
				   " (select t6.clinic_name from  t_clinic t6 where t6.id=t.schedule_clinicid and t6.stop_flg=t.stop_flg and t6.del_flg=t.del_flg ) as clinic,"+
				   " t.schedule_limit as limit,"+
				   " t.schedule_stopreason as stoprEason,"+
				   " (select t0.dept_name from  t_department t0 where t0.dept_code=t.SCHEDULE_WORKDEPT and t0.stop_flg=0 and t0.del_flg=0) as deptName,"+
				   " (select t1.employee_name from  t_employee t1 where t1.employee_jobno=t.schedule_doctor and t1.stop_flg=0 and t1.del_flg=0) as empName,"+
				   " t.schedule_reggrade as grade,"+
				   " (select t2.grade_name from t_register_grade t2 where t2.grade_code=t.schedule_reggrade and t2.stop_flg=0 and t2.del_flg=0) as titleName,"+
				   " t.schedule_isstop as isStop,"+
				   " t.schedule_appflag as appFlag"+
				   " from "+HisParameters.HISPARSCHEMAHISUSER+" T_REGISTER_SCHEDULE_NOW t where"+
				   " to_char(t.schedule_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(newDept)){
			hql = hql + " and t.SCHEDULE_WORKDEPT  = '"+newDept+"'";
		}
		if(StringUtils.isNotBlank(gradeX)){
			hql = hql + " and t.schedule_reggrade = '"+gradeX+"'";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("infoAlready",Hibernate.INTEGER).addScalar("empId").addScalar("midday",Hibernate.INTEGER)
		.addScalar("deptId").addScalar("clinic").addScalar("limit",Hibernate.INTEGER).addScalar("stoprEason").addScalar("deptName").addScalar("empName").addScalar("grade").addScalar("titleName").addScalar("isStop",Hibernate.INTEGER).addScalar("appFlag",Hibernate.INTEGER);
		List<InfoVo> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoVo.class)).list();
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
		return new ArrayList<InfoVo>();
	}

	@Override
	public List<FinanceInvoicedetailNow> findInvoicedetailList(String invoiceNo) {
		String hql = "from FinanceInvoicedetailNow where invoiceNo = '"+invoiceNo+"' and transType=1 and DEL_FLG=0 and STOP_FLG=0";
		List<FinanceInvoicedetailNow> invoicedetailList = super.find(hql, null);
		if(invoicedetailList==null||invoicedetailList.size()<=0){
			return new ArrayList<FinanceInvoicedetailNow>();
		}
		return invoicedetailList;
	}

	@Override
	public FinanceInvoiceInfoNow findInvoiceInfo(String invoiceNo) {
		String hql = "from FinanceInvoiceInfoNow where invoiceNo = '"+invoiceNo+"' and transType=1 and DEL_FLG=0 and STOP_FLG=0";
		List<FinanceInvoiceInfoNow> invoiceInfoList = super.find(hql, null);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new FinanceInvoiceInfoNow();
		}
		return invoiceInfoList.get(0);
	}

	@Override
	public List<SysDepartment> querydeptComboboxs() {
		String hql = "from SysDepartment where  DEL_FLG=0 and STOP_FLG=0";
		List<SysDepartment> invoiceInfoList = super.find(hql, null);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return invoiceInfoList;
	}

	@Override
	public List<SysEmployee> queryempComboboxs() {
		String hql = "from SysEmployee where  DEL_FLG=0 and STOP_FLG=0";
		List<SysEmployee> invoiceInfoList = super.find(hql, null);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return invoiceInfoList;
	}

	@Override
	public List<RegistrationNow> queryChangeDept(String cardNo, String clinicCode) {
		String hql = "from RegistrationNow where transType = 1 and validFlag = 1 and inState = 1 and ynsee = 0 and DEL_FLG=0 and STOP_FLG=0 and to_char(regDate,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' ";
		if(StringUtils.isNotBlank(cardNo)){
			hql = hql + " and cardNo = '"+cardNo+"' ";
		}
		if(StringUtils.isNotBlank(clinicCode)){
			hql = hql + " and clinicCode = '"+clinicCode+"' ";
		}
		List<RegistrationNow> registrationList = super.find(hql, null);
		if(registrationList==null||registrationList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return registrationList;
	}


}
