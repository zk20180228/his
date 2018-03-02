package cn.honry.statistics.drug.billsearch.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.google.inject.internal.BindingBuilder;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.OperationApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.drug.billsearch.dao.BillSearchDAO;
import cn.honry.statistics.drug.billsearch.vo.BillClassHzVo;
import cn.honry.statistics.drug.billsearch.vo.BillClassMxVo;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
/***
 * 摆药单分类DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */          
@Repository("billSearchDAO")
@SuppressWarnings({ "all" })
public class BillSearchDaoImpl extends HibernateEntityDao<OperationBillingInfoVo> implements BillSearchDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**   
	     根据id 查询登录病区关联的科室
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DepartmentContact> getDepConByPid(String pid) throws Exception{
		String hql="select t1.dept_id as deptId,t1.dept_name as deptName from t_department_contact t1 where t1.pardept_id in (select t.id from t_department_contact t where  t.dept_id='"+pid+"' and t.reference_type='03') and t1.del_flg=0";
		SQLQuery query=getSession().createSQLQuery(hql).addScalar("deptId").addScalar("deptName");
		List<DepartmentContact> list=query.setResultTransformer(Transformers.aliasToBean(DepartmentContact.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<DepartmentContact>();
	}
	/**   
	    查询医院维护的所有摆药单分类
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<DrugBillclass> getDrugBillclass() throws Exception{
		String hql = "from DrugBillclass d where d.stop_flg=0 and d.del_flg=0" ;
		List<DrugBillclass> list = super.find(hql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<DrugBillclass>();
	}
	
	/**   
	    查询该科室下的该摆药单类型的摆药单号
	 * @author：tangfeishuai
	 * @createDate：2016-6-12 上午10:52:19  
	 * @modifier：tangfeishuai
	 * @modifyDate：2016-6-12 上午10:52:19  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<DrugApplyout> getDrugOutstore(String deptCode,String billClassCode,String drugedBill,String beginTime,String endTime,String applyState) throws Exception{

		String hql="select distinct t.druged_bill as drugedBill from t_drug_applyout t where t.dept_code = '"+deptCode+"' AND T .billclass_code = '"+billClassCode+"' and t.del_flg=0 and t.stop_flg=0 ";
		if(StringUtils.isNotBlank(drugedBill)){
			hql=hql+" and t.druged_bill like '%"+drugedBill+"%' ";
		}
		if(StringUtils.isNotBlank(beginTime)){
			hql=hql+" and to_char(t.druged_date,'yyyy-MM-dd') >= '"+beginTime+"' ";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql=hql+" and to_char(t.druged_date,'yyyy-MM-dd') <= '"+endTime+"' ";
		}
		if(StringUtils.isNotBlank(applyState)){
			hql=hql+" and t.apply_state= '"+applyState+"' ";
		}
		SQLQuery query=getSession().createSQLQuery(hql).addScalar("drugedBill");
		List<DrugApplyout> datas=query.setResultTransformer(Transformers.aliasToBean(DrugApplyout.class)).list();
		if(datas.size()>0&&datas!=null){
			return datas;
		}
		return new ArrayList<DrugApplyout>();
		
	}
	/***
	 * 得到摆药单汇总hql
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 */
	@Override
	public String getBillClassHzHql(String drugedBill,String bname,String applyState) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT s.drug_name AS drugName,t.specs AS specs,t.apply_num as drugedNum, ");
		sb.append(" (select d.DEPT_NAME from t_department d where d.DEPT_id = t.dept_code) as deptCode, ");
		sb.append(" (select d.DEPT_NAME from t_department d where d.DEPT_id = t.drug_dept_code) as drugDeptCode, ");
		sb.append(" (select tdb.billclass_name from t_drug_billclass tdb where tdb.billclass_id=t.BILLCLASS_CODE) as  billClassName, ");
		sb.append(" DECODE(T.VALID_STATE, '1', '有效', '2','无效','3','不摆药') AS validState , ");
		sb.append("  s.drug_namepinyin AS drugPinYin, s.drug_namewb AS drugWb FROM t_drug_applyout t,t_drug_info s ");
		sb.append(" WHERE T.drug_code = s.drug_id ");
		sb.append(" and T.druged_bill = '"+drugedBill+"' ");
		if(StringUtils.isNotBlank(applyState)){
			sb.append(" AND t.apply_state = '"+applyState+"' ");
		}
		if(StringUtils.isNotBlank(bname)){
			String queryName = bname.toUpperCase();
			sb.append(" and (upper(s.drug_namepinyin) like '%"+queryName+"%'  ");
			sb.append(" or upper(s.drug_namewb) like '%"+queryName+"%'  ");
			sb.append(" or upper(s.drug_name) like '%"+queryName+"%' ) ");
		}
		sb.append("GROUP BY s.drug_name,t.specs,t.apply_num,T.dept_code,T.drug_dept_code,T.billclass_code,");
		sb.append(" T.valid_state,s.drug_namepinyin,s.drug_namewb ORDER BY T.drug_dept_code,s.drug_name ");
		return sb.toString();
	}
	/***
	 * 摆药单汇总明细hql
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 */
	@Override
	public String getBillClassMxHql(String drugedBill,String bname,String applyState) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT (select tbb.bed_name  from t_business_hospitalbed tbb where tbb.bed_id in (select bed_id from t_inpatient_bedinfo where bedinfo_id = r.bedinfo_id)) AS bedName,");
		sb.append(" R.PATIENT_NAME AS patientName,R.medicalrecord_id AS inpatientNo,t.TRADE_NAME AS tradeName,t.SPECS AS specs,t.DOSE_ONCE as doseOnce,");
		sb.append("(select code_name from t_business_dictionary tcd where  tcd.code_type='doseUnit' and tcd.code_name=T.DOSE_UNIT) AS doseUnit,");
		sb.append("(select frequency_name from t_business_frequency tbf where tbf.frequency_id =  T.DFQ_FREQ) AS dfqFerq,");
		sb.append(" T.USE_NAME AS useName,t.apply_num AS drugedNum,(select d.DEPT_NAME from t_department d where d.DEPT_ID = t.dept_code) as deptCode,");
		sb.append(" (select d.DEPT_NAME from t_department d where d.DEPT_code = t.drug_dept_code) as drugDeptCode,");
		sb.append(" (select tdb.billclass_name from t_drug_billclass tdb where tdb.billclass_id=t.BILLCLASS_CODE) as billClassName,");
		sb.append(" DECODE(T.VALID_STATE, '1', '有效', '2','无效','3','不摆药') AS validState,s.drug_namepinyin AS drugPinYin,");
		sb.append(" s.drug_namewb AS drugWb,DECODE(T.PRINT_DATE, '', '未摆', '已摆') AS state,t.DRUGED_BILL AS drugedBill,t.PRINT_DATE AS printDate");
		sb.append(" FROM  T_drug_applyout T, T_drug_info s ,t_inpatient_info R WHERE T.DRUG_CODE = S.DRUG_id  AND T.PATIENT_ID = R.INPATIENT_NO   ");
		sb.append(" and T.druged_bill = '"+drugedBill+"' ");
		if(StringUtils.isNotBlank(applyState)){
			sb.append(" AND  T.apply_state = '"+applyState+"' ");
		}
		if(StringUtils.isNotBlank(bname)){
			String queryName = bname.toUpperCase();
			sb.append(" and (upper(s.drug_namepinyin) like '%"+queryName+"%'  ");
			sb.append(" or upper(s.drug_namewb) like '%"+queryName+"%'  ");
			sb.append(" or upper(t.TRADE_NAME) like '%"+queryName+"%' ) ");
		} 
		sb.append(" GROUP BY R.medicalrecord_id ,R.BEDINFO_ID,R.PATIENT_NAME,T.TRADE_NAME,T.SPECS,T.DOSE_ONCE,T.DOSE_UNIT,T.DFQ_FREQ,T.USE_NAME,  ");
		sb.append(" t.apply_num ,T.DEPT_CODE,T.DRUG_DEPT_CODE,T.BILLCLASS_CODE,T.VALID_STATE,s.drug_namepinyin ,s.drug_namewb ,T.DRUGED_BILL,T.PRINT_DATE ORDER BY r.bedinfo_id,r.patient_name  ");
		return sb.toString();
	}
	/***
	 * 摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 */
	@Override
	public List<BillClassHzVo> getBillClassHzVo(String drugedBill, String applyState,String bname,String page,String rows) throws Exception{
		String hql=this.getBillClassHzHql(drugedBill,bname, applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("drugName").addScalar("specs").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode")
				.addScalar("drugDeptCode").addScalar("billClassName").addScalar("validState")
				.addScalar("drugPinYin").addScalar("drugWb");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<BillClassHzVo> operaArragVoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(BillClassHzVo.class)).list();
		return operaArragVoList;
	}
	/***
	 * 摆药单明细信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassMxVo>
	 */
	@Override
	public List<BillClassMxVo> getBillClassMxVo(String drugedBill, String applyState,String bname,String page,String rows) throws Exception{
		String hql=this.getBillClassMxHql(drugedBill,bname, applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("bedName").addScalar("patientName").addScalar("inpatientNo").addScalar("tradeName")
				.addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit").addScalar("dfqFerq")
				.addScalar("useName").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode").addScalar("drugDeptCode")
				.addScalar("billClassName").addScalar("validState").addScalar("drugPinYin").addScalar("drugWb")
				.addScalar("state").addScalar("drugedBill").addScalar("printDate",Hibernate.DATE);
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<BillClassMxVo> operaArragVoList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(BillClassMxVo.class)).list();
		return operaArragVoList;
	}
	/**
	 * @Description:根据条件查询摆药单汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getBillHzTotal(String drugedBill, String bname,String applyState) throws Exception{
		String hql=this.getBillClassHzHql(drugedBill, bname,applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("drugName").addScalar("specs").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode")
				.addScalar("drugDeptCode").addScalar("billClassName").addScalar("validState")
				.addScalar("drugPinYin").addScalar("drugWb");
		List<BillClassHzVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(BillClassHzVo.class)).list();
		return list.size();
	}
	/**
	 * @Description:根据条件查询摆药单明细记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getBillMxTotal(String drugedBill, String bname,String applyState) throws Exception{
		String hql=this.getBillClassMxHql(drugedBill, bname,applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("bedName").addScalar("patientName").addScalar("inpatientNo").addScalar("tradeName")
				.addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit").addScalar("dfqFerq")
				.addScalar("useName").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode").addScalar("drugDeptCode")
				.addScalar("billClassName").addScalar("validState").addScalar("drugPinYin").addScalar("drugWb")
				.addScalar("state").addScalar("drugedBill").addScalar("printDate",Hibernate.DATE);
		List<BillClassMxVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(BillClassMxVo.class)).list();
		return list.size();
	}

	@Override
	public List<BillClassHzVo> getAllBillClassHzVo(String drugedBill, String applyState, String bname) throws Exception{
		String hql=this.getBillClassHzHql(drugedBill,bname, applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("drugName").addScalar("specs").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode")
				.addScalar("drugDeptCode").addScalar("billClassName").addScalar("validState")
				.addScalar("drugPinYin").addScalar("drugWb");
		List<BillClassHzVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(BillClassHzVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<BillClassHzVo>();
	}

	@Override
	public List<BillClassMxVo> getAllBillClassMxVo(String drugedBill, String applyState, String bname) throws Exception{
		String hql=this.getBillClassMxHql(drugedBill,bname, applyState);
		SQLQuery queryObject = this.getSession().createSQLQuery(hql)
				.addScalar("bedName").addScalar("patientName").addScalar("inpatientNo").addScalar("tradeName")
				.addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit").addScalar("dfqFerq")
				.addScalar("useName").addScalar("drugedNum",Hibernate.DOUBLE).addScalar("deptCode").addScalar("drugDeptCode")
				.addScalar("billClassName").addScalar("validState").addScalar("drugPinYin").addScalar("drugWb")
				.addScalar("state").addScalar("drugedBill").addScalar("printDate",Hibernate.DATE);
		List<BillClassMxVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(BillClassMxVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<BillClassMxVo>();
	}
}
