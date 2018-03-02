package cn.honry.statistics.finance.coststatistics.dao.impl;

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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.statistics.finance.coststatistics.dao.CostStatisticsDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
/**
 *  病人费用汇总查询Dao实现类
 * @author  lyy
 * @createDate： 2016年6月24日 上午11:03:53 
 * @modifier lyy
 * @modifyDate：2016年6月24日 上午11:03:53
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="costStatisticsDAO")
@SuppressWarnings({"all"})
public class CostStatisticsDAOImpl extends HibernateEntityDao<InpatientFeeInfo> implements CostStatisticsDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setsuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<InpatientFeeInfo> getPageCostStatistics(List<String> tnL, String firstData, String endData, String inpatientNo,String page, String rows) {
		if(StringUtils.isNotBlank(firstData)){
			firstData=firstData+" 00:00:00";
		}
		if(StringUtils.isNotBlank(endData)){
			endData=endData+" 23:59:59";
		}
		String sql=this.sqlHer(tnL,firstData, endData, inpatientNo)+"  order by feeDate desc ";
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		SQLQuery query=this.getSession().createSQLQuery(sql.toString());
		query.addScalar("recipeDeptcode").addScalar("recipeDoccode")
			.addScalar("inpatientNo").addScalar("name")
			.addScalar("feeCode").addScalar("totCost",Hibernate.DOUBLE)
			.addScalar("executeDeptcode").addScalar("feeOpercode").addScalar("feeDate");
		query.setFirstResult((start - 1) * count).setMaxResults(count);
		List<InpatientFeeInfo> list= query.setResultTransformer(Transformers.aliasToBean(InpatientFeeInfo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientFeeInfo>();
	}
	@Override
	public int getTotalCostStatistics(List<String> tnL, String firstData, String endData, String inpatientNo) {
		String sql=" SELECT COUNT(1) AS feeCode FROM ( "+this.sqlHer(tnL,firstData, endData, inpatientNo)+" )";
		Map paraMap=new HashMap();
		if(StringUtils.isNotBlank(firstData)){
			paraMap.put("start", firstData+"  00:00:00");
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("end", endData+"  23:59:59");
		}
		if(StringUtils.isNotBlank(inpatientNo)){
			paraMap.put("inpatient", inpatientNo);
		}
		List<InpatientFeeInfo> list= namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientFeeInfo>() {
			@Override
			public InpatientFeeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientFeeInfo vo = new InpatientFeeInfo();
				vo.setFeeCode(rs.getString("feeCode"));
				return vo;
			}
			
		});
		if(null!=list&&list.size()>0){
			return Integer.parseInt(list.get(0).getFeeCode());
		}
		return 0;
	}
	/**
	 *病人费用查询 （仅自己用）
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午11:17:36 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午11:17:36
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param tnL 
	 */
	private String sqlHer(List<String> tnL, String firstData, String endData, String inpatientNo){
		String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		final StringBuffer sb=new StringBuffer(1700); 
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append("SELECT DISTINCT rm").append(i).append(".RECIPE_DEPTCODE as recipeDeptcode,rm").append(i).append(".RECIPE_DOCCODE as recipeDoccode,rm")
			.append(i).append(".inpatient_no as inpatientNo,rm").append(i).append(".name as name,(SELECT stat.fee_stat_name from t_charge_minfeetostat stat  where stat.minfee_code=rm")
			.append(i).append(".Fee_Code and stat.report_code = 'ZY01') feeCode, sum(rm").append(i).append(".tot_cost) as totCost,rm")
			.append(i).append(".EXECUTE_DEPTCODE executeDeptcode,rm").append(i).append(".Fee_Opercode feeOpercode, rm")
			.append(i).append(".fee_date as feeDate ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" WHERE rm")
			.append(i).append(".DEL_FLG =0 AND rm").append(i).append(".STOP_FLG =0 ");
			
			if(StringUtils.isNotBlank(firstData)){
				sb.append(" AND rm").append(i).append(".FEE_DATE >= to_date('"+firstData+"','yyyy-MM-dd HH24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append(" AND rm").append(i).append(".FEE_DATE <= to_date('"+endData+"','yyyy-MM-dd HH24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(inpatientNo)){
				sb.append(" AND rm").append(i).append(".inpatient_no in ("+inpatientNo+")");
			}
			sb.append(" group by rm").append(i).append(".RECIPE_DEPTCODE,rm").append(i).append(".RECIPE_DOCCODE ,rm")
			.append(i).append(".inpatient_no,rm").append(i).append(".name,rm")
			.append(i).append(".Fee_Code,rm").append(i).append(".fee_date,rm")
			.append(i).append(".EXECUTE_DEPTCODE,rm").append(i).append(".Fee_Opercode ");
			//.append("order by,rm").append(i).append(".fee_date asc ");
			
		}
		return sb.toString();
	}

	@Override
	public List<InpatientFeeInfo> queryCostStatistice(List<String> tnL, String firstData, String endData, String inpatientNo) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InpatientFeeInfo>();
		}
		String sql=this.sqlHer(tnL,firstData, endData, inpatientNo);
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql.toString());
		sqlQuery.addScalar("recipeDeptcode").addScalar("recipeDoccode")
			.addScalar("inpatientNo").addScalar("name")
			.addScalar("feeCode").addScalar("totCost",Hibernate.DOUBLE)
			.addScalar("executeDeptcode").addScalar("feeOpercode").addScalar("feeDate");
		List<InpatientFeeInfo> list=sqlQuery.setResultTransformer(Transformers.aliasToBean(InpatientFeeInfo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientFeeInfo>();
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId) {
		StringBuffer sb=new StringBuffer();
		sb.append("select inpatient_no as inpatientNo, medicalrecord_id as medicalrecordId, patient_name as patientName, in_date as inDate from t_inpatient_info where medicalrecord_id=:medicalrecordId  ")
		.append(" union  select inpatient_no as inpatientNo, medicalrecord_id as medicalrecordId, patient_name as patientName, in_date as inDate  from t_inpatient_info_now where medicalrecord_id=:medicalrecordId");
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString());
		sqlQuery.addScalar("inpatientNo").addScalar("medicalrecordId")
			.addScalar("patientName").addScalar("inDate",Hibernate.DATE);
		sqlQuery.setParameter("medicalrecordId", medicalrecordId);
		List<InpatientInfoNow> list=sqlQuery.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin() {
		final String sql = "SELECT MAX(mn.BALANCE_DATE) AS eTime ,MIN(mn.BALANCE_DATE) AS sTime FROM t_inpatient_feeinfo_now mn";
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
	public String getMedicalrecordId(String idCard) {
		String sql="select * from (select t.medicalrecord_id  from t_inpatient_info_now t where t.idcard_no = '"+idCard+"') "
				+ "union all select * from (select t.medicalrecord_id  from t_inpatient_info t  where t.idcard_no = '"+idCard+"')";
		List list = this.getSession().createSQLQuery(sql).list();
		if(list!=null&&list.size()>0){
			return list.get(0).toString();
		}
		return null;
	}
}



