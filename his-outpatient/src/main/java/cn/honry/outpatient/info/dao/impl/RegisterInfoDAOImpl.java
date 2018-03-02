package cn.honry.outpatient.info.dao.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.outpatient.info.dao.RegisterInfoDAO;
import cn.honry.outpatient.info.dao.ViewIdcardPatientsDAO;
import cn.honry.outpatient.info.vo.EmpInfoVo;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoStatistics;
import cn.honry.outpatient.info.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("registerInfoDAO")
@SuppressWarnings({ "all" })
public class RegisterInfoDAOImpl extends HibernateEntityDao<RegisterInfo> implements RegisterInfoDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	private ViewIdcardPatientsDAO viewIdcardPatientsDAO;
	@Autowired
	private KeyvalueInInterDAO keyvalueInInterDAO;
	@Autowired
	private PatinmentInnerDao patinmentInnerDao;
	
	/**  
	 *  
	 * @Description：  门诊卡id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo gethz(String idcardNo) {
		String hql="FROM RegisterInfo r WHERE r.del_flg = 0 ";
		/*if(idcardNo==null){
			return new ArrayList<RegisterInfo>();
		}*/
		hql = hql+" AND r.idcardId = '"+idcardNo+"' ";
		List<RegisterInfo> cardList = super.findByObjectProperty(hql, null);
		if(cardList!=null&&cardList.size()>0){
			return cardList.get(0);
		}
		return null;
	}
	/**  
	 *  
	 * @Description：  查询患者树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterInfo> getInfoByEmployeeId(String id,String type) {
		String hql="from RegisterInfo r where r.del_flg=0 and r.stop_flg=0 AND r.expxrt = '"+id+"' ";
		if(type!=null){
			hql += " AND r.seeFlag = "+type+"";
		}
		List<RegisterInfo>  registerInfoList =super.findByObjectProperty(hql, null);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return null;
	}
	/**  
	 *  
	 * @Description：  单击患者查询id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午5:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午5:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo gethzid(String id) {
		String hql="FROM RegisterInfo r WHERE r.del_flg = 0 ";
		hql = hql+" AND r.id = '"+id+"' ";
		List<RegisterInfo> cardList = super.findByObjectProperty(hql, null);
		if(cardList!=null&&cardList.size()>0){
			return cardList.get(0);
		}
		return null;
	}
	
	@Override
	public List<RegisterInfo> getInfoByTime(Date startTime, Date endTime,String payTypeId,String registrarId) {
		String hql = "from RegisterInfo i where i.del_flg = 0 AND i.createUser = '"+registrarId+"' ";
		if(StringUtils.isNotBlank(payTypeId)){
			hql = hql+ " AND i.payType = '"+payTypeId+"' ";
		}
		if(startTime!=null&&endTime!=null){
			hql = hql+ " AND i.createTime > TO_DATE('"+DateUtils.formatDateY_M_D_H_M_S(startTime)+"','YYYY-MM-DD HH24:MI:SS') AND i.createTime <= TO_DATE('"+DateUtils.formatDateY_M_D_H_M_S(endTime)+"','YYYY-MM-DD HH24:MI:SS')";
		}
		return super.find(hql, null);
	}

	@Override
	public List<RegisterInfo> getInfoListbydeptId(String deptId) {
		String hql="from RegisterInfo r where r.del_flg=0 and r.stop_flg=0 AND r.dept = '"+deptId+"' ";
		List<RegisterInfo>  registerInfoList =super.findByObjectProperty(hql, null);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return null;
	}

	/**  
	 *  
	 * @Description： 根据病历号 查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-19 下午01:09:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo queryRegisterInfoByCaseNo(String id) {
		String hql = " from RegisterInfo i where i.del_flg=0 and i.stop_flg=0 and i.midicalrecordId='"+id+"'" +
					 " and TO_CHAR(i.date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' " +
					 " and i.dept = '"+ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"'" +
					 " ORDER BY date DESC";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}

	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> deptCombobox() {
		String hql = "from SysDepartment where deptIsforregister=1 and del_flg=0 and stop_flg=0 order by deptRegisterno";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList==null||deptList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return deptList;
	}
	/**  
	 * @Description：  挂号级别（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<RegisterGrade> gradeCombobox(String q) {
		String hql = "from RegisterGrade where del_flg=0 and stop_flg=0";
		if(StringUtils.isNotBlank(q)){
			hql += "and (upper(codePinyin) like '%"+q.toUpperCase()+"%' or upper(codeWb) like '%"+q.toUpperCase()+"%' or upper(codeInputcode) like '%"+q.toUpperCase()+"%')";
		}
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return gradeList;
	}
	/**  
	 * @Description：  挂号专家（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<EmpInfoVo> empCombobox(String dept,String grade,Integer midday) {
		String hql = "select e.employee_id as id,e.employee_name as name,e.employee_title as title  from t_register_schedule s left join t_employee e  on s.schedule_doctor = e.employee_id  where 1=1  ";
		if(StringUtils.isNotBlank(dept)){
			hql =hql + " and s.schedule_workdept = '"+dept+"' ";
		}
		if(midday!=null){
			hql = hql + " and s.schedule_midday ='"+midday+"' ";
		}
		if(StringUtils.isNotBlank(grade)){
			hql = hql + " and e.employee_title = '"+grade+"' ";
		}
		hql = hql + " and to_char(s.schedule_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' group by e.employee_id,e.employee_name,e.employee_title";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name").addScalar("title");
		List<EmpInfoVo> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(EmpInfoVo.class)).list();
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
		return new ArrayList<EmpInfoVo>();
	}
	/**  
	 *  
	 * @Description：  合同单位
	 * @parm:
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> queryBusinessContractunit() {
		String hql = "from BusinessContractunit r where r.del_flg = 0  ORDER BY order ";
		List<BusinessContractunit> businessContractunitList = super.find(hql, null);
		if(businessContractunitList==null||businessContractunitList.size()<=0){
			return new ArrayList<BusinessContractunit>();
		}
		return businessContractunitList;
	}
	/**  
	 *  
	 * @Description：  显示挂号费
	 * @parm:id（合同单位ID）
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public RegisterFee queryRegisterFee(String id, String gradeId) {
		String hql = "from RegisterFee r where r.unitId = '"+id+"' and r.registerGrade = '"+gradeId+"'  and r.del_flg = 0 ";
		List<RegisterFee> registerFeeList = super.find(hql, null);
		if(registerFeeList==null||registerFeeList.size()<=0){
			return new RegisterFee();
		}
		return registerFeeList.get(0);
	}
	/**  
	 * @Description：  值班列表
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<InfoVo> findInfoList(String deptId, String empId, String gradeId, String midday) {
	  String hql = "select ((select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_info t3 where t3.register_midday=t.schedule_midday and t3.register_expert=t.schedule_doctor and to_char(t3.register_date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'  and  t3.stop_flg=0 and t3.register_status = 1 and t3.del_flg=0 and t3.register_type in ('01','02','03') )+(select count(1) from  "+HisParameters.HISPARSCHEMAHISUSER+"t_register_preregister t4 where t4.preregister_midday=t.schedule_midday and t4.preregister_expert=t.schedule_doctor  and t4.preregister_status=1 and to_char(t4.preregister_date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t4.stop_flg=0 and t4.del_flg=0)) as infoAlready,"+
	  			   " t.schedule_doctor as empId,"+
	  			   " t.schedule_midday as midday,"+
	  			   " t.SCHEDULE_WORKDEPT as deptId,"+
	  			   " (select t6.clinic_name from "+HisParameters.HISPARSCHEMAHISUSER+"t_clinic t6 where t6.id=t.schedule_clinicid and t6.stop_flg=t.stop_flg and t6.del_flg=t.del_flg ) as clinic,"+
	  			   " t.schedule_limit as limit,"+
	  			   " t.schedule_stopreason as stoprEason,"+
	  			   " (select t0.dept_name from "+HisParameters.HISPARSCHEMAHISUSER+"t_department t0 where t0.dept_id=t.SCHEDULE_WORKDEPT and t0.stop_flg=0 and t0.del_flg=0) as deptName,"+
	  			   " (select t1.employee_name from "+HisParameters.HISPARSCHEMAHISUSER+"t_employee t1 where t1.employee_id=t.schedule_doctor and t1.stop_flg=0 and t1.del_flg=0) as empName,"+
	  			   " t.schedule_reggrade as grade,"+
	  			   " (select t2.grade_name from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_grade t2 where t2.grade_id=t.schedule_reggrade and t2.stop_flg=0 and t2.del_flg=0) as titleName,"+
	  			   " t.schedule_isstop as isStop,"+
	  			   " t.schedule_appflag as appFlag,"+
	  			   " t.schedule_peciallimit as speciallimit"+
	  			   " from "+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_SCHEDULE t where"+
	  			   " to_char(t.schedule_date,'yyyy-MM-dd')= '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t.del_flg = 0 and t.schedule_class = 1 ";
	  if(StringUtils.isNotBlank(deptId)){
		  hql = hql + " and t.SCHEDULE_WORKDEPT  = '"+deptId+"'";
	  }else{
		  hql = hql + " and t.SCHEDULE_WORKDEPT in (select d.dept_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_department d where d.del_flg=0 and d.stop_flg=0 and d.dept_isforregister=1)  ";
	  }
	  if(StringUtils.isNotBlank(empId)){
		  hql = hql + " and t.schedule_doctor = '"+empId+"'";
	  }
	  if(StringUtils.isNotBlank(gradeId)){
		  hql = hql + " and t.schedule_reggrade = '"+gradeId+"'";
	  }
	  if(StringUtils.isNotBlank(midday)){
		  hql = hql + " and t.schedule_midday = '"+midday+"'";
	  }
	  hql  = hql + " order by t.schedule_deptid,t.schedule_reggrade";
	  SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("infoAlready",Hibernate.INTEGER).addScalar("empId").addScalar("midday",Hibernate.INTEGER)
	  .addScalar("deptId").addScalar("clinic",Hibernate.STRING).addScalar("limit",Hibernate.INTEGER).addScalar("stoprEason").addScalar("deptName").addScalar("empName").addScalar("grade").addScalar("titleName").addScalar("isStop",Hibernate.INTEGER).addScalar("appFlag",Hibernate.INTEGER).addScalar("speciallimit",Hibernate.INTEGER);
	  List<InfoVo> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoVo.class)).list();
	  if(infoVoList!=null&&infoVoList.size()>0){
		  return infoVoList;
	  }
	  return new ArrayList<InfoVo>();
	}
	
	/**  
	 * @Description：  根据职称转换
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterGrade queryGradeTitle(String encode) {
		String hql = " from RegisterGrade where del_flg=0 and stop_flg=0 and encode='"+encode+"'  ";
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new RegisterGrade();
		}
		return gradeList.get(0);
	}
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoPatient queryRegisterInfo(String idcardNo) {
		String hql = "select p.patinent_id as patientId,i.idcard_id as idCardNo,p.patient_name as name,p.patient_sex as sex,p.patient_birthday as dates,p.medicalrecord_id as infoMedicalrecordId,p.patient_certificatesno as patientCertificatesno,p.unit_id as cout " +
			    	 "from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_idcard i left join "+HisParameters.HISPARSCHEMAHISUSER+"t_patient p on i.patinent_id = p.patinent_id where i.idcard_no='"+idcardNo+"'and i.del_flg=0  and  i.stop_flg=0  and p.del_flg=0 and p.stop_flg=0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("idCardNo").addScalar("name").addScalar("sex",Hibernate.INTEGER).addScalar("dates",Hibernate.DATE).addScalar("infoMedicalrecordId").addScalar("patientCertificatesno").addScalar("patientId").addScalar("cout");
		List<InfoPatient> infoPatient = queryObject.setResultTransformer(Transformers.aliasToBean(InfoPatient.class)).list();
		if(infoPatient!=null&&infoPatient.size()>0){
			return infoPatient.get(0);
		}
		return new InfoPatient();
	}
	/**  
	 * @Description：  根据患者身份证号查询是否有预约
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterPreregister findPreregister(String patientLinkdoorno) {
		String hql = " from RegisterPreregister where preregisterCertificatesno = '"+patientLinkdoorno+"' and del_flg=0 and stop_flg=0  and to_char(preregisterDate,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and status=1 ";
		List<RegisterPreregister> preregisterList = super.find(hql, null);
		if(preregisterList==null||preregisterList.size()<=0){
			//如果没有预约号 就制造一个门诊号 如果有预约号 就让预约号转为门诊号
			RegisterPreregister registerPreregister = new RegisterPreregister();
			Integer value = keyvalueInInterDAO.getVal("RegisterPreregister");
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
	/**  
	 * @Description：  统计
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoStatistics queryStatistics(String empId,String midday) {
		String hql ="select ((select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_info i where i.register_expert=s.schedule_doctor and i.register_midday=s.schedule_midday and i.stop_flg=0 and i.register_status = 1 and  i.del_flg=0 and to_char(i.register_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"')+(select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_preregister p where p.preregister_expert=s.schedule_doctor and p.preregister_midday = s.schedule_midday and p.stop_flg=0 and p.del_flg=0 and to_char(p.preregister_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and p.preregister_status=1 )) as limitSum,"+
					" (select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_preregister p where p.preregister_expert=s.schedule_doctor and p.preregister_midday = s.schedule_midday and p.stop_flg=0 and p.del_flg=0 and to_char(p.preregister_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and p.preregister_status=1 ) as limitPrereInfo,"+
					" s.schedule_limit as limit , s.schedule_prelimit as limitPrere, s.schedule_netlimit as limitNet ,s.schedule_peciallimit as speciallimit"+
					" from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_schedule s where s.schedule_doctor='"+empId+"' and to_char(s.schedule_date,'yyyy-MM-dd')= '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and s.schedule_midday = '"+midday+"'  and s.stop_flg=0 and s.del_flg=0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("limitSum",Hibernate.INTEGER).addScalar("limit",Hibernate.INTEGER)
		.addScalar("limitPrere",Hibernate.INTEGER).addScalar("limitPrereInfo",Hibernate.INTEGER).addScalar("limitNet",Hibernate.INTEGER).addScalar("speciallimit",Hibernate.INTEGER);
		List<InfoStatistics> infoStatistics = queryObject.setResultTransformer(Transformers.aliasToBean(InfoStatistics.class)).list();
		if(infoStatistics!=null&&infoStatistics.size()>0){
			return infoStatistics.get(0);
		}
		return new InfoStatistics();
	}
	/**  
	 * @Description：  统计医生加号人数
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterInfo findInfoAdd(String empId) {
		String hql = "select count(1) as infoAdd from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_info i where i.register_expert='"+empId+"' and i.register_appflag=1 and i.stop_flg=0 and i.del_flg=0 ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("infoAdd",Hibernate.INTEGER);
		List<RegisterInfo> infoList = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterInfo.class)).list();
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}
	/**  
	 * @Description：  查看历史信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<RegisterInfo> findInfoHisList(String idcardNo) {
		String hql = "from RegisterInfo where del_flg=0 and stop_flg=0 and seeFlag=0 and TO_CHAR(date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and idcardId ='"+idcardNo+"' ";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new ArrayList<RegisterInfo>();
		}
		return infoList;
	}
	/**  
	 * @Description：  验证
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterInfo findInfoVo(String deptId, String empId, String gradeId,String midday,String blhcs) {
		String hql = "from RegisterInfo where del_flg=0 and stop_flg=0 and seeFlag=0 and TO_CHAR(date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and midday ='"+midday+"' and dept = '"+deptId+"' and expxrt = '"+empId+"' and midicalrecordId = '"+blhcs+"' and status = 1 ";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}
	
	/**  
	 * @Description：  查询预约列表
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public List<RegisterPreregister> findPreregisterList(String preregisterNo,String preregisterCertificatesno, String preregisterName, String preDate, String phone) throws ParseException {
		String hql = " from RegisterPreregister where status=1  and del_flg=0 and stop_flg=0 ";
		if(StringUtils.isNotBlank(preregisterNo)){
			hql = hql + " and preregisterNo = '"+preregisterNo+"'";
		}
		if(StringUtils.isNotBlank(preregisterCertificatesno)){
			hql = hql + " and preregisterCertificatesno = '"+preregisterCertificatesno+"'";
		}
		if(StringUtils.isNotBlank(preregisterName)){
			hql = hql + " and preregisterName like '%"+preregisterName+"%'";
		}
		if(StringUtils.isNotBlank(preDate)){
			hql = hql + " and TO_CHAR(preregisterDate,'YYYY-MM-DD') = '"+preDate+"'";
		}
		if(StringUtils.isBlank(preDate)){
			hql = hql + " and TO_CHAR(preregisterDate,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'";
		}
		if(StringUtils.isNotBlank(phone)){
			hql = hql + " and preregisterPhone = '"+phone+"'";
		}
		List<RegisterPreregister> preregisterList = super.find(hql, null);
		if(preregisterList==null||preregisterList.size()<=0){
			return new ArrayList<RegisterPreregister>();
		}
		return preregisterList;
	}
	
	
	/**  
	 * @Description：  查询预约回显
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public Patient queryPreregisterCertificatesno(String idcardId) {
		if(StringUtils.isBlank(idcardId)){
			return new Patient();
		}
		String hql = "select p.id as id,p.unit as unit,p.patientSex as patientSex,"
				+ "p.patientBirthday as patientBirthday from Patient p "
				+ "where cardNo =? and del_flg=0 and stop_flg=0 ";
		List<Patient> patientList = this.createQuery(hql, idcardId)
				.setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if(patientList==null||patientList.size()<=0){
			return new Patient();
		}
		return patientList.get(0);
	}
	/**  
	 * @Description：  判断是否存在就诊卡号
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public InfoPatient judgeIdcrad(String preNo) {
		String hql = " select i.idcard_no as idCardNo,i.patinent_id as patientId from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_idcard i where i.stop_flg=0 and i.del_flg=0  and i.patinent_id = (select p.patinent_id from t_patient p where p.stop_flg=0 and p.del_flg=0 and p.patient_certificatesno = '"+preNo+"' ) ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("idCardNo").addScalar("patientId");
		List<InfoPatient> patientList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoPatient.class)).list();
		if(patientList==null||patientList.size()<=0){
			return new InfoPatient();
		}
		return patientList.get(0);
	}
	/**  
	 * @Description：  退号信息列表查询
	 * @Author：ldl
	 * @CreateDate：2015-11-25
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegisterInfo> queryBackNo(String idcardId) {															
		String hql = " from RegisterInfo where seeFlag=0 and status=1 and stop_flg=0 and del_flg=0 and  to_char(date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and idcardId = '"+idcardId+"'";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new ArrayList<RegisterInfo>();
		}
		return infoList;
	}
	/**  
	 * @Description：  结算记录
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Override
	public List<RegisterDaybalance> querydaybalan() {
		String hql = " from RegisterDaybalance where stop_flg=0 and del_flg=0 ";
		List<RegisterDaybalance> daybalanceList = super.find(hql, null);
		if(daybalanceList==null||daybalanceList.size()<=0){
			return new ArrayList<RegisterDaybalance>();
		}
		return daybalanceList;
	}
	/**  
	 * @Description： 查询挂号员日结详情表
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Override
	public List<RegisterBalancedetail> querybalan(String id,String payType) {
		String hql = " from RegisterBalancedetail where balance = '"+id+"' and payType = '"+payType+"' ";
		List<RegisterBalancedetail> balancedetailList = super.find(hql, null);
		if(balancedetailList==null||balancedetailList.size()<=0){
			return new ArrayList<RegisterBalancedetail>();
		}
		return balancedetailList;
	}
	/**  
	 * @Description：修改挂号员日结详情表
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param pay 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Override
	public void updateBalan(RegisterBalancedetail modls) {
		String hql3 = "update RegisterBalancedetail set quitNum= '"+modls.getQuitNum()+"',sumNum = '"+modls.getSumNum()+"',quitFee = '"+modls.getQuitFee()+"',sumFee= '"+modls.getSumFee()+"' where id = '"+modls.getId()+"' ";
		this.getSession().createQuery(hql3).executeUpdate();
	}

	/**  
	 * @Description：  特诊挂号费
	 * @Author：ldl
	 * @CreateDate：2015-12-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public HospitalParameter speciallimitInfo(String speciallimitInfo) {
		String hql = " from HospitalParameter where parameterCode = '"+speciallimitInfo+"' ";
		List<HospitalParameter> parameter = super.find(hql, null);
		if(parameter==null||parameter.size()<=0){
			return new HospitalParameter();
		}
		return parameter.get(0);
	}
	@Override
	public RegisterInfo getInfoByClinicCode(String clinicCode) {
		String hql = "FROM RegisterInfo i WHERE i.stop_flg=0 AND i.del_flg=0 AND i.no = ? ";
		List<RegisterInfo> infoList = super.find(hql, clinicCode);
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}
	@Override
	public List<RegisterInfo> getInfo(String no) {
		String sql="select t.register_id as id ,t.register_no as no,e.employee_name as expxrtName,d.dept_name as deptName " +
					"from "+HisParameters.HISPARSCHEMAHISUSER+"t_department d right join "+HisParameters.HISPARSCHEMAHISUSER+"t_register_info t  on t.register_dept=d.dept_id left join t_employee e on t.register_expert=e.employee_id where 1=1 ";
		if(StringUtils.isNotBlank(no)){
			sql = sql+" AND t.idcard_id = (SELECT i.IDCARD_ID FROM T_PATIENT_IDCARD i WHERE i.IDCARD_NO = '"+no+"') ";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("id").addScalar("no").addScalar("expxrtName").addScalar("deptName");
		List<RegisterInfo> registerInfoList = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterInfo.class)).list();
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return new ArrayList<RegisterInfo>();
	}
	@Override
	public SysEmployee querySysEmployeeId(String id) {
		String hql = " from SysEmployee where userId ='"+id+"' and del_flg=0 and stop_flg=0 ";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new SysEmployee();
		}
		return employeeList.get(0);
	}
	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,String invoiceType) {
		int invoiceNo = 0;
		String invoiceNosq = "";
		String invoiceNoas = "";
		String invoiceNosh = "";
		String invoiceUsednoa ="";	
		Map<String,String> map=new HashMap<String,String>();
		String hql = " from FinanceInvoice where invoiceGetperson= '"+id+"' and invoiceType ='"+invoiceType+"' and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, null);
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
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}
		return map;
	}
	@Override
	public RegisterInfo queryInfoByOrder(String expxrt,Integer midday) {
		String hql = "from RegisterInfo where  del_flg=0 and stop_flg=0 and expxrt = '"+expxrt+"' and midday = "+midday+" and to_char(date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' order by order desc  ";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}
	@Override
	public Integer keyvalueDAO() {
		Integer value = keyvalueInInterDAO.getVal("RegisterPreregister");
		return value;
	}
	@Override
	public void updateFinanceInvoice(String id,String invoiceType,String invoiceNo) {
		//判断这个发票号是不是本号段最后一位
		String hql = "from FinanceInvoice  where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> financeInvoiceList = super.find(hql, null);
		if(financeInvoiceList.size()>0){
			String invoiceEndno = financeInvoiceList.get(0).getInvoiceEndno();//得到终止号
			String invoiceStartno = financeInvoiceList.get(0).getInvoiceStartno();//得到开始号
			if(invoiceEndno.equals(invoiceNo)){//当等于终止号的时候相当最后一个
				String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).executeUpdate();
			}else if(invoiceStartno.equals(invoiceNo)){//当等于开始号的时候相当于第一个
				String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}else{
				String hql4 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
				this.createQuery(hql4).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}
		}
	}
	@Override
	public void updatePreregister(String preregisterNo) {
		String hql3 = "update RegisterPreregister set status= '3' where id = '"+preregisterNo+"'";
		this.getSession().createQuery(hql3).executeUpdate();
	}
	@Override
	public PatientBlackList queryBlackList(String infoMedicalrecordId) {
		String hql = "from PatientBlackList where  del_flg=0 and stop_flg=0 and medicalrecordId = '"+infoMedicalrecordId+"' ";
		List<PatientBlackList> blackList = super.find(hql, null);
		if(blackList==null||blackList.size()<=0){
			return new PatientBlackList();
		}
		return blackList.get(0);
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
	public HospitalParameter invocePemen() {
		String hql = "from HospitalParameter where 1=1 and parameterCode = 'infoExenp' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}
	@Override
	public List<RegisterSchedule> scheduleCombobox(String dept,String midday) {
		String hql = "from RegisterSchedule where 1=1 and  del_flg=0 and stop_flg=0 ";
		if(StringUtils.isNotBlank(dept)){
			hql = hql + "  and scheduleWorkdept = '"+dept+"'";
		}
		if(StringUtils.isNotBlank(midday)){
			hql = hql + "  and  midday  = '"+midday+"'";
		}
		List<RegisterSchedule> scheduleList = super.find(hql, null);
		if(scheduleList==null||scheduleList.size()<=0){
			return new ArrayList<RegisterSchedule>();
		}
		return scheduleList;
	}
	@Override
	public OutpatientAccount getAccountByMedicalrecord(String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId ='"+midicalrecordId+"' and del_flg=0 and stop_flg=0 ";
		List<OutpatientAccount> accountList = super.find(hql, null);
		if(accountList==null||accountList.size()<=0){
			return new OutpatientAccount();
		}
		return accountList.get(0);
	}
	@Override
	public List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId) {
		String hql = " from OutpatientAccountrecord where medicalrecordId ='"+midicalrecordId+"' and del_flg=0 and stop_flg=0 and TO_CHAR(operDate,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and opertype in ('4','5')";
		List<OutpatientAccountrecord> accountrecordList = super.find(hql, null);
		if(accountrecordList==null||accountrecordList.size()<=0){
			return new ArrayList<OutpatientAccountrecord>();
		}
		return accountrecordList;
	}
	@Override
	public OutpatientAccount veriPassWord(String md5Hex, String blhcs) {
		String hql = " from OutpatientAccount where medicalrecordId = '"+blhcs+"' and accountPassword='"+md5Hex+"'  and  del_flg=0 and stop_flg=0  ";
		List<OutpatientAccount> patientAccountList = super.find(hql, null);
		if(patientAccountList==null||patientAccountList.size()<=0){
			return new OutpatientAccount();
		}
		return patientAccountList.get(0);
	}
	
}

