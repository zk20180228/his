package cn.honry.statistics.drug.patientDispensingOut.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.statistics.drug.patientDispensingOut.dao.PatientDispensingOutDAO;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("patientDispensingOutDAO")
@SuppressWarnings({ "all" })
public class PatientDispensingOutDAOImpl extends HibernateEntityDao<DrugApplyout> implements PatientDispensingOutDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<InpatientInfoNow> queryPatient(String deptId,String type,String flag) throws Exception {
		String sql = "select i.INPATIENT_NO as inpatientNo,i.PATIENT_NAME as patientName,i.REPORT_SEX as reportSex,i.BED_NAME as bedName,i.IN_STATE as inState,"
				+ "i.MEDICALRECORD_ID as medicalrecordId,i.PACT_CODE as pactCode from T_INPATIENT_INFO_NOW i where 1=1 ";
		if("1".equals(flag)){//1在院   2出院
			if("N".equals(type)){
				sql += " and i.IN_STATE='I' and i.NURSE_CELL_CODE=:deptId";
			}else{
				sql += " and i.IN_STATE='I' and i.dept_code=(select tdc.DEPT_ID "
						+ "from T_DEPARTMENT_CONTACT tdc "
						+ "where tdc.id in (select dc.PARDEPT_ID "
						+ "from T_DEPARTMENT_CONTACT dc "
						+ "where dc.DEPT_ID =:deptId "
						+ "and dc.REFERENCE_TYPE = '03'))";
			}
		}else if("2".equals(flag)){
			if("N".equals(type)){
				sql += " and i.IN_STATE='O' and i.NURSE_CELL_CODE=:deptId "
						+ " union all "
						+ " select y.INPATIENT_NO as inpatientNo,y.PATIENT_NAME as patientName,y.REPORT_SEX as reportSex,y.BED_NAME as bedName,y.IN_STATE as inState,y.MEDICALRECORD_ID as medicalrecordId,y.PACT_CODE as pactCode from T_INPATIENT_INFO y "
						+ " where y.IN_STATE='O' and y.NURSE_CELL_CODE=:deptId ";
			}else{
				sql += " and i.IN_STATE='O' and i.dept_code=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) "
						+ " union all "
						+ " select y.INPATIENT_NO as inpatientNo,y.PATIENT_NAME as patientName,y.REPORT_SEX as reportSex,y.BED_NAME as bedName,y.IN_STATE as inState,y.MEDICALRECORD_ID as medicalrecordId,y.PACT_CODE as pactCode from T_INPATIENT_INFO y "
						+ " where y.IN_STATE='O' and y.dept_code=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) ";
			}
		}else  if("12".equals(flag)){
			if("N".equals(type)){
				sql += " and (i.IN_STATE='I' or i.IN_STATE='O') and i.NURSE_CELL_CODE=:deptId "
						+ " union all "
						+ " select y.INPATIENT_NO as inpatientNo,y.PATIENT_NAME as patientName,y.REPORT_SEX as reportSex,y.BED_NAME as bedName,y.IN_STATE as inState,y.MEDICALRECORD_ID as medicalrecordId,y.PACT_CODE as pactCode from T_INPATIENT_INFO y "
						+ " where (y.IN_STATE='I' or y.IN_STATE='O')  and y.NURSE_CELL_CODE=:deptId ";
			}else{
				sql += " and (i.IN_STATE='I' or i.IN_STATE='O') and i.dept_code=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) "
						+ " union all "
						+ " select y.INPATIENT_NO as inpatientNo,y.PATIENT_NAME as patientName,y.REPORT_SEX as reportSex,y.BED_NAME as bedName,y.IN_STATE as inState,y.MEDICALRECORD_ID as medicalrecordId,y.PACT_CODE as pactCode from T_INPATIENT_INFO y "
						+ " where (y.IN_STATE='I' or y.IN_STATE='O') and y.dept_code=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) ";
			}
		}else{
			return new ArrayList<InpatientInfoNow>();
		}
		SQLQuery queryObject = (SQLQuery) super.getSession().createSQLQuery(sql).addScalar("inpatientNo").addScalar("medicalrecordId").addScalar("pactCode")
				.addScalar("patientName").addScalar("reportSex").addScalar("bedName").addScalar("inState").setParameter("deptId", deptId);
		List<InpatientInfoNow> iList = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public SysDepartment queryState(String deptId) throws Exception {
		String hql="from SysDepartment d where d.deptCode='"+deptId+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
		
	}
	@Override
	public List<VinpatientApplyout> queryVinpatientApplyout(List<String> tnL,String deptId,
			String type, String page, String rows,String tradeName,String inpatientNo,String etime,String stime,String flag,String sign) throws Exception {
		final StringBuffer sb = new StringBuffer();
		stime = stime+" 00:00:00";
		etime = etime+" 23:59:59";
		sb.append("select  rownum as n,t.DRUG_CODE as drugCode,t.trade_name as drugName,t.specs as specs,t.DFQ_CEXP as dfqFreq,"
				+ "t.USE_NAME as usageCode,t.apply_num as applyNum,t.dose_unit as doseUnit,"
				+ "t.drug_dept_code as drugDeptCode,DECODE(T.send_type, 1, '集中发送', 2, '临时发送', '全部') as sendType,"
				+ "t.billclass_code as billclassCode,t.apply_opercode as applyOpercode,t.apply_date as applyDate, t.print_empl as printEmpl,"
				+ "t.print_date as printDate,DECODE(T.valid_state, 1, '有效', '无效') as validState,tt.patient_name as patientName,"
				+ "DECODE(T.APPLY_STATE, 2, '已摆', '未摆') as baiyao from T_DRUG_APPLYOUT_NOW t join t_inpatient_info_now tt on tt.inpatient_no = t.patient_id"
				+ " where 1=1 ");
		if(StringUtils.isNotBlank(deptId)){
			if("N".equals(type)){
				sb.append(" and tt.nurse_cell_code='"+deptId+"' ");
			}else{
				sb.append(" and tt.dept_code= (select tdc.DEPT_ID from T_DEPARTMENT_CONTACT tdc "
						+ "where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID ='"+deptId+"' "
						+ " and dc.REFERENCE_TYPE = '03'))");
			}
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and tt.inpatient_no='"+inpatientNo+"' ");
		}
		if(StringUtils.isNotBlank(tradeName)){
			sb.append(" and t.TRADE_NAME like '%"+tradeName+"%' ");
		}
		if(StringUtils.isNotBlank(stime)){
			sb.append(" and t.apply_date >=  to_date('"+stime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(etime)){
			sb.append(" and t.apply_date <=  to_date('"+etime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if("O".equals(flag)){//出院
			int total = super.getSqlTotal(sb.toString());
			
			
			if(total>0){
				/**判断sign  是否查询全部还是分页查询**/
				String s=null;
				Map<String, Object> paraMap = new HashMap<String, Object>();
				if(StringUtils.isBlank(sign)){
				 s="select * from (select * from("+sb.toString()+" )where  n <= (:page) * :row ) where n > (:page -1) * :row  ";
				int start = Integer.parseInt(page == null ? "1" : page);
				int count = Integer.parseInt(rows == null ? "20" : rows);
				
				paraMap.put("page", start);
				paraMap.put("row", count);
				}else{
					s="select * from ("+sb.toString()+")";
				}
				List<VinpatientApplyout> list =  namedParameterJdbcTemplate.query(s,paraMap,new RowMapper<VinpatientApplyout>() {
					@Override
					public VinpatientApplyout mapRow(ResultSet rs, int rowNum) throws SQLException {
						VinpatientApplyout vo = new VinpatientApplyout();
						vo.setDrugCode(rs.getString("drugName"));
						vo.setSpecs(rs.getString("specs"));
						vo.setDfqFreq(rs.getString("dfqFreq"));
						vo.setUsageCode(rs.getString("usageCode"));
						vo.setApplyNum(rs.getDouble("applyNum"));
						vo.setDoseUnit(rs.getString("doseUnit"));
						vo.setDrugDeptCode(rs.getString("drugDeptCode"));
						vo.setSendType(rs.getString("sendType"));
						vo.setBillclassCode(rs.getString("billclassCode"));
						vo.setApplyOpercode(rs.getString("applyOpercode"));
						vo.setApplyDate(rs.getTimestamp("applyDate"));
						vo.setPrintEmpl(rs.getString("printEmpl"));
						vo.setPrintDate(rs.getTimestamp("printDate"));
						vo.setValidState(rs.getString("validState"));
						vo.setPatientName(rs.getString("patientName"));
						vo.setBaiyao(rs.getString("baiyao"));
						return vo;
					}
					
				});
				return list;
			}else{
				sb.delete(0, sb.length());
				sb.append("select t.DRUG_CODE as drugCode,  t.trade_name as drugName,t.specs as specs,t.DFQ_CEXP as dfqFreq,"
				+ "t.USE_NAME as usageCode,t.apply_num as applyNum,t.dose_unit as doseUnit,"
				+ "t.drug_dept_code as drugDeptCode,DECODE(T.send_type, 1, '集中发送', 2, '临时发送', '全部') as sendType,"
				+ "t.billclass_code as billclassCode,t.apply_opercode as applyOpercode,t.apply_date as applyDate, t.print_empl as printEmpl,"
				+ "t.print_date as printDate,DECODE(T.valid_state, 1, '有效', '无效') as validState,tt.patient_name as patientName,"
				+ "DECODE(T.APPLY_STATE, 2, '已摆', '未摆') as baiyao from T_DRUG_APPLYOUT t join t_inpatient_info tt on tt.inpatient_no = t.patient_id"
				+ " where 1=1 ");
				if(StringUtils.isNotBlank(deptId)){
					if("N".equals(type)){
						sb.append(" and tt.nurse_cell_code='"+deptId+"' ");
					}else{
						sb.append(" and tt.dept_code= (select tdc.DEPT_ID from T_DEPARTMENT_CONTACT tdc "
								+ "where tdc.id in (select dc.PARDEPT_ID "
								+ " from T_DEPARTMENT_CONTACT dc "
								+ " where dc.DEPT_ID ='"+deptId+"' "
								+ " and dc.REFERENCE_TYPE = '03'))");
					}
				}
				if(StringUtils.isNotBlank(inpatientNo)){
					sb.append(" and tt.inpatient_no='"+inpatientNo+"' ");
				}
				if(StringUtils.isNotBlank(tradeName)){
					sb.append(" and t.TRADE_NAME like '%"+tradeName+"%' ");
				}
				if(StringUtils.isNotBlank(stime)){
					sb.append(" and t.apply_date >=  to_date('"+stime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				}
				if(StringUtils.isNotBlank(etime)){
					sb.append(" and t.apply_date <=  to_date('"+etime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				}
			}
		}
		String s=null;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(sign)){
		 s="select * from (select * from("+sb.toString()+" ) n where  n <= (:page) * :row ) where n > (:page -1) * :row  ";
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("row", count);
		}else{
			s="select * from ("+sb.toString()+")";
		}
		
		List<VinpatientApplyout> list =  namedParameterJdbcTemplate.query(s,paraMap,new RowMapper<VinpatientApplyout>() {
			@Override
			public VinpatientApplyout mapRow(ResultSet rs, int rowNum) throws SQLException {
				VinpatientApplyout vo = new VinpatientApplyout();
				vo.setDrugCode(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setDfqFreq(rs.getString("dfqFreq"));
				vo.setUsageCode(rs.getString("usageCode"));
				vo.setApplyNum(rs.getDouble("applyNum"));
				vo.setDoseUnit(rs.getString("doseUnit"));
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				vo.setSendType(rs.getString("sendType"));
				vo.setBillclassCode(rs.getString("billclassCode"));
				vo.setApplyOpercode(rs.getString("applyOpercode"));
				vo.setApplyDate(rs.getTimestamp("applyDate"));
				vo.setPrintEmpl(rs.getString("printEmpl"));
				vo.setPrintDate(rs.getTimestamp("printDate"));
				vo.setValidState(rs.getString("validState"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setBaiyao(rs.getString("baiyao"));
				return vo;
			}
			
		});
		return list;
	}
	@Override
	public int qqueryVinpatientApplyoutTotal(List<String> tnL,String deptId, String type,String tradeName,String inpatientNo,String etime,String stime,String flag)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append("select count(drugCode) from( ");
		sb.append("select t.DRUG_CODE as drugCode,t.specs as specs,t.DFQ_FREQ as dfqFreq,"
				+ "t.USAGE_CODE as usageCode,t.apply_num as applyNum,t.dose_unit as doseUnit,"
				+ "t.drug_dept_code as drugDeptCode,DECODE(T.send_type, 1, '集中发送', 2, '临时发送', '全部') as sendType,"
				+ "t.billclass_code as billclassCode,t.apply_opercode as applyOpercode,t.apply_date as applyDate, t.print_empl as printEmpl,"
				+ "t.print_date as printDate,DECODE(T.valid_state, 1, '有效', '无效') as validState,tt.patient_name as patientName,"
				+ "DECODE(T.APPLY_STATE, 2, '已摆', '未摆') as baiyao from T_DRUG_APPLYOUT_NOW t join t_inpatient_info_now tt on tt.inpatient_no = t.patient_id"
				+ " where 1=1 ");
		if(StringUtils.isNotBlank(deptId)){
			if("N".equals(type)){
				sb.append(" and tt.nurse_cell_code='"+deptId+"' ");
			}else{
				sb.append(" and tt.dept_code= (select tdc.DEPT_ID from T_DEPARTMENT_CONTACT tdc "
						+ "where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID ='"+deptId+"' "
						+ " and dc.REFERENCE_TYPE = '03'))");
			}
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			sb.append(" and tt.inpatient_no='"+inpatientNo+"' ");
		}
		if(StringUtils.isNotBlank(tradeName)){
			sb.append(" and t.TRADE_NAME like '%"+tradeName+"%' ");
		}
		if(StringUtils.isNotBlank(stime)){
			sb.append(" and t.apply_date >=  to_date('"+stime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNotBlank(etime)){
			sb.append(" and t.apply_date <=  to_date('"+etime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
		}
		sb.append(")");
		if("2".equals(flag)){//出院
			int total = super.getSqlTotal(sb.toString());		
			if(total>0){
				return total;
			}else{
				sb.delete(0, sb.length());
				sb.append("select t.DRUG_CODE as drugCode,t.specs as specs,t.DFQ_FREQ as dfqFreq,"
				+ "t.USAGE_CODE as usageCode,t.apply_num as applyNum,t.dose_unit as doseUnit,"
				+ "t.drug_dept_code as drugDeptCode,DECODE(T.send_type, 1, '集中发送', 2, '临时发送', '全部') as sendType,"
				+ "t.billclass_code as billclassCode,t.apply_opercode as applyOpercode,t.apply_date as applyDate, t.print_empl as printEmpl,"
				+ "t.print_date as printDate,DECODE(T.valid_state, 1, '有效', '无效') as validState,tt.patient_name as patientName,"
				+ "DECODE(T.APPLY_STATE, 2, '已摆', '未摆') as baiyao from T_DRUG_APPLYOUT t join t_inpatient_info tt on tt.inpatient_no = t.patient_id"
				+ " where 1=1 ");
				if(StringUtils.isNotBlank(deptId)){
					if("N".equals(type)){
						sb.append(" and tt.nurse_cell_code='"+deptId+"' ");
					}else{
						sb.append(" and tt.dept_code= (select tdc.DEPT_ID from T_DEPARTMENT_CONTACT tdc "
								+ "where tdc.id in (select dc.PARDEPT_ID "
								+ " from T_DEPARTMENT_CONTACT dc "
								+ " where dc.DEPT_ID ='"+deptId+"' "
								+ " and dc.REFERENCE_TYPE = '03'))");
					}
				}
				if(StringUtils.isNotBlank(inpatientNo)){
					sb.append(" and tt.inpatient_no='"+inpatientNo+"' ");
				}
				if(StringUtils.isNotBlank(tradeName)){
					sb.append(" and t.TRADE_NAME like '%"+tradeName+"%' ");
				}
				if(StringUtils.isNotBlank(stime)){
					sb.append(" and t.apply_date >=  to_date('"+stime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				}
				if(StringUtils.isNotBlank(etime)){
					sb.append(" and t.apply_date <=  to_date('"+etime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				}
			}
		}
		return jdbcTemplate.queryForObject(sb.toString(), java.lang.Integer.class);
	}
	@Override
	public SysDepartment querySysDepartment(String id)  throws Exception{
		String hql=" from SysDepartment c where c.stop_flg=0 and c.del_flg=0 and c.deptCode='"+id+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new SysDepartment();
	}
	@Override
	public User queryUser(String id)  throws Exception{
		String hql=" from User c where c.stop_flg=0 and c.del_flg=0 and c.account='"+id+"'";
		List<User> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new User();
	}
	@Override
	public DrugBillclass queryDrugBillclass(String id)  throws Exception{
		String hql=" from DrugBillclass c where c.stop_flg=0 and c.del_flg=0 and c.id='"+id+"'";
		List<DrugBillclass> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
       return new DrugBillclass();
	}
	
	

	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: wsj
	 * @CreateDate: 2016年12月02日 
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin()  throws Exception{
		final String sql = "SELECT MAX(mn.APPLY_DATE) AS eTime ,MIN(mn.APPLY_DATE) AS sTime FROM T_DRUG_APPLYOUT_NOW mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	@Override
	public List<InpatientInfoNow> querypatient(String flag,String medicalrecordId)  throws Exception{
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String hql = "";
		String deptId = dept.getDeptCode();
		if("1".equals(flag)){
			hql = "from InpatientInfoNow e where e.del_flg = 0 and e.stop_flg=0 and  e.medicalrecordId = :medicalrecordId and e.inState='I' ";
			if("N".equals(dept.getDeptType())){
				hql+=" and e.nurseCellCode=:deptId ";
			}else{
				hql+=" and e.deptCode=(select tdc.deptId "
						+ "from DepartmentContact tdc "
						+ "where tdc.id in (select dc.pardeptId "
						+ "from DepartmentContact dc "
						+ "where dc.deptId =:deptId "
						+ "and dc.referenceType = '03'))";
			}
		}else if("2".equals(flag)){
			hql = "from InpatientInfoNow e where e.del_flg = 0 and e.stop_flg=0 and  e.medicalrecordId = :medicalrecordId and e.inState='O' ";
			if("N".equals(dept.getDeptType())){
				hql+=" and e.nurseCellCode=:deptId ";
			}else{
				hql+=" and e.deptCode=(select tdc.deptId "
						+ "from DepartmentContact tdc "
						+ "where tdc.id in (select dc.pardeptId "
						+ "from DepartmentContact dc "
						+ "where dc.deptId =:deptId "
						+ "and dc.referenceType = '03'))";
			}
			int total = super.getTotal(hql);
			if(total==0){
				hql = "from InpatientInfo e where e.del_flg = 0 and e.stop_flg=0 and  e.medicalrecordId = :medicalrecordId and e.inState='O' ";
				if("N".equals(dept.getDeptType())){
					hql+=" and e.nurseCellCode=:deptId ";
				}else{
					hql+=" and e.deptCode=(select tdc.deptId "
							+ "from DepartmentContact tdc "
							+ "where tdc.id in (select dc.pardeptId "
							+ "from DepartmentContact dc "
							+ "where dc.deptId =:deptId "
							+ "and dc.referenceType = '03'))";
				}
			}
		}else if("12".equals(flag)){
			hql = "from InpatientInfoNow e where e.del_flg = 0 and e.stop_flg=0 and  e.medicalrecordId = :medicalrecordId  ";
			if("N".equals(dept.getDeptType())){
				hql+=" and e.nurseCellCode=:deptId ";
			}else{
				hql+=" and e.deptCode=(select tdc.deptId "
						+ "from DepartmentContact tdc "
						+ "where tdc.id in (select dc.pardeptId "
						+ "from DepartmentContact dc "
						+ "where dc.deptId =:deptId "
						+ "and dc.referenceType = '03'))";
			}
			int total = super.getTotal(hql);
			if(total==0){
				hql = "from InpatientInfo e where e.del_flg = 0 and e.stop_flg=0 and  e.medicalrecordId = :medicalrecordId  ";
				if("N".equals(dept.getDeptType())){
					hql+=" and e.nurseCellCode=:deptId ";
				}else{
					hql+=" and e.deptCode=(select tdc.deptId "
							+ "from DepartmentContact tdc "
							+ "where tdc.id in (select dc.pardeptId "
							+ "from DepartmentContact dc "
							+ "where dc.deptId =:deptId "
							+ "and dc.referenceType = '03'))";
				}
			}
		}
		Query qureySql=this.getSession().createQuery(hql).setParameter("medicalrecordId", medicalrecordId).setParameter("deptId", deptId);
		List<InpatientInfoNow> InpatientInfoist=qureySql.list();
		if(InpatientInfoist!=null && InpatientInfoist.size()>0){
			return InpatientInfoist;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List queryUnitMap()  throws Exception{
		String sql="select t.CODE_ENCODE,t.CODE_NAME from  t_business_dictionary t where t.CODE_TYPE='drugPackagingunit'";
		List list = this.getSession().createSQLQuery(sql).list();
		return list;
	}
}
