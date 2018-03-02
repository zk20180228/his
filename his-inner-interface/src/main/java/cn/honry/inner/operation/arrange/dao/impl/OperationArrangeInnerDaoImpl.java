package cn.honry.inner.operation.arrange.dao.impl;
/***
 * 手术安排统计DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.arrange.dao.OperationArrangeInnerDAO;
import cn.honry.inner.operation.arrange.vo.OperationArrangeInnerVo;

@Repository("operationArrangeInnerDAO")
@SuppressWarnings({ "all" })
public class OperationArrangeInnerDaoImpl extends HibernateEntityDao<OperationArrange> implements OperationArrangeInnerDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * @Description:根据条件查询手术安排信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationArrangeInnerVo> getOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept, String page, String rows) {
		StringBuffer sb = new StringBuffer();
		sb.append("select aa.in_dept as inDept,aa.pre_date as preDate,aa.patient_no as patientNo,aa.bed_no as bedNo,aa.name as name,");
		sb.append(" (select e.EMPLOYEE_NAME from T_EMPLOYEE e where e.EMPLOYEE_ID = aa.op_doctor ) as opDoctor,");
		sb.append("aa.sex as sex,aa.op_id as opId,aa.ane_way as aneWay,aa.status as opStates,");
		sb.append("( CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12, 0) ");
      	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday), 0) ");
       	sb.append("ELSE  TRUNC(sysdate) - aa.birthday END  || CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
       	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN   '月' ELSE  '天'  END ) as  age,");
       	sb.append("(select dept_name from t_department td where td.dept_id=aa.room_id) as roomId,");
       	sb.append("(select wm_concat(ITEM_NAME) from t_operation_ITEM TOI where toi.operation_id = aa.op_id) as itemName,");
      
		sb.append(" (case  when aa.status = '1' then (select wm_concat(EMPL_NAME)from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') ");
		sb.append("  when aa.status = '3' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '2') ");
		sb.append("  when aa.status = '5' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') END ) as thelper,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as wash,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as tour,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as zanesthesia,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as fanesthesia");
		sb.append(" from (select app.in_dept,app.pre_date,app.patient_no,app.bed_no,app.name,app.sex,app.birthday,app.op_id,app.ane_way,app.op_doctor,app.room_id,app.status  from t_operation_apply app");
		//sb.append("t_operation_record rec where app.op_id=rec.operation_id AND app.stop_flg = 0 ");
		sb.append(" where  app.stop_flg = 0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') >= '"+beganTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and app.exec_dept = '"+execDept+"' ");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and app.status = '"+status+"'");
		}
		sb.append(") aa order by aa.pre_date desc");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.DATE).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("opStates",Hibernate.INTEGER)
								.addScalar("age").addScalar("roomId").addScalar("opId").addScalar("itemName")
								.addScalar("aneWay").addScalar("opDoctor").addScalar("thelper")
								.addScalar("wash").addScalar("tour").addScalar("zanesthesia").addScalar("fanesthesia");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<OperationArrangeInnerVo> operaArragVoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(OperationArrangeInnerVo.class)).list();
		if(operaArragVoList!=null&&operaArragVoList.size()>0){
			return operaArragVoList;
		}
		return new ArrayList<OperationArrangeInnerVo>();
	}

	/**  
	 *  
	 * @Description：拟手术名称下拉框的方法
	 * @Author：zhangjin
	 * @CreateDate：2016-4-8 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugUndrug> getCombobox() {
		String hql=" select d.UNDRUG_ID as id,d.UNDRUG_NAME as name FROM T_DRUG_UNDRUG d left join T_BUSINESS_DICTIONARY b on b.CODE_encode=d.UNDRUG_SYSTYPE WHERE "
				+ "b.del_flg = 0 and b.stop_flg=0 and d.del_flg = 0 and d.stop_flg=0 and b.CODE_NAME='手术' and b.code_type='systemType' ";
		SQLQuery sqlquery=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name");
		
		List<DrugUndrug> undrugList=sqlquery.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
		if(undrugList!=null && undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}
	/**
	 * @Description:根据条件查询手术安排信息总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationArrangeVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String beganTime, String endTime, String status, String execDept) {
		StringBuffer sb = new StringBuffer();
		sb.append("select aa.in_dept as inDept,aa.pre_date as preDate,aa.patient_no as patientNo,aa.bed_no as bedNo,aa.name as name,");
		sb.append(" (select e.EMPLOYEE_NAME from T_EMPLOYEE e where e.EMPLOYEE_ID = aa.op_doctor ) as opDoctor,");
		sb.append("aa.sex as sex,aa.op_id as opId,aa.ane_way as aneWay,aa.status as opStates,");
		sb.append("( CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12, 0) ");
      	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday), 0) ");
       	sb.append("ELSE  TRUNC(sysdate) - aa.birthday END  || CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
       	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN   '月' ELSE  '天'  END ) as  age,");
       	sb.append("(select dept_name from t_department td where td.dept_id=aa.room_id) as roomId,");
       	sb.append("(select wm_concat(ITEM_NAME) from t_operation_ITEM TOI where toi.operation_id = aa.op_id) as itemName,");
       	
       	sb.append(" (case  when aa.status = '1' then (select wm_concat(EMPL_NAME)from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') ");
		sb.append("  when aa.status = '3' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '2') ");
		sb.append("  when aa.status = '5' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') END ) as thelper,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as wash,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as tour,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as zanesthesia,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as fanesthesia");
		sb.append(" from (select app.in_dept,app.pre_date,app.patient_no,app.bed_no,app.name,app.sex,app.birthday,app.op_id,app.ane_way,app.op_doctor,app.room_id,app.status  from t_operation_apply app");
		//sb.append("t_operation_record rec where app.op_id=rec.operation_id AND app.stop_flg = 0 ");
		sb.append(" where app.stop_flg = 0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') >= '"+beganTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and app.room_id = '"+execDept+"' ");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and app.status = '"+status+"'");
		}
		sb.append(") aa order by aa.pre_date desc");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.DATE).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("opStates",Hibernate.INTEGER)
								.addScalar("age").addScalar("roomId").addScalar("opId").addScalar("itemName")
								.addScalar("aneWay").addScalar("opDoctor").addScalar("thelper")
								.addScalar("wash").addScalar("tour").addScalar("zanesthesia").addScalar("fanesthesia");
		List<OperationArrangeInnerVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeInnerVo.class)).list();
		return list.size();
	}
	
	/**
	 * @Description:根据条件查询所有手术安排信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月27日
	 * @param:beganTime 开始时间； endTime 结束时间； status 手术状态； execDept 执行科室  page 当前页数   rows 分页条数
	 * @return List<OperationArrangeVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationArrangeInnerVo> getAllOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept) {
		StringBuffer sb = new StringBuffer();
		sb.append("select aa.in_dept as inDept,aa.pre_date as preDate,aa.patient_no as patientNo,aa.bed_no as bedNo,aa.name as name,");
		sb.append(" (select e.EMPLOYEE_NAME from T_EMPLOYEE e where e.EMPLOYEE_ID = aa.op_doctor ) as opDoctor,");
		sb.append("aa.sex as sex,aa.op_id as opId,aa.ane_way as aneWay,aa.status as opStates,");
		sb.append("( CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12, 0) ");
      	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday), 0) ");
       	sb.append("ELSE  TRUNC(sysdate) - aa.birthday END  || CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
       	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN   '月' ELSE  '天'  END ) as  age,");
       	sb.append("(select dept_name from t_department td where td.dept_id=aa.room_id) as roomId,");
       	sb.append("(select wm_concat(ITEM_NAME) from t_operation_ITEM TOI where toi.operation_id = aa.op_id) as itemName,");
       	
       	sb.append(" (case  when aa.status = '1' then (select wm_concat(EMPL_NAME)from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') ");
		sb.append("  when aa.status = '3' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '2') ");
		sb.append("  when aa.status = '5' THEN (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%thelper%' and OPERATION_ID = aa.op_id and fore_flag = '0') END ) as thelper,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%wash%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as wash,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%tour%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as tour,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%zanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as zanesthesia,");
		sb.append("  (case when aa.status = '1' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '2' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0')");
		sb.append("  when aa.status = '3' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '1')");
		sb.append("  when aa.status = '4' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '2')");
		sb.append("  when aa.status = '5' then (select wm_concat(EMPL_NAME) from t_operation_arrange toa where toa.role_code like '%fanesthesia%' and OPERATION_ID = aa.op_id and fore_flag = '0') end ) as fanesthesia");
		sb.append(" from (select app.in_dept,app.pre_date,app.patient_no,app.bed_no,app.name,app.sex,app.birthday,app.op_id,app.ane_way,app.op_doctor,app.room_id,app.status  from t_operation_apply app");
		//sb.append("t_operation_record rec where app.op_id=rec.operation_id AND app.stop_flg = 0 ");
		sb.append(" where app.stop_flg = 0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') >= '"+beganTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(app.pre_date,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(execDept)){
			sb.append(" and app.room_id = '"+execDept+"' ");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and app.status = '"+status+"'");
		}
		sb.append(") aa order by aa.pre_date desc");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.DATE).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("opStates",Hibernate.INTEGER)
								.addScalar("age").addScalar("roomId").addScalar("opId").addScalar("itemName")
								.addScalar("aneWay").addScalar("opDoctor").addScalar("thelper")
								.addScalar("wash").addScalar("tour").addScalar("zanesthesia").addScalar("fanesthesia");
		List<OperationArrangeInnerVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeInnerVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<OperationArrangeInnerVo>();
	}
}
