package cn.honry.statistics.operation.operationArrange.dao.impl;
/***
 * 手术安排统计DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.BusinessOproom;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.statistics.operation.operationArrange.dao.OperationArrangeDao;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeVo;

@Repository("operationArrangeDao")
@SuppressWarnings({ "all" })
public class OperationArrangeDaoImpl extends HibernateEntityDao<OperationArrangeVo> implements OperationArrangeDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 * 
	 * 手术安排统计列表查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationArrangeVo> getOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept, String page, String rows,String identityCard) {
		String [] execDepts =execDept.split(",");
		List<String> deptList=Arrays.asList(execDepts);
		List<String> deptCodeList =new ArrayList<String>(500);;
		List<List<String>> deptListAll = new ArrayList<List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.in_dept as inDept,t.PRE_DATE   as preDate,");
		sb.append("t.PATIENT_NO as patientNo,t.BED_NO     as bedNo,");
		sb.append( " t.name  as name,t.sex  as sex,t.age||t.AGE_UNIT   as age,");
		sb.append("t.exec_dept  as execDept,t.OP_DOCTOR  as opDoctor,");
		sb.append("ane.anes_type as aneType,ane.anae_docd as anaeDocd,");
		sb.append("ane.anae_helper as anaeHelper,t.op_id as opId,to_char(t.STATUS) as status ");
		sb.append("from t_operation_apply t ");
		sb.append( "left join  t_operation_anaerecord ane on ane.operation_id=t.op_id ");
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( "inner join  t_patient p on p.MEDICALRECORD_ID=t.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo");
		}
		sb.append(" where  t.stop_flg = 0  and t.del_flg=0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and t.pre_date >= to_date(:beganTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.pre_date < to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			for (int i=0,len=deptList.size(),len1=len-1;i<len;i++) {
				String code = deptList.get(i);
				if(StringUtils.isNotBlank(code)){
					if((i+1)%500==0||i==len1){
						deptListAll.add(deptCodeList);
						deptCodeList=new ArrayList<String>(500);
					}
					deptCodeList.add(code);
				}
			}
			sb.append(" AND (");
			for(int i=0,len=deptListAll.size();i<len;i++){
				if(i>0){
					sb.append(" or");
				}
				sb.append("  t.exec_dept in (:execDept"+i+") ");
			}
			sb.append(" )");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and t.status =:status ");
		}
		
		sb.append(" order by t.PRE_DATE");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.TIMESTAMP).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("age")
								.addScalar("execDept").addScalar("opDoctor").addScalar("aneType")
								.addScalar("anaeDocd").addScalar("anaeHelper")
								.addScalar("opId").addScalar("status");
		if(StringUtils.isNotBlank(beganTime)){
			queryObject.setParameter("beganTime", beganTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			queryObject.setParameter("endTime",endTime);
		}
		if(StringUtils.isNotBlank(execDept)){
			for(int i=0,len=deptListAll.size();i<len;i++){
				queryObject.setParameter("execDept"+i, deptListAll.get(i));
			}
		}
		if(StringUtils.isNotBlank(status)){
			queryObject.setParameter("status",status);
		}
		if(StringUtils.isNotBlank(identityCard)){
			queryObject.setParameter("certificatesNo",identityCard);
		}
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<OperationArrangeVo> operaArragVoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(OperationArrangeVo.class)).list();
		if(operaArragVoList!=null&&operaArragVoList.size()>0){
			return operaArragVoList;
		}
		return new ArrayList<OperationArrangeVo>();
	}

	/**
	 * @Description:手术名称map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String, String>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<DrugUndrug> getCombobox() {
		String hql=" select d.UNDRUG_ID as id,d.UNDRUG_NAME as name FROM T_DRUG_UNDRUG d left join T_BUSINESS_DICTIONARY b on b.CODE_encode=d.UNDRUG_SYSTYPE WHERE "
				+ "b.del_flg = 0 and b.stop_flg=0 and d.del_flg = 0 and d.stop_flg=0 and b.CODE_NAME='手术' ";
		SQLQuery sqlquery=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("name");
		
		List<DrugUndrug> undrugList=sqlquery.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
		if(undrugList!=null && undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}
	/**  
	 * 
	 * 手术安排统计列表查询总记录数
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int getTotal(String beganTime, String endTime, String status, String execDept,String identityCard) {
		String [] execDepts =execDept.split(",");
		List<String> deptList=Arrays.asList(execDepts);
		List<String> deptCodeList =new ArrayList<String>(500);;
		List<List<String>> deptListAll = new ArrayList<List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select  distinct t.op_id as opId ");
		sb.append("from t_operation_apply t ");
		sb.append( "left join  t_operation_anaerecord ane on ane.operation_id=t.op_id ");
		sb.append( "left join t_operation_item item on item.operation_id=t.op_id ");
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( "inner join  t_patient p on p.MEDICALRECORD_ID=t.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo");
		}
		sb.append(" where  t.stop_flg = 0  and t.del_flg=0  ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and t.pre_date >= to_date(:beganTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.pre_date < to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			for (int i=0,len=deptList.size(),len1=len-1;i<len;i++) {
				String code = deptList.get(i);
				if(StringUtils.isNotBlank(code)){
					if((i+1)%500==0||i==len1){
						deptListAll.add(deptCodeList);
						deptCodeList=new ArrayList<String>(500);
					}
					deptCodeList.add(code);
				}
			}
			sb.append(" AND (");
			for(int i=0,len=deptListAll.size();i<len;i++){
				if(i>0){
					sb.append(" or");
				}
				sb.append(" t.exec_dept in (:execDept"+i+") ");
			}
			sb.append(" )");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and t.status =:status ");
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString()).addScalar("opId");
		if(StringUtils.isNotBlank(beganTime)){
			queryObject.setParameter("beganTime", beganTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			queryObject.setParameter("endTime",endTime);
		}
		if(StringUtils.isNotBlank(execDept)){
			for(int i=0,len=deptListAll.size();i<len;i++){
				queryObject.setParameter("execDept"+i, deptListAll.get(i));
			}
		}
		if(StringUtils.isNotBlank(status)){
			queryObject.setParameter("status",status);
		}
		if(StringUtils.isNotBlank(identityCard)){
			queryObject.setParameter("certificatesNo",identityCard);
		}
		List<OperationArrangeVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeVo.class)).list();
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
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
	public List<OperationArrangeVo> getAllOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept) {
		StringBuffer sb = new StringBuffer();
		sb.append("select aa.in_dept as inDept,aa.pre_date as preDate,aa.patient_no as patientNo,aa.bed_no as bedNo,aa.name as name,");
		sb.append(" (select e.EMPLOYEE_NAME from T_EMPLOYEE e where e.EMPLOYEE_code = aa.op_doctor ) as opDoctor,");
		sb.append("aa.sex as sex,aa.op_id as opId,aa.ane_way as aneWay,aa.status as opStates,");
		sb.append("( CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12, 0) ");
      	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday), 0) ");
       	sb.append("ELSE  TRUNC(sysdate) - aa.birthday END  || CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
       	sb.append("WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), aa.birthday) > 1 THEN   '月' ELSE  '天'  END ) as  age,");
       	sb.append("(select dept_name from t_department td where td.dept_code=aa.room_id) as roomId,");
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
		sb.append(") aa order by aa.pre_date ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.DATE).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("opStates",Hibernate.INTEGER)
								.addScalar("age").addScalar("roomId").addScalar("opId").addScalar("itemName")
								.addScalar("aneWay").addScalar("opDoctor").addScalar("thelper")
								.addScalar("wash").addScalar("tour").addScalar("zanesthesia").addScalar("fanesthesia");
		List<OperationArrangeVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<OperationArrangeVo>();
	}

	/**
	 *
	 * @Description：获取病床
	 * @Author：zhangjin
	 * @CreateDate：2016年10月21日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<BusinessHospitalbed> getBedno() {
		String hql=" select t.bed_name as bedName,o.bedinfo_id as id from T_BUSINESS_HOSPITALBED t left join T_INPATIENT_BEDINFO o  on o.bed_id=t.bed_id "
				+ " where o.del_flg=0 and o.stop_flg=0 and o.del_flg=0 and o.stop_flg=0";
		List<BusinessHospitalbed> list=this.getSession().createSQLQuery(hql).addScalar("bedName").addScalar("id")
				.setResultTransformer(Transformers.aliasToBean(BusinessHospitalbed.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 *
	 * @Description：获取手术名称
	 * @Author：zhangjin
	 * @CreateDate：2017年1月4日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<OpNameVo> getOperationItem(String opId) {
		StringBuffer sb=new StringBuffer();
		sb.append("select itemName as itemName from OperationItem where stop_flg=0 and del_flg=0 and operationId=:opId");
		List<OpNameVo> list=this.getSession().createQuery(sb.toString()).setParameter("opId", opId)
				.setResultTransformer(Transformers.aliasToBean(OpNameVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**  
	 * 
	 * 导出手术计费信息汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return:  List<OperationArrangeVo> 
	 *
	 */
	@Override
	public List<OperationArrangeVo> getOperationArrangeVo2(String beganTime,
			String endTime, String status, String execDept, String identityCard) {
		String [] execDepts=execDept.split(",");
		List<String> deptList=Arrays.asList(execDepts);
		List<String> deptCodeList =new ArrayList<String>(500);;
		List<List<String>> deptListAll = new ArrayList<List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.in_dept as inDept,t.PRE_DATE   as preDate,");
		sb.append("t.PATIENT_NO as patientNo,t.BED_NO     as bedNo,");
		sb.append( " t.name  as name,t.sex  as sex,t.age||t.AGE_UNIT   as age,");
		sb.append("t.exec_dept  as execDept,t.OP_DOCTOR  as opDoctor,");
		sb.append("ane.anes_type as aneType,ane.anae_docd as anaeDocd,");
		sb.append("ane.anae_helper as anaeHelper,t.op_id as opId,to_char(t.STATUS) as status ");
		sb.append("from t_operation_apply t ");
		sb.append( "left join  t_operation_anaerecord ane on ane.operation_id=t.op_id ");
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( "inner join  t_patient p on p.MEDICALRECORD_ID=t.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo");
		}
		sb.append(" where  t.stop_flg = 0  and t.del_flg=0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and t.pre_date >= to_date(:beganTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.pre_date < to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			for (int i=0,len=deptList.size(),len1=len-1;i<len;i++) {
				String code = deptList.get(i);
				if(StringUtils.isNotBlank(code)){
					if((i+1)%500==0||i==len1){
						deptListAll.add(deptCodeList);
						deptCodeList=new ArrayList<String>(500);
					}
					deptCodeList.add(code);
				}
			}
			sb.append(" AND (");
			for(int i=0,len=deptListAll.size();i<len;i++){
				if(i>0){
					sb.append(" or");
				}
				sb.append(" t.exec_dept in (:execDept"+i+") ");
			}
			sb.append(" )");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and t.status =:status ");
		}
		sb.append(" order by t.PRE_DATE");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.TIMESTAMP).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("age")
								.addScalar("execDept").addScalar("opDoctor").addScalar("aneType")
								.addScalar("anaeDocd").addScalar("anaeHelper")
								.addScalar("opId").addScalar("status");
		if(StringUtils.isNotBlank(beganTime)){
			queryObject.setParameter("beganTime", beganTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			queryObject.setParameter("endTime",endTime);
		}
		if(StringUtils.isNotBlank(execDept)){
			for(int i=0,len=deptListAll.size();i<len;i++){
				queryObject.setParameter("execDept"+i, deptListAll.get(i));
			}
		}
		if(StringUtils.isNotBlank(status)){
			queryObject.setParameter("status",status);
		}
		if(StringUtils.isNotBlank(identityCard)){
			queryObject.setParameter("certificatesNo",identityCard);
		}
		List<OperationArrangeVo> operaArragVoList = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeVo.class)).list();
		if(operaArragVoList!=null&&operaArragVoList.size()>0){
			return operaArragVoList;
		}
		return new ArrayList<OperationArrangeVo>();
	}

	/**
	 *
	 * @Description：获取手术室
	 * @Author：zhangjin
	 * @CreateDate：2017年1月4日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public Map<String, String> getroomdept() {
		StringBuffer sb=new StringBuffer();
		Map<String, String> map=new HashMap<String, String>();
		sb.append("from BusinessOproom where del_flg = 0 and stop_flg =0 ");
		List<BusinessOproom> oproom=super.find(sb.toString(), null);
		if(oproom!=null&&oproom.size()>0){
			for(BusinessOproom op:oproom){
				map.put(op.getId(), op.getRoomName());
			}
			return map;
		}
		return null;
	}

	/**  
	 * 
	 * 手术安排统计报表打印
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationArrangeVo> getOperationArrangeToReport(String beganTime, String endTime, String status,
			String execDept,String identityCard) {
		String [] execDepts=execDept.split(",");
		List<String> deptList=Arrays.asList(execDepts);
		List<String> deptCodeList =new ArrayList<String>(500);;
		List<List<String>> deptListAll = new ArrayList<List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.in_dept as inDept,t.PRE_DATE   as preDate,");
		sb.append("t.PATIENT_NO as patientNo,t.BED_NO     as bedNo,");
		sb.append( " t.name  as name,t.sex  as sex,t.age||t.AGE_UNIT   as age,");
		sb.append("t.exec_dept  as execDept,t.OP_DOCTOR  as opDoctor,");
		sb.append("ane.anes_type as aneType,ane.anae_docd as anaeDocd,");
		sb.append("ane.anae_helper as anaeHelper,t.op_id as opId,to_char(t.STATUS) as status ");
		sb.append("from t_operation_apply t ");
		sb.append( "left join  t_operation_anaerecord ane on ane.operation_id=t.op_id ");
		if(StringUtils.isNotBlank(identityCard)){
			sb.append( "inner join  t_patient p on p.MEDICALRECORD_ID=t.PATIENT_NO  and p.PATIENT_CERTIFICATESNO=:certificatesNo");
		}
		sb.append(" where  t.stop_flg = 0  and t.del_flg=0 ");
		if(StringUtils.isNotBlank(beganTime)){
			sb.append(" and t.pre_date >= to_date(:beganTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.pre_date < to_date(:endTime,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(execDept)){
			for (int i=0,len=deptList.size(),len1=len-1;i<len;i++) {
				String code = deptList.get(i);
				if(StringUtils.isNotBlank(code)){
					if((i+1)%500==0||i==len1){
						deptListAll.add(deptCodeList);
						deptCodeList=new ArrayList<String>(500);
					}
					deptCodeList.add(code);
				}
			}
			sb.append(" AND (");
			for(int i=0,len=deptListAll.size();i<len;i++){
				if(i>0){
					sb.append(" or");
				}
				sb.append(" t.exec_dept in (:execDept"+i+") ");
			}
			sb.append(" )");
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and t.status =:status ");
		}
		sb.append(" order by t.PRE_DATE");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
								.addScalar("inDept").addScalar("preDate",Hibernate.TIMESTAMP).addScalar("patientNo")
								.addScalar("bedNo").addScalar("name").addScalar("sex").addScalar("age")
								.addScalar("execDept").addScalar("opDoctor").addScalar("aneType")
								.addScalar("anaeDocd").addScalar("anaeHelper")
								.addScalar("opId").addScalar("status");
		if(StringUtils.isNotBlank(beganTime)){
			queryObject.setParameter("beganTime", beganTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			queryObject.setParameter("endTime",endTime);
		}
		if(StringUtils.isNotBlank(execDept)){
			for(int i=0,len=deptListAll.size();i<len;i++){
				queryObject.setParameter("execDept"+i, deptListAll.get(i));
			}
		}
		if(StringUtils.isNotBlank(status)){
			queryObject.setParameter("status",status);
		}
		if(StringUtils.isNotBlank(identityCard)){
			queryObject.setParameter("certificatesNo",identityCard);
		}
		List<OperationArrangeVo> operaArragVoList = queryObject.setResultTransformer(Transformers.aliasToBean(OperationArrangeVo.class)).list();
		
		if(operaArragVoList!=null&&operaArragVoList.size()>0){
			return operaArragVoList;
		}
		return new ArrayList<OperationArrangeVo>();
	}

	/**
     * @Description:手术室查询 用于手术安排统计-报表数据获取
	 * @Author: hedong
	 * @CreateDate: 2017年3月1日
	 * @version: 1.0
     */
	@Override
	public List<SysDepartment> likeSearchOpRoom() {
		StringBuffer sb=new StringBuffer();
		sb.append(" from SysDepartment cs  where cs.del_flg = 0 and cs.deptType ='OP' ");
		List<SysDepartment> list=super.find(sb.toString(), null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	/**
	 * @Description 查询科室信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MenuListVO> querysysDeptment() {
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		List<MenuVO> voList = new ArrayList<MenuVO>();
		StringBuffer sb=new StringBuffer();
		sb.append(" from SysDepartment cs  where cs.del_flg = 0 and cs.deptType ='OP' ");
		List<SysDepartment> list=super.find(sb.toString(), null);
		for(SysDepartment sysDep : list){
			MenuVO vo=new MenuVO();
			vo.setId(sysDep.getDeptCode());
			vo.setName(sysDep.getDeptName());
			vo.setType(sysDep.getDeptType());
			voList.add(vo);
		}
		
		String[] arr=new String[]{"OP-手术"};
		
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
	
}
