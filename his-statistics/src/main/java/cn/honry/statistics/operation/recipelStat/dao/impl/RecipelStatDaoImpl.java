package cn.honry.statistics.operation.recipelStat.dao.impl;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;

import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.recipelStat.dao.RecipelStatDao;
import cn.honry.statistics.operation.recipelStat.vo.RecipelInfoVo;
import cn.honry.statistics.operation.recipelStat.vo.RecipelStatVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

/**  
 *  
 * @className：AddrateDAOImpl 
 * @Description： 门诊处方查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Repository("recipelStatDAO")
@SuppressWarnings({"all"})
public class RecipelStatDaoImpl extends HibernateEntityDao<RecipelStatVo> implements RecipelStatDao{
	
	@Resource
	private RedisUtil redisUtil;
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> getRows(final String page,final String rows,final String startTime,final String endTime,final String type, final String para,final String vague,final List<String> tnL) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(tnL==null||tnL.size()<0){
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RecipelStatVo>());
			return retMap;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append("UNION ALL ");
			}
			buffer.append("SELECT DISTINCT ");
			buffer.append("r.RECIPE_NO AS recipeNo, ");//处方号
			buffer.append("'").append("T_STO_RECIPE_NOW".equals(tnL.get(i))?"T_DRUG_APPLYOUT_NOW":"T_DRUG_APPLYOUT").append("' tab, ");//详情信息需要查询的表T_DRUG_APPLYOUT/T_DRUG_APPLYOUT_NOW
			buffer.append("r.PATIENT_NAME AS name, ");//患者姓名
			buffer.append("r.SEX_CODE sex, ");//性别
			buffer.append("r.BIRTHDAY age, ");//年龄
			buffer.append("r.CARD_NO recordNo, ");//病历号
			buffer.append("r.INVOICE_NO invoiceNo, ");//发票号
			buffer.append("r.DRUGED_TERMINAL_NAME disTable, ");//配药台
			buffer.append("r.DRUGED_OPER_NAME disUser, ");//配药人
			buffer.append("TO_CHAR(r.DRUGED_DATE,'yyyy-MM-dd hh24:mi:ss') disTime, ");//配药时间
			buffer.append("r.SEND_TERMINAL_NAME medTable, ");//发药台
			buffer.append("r.SEND_OPER_NAME medUser, ");//发药人
			buffer.append("TO_CHAR(r.SEND_DATE,'yyyy-MM-dd hh24:mi:ss') medTime, ");//发药时间
			buffer.append("r.DOCT_NAME squareDoc ");//开方医生
			buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" r ");
			buffer.append("WHERE r.STOP_FLG = :STOP_FLG ");
			buffer.append("AND r.DEL_FLG = :DEL_FLG ");
			buffer.append("AND r.VALID_STATE = :VALID_STATE ");
			buffer.append("AND r.FEE_DATE>=TO_DATE(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND r.FEE_DATE<TO_DATE(:endTime, 'yyyy-mm-dd hh24:mi:ss') "); 
			if("0".equals(vague)){//精确查询
				if(StringUtils.isNotBlank(para)){
					
					//类型0全部1病历卡号2发票号3姓名4处方号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".CARD_NO = :para OR r")
						.append(".INVOICE_NO = :para OR r")
						.append(".PATIENT_NAME = :para OR r")
						.append(".RECIPE_NO = :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".CARD_NO = :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".INVOICE_NO = :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME = :para ");
					}else if("4".equals(type)){
						buffer.append("AND r").append(".RECIPE_NO = :para ");
					}
				}
			}else{//模糊查询
				if(StringUtils.isNotBlank(para)){
				
					//类型0全部1病历卡号2发票号3姓名4处方号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".CARD_NO LIKE :para OR r")
						.append(".INVOICE_NO LIKE :para OR r")
						.append(".PATIENT_NAME LIKE :para OR r")
						.append(".RECIPE_NO LIKE :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".CARD_NO LIKE :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".INVOICE_NO LIKE :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME LIKE :para ");
					}else if("4".equals(type)){
						buffer.append("AND r").append(".RECIPE_NO LIKE :para ");
					}
				}
			}
		}
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("STOP_FLG", 0);
		paraMap.put("DEL_FLG", 0);
		paraMap.put("VALID_STATE", 1);
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		if(StringUtils.isNotBlank(para)){
			if("0".equals(vague)){
				paraMap.put("para", para);
			}else{
				paraMap.put("para", "%"+para+"%");
			}
		}
		
		//查询总条数
		StringBuffer bufferTotal = new StringBuffer(buffer.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		
		String redKey = startTime+"_"+endTime+"_"+para;
		Integer total = (Integer) redisUtil.get(redKey);
		if(total==null){
			total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
			redisUtil.set(redKey, total);
		}
			String val = parameterInnerDAO.getParameterByCode("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
		
			//查询对象的sql
		StringBuffer bufferRows = new StringBuffer(buffer.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		List<RecipelStatVo> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<RecipelStatVo>() {
			@Override
			public RecipelStatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelStatVo vo = new RecipelStatVo();
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setTab(rs.getString("tab"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setRecordNo(rs.getString("recordNo"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setDisTable(rs.getString("disTable"));
				vo.setDisUser(rs.getString("disUser"));
				vo.setDisTime(rs.getString("disTime"));
				vo.setMedTable(rs.getString("medTable"));
				vo.setMedUser(rs.getString("medUser"));
				vo.setMedTime(rs.getString("medTime"));
				vo.setSquareDoc(rs.getString("squareDoc"));
				return vo;
		}});
		
		retMap.put("total", total);
		retMap.put("rows", voList==null?new ArrayList<RecipelStatVo>():voList);
		
		return retMap;
	}

	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RecipelInfoVo> getRecipelInfoRows(String recipeNo,List tnl) {
		
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<tnl.size();i++){
			if(i!=0){
				buffer.append(" UNION ALL");
			}
			buffer.append(" SELECT ");
			buffer.append(" a.TRADE_NAME||'['||a.SPECS||']' goodsName, ");//商品名to_char(T .DOSE_ONCE,'fm99990.0099')||t.dose_unit AS oneDosage, 
			buffer.append(" to_char(a .DOSE_ONCE,'fm99990.0099')||a.dose_unit AS oneDosage, ");//每次量
			buffer.append(" a.USE_NAME usage, ");//用法
			buffer.append(" a.DFQ_CEXP frequency, ");//频次
			buffer.append(" a.APPLY_NUM gross, ");//总量
			buffer.append(" a.RETAIL_PRICE retailPrice, ");//零售价
			buffer.append(" NVL(a.APPLY_NUM,1)*NVL(a.RETAIL_PRICE,0) money, ");//金额
			buffer.append(" a.DAYS dosageNum, ");//剂数
			buffer.append(" a.VALID_STATE validity ");//有效性
			buffer.append(" FROM ").append(tnl.get(i)).append(" a ");
			buffer.append(" WHERE a.STOP_FLG = 0 ");
			buffer.append(" AND a.DEL_FLG = 0 ");
			buffer.append(" AND a.RECIPE_NO = :recipeNo ");
		}
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recipeNo", recipeNo);
		
		List<RecipelInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<RecipelInfoVo>() {
			@Override
			public RecipelInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelInfoVo vo = new RecipelInfoVo();
				vo.setGoodsName(rs.getString("goodsName"));
				vo.setOneDosage(rs.getString("oneDosage"));
				vo.setUsage(rs.getString("usage"));
				vo.setFrequency(rs.getString("frequency"));
				vo.setGross(rs.getString("gross"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setMoney(rs.getDouble("money"));
				vo.setDosageNum(rs.getInt("dosageNum"));
				vo.setValidity(rs.getString("validity"));
				return vo;
		}});
		
		if(voList!=null&&voList.size()>0){
			RecipelInfoVo vo = new RecipelInfoVo();
			vo.setGoodsName("合计");
			vo.setMoney(0d);
			for(RecipelInfoVo info : voList){
				vo.setMoney(vo.getMoney()+(info.getMoney()==null?0d:info.getMoney()));
			}
			voList.add(vo);
			return voList;
		}
		
		return new ArrayList<RecipelInfoVo>();
	}
	
	/**获取最小最大时间
	 * @Description 
	 * @author  zhangjin
	 * @createDate： 2016年12月3日 
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public StatVo findMaxMin() {
		
		final String sql = "SELECT MAX(mn.FEE_DATE) AS eTime ,MIN(mn.FEE_DATE) AS sTime FROM t_sto_recipe_now mn";
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
	
	/**  
	 * 
	 * 打印选中的患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月17日 上午11:47:59 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月17日 上午11:47:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<RecipelStatVo> getRecipelStatVos(String recipeNos) {
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(recipeNos!=null){
			paraMap.put("recipeNos", Arrays.asList(recipeNos.split(",")));
		}
		StringBuffer sb  = new StringBuffer();
		sb.append(" SELECT distinct ");
		sb.append(" t.RECIPE_NO AS recipeNo,t.PATIENT_NAME AS name, ");
		sb.append(" DECODE(t.SEX_CODE,1, '男',2,'女',3,'未知') AS sex, ");
		sb.append(" (CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12, 0) ");
		sb.append(" WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday), 0) ");
		sb.append(" ELSE  TRUNC(sysdate) - t.birthday END || ");
		sb.append(" CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
		sb.append(" WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) > 1 THEN   '月' ");
		sb.append(" ELSE  '天' END ) AS age, ");
		sb.append(" t.CARD_NO AS recordNo, ");
		sb.append(" t.INVOICE_NO AS invoiceNo, ");
		sb.append(" t.DRUGED_TERMINAL_NAME AS disTable, ");
		sb.append(" t.DRUGED_OPER_NAME as disUser, ");
		sb.append(" TO_CHAR(t.DRUGED_DATE,'yyyy-mm-dd hh24:mi:ss') AS disTime, ");
		sb.append(" t.SEND_TERMINAL_NAME AS medTable, ");
		sb.append(" t.SEND_OPER_NAME AS medUser, ");
		sb.append(" TO_CHAR(t.SEND_DATE,'yyyy-mm-dd hh24:mi:ss') AS medTime, ");
		sb.append(" t.DOCT_NAME AS squareDoc ");
		sb.append(" FROM T_STO_RECIPE_NOW t ");
		sb.append(" WHERE  t.RECIPE_NO in (:recipeNos) ");
		
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT distinct ");
		sb.append(" t.RECIPE_NO AS recipeNo,t.PATIENT_NAME AS name, ");
		sb.append(" DECODE(t.SEX_CODE,1, '男',2,'女',3,'未知') AS sex, ");
		sb.append(" (CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12 > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12, 0) ");
		sb.append(" WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) > 1 THEN  ROUND(MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday), 0) ");
		sb.append(" ELSE  TRUNC(sysdate) - t.birthday END || ");
		sb.append(" CASE WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) * 1.0 / 12 > 1 THEN  '岁' ");
		sb.append(" WHEN  MONTHS_BETWEEN(TRUNC(SYSDATE), t.birthday) > 1 THEN   '月' ");
		sb.append(" ELSE  '天' END ) AS age, ");
		sb.append(" t.CARD_NO AS recordNo, ");
		sb.append(" t.INVOICE_NO AS invoiceNo, ");
		sb.append(" t.DRUGED_TERMINAL_NAME AS disTable, ");
		sb.append(" t.DRUGED_OPER_NAME as disUser, ");
		sb.append(" TO_CHAR(t.DRUGED_DATE,'yyyy-mm-dd hh24:mi:ss') AS disTime, ");
		sb.append(" t.SEND_TERMINAL_NAME AS medTable, ");
		sb.append(" t.SEND_OPER_NAME AS medUser, ");
		sb.append(" TO_CHAR(t.SEND_DATE,'yyyy-mm-dd hh24:mi:ss') AS medTime, ");
		sb.append(" t.DOCT_NAME AS squareDoc ");
		sb.append(" FROM T_STO_RECIPE t ");
		sb.append(" WHERE  t.RECIPE_NO in (:recipeNos) ");
		
		List<RecipelStatVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<RecipelStatVo>() {
			@Override
			public RecipelStatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelStatVo vo = new RecipelStatVo();
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setRecordNo(rs.getString("recordNo"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setDisTable(rs.getString("disTable"));
				vo.setDisUser(rs.getString("disUser"));
				vo.setDisTime(rs.getString("disTime"));
				vo.setMedTable(rs.getString("medTable"));
				vo.setMedUser(rs.getString("medUser"));
				vo.setMedTime(rs.getString("medTime"));
				vo.setSquareDoc(rs.getString("squareDoc"));
				return vo;
		}});
		
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		
		return new ArrayList<RecipelStatVo>();
	}
	
	/**  
	 * 
	 * 打印选中的患者处方信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月17日 上午11:47:59 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月17日 上午11:47:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<RecipelInfoVo> getRecipelInfos(String recipeNos,List tnl) {
		
		StringBuffer sb  = new StringBuffer();
		for(int i=0;i<tnl.size();i++){
			if(tnl!=null&&tnl.size()>0){
				if(i==0){
					sb.append(" SELECT t.TRADE_NAME || '[' || t.SPECS || ']' AS goodsName, ");
					sb.append(" to_char(T .DOSE_ONCE,'fm99990.0099')||t.dose_unit AS oneDosage, ");
					sb.append(" t.use_name AS usage, ");
					sb.append(" t.dfq_cexp AS frequency, ");
					sb.append(" t.APPLY_NUM||t.min_unit AS gross, ");
					sb.append(" round(t.RETAIL_PRICE,4) AS retailPrice, ");
					sb.append(" round(t.APPLY_NUM * t.RETAIL_PRICE,2) AS money, ");
					sb.append(" t.DAYS as dosageNum, ");
					sb.append(" t.RECIPE_NO AS recipeNo, ");
					sb.append(" decode( (SELECT COUNT(*) FROM  T_INPATIENT_CANCELITEM_NOW c WHERE c.RECIPE_NO=t.RECIPE_NO AND c.SEQUENCE_NO= t.SEQUENCE_NO AND c.DRUG_FLAG='1' ");
					sb.append(" AND  lower(substr(c.INPATIENT_NO,0,2)) != 'zy'),'1','药房退药',decode(t.VALID_STATE,'1','有效','2','不摆药','无效'))as validity ");
					sb.append(" FROM t_Drug_Applyout_Now t ");
					sb.append(" WHERE t.RECIPE_NO in (:recipeNos) ");
				}
				if(i>=1){
					sb.append(" UNION ALL ");
					
					sb.append(" SELECT t.TRADE_NAME || '[' || t.SPECS || ']' AS goodsName, ");
					sb.append(" to_char(T .DOSE_ONCE,'fm99990.0099')||t.dose_unit AS oneDosage, ");
					sb.append(" t.use_name AS usage, ");
					sb.append(" t.dfq_cexp AS frequency, ");
					sb.append(" t.APPLY_NUM||t.min_unit AS gross, ");
					sb.append(" round(t.RETAIL_PRICE,4) AS retailPrice, ");
					sb.append(" round(t.APPLY_NUM * t.RETAIL_PRICE,2) AS money, ");
					sb.append(" t.DAYS as dosageNum, ");
					sb.append(" t.RECIPE_NO AS recipeNo, ");
					sb.append(" decode( (SELECT COUNT(*) FROM  T_INPATIENT_CANCELITEM c WHERE c.RECIPE_NO=t.RECIPE_NO AND c.SEQUENCE_NO= t.SEQUENCE_NO AND c.DRUG_FLAG='1' ");
					sb.append(" AND  lower(substr(c.INPATIENT_NO,0,2)) != 'zy'),'1','药房退药',decode(t.VALID_STATE,'1','有效','2','不摆药','无效'))as validity ");
					sb.append(" FROM "+tnl.get(i)+" t ");
					sb.append(" WHERE t.RECIPE_NO in (:recipeNos) ");
					
				}
				
			}
		}
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		if(recipeNos!=null){
			paramMap.put("recipeNos", Arrays.asList(recipeNos.split(",")));
		}
		List<RecipelInfoVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<RecipelInfoVo>() {
			@Override
			public RecipelInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelInfoVo vo = new RecipelInfoVo();
				vo.setGoodsName(rs.getString("goodsName"));
				vo.setOneDosage(rs.getString("oneDosage"));
				vo.setUsage(rs.getString("usage"));
				vo.setFrequency(rs.getString("frequency"));
				vo.setGross(rs.getString("gross"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setMoney(rs.getDouble("money"));
				vo.setDosageNum(rs.getInt("dosageNum"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setValidity(rs.getString("validity"));
				return vo;
		}});
		
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		
		return new ArrayList<RecipelInfoVo>();
	}
	
}
