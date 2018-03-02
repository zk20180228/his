package cn.honry.statistics.operation.operationBillingInfo.dao.impl;
/***
 * 手术计费信息DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.operationBillingInfo.dao.OperationBillingInfoDao;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("operationBillingInfoDao")
@SuppressWarnings({ "all" })
public class OperationBillingInfoDaoImpl extends HibernateEntityDao<OperationBillingInfoVo> implements OperationBillingInfoDao{
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
		@Resource
		private JdbcTemplate jdbcTemplate;
		//扩展工具类,支持参数名传参
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		/**
		 * 根据条件查询手术计费信息
		 * @Description:根据条件查询手术计费信息
		 * @Author: tangfeishuai
		 * @CreateDate: 2016年5月27日
		 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
		 * feeState 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  page 当前页数   rows 分页条数  identityCard身份证号
		 * @return List<OperationBillingInfoVo>
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
	@Override
	public List<OperationBillingInfoVo> getOperationBillingInfoVo(String beginTime, String endTime, String opStatus,
			String execDept, String feeBegTime, String feeEndTime, String feeStates, String inState, String opDoctor,
			String opDoctordept, String page, String rows, String identityCard) {
		String opDoctors = opDoctor.replace(",", "','");
		String opDoctordepts = opDoctordept.replace(",", "','");
		StringBuffer sb = new StringBuffer();
		sb.append("select t.opId,t.patientNo,t.name,t.preDate,t.opDoctor,t.opDoctorId,t.opStatus,t.opDoctordeptId,t.opDoctordept, ");
		sb.append("   t.execDept,t.inState,t.feeState,t.diagName,t.feeDate ");
		sb.append("  from  ( ");
		sb.append(" select distinct app.op_id as opId,app.op_doctordept as opDoctordeptId,app.patient_no as patientNo,app.name as name,app.pre_date as preDate, ");
		sb.append(" app.op_doctor as opDoctorId,app.status as opStatus,td.dept_name as opDoctorDept, e.employee_name as opDoctor,info.in_state as inState,decode(fee.operation_id,null,'1','2') as feeState,");
		sb.append(" fee.fee_date as feeDate,fee.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app.op_id) as diagName ");
		sb.append(" from t_operation_apply app ");
		sb.append("  left join t_department td on td.dept_code=app.op_doctordept  left join t_employee e on e.employee_jobno=app.op_doctor ");
		sb.append("  left join t_inpatient_info info on info.inpatient_no=app.clinic_code ");
		sb.append("  left join t_inpatient_itemlist fee on fee.operation_id=app.op_id and fee.inpatient_no=app.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee.fee_date >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee.fee_date < to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee.execute_deptcode in(:execDept) ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app.stop_flg=0  ");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app.pre_date >= to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app.pre_date< to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app.op_doctor in('"+opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app.op_doctordept in( '"+opDoctordepts+"' )");
		}
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app.status = '"+opStatus+"' ");
		}
		sb.append(" union all ");
		sb.append(" select distinct app2.op_id as opId,app2.op_doctordept as opDoctordeptId,app2.patient_no as patientNo,app2.name as name,app2.pre_date as preDate, ");
		sb.append(" app2.op_doctor as opDoctorId,app2.status as opStatus,td2.dept_name as opDoctorDept, e2.employee_name as opDoctor,info2.in_state as inState,decode(fee2.operation_id,null,'1','2') as feeState,");
		sb.append(" fee2.fee_date as feeDate,fee2.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app2.op_id) as diagName ");
		sb.append(" from t_operation_apply app2 ");
		sb.append("  left join t_department td2 on td2.dept_code=app2.op_doctordept  left join t_employee e2 on e2.employee_jobno=app2.op_doctor ");
		sb.append("  left join t_inpatient_info_now info2 on info2.inpatient_no=app2.clinic_code ");
		sb.append("  left join t_inpatient_itemlist_now fee2 on fee2.operation_id=app2.op_id and fee2.inpatient_no=app2.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee2.fee_date >=to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee2.fee_date <to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee2.execute_deptcode in(:execDept)");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app2.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app2.stop_flg=0  ");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app2.pre_date >=to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app2.pre_date <to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app2.op_doctor in ('"+opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app2.op_doctordept in( '"+opDoctordepts+"' )");
		}
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info2.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app2.status = '"+opStatus+"' ");
		}
		sb.append(" ) t where 1=1 ");
		
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and t.feeDate >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and t.feeDate< to_date(:feeEndTime,'yyyy-MM-dd')");
		}
		if(StringUtils.isNotBlank(feeStates)){
			sb.append(" and  t.feeState='"+feeStates+"' ");
		}
		
		StringBuffer bufferRows = new StringBuffer(sb.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :bag ) WHERE rn > :start ");
		
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(feeBegTime)){
			paraMap.put("feeBegTime", feeBegTime);
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			paraMap.put("feeEndTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(feeEndTime), 1)));
		}
		if(StringUtils.isNotBlank(execDept)){
			paraMap.put("execDept",  Arrays.asList(execDept.split(",")));
		}
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(endTime), 1)));
		}
		if(StringUtils.isNotBlank(identityCard)){
			paraMap.put("certificatesNo",identityCard);
		}
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("bag", start * count);
		List<OperationBillingInfoVo> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<OperationBillingInfoVo>() {
			@Override
			public OperationBillingInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationBillingInfoVo vo = new OperationBillingInfoVo();
				vo.setOpId(rs.getString("opId"));
				vo.setPatientNo(rs.getString("patientNo"));
				vo.setName(rs.getString("name"));
				vo.setFeeState(rs.getString("feeState"));
				vo.setPreDate(rs.getTimestamp("preDate"));
				vo.setOpDoctor(rs.getString("opDoctor"));
				vo.setOpDoctordept(rs.getString("opDoctordept"));
				vo.setInState(rs.getString("inState"));
				vo.setOpStatus(rs.getInt("opStatus"));
				vo.setDiagName(rs.getObject("diagName"));
				vo.setFeeDate(rs.getTimestamp("feeDate"));
				return vo;
		}});
		
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<OperationBillingInfoVo>();
	}
	/**
	 * @Description:查询员工
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月3日
	 * @param:
	 * @return List<SysEmployee>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysEmployee> getEmp() {
		String hql="from SysEmployee  where stop_flg=0 and del_flg=0";
		List<SysEmployee>  list=super.find(hql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}
	
	/**
	 * 根据条件查询手术计费信息总记录数
	 * @Description:根据条件查询手术计费信息总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室  identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String beginTime, String endTime,String opStatus,String execDept,
			String feeBegTime,String feeEndTime,String feeStates,String inState,String opDoctor,String opDoctordept, String identityCard) {
		StringBuffer sb = new StringBuffer();
		String opDoctors =opDoctor.replace(",", "','");
		String opDoctordepts =opDoctordept.replace(",", "','");
		sb.append("select t.opId ");
		sb.append("  from  ( ");
		sb.append(" select distinct app.op_id as opId,app.op_doctordept as opDoctordeptId,app.patient_no as patientNo,app.name as name,app.pre_date as preDate, ");
		sb.append(" app.op_doctor as opDoctorId,app.status as opStatus,td.dept_name as opDoctorDept, e.employee_name as opDoctor,info.in_state as inState,decode(fee.operation_id,null,'1','2') as feeState,");
		sb.append(" fee.fee_date as feeDate,fee.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app.op_id) as diagName ");
		sb.append(" from t_operation_apply app ");
		sb.append("  left join t_department td on td.dept_code=app.op_doctordept  left join t_employee e on e.employee_jobno=app.op_doctor ");
		sb.append("  left join t_inpatient_info info on info.inpatient_no=app.clinic_code ");
		sb.append("  left join t_inpatient_itemlist fee on fee.operation_id=app.op_id and fee.inpatient_no=app.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee.fee_date >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee.fee_date <= to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee.execute_deptcode in(:execDept) ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app.stop_flg=0  ");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app.pre_date >= to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app.pre_date<= to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app.op_doctor in('"+opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app.op_doctordept in( '"+opDoctordepts+"' )");
		}
		
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app.status = '"+opStatus+"' ");
		}
		
		sb.append(" union all ");
		sb.append(" select distinct app2.op_id as opId,app2.op_doctordept as opDoctordeptId,app2.patient_no as patientNo,app2.name as name,app2.pre_date as preDate, ");
		sb.append(" app2.op_doctor as opDoctorId,app2.status as opStatus,td2.dept_name as opDoctorDept, e2.employee_name as opDoctor,info2.in_state as inState,decode(fee2.operation_id,null,'1','2') as feeState,");
		sb.append(" fee2.fee_date as feeDate,fee2.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app2.op_id) as diagName ");
		sb.append(" from t_operation_apply app2 ");
		sb.append("  left join t_department td2 on td2.dept_code=app2.op_doctordept  left join t_employee e2 on e2.employee_jobno=app2.op_doctor ");
		sb.append("  left join t_inpatient_info_now info2 on info2.inpatient_no=app2.clinic_code ");
		sb.append("  left join t_inpatient_itemlist_now fee2 on fee2.operation_id=app2.op_id and fee2.inpatient_no=app2.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee2.fee_date >=to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee2.fee_date <=to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee2.execute_deptcode in(:execDept)");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app2.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app2.stop_flg=0  ");
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app2.op_doctor in('"+opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app2.op_doctordept in( '"+opDoctordept+"' )");
		}
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info2.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app2.status = '"+opStatus+"' ");
		}
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app2.pre_date >=to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app2.pre_date <=to_date(:endTime,'yyyy-MM-dd') ");
		}
		
		
		
		sb.append(" ) t where 1=1 ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and t.feeDate >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and t.feeDate<= to_date(:feeEndTime,'yyyy-MM-dd')");
		}
		if(StringUtils.isNotBlank(feeStates)){
			sb.append(" and  t.feeState='"+feeStates+"' ");
		}
		StringBuffer bufferTotal = new StringBuffer(sb.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(feeBegTime)){
			paraMap.put("feeBegTime", feeBegTime);
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			paraMap.put("feeEndTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(feeEndTime), 1)));
		}
		if(StringUtils.isNotBlank(execDept)){
			paraMap.put("execDept",  Arrays.asList(execDept.split(",")));
		}
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(endTime), 1)));
		}
		if(StringUtils.isNotBlank(identityCard)){
			paraMap.put("certificatesNo",identityCard);
		}
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
		
		
		return total;
		
		
		
	}
	/**
	 * 根据条件查询所有手术计费信息
	 * @Description:根据条件查询所有手术计费信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beginTime 开始时间   endTime 结束时间   opStatus 手术状态   execDept 执行科室  feeBegTime 批费开始时间  feeEndTime 批费结束时间
	 * feeStates 批费状态  inState 在院状态  opDoctor 手术医生  opDoctordept 手术以医生科室    identityCard身份证号
	 * @return List<OperationBillingInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationBillingInfoVo> getAllOperationBillingInfoVo(String beginTime, String endTime, String opStatus,
			String execDept, String feeBegTime, String feeEndTime, String feeStates, String inState, String opDoctor,
			String opDoctordept, String identityCard) {
		String opDoctors = opDoctor.replace(",", "','");
		String opDoctordepts = opDoctordept.replace(",", "','");
		StringBuffer sb = new StringBuffer();
		sb.append("select t.opId,t.patientNo,t.name,t.preDate,t.opDoctor,t.opDoctorId,t.opStatus,t.opDoctordeptId,t.opDoctordept,t.feeDate, ");
		sb.append("   t.execDept,t.inState,t.feeState,t.diagName,t.feeDate ");
		sb.append("  from  ( ");
		sb.append(" select distinct app.op_id as opId,app.op_doctordept as opDoctordeptId,app.patient_no as patientNo,app.name as name,app.pre_date as preDate, ");
		sb.append(" app.op_doctor as opDoctorId,app.status as opStatus,td.dept_name as opDoctorDept, e.employee_name as opDoctor,info.in_state as inState,decode(fee.operation_id,null,'1','2') as feeState,");
		sb.append(" fee.fee_date as feeDate,fee.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app.op_id) as diagName ");
		sb.append(" from t_operation_apply app ");
		sb.append("  left join t_department td on td.dept_code=app.op_doctordept  left join t_employee e on e.employee_jobno=app.op_doctor ");
		sb.append("  left join t_inpatient_info info on info.inpatient_no=app.clinic_code ");
		sb.append("  left join t_inpatient_itemlist fee on fee.operation_id=app.op_id and fee.inpatient_no=app.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee.fee_date >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee.fee_date < to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee.execute_deptcode in(:execDept) ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app.stop_flg=0  ");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app.pre_date >= to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app.pre_date< to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app.op_doctor in('"+opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app.op_doctordept in ( '"+opDoctordepts+"') ");
		}
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app.status = '"+opStatus+"' ");
		}
		
		sb.append(" union all ");
		sb.append(" select distinct app2.op_id as opId,app2.op_doctordept as opDoctordeptId,app2.patient_no as patientNo,app2.name as name,app2.pre_date as preDate, ");
		sb.append(" app2.op_doctor as opDoctorId,app2.status as opStatus,td2.dept_name as opDoctorDept, e2.employee_name as opDoctor,info2.in_state as inState,decode(fee2.operation_id,null,'1','2') as feeState,");
		sb.append(" fee2.fee_date as feeDate,fee2.execute_deptcode as execDept, ");
		sb.append(" (select listagg(DIAG_NAME, ',') within group(order by null) as diagName from T_OPERATION_DIAGNOSE op where op.OPERATION_ID=app2.op_id) as diagName ");
		sb.append(" from t_operation_apply app2 ");
		sb.append("  left join t_department td2 on td2.dept_code=app2.op_doctordept  left join t_employee e2 on e2.employee_jobno=app2.op_doctor ");
		sb.append("  left join t_inpatient_info_now info2 on info2.inpatient_no=app2.clinic_code ");
		sb.append("  left join t_inpatient_itemlist_now fee2 on fee2.operation_id=app2.op_id and fee2.inpatient_no=app2.clinic_code ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and fee2.fee_date >=to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and fee2.fee_date < to_date(:feeEndTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and fee2.execute_deptcode in(:execDept) ");
		}
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( " inner join  t_patient p on p.MEDICALRECORD_ID=app2.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo ");
		}
		sb.append(" where app2.stop_flg=0  ");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and app2.pre_date >=to_date(:beginTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and app2.pre_date < to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(opDoctor)){
			sb.append(" and app2.op_doctor in('"+ opDoctors+"') ");
		}
		if(StringUtils.isNotBlank(opDoctordept)){
			sb.append(" and app2.op_doctordept in ('"+opDoctordepts+"') ");
		}
		if(StringUtils.isNotBlank(inState)){
			sb.append(" and  info.in_state = '"+inState+"'");
		}
		if(StringUtils.isNotBlank(opStatus)){
			sb.append(" and  app2.status = '"+opStatus+"' ");
		}
		
		sb.append(" ) t where 1=1 ");
		if(StringUtils.isNotBlank(feeBegTime)){
			sb.append(" and t.feeDate >= to_date(:feeBegTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			sb.append(" and t.feeDate< to_date(:feeEndTime,'yyyy-MM-dd')");
		}
		
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(feeBegTime)){
			paraMap.put("feeBegTime", feeBegTime);
		}
		if(StringUtils.isNotBlank(feeEndTime)){
			paraMap.put("feeEndTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(feeEndTime), 1)));
		}
		if(StringUtils.isNotBlank(execDept)){
			paraMap.put("execDept",  Arrays.asList(execDept.split(",")));
		}
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(endTime), 1)));
		}
		if(StringUtils.isNotBlank(identityCard)){
			paraMap.put("certificatesNo",identityCard);
		}
		List<OperationBillingInfoVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<OperationBillingInfoVo>() {
			@Override
			public OperationBillingInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OperationBillingInfoVo vo = new OperationBillingInfoVo();
				vo.setOpId(rs.getString("opId"));
				vo.setPatientNo(rs.getString("patientNo"));
				vo.setName(rs.getString("name"));
				vo.setFeeState(rs.getString("feeState"));
				vo.setPreDate(rs.getTimestamp("preDate"));
				vo.setOpDoctor(rs.getString("opDoctor"));
				vo.setOpDoctordept(rs.getString("opDoctordept"));
				vo.setInState(rs.getString("inState"));
				vo.setOpStatus(rs.getInt("opStatus"));
				vo.setDiagName(rs.getObject("diagName"));
				vo.setFeeDate(rs.getTimestamp("feeDate"));
				return vo;
		}});
		if(voList.size()>0&&voList!=null){
			return voList;
		}
		return new ArrayList<OperationBillingInfoVo>();
	}
	
	/**
	 * @Description:员工
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysEmployee>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysEmployee> queryemployeeCombobox() {
		String hql = "from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empList = super.find(hql, null);
		if(empList!=null && empList.size()>0){
			return empList;
		}
		return new ArrayList<SysEmployee>();
	}
	
	/**
	 * @Description:部门
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月20日
	 * @return List<SysDepartment>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<SysDepartment> querydeptmentCombobox() {
		String hql = "from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> empList = super.find(hql, null);
		if(empList!=null && empList.size()>0){
			return empList;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 * 
	 * 获取最大最小时间
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Override
	public StatVo findMaxMin() {
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT MAX(mn.fee_date) AS eTime ,MIN(mn.) AS sTime FROM t_operation_apply mn ");
		final String  sql=sb.toString();
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	
	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Override
	public List<MenuListVO> getDeptList() {
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		List<MenuVO> voList = new ArrayList<MenuVO>();
		List<SysDepartment> sysDepList =deptInInterService.queryAllDept();
		for(SysDepartment sysDep : sysDepList){
			MenuVO vo=new MenuVO();
			vo.setId(sysDep.getDeptCode());
			vo.setName(sysDep.getDeptName());
			vo.setType(sysDep.getDeptType());
			voList.add(vo);
		}
		String[] arr=new String[]{"C-门诊","I-住院","N-护士站","OP-手术"};
			
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			depts.add(d);
		}
		return depts;
	}
	
	/**  
	 * 
	 * 获取医生
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:26:13 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:26:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuListVO>
	 *
	 */
	@Override
	public List<MenuListVO> getDoctorList() {
		StringBuffer buffer = new StringBuffer("SELECT ");
		buffer.append("e.DEPT_ID as deptId, ");
		buffer.append("d.DEPT_TYPE as type, ");
		buffer.append("e.EMPLOYEE_JOBNO as jobNo, ");
		buffer.append("e.EMPLOYEE_NAME as name, ");
		buffer.append("e.EMPLOYEE_PINYIN as pinyin, ");
		buffer.append("e.EMPLOYEE_WB as wb, ");
		buffer.append("e.EMPLOYEE_INPUTCODE as inputCode ");
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_EMPLOYEE e");
		buffer.append(" INNER JOIN ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_DEPARTMENT d");
		buffer.append(" on e.DEPT_ID=d.DEPT_ID");
		buffer.append(" WHERE e.STOP_FLG=0");
		buffer.append(" AND e.DEL_FLG=0");
		List<MenuVO> voList = namedParameterJdbcTemplate.query(buffer.toString(), new RowMapper<MenuVO>(){
			@Override
			public MenuVO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				MenuVO vo = new MenuVO();
				vo.setRelativeId(rs.getString("deptId"));
				vo.setType(rs.getString("type"));
				vo.setId(rs.getString("jobNo"));
				vo.setName(rs.getString("name"));
				vo.setInputCode(rs.getString("inputCode"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				return vo;
			}
		});
		List<MenuListVO> doctors=new ArrayList<MenuListVO>();
		String[] arr=new String[]{"C-门诊","I-住院","N-护士站","OP-手术"};
		
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			doctors.add(d);
		}
		if(doctors!=null&&doctors.size()>0){
			return doctors;
		}
		return new ArrayList<MenuListVO>();
	}

}
