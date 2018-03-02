package cn.honry.outpatient.preregister.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.outpatient.preregister.dao.PreregisterDao;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.preregister.vo.ScheduleInfoVo;
import cn.honry.outpatient.preregister.vo.ScheduleModelVo;
import cn.honry.outpatient.schedule.dao.ScheduleDAO;
import cn.honry.outpatient.scheduleModel.dao.ScheduleModelDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("reregisterDAO")
@SuppressWarnings({ "all" })

public class PreregisterDaoImpl extends HibernateEntityDao<RegisterPreregisterNow> implements PreregisterDao{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
    
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	@Autowired
	@Qualifier(value = "scheduleModelDAO")
	private ScheduleModelDAO scheduleModelDAO;
	@Autowired
	@Qualifier(value = "scheduleDAO")
	private ScheduleDAO scheduleDAO;
	
	@Override
	public List<RegisterPreregisterNow> getPage(
			RegisterPreregisterNow entity, String page, String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(RegisterPreregisterNow entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	public String joint(RegisterPreregisterNow entity){
		String hql="FROM RegisterPreregisterNow r WHERE  r.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getPreregisterExpert())){
				hql = hql+" AND r.preregisterExpert LIKE '%"+entity.getPreregisterExpert()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getPreregisterDept())){
				hql = hql+" AND r.preregisterDept LIKE '%"+entity.getPreregisterDept()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getPreregisterGrade())){
				hql = hql+" AND r.preregisterGrade LIKE '%"+entity.getPreregisterGrade()+"%'";
			}
			if(entity.getStatus()!=null){
				if(entity.getStatus()==3){
					hql = hql+" AND r.status = "+entity.getStatus() ;
				}
				if(entity.getStatus()==1){
					hql = hql+"  AND r.status !=3 ";
				}
			}
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getPreregisterNo())){
				String preregisterNo=entity.getPreregisterNo();
					hql = hql+"  AND (r.preregisterNo LIKE '%"+preregisterNo+"%'"
					 + " OR r.medicalrecordId LIKE '%"+preregisterNo+"%'" 
					 + " OR r.preregisterCertificatesno LIKE '%"+preregisterNo+"%'" 
					+ " OR r.preregisterName LIKE '%"+preregisterNo+"%'" 
					+ " OR r.idcardId LIKE '%"+preregisterNo+"%')";
				}
		}
		hql=hql + " order by preregisterDate desc ";
		return hql;
	}
	
	

	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> deptCombobox(String q) {
		String hql = "from SysDepartment where deptIsforregister = '1' and del_flg=0 and stop_flg=0";
		if(StringUtils.isNotBlank(q)){
			hql += "and (upper(deptPinyin) like '%"+q.toUpperCase()+"%' or upper(deptWb) like '%"+q.toUpperCase()+"%' or upper(deptInputcode) like '%"+q.toUpperCase()+"%')";
		}
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}
	/**  
	 * @Description：  挂号科室（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> getDeptCom(String time,String q) {
		String hql=null;
		 hql = "from SysDepartment d where d.deptIsforregister = '1' and d.del_flg=0 and d.stop_flg=0 ";
		 if(StringUtils.isNotBlank(q)){
				hql += "and (deptName like '%"+q+"%' or deptPinyin like '%"+q.toUpperCase()+"%' or deptWb like '%"+q.toUpperCase()+"%' or deptInputcode like '%"+q.toUpperCase()+"%')";
			}
		 List<SysDepartment> departmentList=new ArrayList<SysDepartment>();
		 departmentList = super.find(hql, null);
		if(departmentList.size()==0){
			 hql="FROM SysDepartment d WHERE d.deptIsforregister = '1' AND d.del_flg=0 and d.stop_flg=0 ";
			 departmentList = super.find(hql, null);
		}
		return departmentList;
	}
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<EmpScheduleVo> getEmpCom(String page, String rows,String time, String gradeid,String deptid,String name) {
 		List<EmpScheduleVo> list = getRegDocInfo(time, gradeid, deptid, name);
 		return list;
	}
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public int getTotalemp(String time, String gradeid,String deptid ,String name ) {
		List<EmpScheduleVo> list = getRegDocInfo(time, gradeid, deptid, name);
		return list.size();
	}
	
	private String jointemp(String time,String gradeid,String deptid,String name){
		if(StringUtils.isBlank(time)){
			return null;
		}else{
			String sql ="select "+
			  "s.schedule_id as scheduleId, "+
			  "s.SCHEDULE_WORKDEPT as deptId, "+
			  "dep.dept_name as deptName, "+
			  "e.employee_jobno as id, "+
			  "e.employee_name as doctId, "+
			  "s.schedule_reggrade as grade, "+
			  "s.schedule_date as sdate, "+
			  "s.schedule_midday as midd, "+
			  "NVL(s.schedule_prelimit,0) as prelimit, "+
			  "NVL(s.schedule_phonelimit,0) as phonelimit,  "+
			  "NVL(s.schedule_netlimit,0) as netlimit, " +
			  "s.schedule_starttime  as starttime," +
			  "s.schedule_endtime as enttime" +
			" from "+HisParameters.HISPARSCHEMAHISUSER+"t_register_schedule_now s, " + 
			HisParameters.HISPARSCHEMAHISUSER+"t_employee e," +HisParameters.HISPARSCHEMAHISUSER+"t_department dep," +
			HisParameters.HISPARSCHEMAHISUSER+"t_business_dictionary d," +HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_GRADE g" +
			" where e.employee_jobno=s.schedule_doctor and dep.dept_code=s.SCHEDULE_WORKDEPT "
			+ "and e.employee_title=d.code_encode and d.code_type='title' and g.grade_code = d.code_mark "
			+ "and g.grade_title=d.code_encode "
			+ "and trunc(S.SCHEDULE_DATE,'dd')=to_date(:time,'yyyy-MM-dd') and s.del_flg=0 and s.stop_flg=0 and s.schedule_isstop=2 ";//zpty20160525屏蔽停诊的医生
			if(StringUtils.isNotBlank(gradeid)){
				sql += "and s.SCHEDULE_REGGRADE = :gradeid ";
			}
			if(StringUtils.isNotBlank(deptid)){
				sql += "and s.SCHEDULE_WORKDEPT = :deptid ";
			}
			if(StringUtils.isNotBlank(name)){
				sql = sql+" AND (e.EMPLOYEE_NAME LIKE '%"+name.toUpperCase()+"%'"
						 + " OR e.EMPLOYEE_OLDNAME LIKE '%"+name.toUpperCase()+"%'" 
						 + " OR e.EMPLOYEE_PINYIN LIKE '%"+name.toUpperCase()+"%'" 
						+ " OR e.EMPLOYEE_WB LIKE '%"+name.toUpperCase()+"%'" 
						+ " OR e.EMPLOYEE_INPUTCODE LIKE '%"+name.toUpperCase()+"%')";
			}
			return sql;	
		}
	}
	private  String jointempTotle(String time,String gradeid,String deptid,String name){
		if(StringUtils.isBlank(time)){
			return null;
		}else{
			String sql="select " +
			        "sm.model_id as scheduleId, "+
					"e.employee_jobno as id," +
					"sm.model_deptid as deptId," +
					"dep.dept_name as deptName, "+
					"e.employee_name as doctId," +
					"sm.model_reggrade as grade," +
					"sm.model_midday as midd," +
					"sm.model_prelimit as prelimit," +
					"sm.model_phonelimit as phonelimit," +
					"sm.model_netlimit as netlimit," +
					"sm.model_starttime as starttime," +
					"sm.model_endtime as enttime" +
					" from "+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_SCHEDULEMODEL sm," +
					HisParameters.HISPARSCHEMAHISUSER+"t_employee e,"+HisParameters.HISPARSCHEMAHISUSER+"t_department dep," +
					HisParameters.HISPARSCHEMAHISUSER+"t_business_dictionary d,"+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_GRADE g" +
					" where e.employee_jobno=sm.model_doctor and dep.dept_id=sm.model_deptid "
					+ "and e.employee_title=d.code_encode and d.code_type='title' "
					+ "and g.grade_code = d.code_mark and g.grade_title=d.code_encode "
					+ "and sm.model_week = :time and sm.del_flg=0 and sm.stop_flg=0 ";
			if(StringUtils.isNotBlank(gradeid)){
				sql += "and sm.model_reggrade = :gradeid ";
			}
			if(StringUtils.isNotBlank(deptid)){
				sql += "and sm.model_deptid = :deptid ";
			}
			if(StringUtils.isNotBlank(name)){
				sql = sql+" AND (e.EMPLOYEE_NAME LIKE '%"+name.toUpperCase()+"%'"
						 + " OR e.EMPLOYEE_OLDNAME LIKE '%"+name.toUpperCase()+"%'" 
						 + " OR e.EMPLOYEE_PINYIN LIKE '%"+name.toUpperCase()+"%'" 
						+ " OR e.EMPLOYEE_WB LIKE '%"+name.toUpperCase()+"%'" 
						+ " OR e.EMPLOYEE_INPUTCODE LIKE '%"+name.toUpperCase()+"%')";
			}
			return sql;
		}
		
	}
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysEmployee> getPageSys(String page, String rows,String name,String deptId,String grade) {
		StringBuffer sb = new StringBuffer();
		sb.append("select e.employee_jobno as jobNo,e.employee_name as name,r.grade_name as title,e.dept_id as departmentId "
				+ "from t_register_grade r left join t_Employee e on e.employee_title=r.grade_title "
				+ "where e.del_flg=0 and e.stop_flg=0 and e.employee_type=1");
		if(StringUtils.isNotBlank(deptId)){
			sb.append(" and e.dept_id ='"+deptId+"'");
		}
		if(StringUtils.isNotBlank(grade)){
			sb.append(" and r.grade_title ='"+grade+"'");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append("  AND (e.employee_name LIKE '%"+name.toUpperCase()+"%'"
					+ " OR e.employee_oldName LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_pinyin LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_wb LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_jobno LIKE '%"+name+"%'" 
					+ " OR e.employee_inputCode LIKE '%"+name.toUpperCase()+"%')");
		}
		int p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):10;
		String hql = "select * from (select tab.*,rownum rn from ("+sb.toString()+" ) tab where rownum<="+p*r+") where rn> "+(p-1)*r;
		
		SQLQuery query = this.getSession().createSQLQuery(hql);
		query.addScalar("jobNo").addScalar("name").addScalar("title").addScalar("departmentId");
		List<SysEmployee> list = query.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		return list;
	}
	public int getTotalSys(String name,String deptId,String grade){
		StringBuffer sb = new StringBuffer();
		sb.append("select e.employee_jobno as jobNo,e.employee_name as name,r.grade_name as title,e.dept_id as departmentId "
				+ "from t_register_grade r left join t_Employee e on e.employee_title=r.grade_title "
				+ "where e.del_flg=0 and e.stop_flg=0 and e.employee_type='1'");
		if(StringUtils.isNotBlank(deptId)){
			sb.append(" and e.dept_id ='"+deptId+"'");
		}
		if(StringUtils.isNotBlank(grade)){
			sb.append(" and r.grade_title ='"+grade+"'");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append("  AND (e.employee_name LIKE '%"+name.toUpperCase()+"%'"
					+ " OR e.employee_oldName LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_pinyin LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_wb LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.employee_inputCode LIKE '%"+name.toUpperCase()+"%')");
		}
		return super.getSqlTotal(sb.toString());
	}
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public int getTotalSys(String name) {
		String hql =" from SysEmployee e where  e.del_flg=0 and e.stop_flg=0 and e.type=1 ";
		if(StringUtils.isNotBlank(name)){
			hql = hql+" AND (e.name LIKE '%"+name.toUpperCase()+"%'"
					 + " OR e.oldName LIKE '%"+name.toUpperCase()+"%'" 
					 + " OR e.pinyin LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.wb LIKE '%"+name.toUpperCase()+"%'" 
					+ " OR e.inputCode LIKE '%"+name.toUpperCase()+"%')";
		}
		return super.getTotal(hql);
	}
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 5:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  number身份证号
	 * @param  date预约时间
	 */

	@Override
	public String queryPreregisterByMid(String number, String dept, String date) {
		String hql="from RegisterPreregisterNow r WHERE TO_CHAR(r.preregisterDate, 'YYYY-MM-DD') = '"+date+"' and r.preregisterCertificatesno='"+number+"'"
				+ "and r.del_flg=0 and r.stop_flg=0 ";
			List<RegisterPreregister> reList = super.find(hql, null);
			if(reList.size()>=2){
				return "1";//每天预约不能超过两次
			}else{
				//这里应该是同一个人,同一天,同一个科室只能预约一次,少一个同一个人的条件,这样就造成了,无论什么人,这一天这个科室只能预约一次了,所以加入同一个人的条件zpty20160524
				String ehql="from RegisterPreregisterNow r WHERE TO_CHAR(r.preregisterDate, 'YYYY-MM-DD') = '"+date+"' and r.preregisterDept='"+dept+"' and r.preregisterCertificatesno='"+number+"'"
						+ "and r.del_flg=0 and r.stop_flg=0 ";
				List<RegisterPreregisterNow> regList = super.find(ehql, null);
					if(regList.size()>0){
						return "2";//同一科室只能预约一次
						
					}else{
						return "0";
					}
			}
	}
	
	
	/**  
	 *  
	 * @Description：  根据就诊卡ID查询患者是否在患者黑名单中
	 * @Author：zpty
	 * @CreateDate：2016-5-26  
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 * @version 1.0
	 *
	 */
	@Override
	public int getPatientCount(String idcardNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.BLACKLIST_ID as id from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_BLACKLIST b where b.PATINENT_ID in (select p.PATINENT_ID from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p LEFT JOIN T_PATIENT_IDCARD i ON p.PATINENT_ID = i.PATINENT_ID WHERE p.DEL_FLG=0 and p.stop_flg=0 and i.del_flg=0 and i.stop_flg=0 AND i.IDCARD_NO = '"+idcardNo+"') and b.DEL_FLG=0 and b.stop_flg=0");
		return super.getSqlTotal(sql.toString());
	}
	
	/**
	 * 根据预约时间、挂号级别、科室编码、专家名称等查询医生的已挂号信息和剩余挂号数量
	 * @param time 预约时间
	 * @param gradeid 挂号级别编码
	 * @param deptid 挂号科室编码
	 * @param name 专家名称(模糊查询)
	 * @return
	 */
	private List<EmpScheduleVo> getRegDocInfo(String time,String gradeid,String deptid,String name){
		List<EmpScheduleVo> empList = new ArrayList<>();
		if(StringUtils.isBlank(time)){
			return empList;
		}
		StringBuffer sbf= new StringBuffer("select s.schedule_id as scheduleId,s.SCHEDULE_WORKDEPT as deptId,")
		.append(" d.dept_name as deptName,s.schedule_doctor as id,s.schedule_doctorname as empName,")
		.append(" s.schedule_reggrade as gradeName,to_char(s.schedule_date,'yyyy-mm-dd') as preDate,")
		.append(" s.schedule_midday as midday,nvl(s.schedule_prelimit, 0) as preL,")
		.append(" nvl(s.schedule_phonelimit, 0) as phoneL,nvl(s.schedule_netlimit, 0) as netL,")
		.append(" s.schedule_starttime as starttime,s.schedule_endtime as enttime,")
		.append(" t.preregister_id as preId,")
		.append(" nvl(t.preregister_isnet, 0) as isNet,nvl(t.preregister_isphone, 0) as isPhone ")
		.append(" from t_register_schedule_now s")
		.append(" left join t_register_preregister_now t on s.schedule_doctor = t.preregister_expert ")
		.append(" and s.schedule_midday = t.preregister_midday ")
		.append(" and trunc(s.schedule_date, 'dd') = t.preregister_date ")
		.append(" and t.del_flg=0 and t.stop_flg=0 ")
		.append(" left join t_employee e on e.employee_jobno=s.schedule_doctor and e.stop_flg=0 and e.del_flg=0")
		.append(" left join t_department d on d.dept_code = s.schedule_workdept and d.stop_flg = 0 and d.del_flg = 0")
		.append(" where trunc(s.schedule_date, 'dd') = to_date(:time, 'yyyy-mm-dd') ")
		.append(" and s.schedule_prelimit>0 and s.del_flg=0 and s.stop_flg=0   and s.schedule_isstop = '2'");
		if(StringUtils.isNotBlank(gradeid)){
			sbf.append(" and s.schedule_reggrade = :grade ");
		}
		if(StringUtils.isNotBlank(deptid)){
			sbf.append(" and s.schedule_workdept=:dept");
		}
		if(StringUtils.isNotBlank(name)){
			sbf.append(" and (e.employee_name like '%"+name+"%'")
			.append(" or e.employee_pinyin like '%"+name.toUpperCase()+"%'")
			.append(" or e.employee_wb like '%"+name.toUpperCase()+"%'")
			.append(" or e.employee_jobno like '%"+name+"%'")
			.append(" or e.employee_inputcode like '%"+name.toUpperCase()+"%')");
		}
		SQLQuery query = this.getSession().createSQLQuery(sbf.toString())
				.addScalar("scheduleId").addScalar("deptId").addScalar("deptName").addScalar("id")
				.addScalar("empName").addScalar("gradeName").addScalar("preDate").addScalar("preId")
				.addScalar("midday" ,Hibernate.INTEGER).addScalar("preL" ,Hibernate.INTEGER)
				.addScalar("phoneL" ,Hibernate.INTEGER).addScalar("netL" ,Hibernate.INTEGER)
				.addScalar("isPhone",Hibernate.INTEGER).addScalar("isNet",Hibernate.INTEGER)
				.addScalar("starttime").addScalar("enttime");
		query.setParameter("time", time);
		if(StringUtils.isNotBlank(gradeid)){
			query.setParameter("grade", gradeid);
		}
		if(StringUtils.isNotBlank(deptid)){
			query.setParameter("dept", deptid);
		}
		//查询预约的医生信息(名称、科室、挂号级别等)和患者信息(预约挂号方式:网络预约或电话预约或现场预约等)
		List<EmpScheduleVo> list = 
				query.setResultTransformer(Transformers.aliasToBean(EmpScheduleVo.class)).list();
		Map<String,EmpScheduleVo> map= new HashMap<>();//用于统计同一个医生的患者个数(每个list的元素都代表了一个患者)
		if(list!=null&& list.size()>0){
			for (EmpScheduleVo vo : list) {
				Integer isNet = vo.getIsNet();//是否为网络预约(0-否,1-是)
				Integer isPhone = vo.getIsPhone();//是否为电话预约(0-否,1-是)
				//日期+科室+午别+医生工作号确定一条医生信息
				String key=vo.getPreDate()+"--"+vo.getDeptId()+"--"+vo.getMidday()+"--"+vo.getId();
				 EmpScheduleVo empVo = map.get(key);
				 if(empVo!=null){
					 if(isNet==1){//如果是网络预约,已预约网络数+1
						 empVo.setNetA(empVo.getNetA()+1);
					 }else if(isPhone==1){//如果是电话预约,已预约电话数+1
						 empVo.setPhoneA(empVo.getPhoneA()+1);
					 }else{//既不是网络预约,也不是电话预约则为现场预约,现场预约数+1
						 empVo.setNowA(empVo.getNowA()+1);
					 }
					 empVo.setPreA(empVo.getPreA()+1);
				 }else{
					 vo.setNowL(vo.getPreL());
					 if(StringUtils.isNotBlank(vo.getPreId())){
						 if(isNet==1){
							 vo.setNetA(1);
						 }else if(isPhone==1){
							 vo.setPhoneA(1);
						 }else{
							 vo.setNowA(1);
						 }
						 vo.setPreA(1);
					 }
					 map.put(key, vo);
				 }
			}
			empList.addAll(map.values());
		}
		return empList;
	}
}
