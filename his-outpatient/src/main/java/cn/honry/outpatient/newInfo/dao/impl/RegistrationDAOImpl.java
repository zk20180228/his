package cn.honry.outpatient.newInfo.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.outpatient.newInfo.vo.EmpInfoVo;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.newInfo.vo.InfoStatistics;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.outpatient.newInfo.vo.RegPrintVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("ationDAO")
@SuppressWarnings({ "all" })
public class RegistrationDAOImpl extends HibernateEntityDao<RegistrationNow> implements RegistrationDAO{
	
	@Autowired
	private KeyvalueInInterDAO keyvalueDAO;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	

	@Override
	public List<RegisterGrade> gradeCombobox(String q) {
		String hql = "from RegisterGrade where del_flg=0 and stop_flg=0";
		if(StringUtils.isNotBlank(q)){
			hql += "and (name like '%"+q+"%' or code like '%"+q+"%' or encode like '%"+q+"%' or codePinyin like '%"+q.toUpperCase()+"%' or codeWb like '%"+q.toUpperCase()+"%' or codeInputcode like '%"+q.toUpperCase()+"%')";
		}
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return gradeList;
	}

	@Override
	public List<SysDepartment> deptCombobox(String q) {
		String hql = "from SysDepartment where deptType='C' and  del_flg=0 and stop_flg=0 "; //deptType='C'加了这个，只查类型是门诊的科室,去掉了deptIsforregister=1，挂号管理里面既有挂号排班管理，也有工作排班管理
		if(StringUtils.isNotBlank(q)){
			hql += " and (deptCode like '%"+q+"%' or deptName like '%"+q+"%' or deptPinyin like '%"+q.toUpperCase()+"%' or deptWb like '%"+q.toUpperCase()+"%' or deptInputcode like '%"+q.toUpperCase()+"%')";
		}
		hql+=" order by deptRegisterno ";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList==null||deptList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return deptList;
	}

	@Override
	public List<EmpInfoVo> empCombobox(String deptCode, String reglevlCode,Integer noonCode,String q) {
		StringBuffer sql=new StringBuffer("");
/**		2016年12月19日15:25:42  gh 注掉  更新数据源  不从排班表中获取挂号专家下拉框的数据  改为从号源表中获取*/
		sql.append("select t.employee_code as id ,");
		sql.append("       t.employee_name as name, ");
		sql.append("       t.dept_code as dept, ");
		sql.append(" 	   t.grade_code as title, ");
		sql.append(" 	   t.id as sourceID");
		sql.append("       from t_register_doc_source t  ");
		sql.append(" where  t.REG_DATE>=to_date(:today,'yyyy-mm-dd') and t.REG_DATE<to_date(:tomorrow,'yyyy-mm-dd')");
		sql.append("   and t.isstop= 2 ");
		sql.append("   and t.del_flg = 0 ");
		sql.append("   and t.DEPT_CODE in (select d.dept_code from t_department d where d.del_flg = 0 and d.stop_flg = 0 and d.dept_isforregister = 1)");
		if(noonCode==null){
			sql.append("   and (t.midday_code = 1 OR t.midday_code = 2 OR t.midday_code = 3 )");
		}else{
			sql.append("   and t.midday_code = :noonCode");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sql.append(" and t.dept_code =:deptCode ");
		}
		if(StringUtils.isNotBlank(reglevlCode)){
			sql.append(" and t.grade_code =:reglevlCode ");
		}
		if(StringUtils.isNotBlank(q)){
			sql.append(" and (t.EMPLOYEE_CODE like :q or t.EMPLOYEE_NAME like :q )");
		}
		sql.append(" order by t.dept_code,t.grade_code");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("id")
				.addScalar("name")
				.addScalar("title")
				.addScalar("dept")
				.addScalar("sourceID")
				.setParameter("today", DateUtils.formatDateY_M_D(new Date()));
		if(noonCode!=null){
			queryObject.setParameter("noonCode",noonCode);
		}
		queryObject.setParameter("tomorrow", DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(),1)));
		if(StringUtils.isNotBlank(deptCode)){
			queryObject.setParameter("deptCode",deptCode);
		}
		if(StringUtils.isNotBlank(reglevlCode)){
			queryObject.setParameter("reglevlCode",reglevlCode);
		}
		if(StringUtils.isNotBlank(q)){
			queryObject.setParameter("q","%"+q+"%");
		}
		List<EmpInfoVo> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(EmpInfoVo.class)).list();
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
		return new ArrayList<EmpInfoVo>();
	}

	@Override
	public List<BusinessContractunit> contCombobox(String q) {
		String hql = "from BusinessContractunit r where r.del_flg = 0 ";
		if(StringUtils.isNotBlank(q)){
			hql+=" and (encode like '%"+q+"%' or name like '%"+q+"%'or pinyin like '%"+q.toUpperCase()+"%' or wb like '%"+q.toUpperCase()+"%' or inputCode like '%"+q.toUpperCase()+"%')";
		}
		hql+= " ORDER BY order ";
		List<BusinessContractunit> businessContractunitList = super.find(hql, null);
		if(businessContractunitList==null||businessContractunitList.size()<=0){
			return new ArrayList<BusinessContractunit>();
		}
		return businessContractunitList;
	}


	@Override
	public List<InfoVo> findInfoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode) {
		String todayStr=DateUtils.formatDateY_M_D(new Date());
		StringBuffer sql=new StringBuffer("");
		sql.append(" select ( ");
		sql.append("    ( ");
		sql.append("      select count(t3.id) ");
		sql.append("      from t_register_main_now t3 ");
		sql.append("      where t3.noon_code = t.schedule_midday ");
		sql.append("        and t3.doct_code = t.schedule_doctor ");
		sql.append("        and trunc(t3.reg_date,'dd')=to_date(:todayStr,'yyyy-mm-dd') ");
		sql.append("        and t3.in_state = 0 ");
		sql.append("        and t3.trans_type = 1 ");
		sql.append("    ) ");
		sql.append("   + ");
		sql.append("   ( ");
		sql.append("    select count(t4.schedule_id) ");
		sql.append("    from t_register_preregister_now t4 ");
		sql.append("    where t4.preregister_midday = t.schedule_midday ");
		sql.append("    and t4.preregister_expert = t.schedule_doctor ");
		sql.append("    and t4.preregister_status = 1 ");
		sql.append("    and t4.preregister_date=to_date(:todayStr,'yyyy-mm-dd') ");
		sql.append("   ) ");
		sql.append(" ) as infoAlready, ");
		sql.append(" t.schedule_doctor as empId, ");
		sql.append(" t.schedule_midday as midday, ");
		sql.append(" t.SCHEDULE_WORKDEPT as deptId, ");
		sql.append(" t.schedule_clinicname as clinic, ");
		sql.append(" t.schedule_limit as limit, ");
		sql.append(" t.schedule_stopreason as stoprEason, ");
		sql.append(" t.schedule_deptname as deptName, ");
		sql.append(" t.schedule_doctorname as empName, ");
		sql.append(" t.schedule_reggrade as grade, ");
		sql.append(" (select t2.grade_name  from t_register_grade t2 where t2.grade_code = t.schedule_reggrade ) as titleName, ");
		sql.append(" t.schedule_isstop as isStop, ");
		sql.append(" t.schedule_appflag as appFlag, ");
		sql.append(" t.schedule_peciallimit as speciallimit, ");
		sql.append(" t.schedule_id as workdeptId, ");
		sql.append(" t.schedule_starttime as scheduleStarttime, ");
		sql.append(" t.schedule_endtime as scheduleEndtime, ");
		sql.append(" t.schedule_date as scheduleDate ");
		sql.append(" from T_REGISTER_SCHEDULE_NOW t ");
		sql.append(" where  ");
		sql.append(" trunc(t.schedule_date,'dd')=to_date(:todayStr,'yyyy-mm-dd') ");
		sql.append(" and t.schedule_class = 1 ");
		if(StringUtils.isNotBlank(deptCode)){
			  sql.append(" and t.SCHEDULE_WORKDEPT = :deptCode ");
		}else{
			  sql.append(" and t.SCHEDULE_WORKDEPT in (select d.dept_id from  t_department d where d.del_flg=0 and d.stop_flg=0 and d.dept_isforregister=1) ");
		}
		if(StringUtils.isNotBlank(doctCode)){
			  sql.append(" and t.schedule_doctor = :doctCode");
		}
		if(StringUtils.isNotBlank(reglevlCode)){
			  sql.append(" and t.schedule_reggrade = :reglevlCode ");
		}
		if(noonCode!=null){
			  sql.append(" and t.schedule_midday = :noonCode ");
		}
		sql.append(" order by t.schedule_deptid,t.schedule_reggrade");
		logger.info("门诊挂号值班信息查询："+sql.toString());
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("infoAlready",Hibernate.INTEGER).addScalar("empId").addScalar("midday",Hibernate.INTEGER)
		.addScalar("deptId").addScalar("clinic",Hibernate.STRING).addScalar("limit",Hibernate.INTEGER).addScalar("stoprEason").addScalar("deptName").addScalar("empName").addScalar("grade").addScalar("titleName").addScalar("isStop",Hibernate.INTEGER).addScalar("appFlag",Hibernate.INTEGER).addScalar("speciallimit",Hibernate.INTEGER)
		.addScalar("workdeptId").addScalar("scheduleStarttime").addScalar("scheduleEndtime").addScalar("scheduleDate",Hibernate.TIMESTAMP);
		queryObject.setParameter("todayStr", todayStr);
		if(StringUtils.isNotBlank(deptCode)){
			  queryObject.setParameter("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(doctCode)){
			  queryObject.setParameter("doctCode", doctCode);
		}
		if(StringUtils.isNotBlank(reglevlCode)){
			  queryObject.setParameter("reglevlCode", reglevlCode);
		}
		if(noonCode!=null){
			queryObject.setParameter("noonCode", noonCode);
		}
		List<InfoVo> infoVoList = queryObject.setResultTransformer(
				Transformers.aliasToBean(InfoVo.class)).list();
		if (infoVoList != null && infoVoList.size() > 0) {
			return infoVoList;
		}
		return new ArrayList<InfoVo>();
	}

	@Override
	public InfoStatistics queryStatistics(String doctCode, Integer noonCode) {
		//把预约状态都去掉了
		String hql ="select ((select count(i.id) from T_REGISTER_MAIN_NOW i where i.doct_code=s.schedule_doctor and i.noon_code=s.schedule_midday and i.stop_flg=0 and i.in_state = 0 and  i.del_flg=0 and trunc(i.reg_date,'dd')=to_date(:regDate,'yyyy-MM-dd'))+(select count(p.preregister_id) from t_register_preregister_now p where p.preregister_expert=s.schedule_doctor and p.preregister_midday = s.schedule_midday and p.stop_flg=0 and p.del_flg=0 and p.preregister_date=to_date(:preregisterDate,'yyyy-MM-dd'))) as limitSum,"+
				" (select count(p.preregister_id) from t_register_preregister_now p where p.preregister_expert=s.schedule_doctor and p.preregister_midday = s.schedule_midday and p.stop_flg=0 and p.del_flg=0 and p.preregister_date=to_date(:preregisterDate,'yyyy-MM-dd') ) as limitPrereInfo,"+
				" s.schedule_limit as limit , s.schedule_prelimit as limitPrere, s.schedule_netlimit as limitNet ,s.schedule_peciallimit as speciallimit"+
				" from t_register_schedule_now s where s.schedule_doctor=:doctCode and trunc(s.schedule_date,'dd')=to_date(:scheduleDate,'yyyy-MM-dd') and s.schedule_midday = :noonCode  ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("limitSum",Hibernate.INTEGER).addScalar("limit",Hibernate.INTEGER)
		.addScalar("limitPrere",Hibernate.INTEGER).addScalar("limitPrereInfo",Hibernate.INTEGER).addScalar("limitNet",Hibernate.INTEGER).addScalar("speciallimit",Hibernate.INTEGER);
		queryObject.setParameter("regDate", DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()))
		.setParameter("preregisterDate",  DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()))
		.setParameter("doctCode", doctCode).setParameter("noonCode", noonCode)
		.setParameter("scheduleDate",  DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		
		List<InfoStatistics> infoStatistics = queryObject.setResultTransformer(Transformers.aliasToBean(InfoStatistics.class)).list();
		if(infoStatistics!=null&&infoStatistics.size()>0){
			return infoStatistics.get(0);
		}
		return new InfoStatistics();
	}

	@Override
	public RegistrationNow queryRegistrationByEmp(String doctCode) {
		String hql = "select count(i.id) as infoAdd from T_REGISTER_MAIN_NOW i where i.DOCT_CODE=:doctCode and i.APPEND_FLAG=1 and i.stop_flg=0 and i.del_flg=0 ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("infoAdd",Hibernate.INTEGER);
		queryObject.setParameter("doctCode", doctCode);
		List<RegistrationNow> infoList = queryObject.setResultTransformer(Transformers.aliasToBean(RegistrationNow.class)).list();
		if(infoList==null||infoList.size()<=0){
			return new RegistrationNow();
		}
		return infoList.get(0);
	}

	@Override
	public HospitalParameter speciallimitInfo(String speciallimitInfo) {
		String hql = " from HospitalParameter where parameterCode = :speciallimitInfo ";
		List<HospitalParameter> parameter = super.find(hql, speciallimitInfo);
		if(parameter==null||parameter.size()<=0){
			return new HospitalParameter();
		}
		return parameter.get(0);
	}

	@Override
	public RegisterFee feeCombobox(String pactCode, String reglevlCode) {
		String hql = "select r.id as id,r.registerFee as registerFee,r.checkFee as checkFee,"
				+ "r.treatmentFee as treatmentFee,r.otherFee as otherFee from RegisterFee r "
				+ "where r.unitId = ? and r.registerGrade = ?  and r.del_flg = 0 and r.stop_flg=0 ";
		List<RegisterFee> registerFeeList = this.createQuery(hql, pactCode,reglevlCode)
				.setResultTransformer(Transformers.aliasToBean(RegisterFee.class)).list();
		if(registerFeeList==null||registerFeeList.size()<=0){
			return new RegisterFee();
		}
		return registerFeeList.get(0);
	}

	@Override
	public PatientIdcard queryPatientIdcard(String cardNo) {
		String hql = "from PatientIdcard  where  del_flg = 0  and idcardNo = ?";
		List<PatientIdcard> patientIdcardList = super.find(hql, cardNo);
		if(patientIdcardList==null||patientIdcardList.size()<=0){
			return new PatientIdcard();
		}
		return patientIdcardList.get(0);
	}

	@Override
	public PatientBlackList queryBlackList(String id) {
		String hql = "from PatientBlackList where  del_flg=0 and stop_flg=0 and patient.id = ?";
		List<PatientBlackList> blackList =super.find(hql, id);
		if(blackList==null||blackList.size()<=0){
			return new PatientBlackList();
		}
		return blackList.get(0);
	}

	@Override
	public RegisterPreregisterNow findPreregister(String patientCertificatesno) {
		String hql = " from RegisterPreregisterNow where preregisterCertificatesno = ? and del_flg=0 and stop_flg=0  and preregisterDate = to_DATE(?,'yyyy-MM-dd')  and status=1 ";
		List<RegisterPreregisterNow> preregisterList = super.find(hql, patientCertificatesno,DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		if(preregisterList==null||preregisterList.size()<=0){
			//如果没有预约号 就制造一个门诊号 如果有预约号 就让预约号转为门诊号
			RegisterPreregisterNow registerPreregister = new RegisterPreregisterNow();
			Integer value = keyvalueDAO.getVal("RegisterPreregister");
			registerPreregister.setPreregisterNo(DateUtils.getStringYear()+DateUtils.getStringMonth()+DateUtils.getStringDay()+count(6,value.toString().length())+value);
			return registerPreregister;
		}
		return preregisterList.get(0);
	}
	
	public String count(Integer length,Integer dlen){
		String cV = "";
		Integer wS = length - dlen;
		if(wS>0){
			for (int i = 1; i <= wS; i++) {
				cV += "0";
			}
		}
		return cV;
	}

	@Override
	public List<SysDepartment> querydeptComboboxs() {
		String hql = "from SysDepartment where  del_flg=0 and stop_flg=0";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList==null||deptList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return deptList;
	}

	@Override
	public List<RegisterGrade> querygradeComboboxs() {
		String hql = "from RegisterGrade where del_flg=0 and stop_flg=0";
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return gradeList;
	}

	@Override
	public List<SysEmployee> queryempComboboxs() {
		String hql = "from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empList = super.find(hql, null);
		if(empList==null||empList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return empList;
	}

	@Override
	public List<RegistrationNow> findInfoHisList(String cardNo) {
		List<RegistrationNow> registrationList = null;
		if(StringUtils.isNotBlank(cardNo)){
			String hql = "from RegistrationNow where cardNo = ? and transType = 1 and inState = 0 and del_flg=0 and stop_flg=0  and trunc(regDate,'dd')=TO_DATE(?,'YYYY-MM-DD') ";
			registrationList = super.find(hql, cardNo,DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		}else{
			String hql = "from RegistrationNow where transType = 1 and inState = 0 and del_flg=0 and stop_flg=0  and trunc(regDate,'dd')=TO_DATE(?,'YYYY-MM-DD') ";
			registrationList = super.find(hql,DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		}
		if(registrationList==null||registrationList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return registrationList;
	}

	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,String invoiceType) {
		int invoiceNo = 0;
		String invoiceNosq = "";
		String invoiceNoas = "";
		String invoiceNosh = "";
		String invoiceUsednoa ="";	
		Map<String,String> map=new HashMap<String,String>();
		String hql = " from FinanceInvoice where invoiceGetperson= ? and invoiceType = ? and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, id,invoiceType);
		if(invoiceList.size()>0){
			String invoiceNos = invoiceList.get(0).getInvoiceUsedno();//获得当前已使用号
			invoiceNosq = invoiceNos.substring(1);//截取后面的数字
			invoiceNoas = invoiceNos.substring(0, 1);//前面的字母
			int invoiceNoa = Integer.parseInt(invoiceNosq);//转为int类型
			invoiceNo = invoiceNoa+1;//加1是下一个要使用的还未使用的发票号
			invoiceNosh = invoiceNo+"";
			int lengths = 13-invoiceNosh.length();
			for(int a=0;a<lengths;a++){
				invoiceUsednoa=invoiceUsednoa+"0";
			}
			String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNosh;
			map.put("resMsg", "success");
			map.put("resCode", invoiceUserNo);
			map.put("invooiceId", invoiceList.get(0).getId());//发票号所在发票组的id
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}
		return map;
	}

	@Override
	public OutpatientAccount queryAccountByMedicalrecord(String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId = ? and del_flg=0 and stop_flg=0 ";
		List<OutpatientAccount> accountList = super.find(hql, midicalrecordId);
		if(accountList==null||accountList.size()<=0){
			return new OutpatientAccount();
		}
		return accountList.get(0);
	}

	@Override
	public List<OutpatientAccount> queryAccountrecord(String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId = ? and del_flg=0 and stop_flg=0 ";
		List<OutpatientAccount> accountrecordList = super.find(hql, midicalrecordId);
		if(accountrecordList==null||accountrecordList.size()<=0){
			return new ArrayList<OutpatientAccount>();
		}
		return accountrecordList;
	}

	@Override
	public List<RegistrationNow> findInfoVoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode, String midicalrecordId) {
		String hql = " from RegistrationNow where midicalrecordId =? and del_flg=0 and stop_flg=0 and trunc(regDate,'dd')=TO_date(?,'YYYY-MM-DD')  and noonCode = ? and doctCode = ? and transType = 1 and inState = 0";
		
		List<RegistrationNow> ationList = super.find(hql, midicalrecordId,DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()),noonCode,doctCode);
		if(ationList==null||ationList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return ationList;
	}

	@Override
	public HospitalParameter changePay() {
		String hql = "from HospitalParameter where parameterCode = 'benPay' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}

	@Override
	public OutpatientAccount veriPassWord(String md5Hex, String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId = :midicalrecordId  and  del_flg=0 and stop_flg=0  ";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("midicalrecordId", midicalrecordId);
		List<OutpatientAccount> patientAccountList = query.list();
		if(patientAccountList==null||patientAccountList.size()<=0){
			//没有该账户
			OutpatientAccount oa = new OutpatientAccount();
			oa.setAccountPassword("0");
			oa.setIsEmpower(-1);
			return oa;
		}else{
			//密码不正确
			if(!patientAccountList.get(0).getAccountPassword().equals(md5Hex)){
				return new OutpatientAccount();
			}
		}
		return patientAccountList.get(0);
	}

	@Override
	public HospitalParameter invocePemen() {
		String hql = "from HospitalParameter where 1=1 and parameterCode = 'infoExenp' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}

	@Override
	public RegistrationNow queryInfoByOrder(String doctCode, Integer noonCode) {
		String hql = "from RegistrationNow where  del_flg=0 and stop_flg=0 and doctCode = :doctCode and noonCode = :noonCode and trunc(regDate,'dd')=to_date(:time,'yyyy-MM-dd') order by orderNo desc  ";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("doctCode", doctCode);
		query.setInteger("noonCode", noonCode);
		query.setParameter("time", DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		List<RegistrationNow> infoList = query.list();
		if(infoList==null||infoList.size()<=0){
			return new RegistrationNow();
		}
		return infoList.get(0);
	}

	@Override
	public Integer keyvalueDAO() {
		Integer value = keyvalueDAO.getVal("RegisterPreregister");
		return value;
	}

	@Override
	public Patient queryPatientByBLh(String midicalrecordId) {
		String hql = "from Patient where  del_flg=0 and stop_flg=0 and medicalrecordId = ?";
		List<Patient> patient = super.find(hql, midicalrecordId);
		if(patient==null||patient.size()<=0){
			return new Patient();
		}
		return patient.get(0);
	}

	@Override
	public RegisterFee findFeeByfee(String pactCode, String reglevlCode) {
		String hql = "from RegisterFee where registerGrade = :reglevlCode and unitId = :pactCode and  del_flg=0 and stop_flg=0 ";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("reglevlCode", reglevlCode);
		query.setParameter("pactCode", pactCode);
		List<RegisterFee> registerFeeList = query.list();
		if(registerFeeList==null||registerFeeList.size()<=0){
			return new RegisterFee();
		}
		return registerFeeList.get(0);
	}

	@Override
	public BusinessContractunit findContractunitById(String pactCode) {
		String hql = "from BusinessContractunit where id = ? and del_flg=0 and stop_flg=0 ";
		List<BusinessContractunit> contractunitList = super.find(hql, pactCode);
		if(contractunitList==null||contractunitList.size()<=0){
			return new BusinessContractunit();
		}
		return contractunitList.get(0);
	}

	@Override
	public List<RegisterPreregisterNow> findPreregisterList(RegisterPreregisterNow preregister,String page,String rows) {
		String hql ="select t.schedule_id as scheduleId,t.preregister_id as id,t.preregister_no as preregisterNo,t.preregister_dept as preregisterDept,"
				+ " t.preregister_deptname  as preregisterDeptname,t.preregister_expertname as preregisterExpertname,   "
				+"t.preregister_expert as preregisterExpert,t.preregister_grade as preregisterGrade, t.preregister_gradename as preregisterGradename,"
				+ "t.preregister_date as preregisterDate,"
				+"t.preregister_starttime as preregisterStarttime,t.preregister_endtime as preregisterEndtime,t.medicalrecord_id as medicalrecordId,"
				+"t.idcard_id as idcardId,t.preregister_certificatestype as preregisterCertificatestype,t.preregister_certificatesno as preregisterCertificatesno,t.preregister_name as preregisterName,"
				+"t.preregister_sex as preregisterSex,t.preregister_age as preregisterAge,t.preregister_ageunit as preregisterAgeunit,t.preregister_phone as preregisterPhone,"
				+"t.preregister_address as preregisterAddress,t.PREREGISTER_MIDDAYNAME as preregisterMiddayname ,t.CREATETIME as createTime,t.Preregister_Midday as midday  "
				+"from T_REGISTER_PREREGISTER_NOW t ";
				
		hql+=" where t.PREREGISTER_STATUS = 1 "
		+"and t.DEL_FLG = 0 "
		+"and t.STOP_FLG = 0";
		if(preregister!=null){
			if(StringUtils.isNotBlank(preregister.getIdcardId())){
				hql+=" and t.IDCARD_ID =  :idcardId";
			}
		}
		if(preregister!=null){
			if(StringUtils.isNotBlank(preregister.getPreregisterNo())){
				hql+=" and t.preregister_no=:preregisterNo";
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterCertificatesno())){
				hql+=" and t.preregister_certificatesno=:preregisterCertificatesno";
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterName())){
				hql+= " and t.preregister_name like :preregisterName";
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterPhone())){
				hql+=" and t.preregister_phone = :preregisterPhone";
			}
			
			if(preregister.getPreregisterDate()!=null){
				hql+=" and trunc(t.preregister_date,'dd')=to_date(:preregisterDate,'yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterDept())){
				hql+=" and t.PREREGISTER_DEPT = :preregisterDept";
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterExpert())){
				hql+=" and t.PREREGISTER_EXPERT = :preregisterExpert";
			}
			if(StringUtils.isNotBlank(preregister.getMedicalrecordId())){
				hql+=" and t.MEDICALRECORD_ID = :medicalrecordId";
			}
		}
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):10;
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("scheduleId").addScalar("id")
		.addScalar("preregisterNo").addScalar("preregisterDept").addScalar("preregisterDeptname")
		.addScalar("preregisterExpertname").addScalar("preregisterExpert").addScalar("preregisterGrade").addScalar("preregisterGradename")
		.addScalar("preregisterDate",Hibernate.TIMESTAMP).addScalar("preregisterStarttime").addScalar("preregisterEndtime")
		.addScalar("medicalrecordId").addScalar("idcardId").addScalar("preregisterCertificatestype").addScalar("preregisterCertificatesno").addScalar("preregisterName")
		.addScalar("preregisterSex").addScalar("preregisterAge",Hibernate.INTEGER).addScalar("preregisterAgeunit").addScalar("preregisterPhone").addScalar("preregisterAddress")
		.addScalar("preregisterMiddayname").addScalar("createTime",Hibernate.DATE).addScalar("midday", Hibernate.INTEGER);
		if(preregister!=null){
			if(StringUtils.isNotBlank(preregister.getIdcardId())){
				queryObject.setParameter("idcardId", preregister.getIdcardId());
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterNo())){
				queryObject.setParameter("preregisterNo", preregister.getPreregisterNo());
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterCertificatesno())){
				queryObject.setParameter("preregisterCertificatesno", preregister.getPreregisterCertificatesno());
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterName())){
				queryObject.setParameter("preregisterName", "%"+preregister.getPreregisterName()+"%");
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterPhone())){
				queryObject.setParameter("preregisterPhone", preregister.getPreregisterPhone());
			}
			
			if(preregister.getPreregisterDate()!=null){
				queryObject.setParameter("preregisterDate", DateUtils.formatDateY_M_D(preregister.getPreregisterDate()));
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterDept())){
				queryObject.setParameter("preregisterDept", preregister.getPreregisterDept());
			}
			if(StringUtils.isNotBlank(preregister.getPreregisterExpert())){
				queryObject.setParameter("preregisterExpert", preregister.getPreregisterExpert());
			}
			if(StringUtils.isNotBlank(preregister.getMedicalrecordId())){
				queryObject.setParameter("medicalrecordId", preregister.getMedicalrecordId());
			}
		}
		
		List<RegisterPreregisterNow> preregisters = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterPreregisterNow.class)).setFirstResult((p-1)*r).setMaxResults(p*r).list();
		if(preregisters!=null&&preregisters.size()>0){
			return preregisters;
		}
		return new ArrayList<RegisterPreregisterNow>();
	}

	@Override
	public InfoPatient judgeIdcrad(String idCardNo) {
		String hql = " select i.idcard_no as idCardNo,i.patinent_id as patientId from t_patient_idcard i"
				+ " where i.stop_flg=0 and i.del_flg=0 and i.idcard_status=1 and i.idcard_no=?";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("idCardNo").addScalar("patientId");
		queryObject.setParameter(0, idCardNo);
		List<InfoPatient> patientList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoPatient.class)).list();
		if(patientList==null||patientList.size()<=0){
			return new InfoPatient();
		}
		return patientList.get(0);
	}

	@Override
	public List<RegistrationNow> queryBackNo(String cardNo, String no) {
		String hql = "from RegistrationNow where trunc(regDate,'dd')=to_DATE(?,'yyyy-MM-dd')";
		if(!"".equals(cardNo)){
			hql += " and cardNo = '"+cardNo+"'";
		}
		if(!"".equals(no)){
			hql += " and clinicCode = '"+no+"'";
		}
		hql += " and transType=1 and ynsee=0 and inState=0 and del_flg=0 and stop_flg=0 ";
		List<RegistrationNow> registrationList = super.find(hql, DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()));
		if(registrationList==null||registrationList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return registrationList;
	}

	@Override
	public BusinessPayModeNow queryPayMode(String invoiceNo) {
		String hql = "from BusinessPayModeNow where invoiceNo =? and transType=1 and del_flg=0 and stop_flg=0 ";
		List<BusinessPayModeNow> payModeList = super.find(hql, invoiceNo);
		if(payModeList==null||payModeList.size()<=0){
			return new BusinessPayModeNow();
		}
		return payModeList.get(0);
	}

	@Override
	public void updateFinanceInvoice(String invoiceId,String id, String invoiceType,String invoiceNo) {
		//判断这个发票号是不是本号段最后一位
		String hql = "from FinanceInvoice  where id = ? and invoiceGetperson = ? and  invoiceType= ? and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> financeInvoiceList = super.find(hql,invoiceId, id,invoiceType);
		if(financeInvoiceList!=null && financeInvoiceList.size()>0){
			String invoiceEndno = financeInvoiceList.get(0).getInvoiceEndno();//得到终止号
			String invoiceStartno = financeInvoiceList.get(0).getInvoiceStartno();//得到开始号
			if(invoiceEndno.equals(invoiceNo)){//当等于终止号的时候相当最后一个
				String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = ? and  invoiceType= ? and id = ?";
				this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).setString(2, id).setString(3, invoiceType).setString(4, invoiceId).executeUpdate();
			}else if(invoiceStartno.equals(invoiceNo)){//当等于开始号的时候相当于第一个
				String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = ? and  invoiceType= ? and id = ?";
				this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).setString(2, id).setString(3, invoiceType).setString(4, invoiceId).executeUpdate();
			}else{
				String hql4 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = ? and  invoiceType= ? and id = ?";
				this.createQuery(hql4).setString(0,invoiceNo).setLong(1,1 ).setString(2, id).setString(3, invoiceType).setString(4, invoiceId).executeUpdate();
			}
		}
	}

	/**  
	 * @Description：获得患者挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public RegistrationNow queryInfoByCliNo(String clinicCode) {
		String hql = "from RegistrationNow r where r.clinicCode = ? and inState = 0";
		RegistrationNow registration = (RegistrationNow) super.excHqlGetUniqueness(hql, clinicCode);
		if(StringUtils.isNotBlank(registration.getId())){
			return registration;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  根据病历号和看诊序号查询当天的挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:53:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:53:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegistrationNow getRegisterInfoByPatientNoAndNo(String patientNo,String no) {
		String hql = " FROM RegistrationNow i WHERE i.inState=0 AND i.stop_flg=0 AND i.del_flg=0 AND i.clinicCode = ? AND i.midicalrecordId = ? AND i.deptCode = ?";
		RegistrationNow registration = (RegistrationNow) super.excHqlGetUniqueness(hql,no,patientNo,ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		if(registration!=null&&StringUtils.isNotBlank(registration.getId())){
			return registration;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：诊出
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegistrationNow getPatient(String clinicNo) {
		String hql = " FROM RegistrationNow i WHERE i.stop_flg=0 AND i.del_flg=0 AND i.clinicCode = ? ";
		RegistrationNow registration = (RegistrationNow) super.excHqlGetUniqueness(hql,clinicNo);
		if(registration!=null&&StringUtils.isNotBlank(registration.getId())){
			return registration;
		}
		return null;
	}


	@Override
	public List<BusinessContractunit> querypackCode(String code) {
		String hql = " FROM BusinessContractunit i WHERE i.stop_flg=0 AND i.del_flg=0 AND i.encode =:code ";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("code", code);
		List<BusinessContractunit> registrationList =query.list();
		if(registrationList==null||registrationList.size()<=0){
			return new ArrayList<BusinessContractunit>();
		}
		return registrationList;
	}


	@Override
	public RegistrationNow queryRegistrationById(String code) {
		String hql = "from RegistrationNow where id = :code and del_flg=0 and stop_flg=0";
		Query query=this.getSession().createQuery(hql);
		query.setParameter("code", code);
		List<RegistrationNow> registrationList=query.list();
		if(registrationList==null||registrationList.size()<=0){
			return new RegistrationNow();
		}
		return registrationList.get(0);
	}


	@Override
	public void saveRegisterPreregister(RegisterPreregisterNow registerPreregister) {
		super.update(registerPreregister);
	}
	
	/**  
	 * @Description：  值班列表
	 * @Author：GH 
	 * @CreateDate：2016年12月2日16:20:10
	 * @ModifyRmk：查询医生号源表的挂号数据  RegisterDocSource
	 * @version 1.0
	 */
	@Override
	public List<RegisterDocSource> findNewInfoList(String deptId, String empId,
			String gradeId, Integer midday,String docSource) {
	    StringBuffer sql = new StringBuffer("select ");

	    sql.append(" t.ID as id, ");
	    sql.append(" t.EMPLOYEE_CODE as employeeCode, ");
	    sql.append(" t.EMPLOYEE_NAME as employeeName, ");
	    sql.append(" t.GRADE_CODE as gradeCode, ");
	    sql.append(" t.GRADE_NAME as gradeName, ");
	    sql.append(" t.DEPT_CODE as deptCode, ");
	    sql.append(" t.DEPT_NAME as deptName, ");
	    sql.append(" t.MIDDAY_CODE as middayCode, ");
	    sql.append(" t.MIDDAY_NAME as middayName, ");
	    sql.append(" t.CLINIC_CODE as clinicCode, ");
	    sql.append(" t.CLINIC_NAME as clinicName, ");
	    sql.append(" t.LIMIT_SUM as limitSum, ");
	    sql.append(" t.PECIALLIMIT_SUM as peciallimitSum, ");
	    sql.append(" t.CLINIC_SUM as clinicSum, ");
	    sql.append(" t.APPFLAG as appFlag, ");
	    sql.append(" t.ISSTOP as isStop, ");
	    sql.append(" t.STOPREASON as stopReason, ");
	    sql.append(" t.SCHEDULE_ID as scheduleId, ");
	    sql.append(" t.PREREGISTER_SUM as preregisterSum,");
	    sql.append(" t.PRECLINIC_SUM as preclinicSum,");
	    sql.append(" t.REG_DATE as regDate,t.VERSION as version ");
	    sql.append(" from T_REGISTER_DOC_SOURCE t ");
	    sql.append(" where t.del_flg=0 ");
	    sql.append(" and t.REG_DATE>=to_date(:today,'yyyy-mm-dd') and t.REG_DATE<to_date(:tomorrow,'yyyy-mm-dd')");
		if (StringUtils.isNotBlank(deptId)) {
			sql.append(" and t.DEPT_CODE  = :deptId");
		} else {
			sql.append(" and t.DEPT_CODE in (select d.dept_code from  t_department d where d.del_flg=0 and d.stop_flg=0 and d.dept_isforregister=1) ");
		}
		if (StringUtils.isNotBlank(empId)) {
			sql.append(" and t.EMPLOYEE_CODE = :empId ");
		}
		if (StringUtils.isNotBlank(gradeId)) {
			sql.append(" and t.GRADE_CODE = :gradeId ");
		}
		if (midday!=null) {
			sql.append(" and t.MIDDAY_CODE = :midday ");
		}
		if (StringUtils.isNotBlank(docSource)) {
			sql.append(" and t.id = :docSource ");
		}
		sql.append(" order by t.DEPT_CODE,t.GRADE_CODE");
		
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("id")
				.addScalar("employeeCode").addScalar("employeeName").addScalar("gradeCode")
				.addScalar("gradeName").addScalar("deptCode").addScalar("deptName").addScalar("middayCode")
				.addScalar("middayName").addScalar("clinicCode").addScalar("clinicName")
				.addScalar("version",Hibernate.INTEGER).addScalar("limitSum",Hibernate.INTEGER)
				.addScalar("peciallimitSum",Hibernate.INTEGER).addScalar("clinicSum",Hibernate.INTEGER)
				.addScalar("appFlag",Hibernate.INTEGER).addScalar("isStop",Hibernate.INTEGER)
				.addScalar("preregisterSum", Hibernate.INTEGER).addScalar("preclinicSum", Hibernate.INTEGER)
				.addScalar("stopReason").addScalar("scheduleId").addScalar("regDate",Hibernate.DATE);
		queryObject.setParameter("today", DateUtils.formatDateY_M_D(new Date()));
		queryObject.setParameter("tomorrow", DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(),1)));
		
		if (StringUtils.isNotBlank(deptId)) {
			queryObject.setParameter("deptId", deptId);
		}
		if (StringUtils.isNotBlank(empId)) {
			queryObject.setParameter("empId", empId);
		}
		if (StringUtils.isNotBlank(gradeId)) {
			queryObject.setParameter("gradeId", gradeId);
		}
		if (midday!=null) {
			queryObject.setParameter("midday", midday);
		}
		if (StringUtils.isNotBlank(docSource)) {
			queryObject.setParameter("docSource", docSource);
		}
		List<RegisterDocSource> list = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterDocSource.class)).list();
		if(list!=null&&list.size()>0){
			 return list;
		}
		return new ArrayList<RegisterDocSource>();
	}
	@Override
	public RegisterDaybalance getRegDaybalance(String dept, String userid,Date date) {
		String hql = " from RegisterDaybalance t where t.createUser = ? and t.createDept = ? and trunc(createTime,'dd') = to_date(?,'yyyy-MM-dd')";
		List<RegisterDaybalance> list = super.find(hql, userid,dept,DateUtils.formatDateY_M_D(date));
		if(list==null||list.size()<0){
			return null;
		}
		return list.get(0);
	}


	@Override
	public List<RegistrationNow> getRegisterByCliNo(String clinicCode) {
		String hql = " from RegistrationNow t where t.clinicCode = ? and t.transType = 1 and t.validFlag = 1 and t.inState = 1  ";
		List<RegistrationNow> list = super.find(hql, clinicCode);
		if(list==null||list.size()<0){
			return null;
		}
		return list;
	}


	@Override
	public RegisterBalancedetail getRegBalanceDetail(RegisterDaybalance balance, String payType) {
		String hql = " from RegisterBalancedetail t where t.balance = ? and t.payType = ?  ";
		List<RegisterBalancedetail> list = super.find(hql, balance,payType);
		if(list==null||list.size()<0){
			return null;
		}
		return list.get(0);
	}


	@Override
	public List<RegistrationNow> queryRestration(final RegistrationNow actionInfo,String page,String rows) {
		final String sql = queryRestrationSql(actionInfo, page, rows);
		final int start = Integer.parseInt(page == null? "1" : page);
		final int count = Integer.parseInt(rows == null? "30" : rows);
		List<RegistrationNow> list = this.getHibernateTemplate().execute(new HibernateCallback<List<RegistrationNow>>() {

			@Override
			public List<RegistrationNow> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sql);
				if(StringUtils.isNotBlank(actionInfo.getCardNo())){
					query.setParameter("cardNo", actionInfo.getCardNo());
				}
				if(StringUtils.isNotBlank(actionInfo.getDeptCode())){
					query.setParameter("deptCode", actionInfo.getDeptCode());
				}
				if(StringUtils.isNotBlank(actionInfo.getDoctCode())){
					query.setParameter("doctCode", actionInfo.getDoctCode());
				}
				if(StringUtils.isNotBlank(actionInfo.getReglevlCode())){
					query.setParameter("reglevlCode", actionInfo.getReglevlCode());
				}
				if(actionInfo.getNoonCode()!=null){
					query.setParameter("noonCode", actionInfo.getNoonCode());
				}
				query.list();
				return query.setFirstResult((start - 1) * count).setMaxResults(count).list();
			}
		});
		if(list==null||list.size()<0){
			return new ArrayList<RegistrationNow>();
		}
		return list;
	}
	public int queryRestrationTotal(RegistrationNow actionInfo,String page,String rows){
		String sql = queryRestrationSql(actionInfo, page, rows);
		sql = " select count(id) "+sql;
		Query query = this.getSession().createQuery(sql);
		if(StringUtils.isNotBlank(actionInfo.getCardNo())){
			query.setParameter("cardNo", actionInfo.getCardNo());
		}
		if(StringUtils.isNotBlank(actionInfo.getDeptCode())){
			query.setParameter("deptCode", actionInfo.getDeptCode());
		}
		if(StringUtils.isNotBlank(actionInfo.getDoctCode())){
			query.setParameter("doctCode", actionInfo.getDoctCode());
		}
		if(StringUtils.isNotBlank(actionInfo.getReglevlCode())){
			query.setParameter("reglevlCode", actionInfo.getReglevlCode());
		}
		if(actionInfo.getNoonCode()!=null){
			query.setParameter("noonCode", actionInfo.getNoonCode());
		}
		int total = ((Long) query.uniqueResult()).intValue();
		return total;
	}
	public String queryRestrationSql(RegistrationNow actionInfo,String page,String rows){
		StringBuffer sb = new StringBuffer();
		sb.append(" from RegistrationNow where del_flg=0 and stop_flg=0 and transType=1");
		if(StringUtils.isNotBlank(actionInfo.getCardNo())){
			sb.append(" and cardNo = :cardNo ");
		}
		if(StringUtils.isNotBlank(actionInfo.getDeptCode())){
			sb.append(" and deptCode = :deptCode ");
		}
		if(StringUtils.isNotBlank(actionInfo.getDoctCode())){
			sb.append(" and doctCode = :doctCode");
		}
		if(StringUtils.isNotBlank(actionInfo.getReglevlCode())){
			sb.append(" and reglevlCode = :reglevlCode");
		}
		if(actionInfo.getNoonCode()!=null){
			sb.append(" and noonCode = :noonCode ");
		}
		sb.append(" order by regDate desc group by deptCode,doctCode ");
		return sb.toString();
	}

	@Override
	public List<OutpatientRecipedetailNow> checkISsee(String clinicCode) {
		String hql = "from OutpatientRecipedetailNow t where t.clinicCode = ?";
		List<OutpatientRecipedetailNow> list = super.find(hql, clinicCode);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public RegPrintVO getRegByid(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Select m.REG_DATE As regDate,m.Patient_Name As patientName,m.Dept_Name As deptName,m.Reg_Fee As regFee,");
		sb.append(" m.DIAG_FEE As chckFee,m.Order_No As orderNo,m.NOON_CODENMAE As noonCodeNmae,d.DEPT_ADDRESS As deptAdress,");
		sb.append(" m.MEDICALRECORDID As midicalrecordId,m.CREATEHOS As createhos,m.SCHEMA_NO as schemaNo,m.CLINIC_CODE as clinicCode,m.DOCT_NAME as doctName ");
		sb.append(" From t_Register_Main_Now m Left Join T_DEPARTMENT d On m.DEPT_CODE = d.DEPT_CODE");
		sb.append(" Where m.Id = ? ");
		SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());
		query.setParameter(0, id);
		query.addScalar("regDate",Hibernate.TIMESTAMP).addScalar("patientName").addScalar("deptName").addScalar("regFee",Hibernate.DOUBLE).addScalar("doctName")
		.addScalar("chckFee",Hibernate.DOUBLE).addScalar("orderNo",Hibernate.INTEGER).addScalar("noonCodeNmae").addScalar("deptAdress")
		.addScalar("midicalrecordId").addScalar("createhos").addScalar("schemaNo").addScalar("clinicCode",Hibernate.STRING);
		return (RegPrintVO) query.setResultTransformer(Transformers.aliasToBean(RegPrintVO.class)).uniqueResult();
	}

	@Override
	public List<RegistrationNow> queryBackNo(String cardNo) {
		return null;
	}
	public int isEmployeeBlack(){
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前的登录人
		StringBuilder sql = new StringBuilder();
		sql.append("select * from T_EMPLOYEE_BLACKLIST t where t.del_flg='0' and t.employee_id='"+account+"'");
		return super.getSqlTotal(sql.toString());
	}

	@Override
	public HospitalVo queryHospitalInfo(String deptCode) {
		
		String sql="SELECT HOSPITAL_ID AS HOSPITAL_ID,DEPT_AREA_CODE AS DEPT_AREA_CODE FROM T_DEPARTMENT WHERE DEPT_ID='"+deptCode+"'";
		
		
		return (HospitalVo) namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(HospitalVo.class)).get(0);
	}
}
