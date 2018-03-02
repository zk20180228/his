package cn.honry.outpatient.changeDeptLog.dao.impl;

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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.changeDeptLog.dao.RegisterChangeDeptLogDAO;
import cn.honry.outpatient.info.dao.RegisterInfoDAO;
import cn.honry.outpatient.info.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("registerChangeDeptLogDAO")
@SuppressWarnings({ "all" })
public class RegisterChangeDeptLogDAOImpl extends HibernateEntityDao<RegisterChangeDeptLog> implements RegisterChangeDeptLogDAO  {

	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private RegisterInfoDAO registerInfoDAO;
	
	/**  
	 *  
	 * @Description：  卡号查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo findInfo(String idcardNo) {
		String hql = "from RegisterInfo ri "
				+ "where TO_CHAR(ri.date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' "
				+ "and idcardId= '"+idcardNo+"' and ri.DEL_FLG=0 and ri.STOP_FLG=0";
		List<RegisterInfo> changeDeptLogList = super.find(hql, null);
		if(changeDeptLogList==null||changeDeptLogList.size()<=0){
			return new RegisterInfo();
		}
		return changeDeptLogList.get(0);
	}
	/**  
	 *  
	 * @Description：  根据医生Id查询到医生姓名
	 * @param:expxrtId(医生的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysEmployee findSysEmployee(String expxrtId) {
		String hql = "from SysEmployee where id = '"+expxrtId+"'and DEL_FLG=0 and STOP_FLG=0 ";
		List<SysEmployee> sysEmployeeList = super.find(hql, null);
		if(sysEmployeeList==null||sysEmployeeList.size()<=0){
			return new SysEmployee();
		}
		return sysEmployeeList.get(0);
	}
	/**  
	 *  
	 * @Description：  根据科室Id查询到科室名
	 * @param:expxrtId(科室的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysDepartment findSysDepartment(String deptId) {
		String hql = "from SysDepartment where id = '"+deptId+"' and DEL_FLG=0 and STOP_FLG=0 ";
		List<SysDepartment> sysDepartmentList = super.find(hql, null);
		if(sysDepartmentList==null||sysDepartmentList.size()<=0){
			return new SysDepartment();
		}
		return sysDepartmentList.get(0);
	}
	/**  
	 *  
	 * @Description：  根据级别Id查询到级别名
	 * @param:gradeId(级别的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterGrade findfindRegisterGrade(String gradeId) {
		String hql = "from RegisterGrade where id = '"+gradeId+"' and DEL_FLG=0 and STOP_FLG=0 ";
		List<RegisterGrade> registerGradeList = super.find(hql, null);
		if(registerGradeList==null||registerGradeList.size()<=0){
			return new RegisterGrade();
		}
		return registerGradeList.get(0);
	}
	/**  
	 *  
	 * @Description：  根据合同单位Id查询到合同单位名
	 * @param:contractunitId(合同单位的ID)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public BusinessContractunit findBusinessContractunit(String contractunitId) {
		String hql = "from BusinessContractunit where id = '"+contractunitId+"' and DEL_FLG=0 and STOP_FLG=0 ";
		List<BusinessContractunit> registerBusinessContractunitList = super.find(hql, null);
		if(registerBusinessContractunitList==null||registerBusinessContractunitList.size()<=0){
			return new BusinessContractunit();
		}
		return registerBusinessContractunitList.get(0);
	}
	/**  
	 *  
	 * @Description：  根据挂号Id查询到医生和科室
	 * @param:id（挂号表id）
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo findInfoId(String id) {
		String hql = "from RegisterInfo ri where TO_CHAR(ri.date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and ri.DEL_FLG=0 and ri.STOP_FLG=0 and id= '"+id+"' ";
		List<RegisterInfo> changeDeptLogList = super.find(hql, null);
		if(changeDeptLogList==null||changeDeptLogList.size()<=0){
			return new RegisterInfo();
		}
		return changeDeptLogList.get(0);
	}
	

	/**  
	 *  
	 * @Description：  下拉框科室
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getCombobox() {
		String hql="FROM SysDepartment d WHERE d.del_flg = 0 AND d.deptIsforregister=1 ";
		List<SysDepartment> changeDeptLogList = super.find(hql, null);
		if(changeDeptLogList!=null && changeDeptLogList.size()>0){
			return changeDeptLogList;
		}
		return new ArrayList<SysDepartment>();
	}
	/**  
	 *  
	 * @Description：  下拉框医生
	 * @param:departmentId(科室Id)，grade（级别id）
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InfoVo> EgetCombobox(String departmentId, String grade) {
		 String hql = "select ((select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+" t_register_info t3 where t3.register_midday=t.schedule_midday and t3.register_expert=t.schedule_doctor and to_char(t3.register_date,'yyyy-MM-dd') ='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and  t3.stop_flg=0 and t3.del_flg=0 )+(select count(1) from "+HisParameters.HISPARSCHEMAHISUSER+" t_register_preregister t4 where t4.preregister_midday=t.schedule_midday and t4.preregister_expert=t.schedule_doctor  and t4.preregister_status=1 and to_char(t4.preregister_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t4.stop_flg=0 and t4.del_flg=0)) as infoAlready,"+
		   " t.schedule_doctor as empId,"+
		   " t.schedule_midday as midday,"+
		   " t.schedule_deptid as deptId,"+
		   " (select t6.clinic_name from "+HisParameters.HISPARSCHEMAHISUSER+" t_clinic t6 where t6.id=t.schedule_clinicid and t6.stop_flg=t.stop_flg and t6.del_flg=t.del_flg ) as clinic,"+
		   " t.schedule_limit as limit,"+
		   " t.schedule_stopreason as stoprEason,"+
		   " (select t0.dept_name from "+HisParameters.HISPARSCHEMAHISUSER+" t_department t0 where t0.dept_id=t.SCHEDULE_WORKDEPT and t0.stop_flg=0 and t0.del_flg=0) as deptName,"+
		   " (select t1.employee_name from "+HisParameters.HISPARSCHEMAHISUSER+" t_employee t1 where t1.employee_id=t.schedule_doctor and t1.stop_flg=0 and t1.del_flg=0) as empName,"+
		   " t.schedule_reggrade as grade,"+
		   " (select t2.grade_name from "+HisParameters.HISPARSCHEMAHISUSER+" t_register_grade t2 where t2.grade_id=t.schedule_reggrade and t2.stop_flg=0 and t2.del_flg=0) as titleName,"+
		   " t.schedule_isstop as isStop,"+
		   " t.schedule_appflag as appFlag"+
		   " from "+HisParameters.HISPARSCHEMAHISUSER+" T_REGISTER_SCHEDULE t where"+
		   " to_char(t.schedule_date,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(departmentId)){
			hql = hql + " and t.SCHEDULE_WORKDEPT  = '"+departmentId+"'";
		}
		if(StringUtils.isNotBlank(grade)){
			hql = hql + " and t.schedule_reggrade = '"+grade+"'";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("infoAlready",Hibernate.INTEGER).addScalar("empId").addScalar("midday",Hibernate.INTEGER)
		.addScalar("deptId").addScalar("clinic").addScalar("limit",Hibernate.INTEGER).addScalar("stoprEason").addScalar("deptName").addScalar("empName").addScalar("grade").addScalar("titleName").addScalar("isStop",Hibernate.INTEGER).addScalar("appFlag",Hibernate.INTEGER);
		List<InfoVo> infoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoVo.class)).list();
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
		return new ArrayList<InfoVo>();
	}
	/**
	 * 添加&修改
	 * @author  liudelin
	 * @date 2015-06-26
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public void saveChange(RegisterChangeDeptLog entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateTime(DateUtils.getCurrentTime());
			super.save(entity);
			String hql ="update RegisterInfo set expxrt=?,dept=? where id=? ";
			this.getSession().createQuery(hql).setString(0,entity.getNewDoc()).setString(1,entity.getNewDept() ).setString(2, entity.getRigisterId()).executeUpdate();
		}
		
	}
	/**
	 * 根据就诊卡号查询该患者所挂的号
	 * @author  liudelin
	 * @date 2015-11-5
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RegisterInfo> findInfoList(String idcardNo,String no, String state) {
		String hql = "";
		if("1".equals(state)){
			hql = hql + "from RegisterInfo where idcardId = '"+idcardNo+"'";
		}
		if("2".equals(state)){
			hql = hql + " from RegisterInfo where no = '"+no+"'";
		}
		if("3".equals(state)){
			hql = hql + "from RegisterInfo where idcardId = '"+idcardNo+"'";
		}
		hql = hql + " and TO_CHAR(date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and del_flg=0 and stop_flg=0 and status=1 and seeFlag=0 ";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList;
		}
		return new ArrayList<RegisterInfo>();
	}
	/**  
	 *  
	 * @Description：  患者信息
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-5 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo findPatientList(String idcardNo, String no) {
		
		String hql = "from RegisterInfo where del_flg=0 and stop_flg=0 and seeFlag=0 and status=1 ";
		if(StringUtils.isNotBlank(idcardNo)){
			hql = hql + " and idcardId = '"+idcardNo+"' ";
		}
		if(StringUtils.isNotBlank(no)){
			hql = hql + " and no = '"+no+"' ";
		}
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new RegisterInfo();
		}
		return infoList.get(0);
	}
	/**  
	 *  
	 * @Description：  渲染部门
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> querydeptCombobox() {
		String hql = "from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> deptList = super.find(hql, null);
		if(deptList!=null && deptList.size()>0){
			return deptList;
		}
		return new ArrayList<SysDepartment>();
	}
	/**  
	 *  
	 * @Description：  渲染级别
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterGrade> querygradeCombobox() {
		String hql = "from RegisterGrade where del_flg=0 and stop_flg=0";
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList!=null && gradeList.size()>0){
			return gradeList;
		}
		return new ArrayList<RegisterGrade>();
	}
	/**  
	 *  
	 * @Description：  渲染人员
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryempCombobox() {
		String hql = "from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empList = super.find(hql, null);
		if(empList!=null && empList.size()>0){
			return empList;
		}
		return new ArrayList<SysEmployee>();
	}
	/**  
	 *  
	 * @Description：  渲染合同单文
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> querycontCombobox() {
		String hql = "from BusinessContractunit where del_flg=0 and stop_flg=0";
		List<BusinessContractunit> coutList = super.find(hql, null);
		if(coutList!=null && coutList.size()>0){
			return coutList;
		}
		return new ArrayList<BusinessContractunit>();
	}
	/**  
	 *  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterChangeDeptLog> queryChangeDeptLogList(String ids) {
		String hql = "from RegisterChangeDeptLog where del_flg=0 and stop_flg=0";
		if(StringUtils.isNotBlank(ids)){
			hql = hql + " and rigisterId in ('"+ids+"')";
		}
		List<RegisterChangeDeptLog> changeDeptLogList = super.find(hql, null);
		if(changeDeptLogList!=null && changeDeptLogList.size()>0){
			return changeDeptLogList;
		}
		return new ArrayList<RegisterChangeDeptLog>();
	}
	/**  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public PatientIdcard queryPatientIdcardByidcardNo(String idcardNo) {
		String hql = "from PatientIdcard where idcardNo = '"+idcardNo+"' and   del_flg=0 and stop_flg=0";
		List<PatientIdcard> patientIdcardList = super.find(hql, null);
		if(patientIdcardList!=null && patientIdcardList.size()>0){
			return patientIdcardList.get(0);
		}
		return new PatientIdcard();
	}
	@Override
	public List<RegisterInfo> findInfos(String id) {
		String hql = "from RegisterInfo where idcardId = '"+id+"' and   del_flg=0 and stop_flg=0";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList;
		}
		return new ArrayList<RegisterInfo>();
	}
}
