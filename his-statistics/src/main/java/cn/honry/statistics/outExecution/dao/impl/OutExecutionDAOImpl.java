package cn.honry.statistics.outExecution.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.leaveOrder.vo.InpatientExecDrugVo;
import cn.honry.statistics.leaveOrder.vo.InpatientExecUnDrugVo;
import cn.honry.statistics.outExecution.dao.OutExecutionDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("outExecutionDAO")
@SuppressWarnings({ "all" })
public class OutExecutionDAOImpl extends HibernateEntityDao<InpatientExecDrugVo> implements OutExecutionDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**查询住院信息   标识 已出院    
	 * GH
	 * @param queryLsh  住院流水
	 * @param queryBlh  病历号或
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public List<InpatientInfoNow> queryInfoNows(String queryBlh,String queryLsh,String startTime, String endTime) {
		StringBuffer sqlnew=new StringBuffer();
		StringBuffer sqlold=new StringBuffer();
		sqlnew.append("select t.INPATIENT_NO as inpatientNo,t.OUT_DATE as outDate,t.IN_DATE as inDate from T_INPATIENT_INFO_NOW t where t.IN_STATE ='O' and t.stop_flg=0 and t.del_flg=0 ");
		sqlold.append("select t1.INPATIENT_NO as inpatientNo,t1.OUT_DATE as outDate,t1.IN_DATE as inDate  from T_INPATIENT_INFO t1 where t1.IN_STATE ='O' and t1.stop_flg=0 and t1.del_flg=0 ");
		
		if(StringUtils.isNotBlank(queryBlh)){
			sqlnew.append(" and  t.MEDICALRECORD_ID='"+queryBlh+"' ");
			sqlold.append(" and  t1.MEDICALRECORD_ID='"+queryBlh+"' ");
		}
		if(StringUtils.isNotBlank(queryLsh)){
			sqlnew.append(" and t.inpatient_No='"+queryLsh+"' ");
			sqlold.append(" and t1.inpatient_No='"+queryLsh+"' ");
		}
		if(StringUtils.isNotBlank(startTime)){
			sqlnew.append("and t.mo_Date >=to_date('"+startTime+"','yyyy-mm-dd')");
			sqlold.append("and t1.mo_Date >=to_date('"+startTime+"','yyyy-mm-dd')");
		}
		if(StringUtils.isNotBlank(endTime)){
			sqlnew.append("and t.mo_Date <to_date('"+endTime+"','yyyy-mm-dd')");
			sqlold.append("and t1.mo_Date <to_date('"+endTime+"','yyyy-mm-dd')");
		}
		sqlnew.append(" union all ");
		sqlnew.append(sqlold.toString());
		SQLQuery query=this.getSession().createSQLQuery(sqlnew.toString());
		query.addScalar("inpatientNo").addScalar("outDate",Hibernate.DATE).addScalar("inDate",Hibernate.DATE);
		query.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class));
		return query.list();
	}
	/**查询医嘱列表  药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public int queryDrugTotal(String queryName, String startTime, String endTime) {
		String hql="from InpatientExecdrugNow t where t.stop_flg=0 and t.del_flg=0 ";
		String hql1="from InpatientExecdrug t where t.stop_flg=0 and t.del_flg=0 ";
		if(StringUtils.isNotBlank(queryName)){
			hql+=" and t.inpatientNo in("+queryName+")";
			hql1+=" and t.inpatientNo in("+queryName+")";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+="and t.moDate >=to_date('"+startTime+"','yyyy-mm-dd')";
			hql1+="and t.moDate >=to_date('"+startTime+"','yyyy-mm-dd')";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+="and t.moDate <to_date('"+endTime+"','yyyy-mm-dd')";
			hql1+="and t.moDate <to_date('"+endTime+"','yyyy-mm-dd')";
		}
		int old=this.getTotal(hql1);
		int now = this.getTotal(hql);
		return now+old;
	}
	/**查询医嘱列表
	 * GH
	 * @param queryName  住院流水号
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	@Override
	public List<InpatientExecDrugVo> queryDrugList(String queryName, String startTime, String endTime,String page, String rows) {
		StringBuffer hql=new StringBuffer();
		StringBuffer hql1=new StringBuffer();
		
		
		hql.append("select t.INPATIENT_NO as inpatientNo,t.PATIENT_NO as patientNo,t.DRUG_NAME as drugName,");
		hql.append("t.DOSE_UNIT as doseUnit,t.MIN_UNIT as minUnit,t.QTY_TOT as qtyTot,t.PACK_QTY AS packqty,");
		hql.append("t.ITEM_PRICE as itemPrice,t.FREQUENCY_NAME AS frequencyName,t.EXEC_DATE as execDate,t1.BILL_NAME as billName");
		hql.append(" from T_INPATIENT_EXECDRUG_now t  ");
		hql.append(" left join t_inpatient_drugbilldetail t1 on t.TYPE_CODE =t1.type_code and t.USAGE_CODE = t1.USAGE_CODE and t.DRUG_TYPE = t1.DRUG_TYPE  ");
		hql.append(" where t.stop_flg=0 and t.del_flg=0 ");
		
		hql1.append("select t.INPATIENT_NO as inpatientNo,t.PATIENT_NO as patientNo,t.DRUG_NAME as drugName,");
		hql1.append("t.DOSE_UNIT as doseUnit,t.MIN_UNIT as minUnit,t.QTY_TOT as qtyTot,t.PACK_QTY AS packqty,");
		hql1.append("t.ITEM_PRICE as itemPrice,t.FREQUENCY_NAME AS frequencyName,t.EXEC_DATE as execDate,t1.BILL_NAME as billName");
		hql1.append(" from T_INPATIENT_EXECDRUG t  ");
		hql1.append(" left join t_inpatient_drugbilldetail t1 on t.TYPE_CODE =t1.type_code and t.USAGE_CODE = t1.USAGE_CODE and t.DRUG_TYPE = t1.DRUG_TYPE  ");
		hql1.append(" where t.stop_flg=0 and t.del_flg=0 ");
		
		if(StringUtils.isNotBlank(queryName)){
			hql.append(" and t.INPATIENT_NO in("+queryName+")");
			hql1.append(" and t.INPATIENT_NO in("+queryName+")");
		} 
		if(StringUtils.isNotBlank(startTime)){
			hql.append("and t.EXEC_DATE >=to_date('"+startTime+"','yyyy-mm-dd')");
			hql1.append("and t.EXEC_DATE >=to_date('"+startTime+"','yyyy-mm-dd')");
		}
		if(StringUtils.isNotBlank(endTime)){
			hql.append("and t.EXEC_DATE <to_date('"+endTime+"','yyyy-mm-dd')");
			hql1.append("and t.EXEC_DATE <to_date('"+endTime+"','yyyy-mm-dd')");
		}
		
		hql.append(" union all "+hql1.toString());
		SQLQuery query=this.getSession().createSQLQuery(hql.toString());
		hql.append("select t.INPATIENT_NO as inpatientNo,t.PATIENT_NO as patientNo,t.DRUG_NAME as drugName,");
		hql.append("t.DOSE_UNIT as doseUnit,t.MIN_UNIT as minUnit,t.QTY_TOT as qtyTot,t.PACK_QTY AS packqty,");
		hql.append("t.ITEM_PRICE as itemPrice,t.FREQUENCY_NAME AS frequencyName,t1.BILL_NAME as billName");
		
		query.addScalar("execDate",Hibernate.DATE).addScalar("inpatientNo").addScalar("patientNo")
		.addScalar("drugName").addScalar("doseUnit").addScalar("minUnit")
		.addScalar("qtyTot",Hibernate.DOUBLE).addScalar("packQty",Hibernate.INTEGER).addScalar("itemPrice",Hibernate.DOUBLE)
		.addScalar("frequencyName").addScalar("billName");
		query.setResultTransformer(Transformers.aliasToBean(InpatientExecDrugVo.class));
		return query.list();
	}
	
	
	
	/**查询医嘱列表  非药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public int queryUnDrugTotal(String queryName, String startTime,
			String endTime) {
		String hql="from InpatientExecUndrugNow t where t.stop_flg=0 and t.del_flg=0 ";
		String hql1="from InpatientExecUndrug t where t.stop_flg=0 and t.del_flg=0 ";
		if(StringUtils.isNotBlank(queryName)){
			hql+=" and t.inpatientNo in("+queryName+")";
			hql1+=" and t.inpatientNo in("+queryName+")";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+="and t.moDate >=to_date('"+startTime+"','yyyy-mm-dd')";
			hql1+="and t.moDate >=to_date('"+startTime+"','yyyy-mm-dd')";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+="and t.moDate <to_date('"+endTime+"','yyyy-mm-dd')";
			hql1+="and t.moDate <to_date('"+endTime+"','yyyy-mm-dd')";
		}
		int old=this.getTotal(hql1);
		int now = this.getTotal(hql);
		return now+old;
	}
	
	/**查询医嘱列表 非  药品 总数据
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public List<InpatientExecUnDrugVo> queryUnDrugList(String queryName,String startTime, String endTime, String page, String rows) {
		StringBuffer hql=new StringBuffer();
		StringBuffer hql1=new StringBuffer();
		
		hql.append("select t.INPATIENT_NO as inpatientNo,t.PATIENT_NO as patientNo,t.UNDRUG_NAME as undrugName,");
		hql.append("t.CLASS_NAME as className,t.EXEC_DPNM as execDpnm,t.QTY_TOT as qtyTot,t.STOCK_UNIT AS stockUnit,t1.BILL_NAME as billName,");
		hql.append("t.EXEC_DATE as execDate");
		hql.append(" from T_INPATIENT_EXECUNDRUG_now t  ");
		hql.append(" left join t_inpatient_drugbilldetail t1 on t.TYPE_CODE =t1.type_code and t.UNDRUG_CODE = t1.USAGE_CODE and t.CLASS_CODE = t1.DRUG_TYPE  ");
		hql.append(" where t.stop_flg=0 and t.del_flg=0 ");
		
		
		hql1.append("select t.INPATIENT_NO as inpatientNo,t.PATIENT_NO as patientNo,t.UNDRUG_NAME as undrugName,");
		hql1.append("t.CLASS_NAME as className,t.EXEC_DPNM as execDpnm,t.QTY_TOT as qtyTot,t.STOCK_UNIT AS stockUnit,t1.BILL_NAME as billName,");
		hql1.append("t.EXEC_DATE as execDate");
		hql1.append(" from T_INPATIENT_EXECUNDRUG t  ");
		hql1.append(" left join t_inpatient_drugbilldetail t1 on t.TYPE_CODE =t1.type_code and t.UNDRUG_CODE = t1.USAGE_CODE and t.CLASS_CODE = t1.DRUG_TYPE  ");
		hql1.append(" where t.stop_flg=0 and t.del_flg=0 ");
		
		if(StringUtils.isNotBlank(queryName)){
			hql.append(" and t.INPATIENT_NO in("+queryName+")");
			hql1.append(" and t.INPATIENT_NO in("+queryName+")");
		} 
		if(StringUtils.isNotBlank(startTime)){
			hql.append("and t.EXEC_DATE >=to_date('"+startTime+"','yyyy-mm-dd')");
			hql1.append("and t.EXEC_DATE >=to_date('"+startTime+"','yyyy-mm-dd')");
		}
		if(StringUtils.isNotBlank(endTime)){
			hql.append("and t.EXEC_DATE <to_date('"+endTime+"','yyyy-mm-dd')");
			hql1.append("and t.EXEC_DATE <to_date('"+endTime+"','yyyy-mm-dd')");
		}
		
		hql.append(" union all "+hql1.toString());
		SQLQuery query=this.getSession().createSQLQuery(hql.toString());
	
		query.addScalar("inpatientNo").addScalar("patientNo").addScalar("undrugName")
		.addScalar("className").addScalar("execDpnm").addScalar("qtyTot",Hibernate.INTEGER)
		.addScalar("stockUnit").addScalar("billName").addScalar("execDate",Hibernate.DATE);
		query.setResultTransformer(Transformers.aliasToBean(InpatientExecUnDrugVo.class));
		return query.list();
	}

	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids, String billName,SysDepartment dept) {
		List<InpatientExecbill> inpatientExecbill=new ArrayList<InpatientExecbill>();
		//获取当前登录的科室,默认加载当前科室所对应的执行单
		String hql = "from InpatientExecbill t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(dept.getDeptCode()==null){
			return inpatientExecbill;
		}else{
			if("N".equals(dept.getDeptType())){
				hql=hql+ " and t.nurseCellCode='"+dept.getDeptCode()+"'";
			}else{
				String hql1 = "select t1.dept_code as deptCode from t_department_contact t1 where t1.id in (select t.pardept_id from t_department_contact t where  t.dept_id='"+dept.getDeptCode()+"' and t.reference_type='03')";
				SQLQuery query = this.getSession().createSQLQuery(hql1.toString());
				query.addScalar("deptCode");	
				query.setResultTransformer(Transformers.aliasToBean(DepartmentContact.class));
				List<DepartmentContact> list = query.list();
				if(list!=null && list.size()>0){
					hql=hql+ " and t.nurseCellCode='"+list.get(0).getDeptCode()+"'";
				}else{
					hql=hql+ " and t.nurseCellCode='"+dept.getDeptCode()+"'";
				}
			}
		}
		if(StringUtils.isNotBlank(ids)){
			hql =hql+" and t.id='"+ids+"'";
		}
		if(StringUtils.isNotBlank(billName)){
			hql =hql+" and t.billName='"+billName+"'";
		}
		inpatientExecbill=this.getSession().createQuery(hql).list();
		if(inpatientExecbill!=null && inpatientExecbill.size()>0){
			return inpatientExecbill;
		}		
		return new ArrayList<InpatientExecbill>();
	}

	@Override
	public List queryDeptList() {
	 DetachedCriteria criteria = DetachedCriteria.forClass(SysDepartment.class);
	 criteria.add(Restrictions.eq("deptType","N"));
	  List<SysDepartment> list = (List<SysDepartment>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}


	//分层展示数据                                                                                                                                            去重后病历号                                   根据病历号查询流水号，姓名
	public Map queryInpatientByDept(String deptCode){//结构 map{..,children[map{},map{},children[map{},map{}]]}
		List list = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		map.put("id",deptCode);
		map.put("text","患者列表");
		map.put("state", "closed");
		String sql="select DISTINCT  B.MEDICALRECORD_ID,B.PATIENT_NAME FROM ( SELECT O.MEDICALRECORD_ID,O.PATIENT_NAME FROM T_INPATIENT_INFO_NOW O "
				+ " WHERE O.IN_STATE = 'O' AND O.Nurse_Cell_Code='"+deptCode+"' union all SELECT N.MEDICALRECORD_ID,N.PATIENT_NAME "
				+ " FROM T_INPATIENT_INFO N where N.IN_STATE = 'O' AND N.Nurse_Cell_Code='"+deptCode+"') B";   
		Query query  = this.getSession().createSQLQuery(sql);
		List list2 = query.list();
		Iterator it = list2.iterator();
		while(it.hasNext()){
			Object[] obj = (Object[]) it.next();
			//病人姓名去重后显示
			Map<String,Object> map1 = new HashMap<>();
			map1.put("id",obj[0].toString());
			map1.put("text",obj[1].toString());
			map1.put("state", "closed");
			list.add(map1);
			//封装下一层子类
			List list3 = new ArrayList<>();
		    //根据病人病历号查询病人流水号显示
			String sql1="select  B.INPATIENT_NO,B.PATIENT_NAME FROM (SELECT O.MEDICALRECORD_ID,O.PATIENT_NAME,O.INPATIENT_NO FROM T_INPATIENT_INFO_NOW O "
					+ " WHERE O.IN_STATE = 'O' AND O.Nurse_Cell_Code='"+deptCode+"' union all SELECT N.MEDICALRECORD_ID,N.PATIENT_NAME,N.INPATIENT_NO "
					+ " FROM T_INPATIENT_INFO N where N.IN_STATE = 'O' AND  N.Nurse_Cell_Code='"+deptCode+"') B WHERE B.MEDICALRECORD_ID="+obj[0].toString();
			Query query1 =this.getSession().createSQLQuery(sql1);
			List list4 = query1.list();
			Iterator it2 = list4.iterator();
			while(it2.hasNext()){
			    Object[] obj1 = (Object[]) it2.next();
			    //显示最下层姓名以及住院流水号
			    Map map2 = new HashMap<>();
			    map2.put("id", obj[0].toString());
			    map2.put("text",obj[1].toString());
			    map2.put("state", "closed");
			    list3.add(map2);
			}
			if(list4==null||list4.size()==0){
				map1.put("state", "open");
			}
			map1.put("children", list3);
		}
		if(list2==null||list2.size()==0){
			map.put("state", "open");
		}
		map.put("children", list);
		return map;
	}
	@Override
	public List queryOneList(List<String> tnL,String id,String deptCode,String startTime,String endTime) {
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(500);
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union ");
				}
				buffer.append("SELECT O"+i+".INPATIENT_NO, O"+i+".PATIENT_NAME,O"+i+".MEDICALRECORD_ID ");
				buffer.append("FROM "+tnL.get(i)+" O"+i+" ");
				if(StringUtils.isNotBlank(id)){
					buffer.append("where O"+i+".MEDICALRECORD_ID ='"+id+"'");
				}else{
					buffer.append("where O"+i+".IN_STATE = 'O' AND O"+i+".Nurse_Cell_Code='"+deptCode+"'");
				}
				buffer.append(" and O"+i+".OUT_DATE>=to_date('"+startTime+"','yyyy-mm-dd HH24:mi:ss') and O"+i+".OUT_DATE<to_date('"+endTime+"','yyyy-mm-dd HH24:mi:ss')");
			}
			Query query = this.getSession().createSQLQuery(buffer.toString());
			List list = query.list();
			if(list.size()>0){
				return list;
			}
		}
		return new ArrayList();
	}

	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids, String billName,
			String deptCode) {
		List<InpatientExecbill> inpatientExecbill=new ArrayList<InpatientExecbill>();
		//获取当前登录的科室,默认加载
        DetachedCriteria criteria = DetachedCriteria.forClass(SysDepartment.class);
        criteria.add(Restrictions.eq("deptCode", deptCode));
        List<SysDepartment>  sd= (List<SysDepartment>) this.getHibernateTemplate().findByCriteria(criteria);
        SysDepartment dept=null;
        if(sd!=null&&sd.size()!=0){
        	dept=sd.get(0);
        }else{
        	 dept=new SysDepartment();
        }
		String hql = "from InpatientExecbill t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(dept.getDeptCode()==null){
			return inpatientExecbill;
		}else{
			if("N".equals(dept.getDeptType())){
				hql=hql+ " and t.nurseCellCode='"+dept.getDeptCode()+"'";
			}
		}
		if(StringUtils.isNotBlank(ids)){
			hql =hql+" and t.id='"+ids+"'";
		}
		if(StringUtils.isNotBlank(billName)){
			hql =hql+" and t.billName='"+billName+"'";
		}
		inpatientExecbill=this.getSession().createQuery(hql).list();
		if(inpatientExecbill!=null && inpatientExecbill.size()>0){
			return inpatientExecbill;
		}		
		return new ArrayList<InpatientExecbill>();
		
	}

	@Override
	public List<InpatientExecundrugNow> queryExecundrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo) {
		String hql=execundrugHql(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
		String [] inpatientNo1 = null;
		if(StringUtils.isBlank(inpatientNo)){
		}else{
			inpatientNo1=inpatientNo.split(",");
		}
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			query.setParameter("beginDate", beginDate);
		 }
		 if(StringUtils.isNotBlank(endDate)){
			 query.setParameter("endDate", endDate);
		 }
		 if(StringUtils.isNotBlank(validFlag)){
			 query.setParameter("validFlag", 1);
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 query.setParameterList("inpatientNo", inpatientNo1);
		 }
		query.setParameter("billNo", billNo);
	    List<InpatientExecundrugNow> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<InpatientExecundrugNow>();
	}

	@Override
	public int queryExecundrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo) {
		String hql="from InpatientExecundrugNow c where 1=1  ";
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		 if("N".equals(department.getDeptType())){
			 hql+=" and c.nurseCellCode='"+department.getDeptCode()+"'";
		 }else{
			 hql+=" and c.deptCode='"+department.getDeptCode()+"'";
		 }
		 if(StringUtils.isNotBlank(beginDate)){
			 hql+=" and c.moDate >= to_date('"+beginDate+"' ,'yyyy-MM-dd hh24:mi:ss') ";
		 }
		 if(StringUtils.isNotBlank(endDate)){
			 hql+=" and c.moDate <= to_date('"+endDate+"' ,'yyyy-MM-dd hh24:mi:ss') ";
		 }
		 if(StringUtils.isNotBlank(validFlag)){
				if("1".equals(validFlag)){
					hql+=" and c.validFlag=1";
				}
				else{
					hql+="  and c.validFlag!=1";
				}
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 inpatientNo = inpatientNo.replace(",", "','");
			 hql+=" and c.inpatientNo in ('"+inpatientNo+"') ";
		 }
		 hql+= " and (c.typeCode,c.undrugCode,c.classCode) in (select b.typeCode,b.usageCode,b.drugType from "
		+ "InpatientDrugbilldetail b where b.del_flg=0 and b.stop_flg=0 and (b.billNo) in "
		+ "(select a.billNo from InpatientExecbill a where a.billNo='"+billNo+"' and a.del_flg=0 and a.stop_flg=0))";
		 
		return super.getTotal(hql);
	}
	public String execundrugHql(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo){
		 String hql="from InpatientExecundrugNow c where 1=1  ";
		 SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		 if("N".equals(department.getDeptType())){
			 hql+=" and c.nurseCellCode='"+department.getDeptCode()+"'";
		 }else{
			 hql+=" and c.deptCode='"+department.getDeptCode()+"'";
		 }
		 if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			 hql+=" and c.moDate >= to_date(:beginDate ,'yyyy-MM-dd hh24:mi:ss') ";
		 }
		 if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			 hql+=" and c.moDate <= to_date(:endDate ,'yyyy-MM-dd hh24:mi:ss') ";
		 }
		 if(StringUtils.isNotBlank(validFlag)){
				if("1".equals(validFlag)){
					hql+=" and c.validFlag=1";
				}
				else{
					hql+="  and c.validFlag!=1";
				}
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 hql+=" and c.inpatientNo in (:inpatientNo) ";
		 }
		 hql+= " and (c.typeCode,c.undrugCode,c.classCode) in (select b.typeCode,b.usageCode,b.drugType from "
		+ "InpatientDrugbilldetail b where b.del_flg=0 and b.stop_flg=0 and (b.billNo) in "
		+ "(select a.billNo from InpatientExecbill a where a.billNo=:billNo and a.del_flg=0 and a.stop_flg=0))";
		return hql;
	}
	
	
	
	//药品执行单查询
	@Override
	public List queryExecdrugpage(List<String> tnL,String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo){ 
		if(tnL.size()==0){
			return new ArrayList();
		}
	String sql=execDrugSql(tnL,billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
	sql="select * from ( select t.*,rownum as rn from( "+sql+"  ) t where rownum <(:page *:rows) ) F where F.rn>=(:page-1)*:rows";
    int start = Integer.parseInt(page==null?"1":page);
	int count = Integer.parseInt(rows==null?"20":rows);
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("page", start);
	map.put("rows", count);
	if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
		map.put("beginDate", beginDate);
	 }
	 if(StringUtils.isNotBlank(endDate)){
		 map.put("endDate", endDate);
	 }
	 if(StringUtils.isNotBlank(validFlag)){
		 map.put("validFlag", 1);
	 }
	 if(StringUtils.isNotBlank(inpatientNo)){
		 map.put("inpatientNo", inpatientNo);
	 }
	 if(StringUtils.isNotBlank(drugedFlag)){
		 map.put("drugedFlag", 3);
	 }
	 map.put("billNo", billNo);
	List<InpatientExecdrugNow> list =  namedParameterJdbcTemplate.query(sql.toString(),map,new RowMapper<InpatientExecdrugNow>() {
		@Override
		public InpatientExecdrugNow mapRow(ResultSet rs, int rowNum) throws SQLException {
			InpatientExecdrugNow vo = new  InpatientExecdrugNow();
			vo.setId(rs.getString("id"));
			vo.setDrugName(rs.getString("drugName"));
			vo.setSpecs(rs.getString("specs"));
			vo.setQtyTot(rs.getDouble("qtyTot"));
			vo.setPriceUnit(rs.getString("priceUnit"));
			vo.setDocName(rs.getString("docName"));
			vo.setValidFlag(rs.getInt("validFlag"));
			vo.setFrequencyName(rs.getString("frequencyName"));
			vo.setMoDate(rs.getTimestamp("moDate"));
			vo.setUseTime(rs.getTimestamp("useTime"));
			vo.setDecoDate(rs.getTimestamp("decoDate"));
			vo.setExecDeptcdName(rs.getString("execDpcd"));
			vo.setExecFlag(rs.getInt("execFlag"));
			vo.setExecPrnflag(rs.getInt("execPrnflag"));
			return vo;
		}
	});
	
   	return list;    	
    	
	}
	public String execDrugSql(List<String> tnL,String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo){
		StringBuffer sql=new StringBuffer(500);
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		sql.append("select * from( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				sql.append(" union all ");
			}
			sql.append(" SELECT E"+i+".ID as id,E"+i+".DRUG_NAME as drugName,");
			sql.append("E"+i+".SPECS as specs,E"+i+".QTY_TOT as qtyTot,E"+i+".PRICE_UNIT as priceUnit,");
			sql.append("E"+i+".DOC_NAME as docName,E"+i+".VALID_FLAG as validFlag,");
			sql.append("E"+i+".FREQUENCY_NAME as frequencyName,E"+i+".MO_DATE as moDate,");
			sql.append("E"+i+".USE_TIME as useTime,E"+i+".DECO_DATE as decoDate,");
			sql.append("E"+i+".EXEC_DPCD_NAME as execDpcd,E"+i+".EXEC_FLAG as execFlag,");
			sql.append("E"+i+".EXEC_PRNFLAG as execPrnflag,E"+i+".NURSE_CELL_CODE as nurseCellCode, ");
			sql.append("E"+i+".DEPT_CODE as deptCode,E"+i+".INPATIENT_NO as inpatientNo,");
			sql.append("E"+i+".TYPE_CODE as typeCode,E"+i+".USAGE_CODE as usageCode,E"+i+".DRUG_TYPE as drugType,");
			sql.append("E"+i+".DRUGED_FLAG as drugedFlag ");
			sql.append(" from "+tnL.get(i)+" E"+i+" ");
		}
		sql.append(" ) C where 1=1 ");
		if("N".equals(department.getDeptType())){
			 sql.append(" and C.nurseCellCode='"+department.getDeptCode()+"'");
		 }else{
			 sql.append(" and C.deptCode='"+department.getDeptCode()+"'");
		 }
		 if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			 sql.append(" and C.moDate>=to_date(:beginDate ,'yyyy-MM-dd hh24:mi:ss') ");
		 }
		 if(StringUtils.isNotBlank(endDate)){
			 sql.append(" and C.moDate<=to_date(:endDate ,'yyyy-MM-dd hh24:mi:ss') ");
		 }
		 if(StringUtils.isNotBlank(validFlag)){
				if("1".equals(validFlag)){
					sql.append(" and C.validFlag=:validFlag");
				}
				else{
					sql.append(" and C.validFlag!=:validFlag");
				}
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 sql.append(" and C.inpatientNo =:inpatientNo ");
		 }
		 if(StringUtils.isNotBlank(drugedFlag)){
			 if("1".equals(drugedFlag)){
				 sql.append(" and C.drugedFlag=:drugedFlag");
			 }
			 else{
				 sql.append("  and C.drugedFlag!=:drugedFlag");
			 }
		 }
		 sql.append( " and (C.typeCode,C.usageCode,C.drugType) in (select b.TYPE_CODE,b.USAGE_CODE,b.DRUG_TYPE from "
		 		+ "T_INPATIENT_DRUGBILLDETAIL b where b.DEL_FLG=0 and b.STOP_FLG=0 and (b.BILL_NO) in "
		 		+ "(select a.BILL_NO from T_INPATIENT_EXECBILL a where a.BILL_NO=:billNo and a.DEL_FLG=0 and a.STOP_FLG=0))");
		return sql.toString();
	}
	@Override
	public int queryExecdrugToatl(List<String> tnL,String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo) {
		String sql=execDrugSql(tnL,billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
		sql="select count(1) as stockMin from ( "+sql+"  )";
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
			map.put("beginDate", beginDate);
		 }
		 if(StringUtils.isNotBlank(endDate)){
			 map.put("endDate", endDate);
		 }
		 if(StringUtils.isNotBlank(validFlag)){
			 map.put("validFlag", 1);
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 map.put("inpatientNo", inpatientNo);
		 }
		 if(StringUtils.isNotBlank(drugedFlag)){
			 map.put("drugedFlag", 3);
		 }
		 map.put("billNo", billNo);
		List<InpatientExecdrugNow> list =  namedParameterJdbcTemplate.query(sql.toString(),map,new RowMapper<InpatientExecdrugNow>() {
			@Override
			public InpatientExecdrugNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientExecdrugNow vo = new  InpatientExecdrugNow();
				vo.setStockMin(rs.getInt("stockMin"));
				return vo;
			}
		});
		if(list.size()>0){
			return list.get(0).getStockMin();
		}
		return 0;
	}
	/**
	 * 医嘱执行单明细  药嘱类型
	 */
	@Override
	public Integer queryDrugBillDetail(String billNo) {
		String sql="SELECT T.BILL_TYPE as type FROM T_INPATIENT_DRUGBILLDETAIL T WHERE T.BILL_NO='"+billNo+"'";
		List<Integer> list =  namedParameterJdbcTemplate.query(sql.toString(),new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer  type=rs.getInt("type");
				return type;
			}
		});
		if(list.size()>0){
			return list.get(0);
		}
		return 0;
	}	
	
}
