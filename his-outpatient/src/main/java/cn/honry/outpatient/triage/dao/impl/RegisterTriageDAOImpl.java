package cn.honry.outpatient.triage.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.Clinic;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.triage.dao.RegisterTriageDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Repository("registerTriageDAO")
@SuppressWarnings({ "all" })
public class RegisterTriageDAOImpl extends HibernateEntityDao<RegistrationNow> implements RegisterTriageDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	/**  根据门诊科室code查询下属诊室关系
	* @Title: queryClinicList  
	* @Description:  根据门诊科室code查询下属诊室关系 
	* @param deptCode门诊科室code
	* @author dtl 
	* @date 2016年10月20日
	*/
	@Override
	public List<Clinic> queryClinicList (String deptCode) {
		
		String hql = " from Clinic where stop_flg = 0 and del_flg = 0 and clinicDeptId = '" + deptCode + "'"; 
		List<Clinic> clinics = super.find(hql, null);
		if(clinics != null && clinics.size() > 0){
			return clinics;
		}
		return new ArrayList<Clinic>();
	}
	
	/**  得到分诊区下面管理的科室
	 * @Description： 
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<DepartmentContact> queryContactList(String parDeptId) {
		String hql = " from DepartmentContact where pardeptId = '" + parDeptId + "' and referenceType = '02' and stop_flg = 0 and del_flg = 0 and validState = 1 and deptType = 'C' "; 
		List<DepartmentContact> contactList = super.find(hql, null);
		if(contactList!=null&&contactList.size()>0){
			return contactList;
		}
		return new ArrayList<DepartmentContact>();
	}
	
	/**  
	 * @Description：  科室ID之间的转化
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public DepartmentContact queryContact(String id) {
		String  hql = " from DepartmentContact where deptCode = '"+id+"' and referenceType = '02' and stop_flg = 0 and del_flg = 0 and validState = 1 and deptType = 'N' ";
		List<DepartmentContact> contactList = super.find(hql, null);
		if(contactList!=null&&contactList.size()>0){
			return contactList.get(0);
		}
		return new DepartmentContact();
	}
	@Override
	public SysEmployee queryEmployee(String jobNo) {
		String hql = " from SysEmployee where jobNo= '"+jobNo+"'";
		List<SysEmployee> employees = super.find(hql, null);
		if(employees != null && employees.size() > 0){
			return employees.get(0);
		}
		return new SysEmployee();
	}
	
	/**  
	 * @Description：  根据科室查询科室下的医生
	 * @Author：liudelin
	 * @CreateDate：2015-11-30下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegisterScheduleNow> queryRegisterSchedule(String deptId) {
		String hql = " from RegisterScheduleNow where department= '"+deptId+"' and isStop = 2 and stop_flg = 0 and del_flg = 0 and to_char(date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'";
		List<RegisterScheduleNow> scheduleList = super.find(hql, null);
		if(scheduleList!=null&&scheduleList.size()>0){
			return scheduleList;
		}
		return new ArrayList<RegisterScheduleNow>();
	}
	
	
	/**  
	 * @Description：  分页患者列表
	 * @Author：liudelin
	 * @CreateDate：2015-11-27 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotal(RegistrationNow registration) {
		String hql = joint(registration);
		return super.getTotal(hql);
	}
	/**  
	 * @Description：  分页患者列表（记录）
	 * @Author：liudelin
	 * @CreateDate：2015-11-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegistrationNow> getPage(RegistrationNow registration, String page,String rows) {
		String hql = joint(registration);
		return super.getPage(hql, page, rows);
	}
	
	public String joint(RegistrationNow entity){
		String hql = " from Registration where stop_flg = 0 and del_flg = 0 and ynregchrg = 1 ynsee=0 and inState not in (2,3) and to_char(regDate,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'";
		if(StringUtils.isNotBlank(entity.getDeptCode())){
			hql = hql +" and deptCode = '"+entity.getDeptCode()+"'";
		}
		if(StringUtils.isNotBlank(entity.getDoctCode())){
			hql = hql +" and doctCode = '"+entity.getDoctCode()+"'";
		}
		return hql;
	}
	
	 /** 得到系统参数患者是否先登记后分诊
	 * @Description: 得到系统参数患者是否先登记后分诊
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月18日
	 * @Version: V 1.0
	 */
	@Override
	public Integer getmzfzdj() {
		HospitalParameter parameter = super.findUniqueBy(HospitalParameter.class, "parameterCode", "MZFZDJ");
		if(parameter != null){
			return Integer.parseInt(parameter.getParameterValue());
		}
		return null;
	}
	/**门诊分诊分页
	 * @Description: 门诊分诊分页
	 * @param registration 挂号实体
	 * @param page
	 * @param rows
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月25日
	 * @Version: V 1.0
	 */
	@Override
	public List<RegistrationNow> getPage(RegistrationNow registration, String page,String rows,String flag) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id as id,t.NOON_CODE as noonCode,t.medicalrecordid as midicalrecordId,t.DOCT_CODE as doctCode,t.DEPT_CODE as deptCode,t.reg_triagetype as triageType,");
		sql.append(" t.CLINIC_CODE as clinicCode,t.PATIENT_NAME as patientName,t.ORDER_NO as orderNo,t.EGISTER_SEEOPTIMIZE as seeOptimize,");
		sql.append(" t.DOCT_NAME as doctName,t.REGLEVL_NAME as gradeName,t.DEPT_NAME as deptName,t.CARD_NO as cardId,t.DEPT_NAME as clinicName ");
		sql.append(" from T_REGISTER_MAIN_NOW t where t.IN_STATE = 0 and t.ynregchrg = 1 and t.ynsee = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		sql.append(" and t.REG_DATE >= to_date(:begin,'yyyy/mm/dd') and t.REG_DATE < to_date(:end,'yyyy/mm/dd')");
		SQLQuery query = joint(registration, flag, sql);
		int start = Integer.parseInt(page == null? "1" : page);
		int count = Integer.parseInt(rows == null? "20" : rows);
		SQLQuery queryObject = query.addScalar("id").addScalar("noonCode",Hibernate.INTEGER).addScalar("midicalrecordId").addScalar("triageType")
				.addScalar("clinicCode").addScalar("clinicName").addScalar("patientName").addScalar("orderNo",Hibernate.INTEGER).addScalar("seeOptimize",Hibernate.INTEGER)
				.addScalar("doctName").addScalar("gradeName").addScalar("deptName").addScalar("cardId").addScalar("doctCode").addScalar("deptCode");
		List<RegistrationNow> list = queryObject.setResultTransformer(Transformers.aliasToBean(RegistrationNow.class)).setFirstResult((start - 1) * count).setMaxResults(count).list();
		if(list != null && list.size() > 0){
			for(RegistrationNow info : list){
				if(info.getSeeOptimize() != null && info.getSeeOptimize() == 0){
					
				}else{
					info.setSeeOptimize(1);
				}
			}
			return list;
		}
		return new ArrayList<RegistrationNow>();
	}
	/**门诊分诊分页
	 * @Description: 门诊分诊分页
	 * @param registration 挂号实体
	 * @param flag 1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者
	 * @return 
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月25日
	 * @Version: V 1.0
	 */
	@Override
	public int getTotal(RegistrationNow registration, String flag) {
		StringBuilder sql = new StringBuilder();
		sql.append("select  count(1) from T_REGISTER_MAIN_NOW t where t.IN_STATE = 0 and t.ynregchrg = 1 and t.ynsee = 0 ");
		sql.append("and t.REG_DATE >= to_date(:begin,'yyyy/mm/dd hh24:mi:ss') and t.REG_DATE < to_date(:end,'yyyy/mm/dd hh24:mi:ss')");
		SQLQuery query = joint(registration, flag, sql);
		Object count = query.uniqueResult();
		if("0".equals(count.toString())){
			return 0;
		}
		return Integer.valueOf(count.toString());
	}
	/**为门诊分诊查询添加条件
	 * @Description: 为门诊分诊查询添加条件
	 * @param registration 挂号实体
	 * @param flag 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，显示当天所有挂号信息；1-需要登记才可分诊，列表默认显示已分诊挂号； 2登记查询时使用，查询未分诊的患者 
	 * @param sql
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月26日
	 * @Version: V 1.0
	 */
	public SQLQuery joint(RegistrationNow registration, String flag, StringBuilder sql){
		String loginDeptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();
		if(StringUtils.isNotBlank(registration.getDeptCode())){
			sql.append(" and t.DEPT_CODE = :deptCode");
		}else{
			sql.append(" and t.DEPT_CODE in (select tt.dept_code from t_department_contact tt where");
			sql.append(" tt.pardept_id = '02'|| :loginDeptId ||'ID' and tt.reference_type = '02'");
			sql.append(" and tt.stop_flg = 0 and tt.del_flg = 0)");
		}
		if(StringUtils.isNotBlank(registration.getDoctCode())){
			sql.append(" and t.DOCT_CODE = :doctCode");
		}
		if(registration.getNoonCode() != null){
			sql.append(" and t.NOON_CODE = :noonCode");
		}
		if(StringUtils.isNotBlank(registration.getQueryNo())){
			sql.append(" and (t.CLINIC_CODE = :queryNo or t.medicalrecordid = :queryNo ) ");
		}
		if("1".equals(flag)){
			sql.append(" and t.TRIAGE_FLAG = 1");
		}
		if("2".equals(flag)){
			sql.append(" and t.TRIAGE_FLAG = 0");
		}
		sql.append(" order by t.DEPT_CODE,t.DOCT_CODE,t.EGISTER_SEEOPTIMIZE");
		String sDate = DateUtils.formatDateY_M_SLASH(new Date());
		String eDate = DateUtils.formatDateY_M_SLASH(DateUtils.addDay(new Date(), 1));
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("begin", sDate);
		query.setParameter("end", eDate);
		if(StringUtils.isNotBlank(registration.getDeptCode())){
			query.setParameter("deptCode", registration.getDeptCode());
		}else{
			query.setParameter("loginDeptId", loginDeptId);
		}
		
		if(StringUtils.isNotBlank(registration.getDoctCode())){
			query.setParameter("doctCode", registration.getDoctCode());
		}
		if(registration.getNoonCode() != null ){
			query.setParameter("noonCode", registration.getNoonCode());
		}
		if(StringUtils.isNotBlank(registration.getQueryNo())){
			query.setParameter("queryNo", registration.getQueryNo());
		}
		return query;
	}

	/** 通过门诊号查询患者挂号信息
	* @Title: queryTriagePatientByClinicCode 
	* @Description: 通过门诊号查询患者挂号信息
	* * @param queryNo
	* @author dtl 
	* @date 2016年11月3日
	*/
	@Override
	public RegistrationNow queryTriagePatientByClinicCode(String queryNo) {
		String hql = "FROM RegistrationNow r WHERE r.transType = 1 and r.inState= 0 and r.del_flg = 0 and r.stop_flg = 0 and ynsee = 0 and r.clinicCode = '" + queryNo + "'";
		List<RegistrationNow> list = super.find(hql);
		return list.get(0);
	}

	@Override
	public void updateTriage(RegistrationNow registration) {
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String hql = "update RegistrationNow set triageFlag = ?,triageOpcd = ?,"
				+ "triageDate = ?,updateTime = ?,updateUser = ?,seeOptimize = ?,"
				+ "triageType = ? where id = ?";
		super.excUpdateHql(hql, registration.getTriageFlag(),registration.getTriageOpcd(),
				registration.getTriageDate(),new Date(),userAccount,registration.getSeeOptimize(),
				registration.getTriageType(),registration.getId());
		
	}
}
