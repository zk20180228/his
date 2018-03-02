package cn.honry.outpatient.preregister.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.preregister.dao.BusinessPreregisterDAO;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.preregister.vo.IdCardPreVo;
import cn.honry.outpatient.preregister.vo.RegInfoInInterVo;
import cn.honry.outpatient.schedule.dao.ScheduleDAO;
@Repository("businessPreregisterDAO")
@SuppressWarnings({ "all" })
public class BusinessPreregisterDAOImpl extends HibernateEntityDao<RegisterInfo> implements BusinessPreregisterDAO{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "scheduleDAO")
	private ScheduleDAO scheduleDAO;
	/**  
	 *  
	 * @Description： 数据源
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysEmployee queryInfoPeret(String userId) {
		String hql = " from SysEmployee where jobNo = '"+userId+"' and del_flg=0 and stop_flg=0";
		List<SysEmployee> sysEmployeeList = super.find(hql, null);
		if(sysEmployeeList==null||sysEmployeeList.size()<=0){
			return new SysEmployee();
		}
		return sysEmployeeList.get(0);
	}
	/**  
	 * @Description：查询排班信息（剩余号数）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegInfoInInterVo> findInfoList(String id) {
		StringBuffer sbf= new StringBuffer("select ");
		sbf.append(" (select count(*) from t_register_preregister_now t4 ")
		.append(" where t4.preregister_midday=t.schedule_midday and t4.preregister_expert=t.schedule_doctor ")
		.append(" and t4.preregister_status=1 and t4.preregister_date=trunc(t.schedule_date,'dd') ")
		.append(" and t4.stop_flg=0 and t4.del_flg=0) as infoAlready,")
		
		.append(" t.schedule_doctor as empId,t.schedule_date as dates,t.schedule_midday as midday,")
		.append(" t.schedule_workdept as deptId,t.schedule_prelimit as limit,")
		.append(" t.schedule_appflag as appFlag,t.schedule_reggrade as grade,")
		.append(" t.schedule_isstop  as isStop, t.schedule_stopreason as stoprEason,")
		
		.append(" (select t6.clinic_name from t_clinic t6 where t6.id=t.schedule_clinicid and ")
		.append(" t6.stop_flg=t.stop_flg and t6.del_flg=t.del_flg) as clinic,")
		
		.append(" (select t0.dept_name from t_department t0 where t0.dept_code=t.schedule_deptid and ")
		.append(" t0.stop_flg=0 and t0.del_flg=0) as deptName,")
		
		.append(" (select t1.employee_name from t_employee t1 where t1.employee_jobno=t.schedule_doctor and ")
		.append(" t1.stop_flg=0 and t1.del_flg=0) as empName,")
		
		.append(" (select t2.grade_name from t_register_grade t2 where t2.grade_code=t.schedule_reggrade ")
		.append(" and t2.stop_flg=0 and t2.del_flg=0) as titleName ")
		
		.append(" from T_REGISTER_SCHEDULE_NOW t ")
		.append(" where t.schedule_doctor = :id and t.STOP_FLG=0 and t.DEL_FLG=0 and ")
		.append(" t.schedule_class=1 and t.schedule_isstop=2 ");
		
		String sql = sbf.toString();
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("infoAlready",Hibernate.INTEGER)
				.addScalar("empId").addScalar("dates",Hibernate.DATE).addScalar("midday",Hibernate.INTEGER)
				.addScalar("deptId").addScalar("clinic").addScalar("limit",Hibernate.INTEGER)
				.addScalar("stoprEason").addScalar("deptName").addScalar("empName").addScalar("grade")
				.addScalar("titleName").addScalar("isStop",Hibernate.INTEGER)
				.addScalar("appFlag",Hibernate.INTEGER);
		queryObject.setParameter("id", id);
		List<RegInfoInInterVo> infoVoList =
				queryObject.setResultTransformer(Transformers.aliasToBean(RegInfoInInterVo.class)).list();
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
		return new ArrayList<RegInfoInInterVo>();
	}
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public IdCardPreVo searchIdcard(String idcardNo) {
		String hql = " select p.patient_name as voPname, p.patient_sex as voPsex,p.patient_age as voPage,p.patient_ageunit as voPageUnit,p.patient_address as voPaddrss, p.patient_certificatestype as voPtype, p.patient_certificatesno as voPtypeNo,p.patient_phone as voPphone,p.medicalrecord_id as voPmedicalrecordId, i.idcard_no as idCardNo,p.patient_birthday as voPdata,i.idcard_createtime as cIdCardNoTime  from t_patient_idcard i left join t_patient p on i.patinent_id=p.patinent_id where i.idcard_no = '"+idcardNo+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("voPname").addScalar("voPsex",Hibernate.INTEGER).addScalar("voPage",Hibernate.INTEGER).addScalar("voPageUnit").addScalar("voPaddrss").addScalar("voPtype").addScalar("voPtypeNo").addScalar("voPphone").addScalar("voPmedicalrecordId").addScalar("idCardNo").addScalar("voPdata",Hibernate.DATE).addScalar("cIdCardNoTime",Hibernate.DATE);
		List<IdCardPreVo> idCardPreVo = queryObject.setResultTransformer(Transformers.aliasToBean(IdCardPreVo.class)).list();
		if(idCardPreVo!=null&&idCardPreVo.size()>0){
			return idCardPreVo.get(0);
		}
		return new IdCardPreVo();
	}
	/**  
	 * @Description：级别转换
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public RegisterGrade findGradeEdit(String gradeId) {
		String hql = " from RegisterGrade where encode = '"+gradeId+"' and del_flg=0 and stop_flg=0";
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new RegisterGrade();
		}
		return gradeList.get(0);
	}
	/**  
	 * @Description：  通过预约午别带入开始结束时间
	 * @Author：wujiao
	 * @CreateDate：2016-1-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public RegisterScheduleNow queryAll(String time, String gradeid,
			String deptid,String middy) {
		StringBuffer sbf= new StringBuffer("select s.id as id,")
		.append(" s.startTime as startTime,s.endTime as endTime,s.date as date ")
		.append(" from RegisterScheduleNow s ")
		.append(" where trunc(s.date,'dd')=to_date(?,'yyyy-mm-dd') ")
		.append(" and s.department in(select d.deptCode from SysDepartment d where d.deptIsforregister = '1') ");
		
		if(StringUtils.isNotBlank(gradeid)){
			sbf.append(" and s.reggrade ='"+gradeid+"'");
		}
		if(StringUtils.isNotBlank(deptid)){
			sbf.append(" and s.department='"+deptid+"'");
		}if(StringUtils.isNotBlank(middy)){
			sbf.append("and s.midday="+middy);
		}
		List<RegisterScheduleNow> schList=new ArrayList<RegisterScheduleNow>();
		schList = this.createQuery(sbf.toString(), time).
				 setResultTransformer(Transformers.aliasToBean(RegisterScheduleNow.class)).list();
		if(schList!=null && schList.size()>0){
			return schList.get(0);
		}
		return null;
	}
	@Override
	public RegisterPreregisterNow queryPreInfo(String dates, String idCardno,String empId, String midday) {
		String hql = "from RegisterPreregisterNow where preregisterExpert = ? and idcardId = ? and preregisterDate =to_date(?,'yyyy-MM-dd')  and midday = ? and del_flg=0 and stop_flg=0 ";
		List<RegisterPreregisterNow> preregisterList = super.find(hql, empId,idCardno,dates,Integer.parseInt(midday));
		if(preregisterList==null||preregisterList.size()<=0){
			return new RegisterPreregisterNow();
		}
		return preregisterList.get(0);
	}
	
	@Override
	public PatientIdcard queryPatientIdcard(String idCardno) {
		String hql = "from PatientIdcard where idcardNo = ?  ";
		List<PatientIdcard> patientIdcard = super.find(hql, idCardno);
		if(patientIdcard==null||patientIdcard.size()<=0){
			return new PatientIdcard();
		}
		return patientIdcard.get(0);
	}
	@Override
	public EmpScheduleVo getEmpee(String empId) {
		StringBuffer sbf=new StringBuffer("select e.employee_name as empName,g.grade_name as gradeName");
		sbf.append(" from T_EMPLOYEE e,T_BUSINESS_DICTIONARY d,T_REGISTER_GRADE g");
		sbf.append(" where e.employee_title = d.code_encode and d.code_type = 'title'");
		sbf.append(" and g.grade_code = d.code_mark and g.grade_title = d.code_encode");
		sbf.append(" and e.employee_jobno=:empId ");
		String sql=sbf.toString();
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("gradeName").addScalar("empName");
		queryObject.setParameter("empId", empId);
		List<EmpScheduleVo> find =
				queryObject.setResultTransformer(Transformers.aliasToBean(EmpScheduleVo.class)).list();
		if(find!=null||find.size()>0){
			return find.get(0);
		}
		return null;
	}
}
