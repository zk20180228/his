package cn.honry.statistics.sys.medicalFeeDetail.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.sys.medicalFeeDetail.dao.MedicalFeeDetailDAO;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeDetailsVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;
@Repository("medicalFeeDetailDAO")
@SuppressWarnings({ "all" })
public class MedicalFeeDetailDAOImpl extends HibernateEntityDao<InpatientInfo>  implements MedicalFeeDetailDAO{
	
	private static final int List = 0;
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
	
	@Override
	public List<FeeNameVo> queryFeeName() throws Exception{
		String sql = "select distinct t.FEE_STAT_CODE as feeStatCode,t.FEE_STAT_NAME as feeStatName from T_CHARGE_MINFEETOSTAT t where t.stop_flg = 0 and t.del_flg = 0 and t.report_code='ZY01' order by t.FEE_STAT_CODE";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("feeStatCode")
				.addScalar("feeStatName");
		List<FeeNameVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(FeeNameVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<FeeNameVo>();
	}
	
	@Override
	public List<FeeDetailsVo> queryFeeDetails() throws Exception{
		String sql = "select c.INPATIENT_NO as inpatientNo,c.FEE_CODE as feeCode,sum(c.TOT_COST) as totalCost from T_INPATIENT_MEDICINELIST_NOW c where c.stop_flg = 0 and c.del_flg = 0 GROUP BY c.INPATIENT_NO,c.FEE_CODE ";
			sql +="union select d.INPATIENT_NO as inpatientNo,d.FEE_CODE as feeCode,sum(d.TOT_COST) as totalCost from T_INPATIENT_ITEMLIST_NOW d where d.stop_flg = 0 and d.del_flg = 0 GROUP BY d.INPATIENT_NO,d.FEE_CODE ";
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			List<FeeDetailsVo> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<FeeDetailsVo>() {
				@Override
				public FeeDetailsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					FeeDetailsVo vo = new FeeDetailsVo();
					vo.setInpatientNo(rs.getString("inpatientNo"));
					vo.setFeeCode(rs.getString("feeCode"));
					vo.setTotalCost(rs.getDouble("totalCost"));
					return vo;
			}});
			return voList;
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId,String startTime,String endTime) throws Exception{
		/**Y.FEECODE 最小费用代码 Y.TOTALCOST 相同住院号最小费用总和  (T.PREPAY_COST-Y.TOTALCOST) 出院退费**/
		StringBuffer buffer=new StringBuffer(890);
		buffer.append("SELECT T.MEDICALRECORD_ID AS inpatientNo,T.PATIENT_NAME AS patientName,T.PREPAY_COST AS prepayCost,(SELECT DISTINCT X.FEE_STAT_CODE FROM T_CHARGE_MINFEETOSTAT X WHERE X.MINFEE_CODE= Y.FEECODE AND X.STOP_FLG = 0 AND X.DEL_FLG = 0 AND X.REPORT_CODE = 'ZY01') AS profCode,Y.TOTALCOST AS height,(T.PREPAY_COST-Y.TOTALCOST) AS weight ");
		buffer.append("FROM T_INPATIENT_INFO_NOW T LEFT JOIN ( ");
		buffer.append("SELECT C.INPATIENT_NO AS INPATIENTNO,");
		buffer.append("C.FEE_CODE AS FEECODE,SUM(C.TOT_COST) AS TOTALCOST ");
		buffer.append("FROM T_INPATIENT_MEDICINELIST_NOW C ");
		buffer.append("WHERE C.STOP_FLG = 0 AND C.DEL_FLG = 0 ");
		buffer.append("GROUP BY C.INPATIENT_NO, C.FEE_CODE ");
		buffer.append("UNION ");
		buffer.append("SELECT D.INPATIENT_NO AS INPATIENTNO,");
		buffer.append("D.FEE_CODE AS FEECODE,SUM(D.TOT_COST) AS TOTALCOST ");
		buffer.append("FROM T_INPATIENT_ITEMLIST_NOW D ");
		buffer.append("WHERE D.STOP_FLG = 0 AND D.DEL_FLG = 0 ");
		buffer.append("GROUP BY D.INPATIENT_NO, D.FEE_CODE ) Y ON T.INPATIENT_NO=Y.INPATIENTNO ");
		buffer.append("WHERE T.IN_STATE IN ('R', 'I') AND T.STOP_FLG = 0 ");
		buffer.append("AND T.DEL_FLG = 0 ");
		Map paraMap=new HashMap();
		if(StringUtils.isNotBlank(startTime)&& StringUtils.isNotBlank(endTime)){
			buffer.append("AND T.IN_DATE >= TO_DATE(:startTime, 'yyyy-mm-dd hh24:mi:ss') AND T.IN_DATE <=  TO_DATE(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
			paraMap.put("startTime", startTime);
			paraMap.put("endTime", endTime);
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			buffer.append("AND T.MEDICALRECORD_ID = :medicalrecordId");	
			paraMap.put("medicalrecordId", medicalrecordId);
		}
		List<InpatientInfoNow> list =  namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<InpatientInfoNow>() {
			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow vo=new InpatientInfoNow();
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setPrepayCost(rs.getDouble("prepayCost"));//预交金
				vo.setProfCode(rs.getString("profCode"));//最小费用代码
				vo.setHeight(rs.getDouble("height"));//费用总和
				vo.setWeight(rs.getDouble("weight"));//退费
				return vo;
			}});
		
		if(list!=null&&list.size()>0){
			return list;
		}
		
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public List<MinfeeStatCode> queryMinfeeStat(String feeCode) throws Exception{
		String hql = "from MinfeeStatCode t where t.stop_flg = 0 and t.del_flg = 0  and t.feeStatCode = '"+feeCode+"' and t.reportCode='ZY01'";							
		List<MinfeeStatCode> list = this.getSession().createQuery(hql).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<MinfeeStatCode>();
	}

	@Override
	public java.util.List<FeeNameVo> queryFeeNameToMinfee() throws Exception{
		String sql = "select distinct t.FEE_STAT_CODE as feeStatCode,t.FEE_STAT_NAME as feeStatName,t.MINFEE_CODE AS minFeeCode from T_CHARGE_MINFEETOSTAT t where t.stop_flg = 0 and t.del_flg = 0 and t.report_code='ZY01'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("minFeeCode")
				.addScalar("feeStatCode")
				.addScalar("feeStatName");
		List<FeeNameVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(FeeNameVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<FeeNameVo>();
	}
}
