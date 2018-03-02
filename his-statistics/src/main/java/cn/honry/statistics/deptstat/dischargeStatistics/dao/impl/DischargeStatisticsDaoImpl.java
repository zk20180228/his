package cn.honry.statistics.deptstat.dischargeStatistics.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.dischargeStatistics.dao.DischargeStatisticsDao;
import cn.honry.statistics.deptstat.dischargeStatistics.vo.DischargeStatisticsVo;
import cn.honry.utils.ShiroSessionUtils;
@Repository("dischargeStatisticsDao")
@SuppressWarnings({ "all" })
public class DischargeStatisticsDaoImpl extends  HibernateEntityDao<DischargeStatisticsVo> implements DischargeStatisticsDao {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<DischargeStatisticsVo> queryIllness() {
		String sql="select t.code_encode as illCode,t.code_name as illName  from t_business_dictionary t  where t.ext_c1 = '疾病分类' ";
		List<DischargeStatisticsVo> list=this.getSession().createSQLQuery(sql).addScalar("illCode").addScalar("illName")
				.setResultTransformer(Transformers.aliasToBean(DischargeStatisticsVo.class)).list();
		return list;
	}

	/**  
	 * 出院病种统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DischargeStatisticsVo> queryDischargeStat(String startTime,String endTime,String deptCode,String page, String rows, String menuAlias) {
		StringBuffer sql=new StringBuffer(4000);
		sql.append("SELECT * from (");
//		sql.append("SELECT  ROWNUM AS n,diseaseKind,icd_code,outPatient,subTotal,cure,better,notCure,death,other, cureRate,"
//				+ "betterRate,deathRate,avgInpatDay,avgBeforeOperDay,cost,avgBedFee,avgDrugFee,avgOperFee,avgCheckTreat,drugProp FROM( ");
		sql.append("select rownum n, AA.diseaseKind, AA.icd_code, AA.outPatient,  AA.subTotal, AA.cure, AA.better, AA.notCure, AA.death, (AA.outPatient - AA.subTotal) other, "
				+ "round(((AA.outPatient - AA.subTotal+AA.cure) * 100) / AA.outPatient, 1) cureRate,"
				+ "round((AA.better * 100) / AA.outPatient, 1) betterRate,"
				+ "round((AA.death * 100) / AA.outPatient, 1) deathRate,"
				+ "round((AA.outPatByBeds / AA.outPatient), 1) avgInpatDay ,"
				+ "decode(nvl(AA.beforeOperDay,0),0,0,round((AA.beforeOperDay /AA.operation), 1)) avgBeforeOperDay,"
				+ "round(AA.num / AA.outPatient, 2) cost,"
				+ "round(AA.bedFee / AA.outPatient, 2) avgBedFee,"
				+ "round(AA.drugFee / AA.outPatient, 2) avgDrugFee,"
				+ "round(AA.operFee / AA.outPatient, 2) avgOperFee, "
				+ "round(AA.checkTreat / AA.outPatient, 2) avgCheckTreat,"
				+ "round((AA.drugFee * 100) / AA.num, 1) drugProp  "
				+ "from (select /*+rule*/icd_name diseaseKind,icd_code,count(distinct inpatient_no) outPatient,"
				+ "(sum(cure) + sum(better) + sum(notCure) + sum(death)) subTotal,"
				+ "sum(cure) as cure, sum(better) as better, sum(notCure) as notCure, sum(death) as death, sum(familyPlan) familyPlan,"
				+ "sum(childBirth) childBirth, sum(outPatByBeds) as outPatByBeds, sum(beforeOperDay) as beforeOperDay, sum(operation) as operation,"
				+ "round(sum(num), 2) as num, sum(bedFee) as bedFee, sum(drugFee) as drugFee, sum(operFee) as operFee, sum(checkTreat) as checkTreat "
				+ "from (select t.main_diagicdname icd_name, main_diagicd icd_code,t.inpatient_no, "
				+ "SUM(DECODE(t.zg, '1', 1, 0)) cure, SUM(DECODE(t.zg, '2', 1, 0)) better, SUM(DECODE(t.zg, '3', 1, 0)) notCure,"
				+ "SUM(DECODE(t.zg, '4', 1, 0)) death,sum(DECODE(t.zg, '6', 1, 0)) familyPlan,sum(DECODE(t.zg, '7', 1, 0)) childBirth,"
				+ "sum(PI_DAYS) outPatByBeds,"
				+ "(select sum(b.beforeoper_days) from t_operation_detail b where b.stat_flag = '0' and b.oper_type = '2' and b.inpatient_no = t.inpatient_no)  beforeOperDay,"
				+ "(select /*+index(b inpatient_no)*/count(1) from t_operation_detail b where b.stat_flag = '0' and b.oper_type = '2' and b.inpatient_no = t.inpatient_no) operation,"
				+ "sum(num) num, sum(bedFee) bedFee, sum(drugFee) drugFee, sum(operFee) operFee, sum(checkTreat) checkTreat "
				+ "from (select mcb.inpatient_no,mcb.main_diagicd,mcb.main_diagicdname,mcb.dept_code,mcb.pi_days,mcb.zg from t_emr_base mcb  where mcb.case_stus in ('3', '4') and (fun_splitstring(mcb.henanadd2012, '|', 29) !=  'DOC' "
				+ "or fun_splitstring(mcb.henanadd2012, '|', 29) is null) "
				+ "and mcb.out_date >= to_Date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "and mcb.out_date <=  to_Date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')) t,"
				+ "(select d.INPATIENT_NO,  sum(d.tot_cost) num,  "
				+ "SUM(CASE WHEN d.fee_id in  ('04')  THEN d.TOT_COST  ELSE 0 END) bedFee,"
				+ "SUM(CASE WHEN d.fee_id in  ('01', '02', '03')  THEN d.TOT_COST  ELSE 0 END) drugFee,"
				+ "SUM(CASE WHEN d.fee_id in  ('10')  THEN  d.TOT_COST  ELSE 0 END) operFee,"
				+ "SUM(CASE WHEN d.fee_id in  ('05', '07', '09','08')  THEN d.TOT_COST ELSE 0  END) checkTreat  "
				+ "from t_inpatient_out_fees d where d.inpatient_no in "
				+ "(select mcb.inpatient_no from t_emr_base mcb where mcb.case_stus in ('3', '4')  and (fun_splitstring(mcb.henanadd2012,  '|',  29) != 'DOC'  "
				+ "or fun_splitstring(mcb.henanadd2012, '|', 29) is null) "
				+ "and mcb.out_date >= to_Date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "and mcb.out_date <=  to_Date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')) "
				+ "GROUP BY d.INPATIENT_NO) t3  where t.inpatient_no(+) = t3.inpatient_no ");
				Map<String, Object> paraMap = new HashMap<String, Object>();		
				if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and t.dept_code in('"+deptCode+"')");
				}
				else{
					sql.append("and  t.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
				}
				sql.append(" "
				+ "group by t.inpatient_no, main_diagicdname, main_diagicd) aa  group by icd_name, icd_code  "
				+ " ) AA where rownum <= :page * :rows "); 
		sql.append(") where n > (:page - 1) * :rows");
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<DischargeStatisticsVo> DischargeStatisticsVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DischargeStatisticsVo>() {
			@Override
			public DischargeStatisticsVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DischargeStatisticsVo vo = new DischargeStatisticsVo();
				vo.setDiseaseKind(rs.getString("diseaseKind"));
				vo.setIcd_code(rs.getString("icd_code"));
				vo.setOutPatient(rs.getString("outPatient"));
				vo.setSubTotal(rs.getString("subTotal"));
				vo.setCure(rs.getString("cure"));
				vo.setBetter(rs.getString("better"));
				vo.setNotCure(rs.getString("notCure"));
				vo.setDeath(rs.getString("death"));
				vo.setOther(rs.getString("other"));
				if(StringUtils.isNotBlank(rs.getString("cureRate"))&&rs.getString("cureRate").startsWith(".")){
					vo.setCureRate("0"+rs.getString("cureRate"));
				}else{
					vo.setCureRate(rs.getString("cureRate"));
				}
				if(StringUtils.isNotBlank(rs.getString("betterRate"))&&rs.getString("betterRate").startsWith(".")){
					vo.setBetterRate("0"+rs.getString("betterRate"));
				}else{
					vo.setBetterRate(rs.getString("betterRate"));
				}
				if(StringUtils.isNotBlank(rs.getString("deathRate"))&&rs.getString("deathRate").startsWith(".")){
					vo.setDeathRate("0"+rs.getString("deathRate"));
				}else{
					vo.setDeathRate(rs.getString("deathRate"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgInpatDay"))&&rs.getString("avgInpatDay").startsWith(".")){
					vo.setAvgInpatDay("0"+rs.getString("avgInpatDay"));
				}else{
					vo.setAvgInpatDay(rs.getString("avgInpatDay"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgBeforeOperDay"))&&rs.getString("avgBeforeOperDay").startsWith(".")){
					vo.setAvgBeforeOperDay("0"+rs.getString("avgBeforeOperDay"));
				}else{
					vo.setAvgBeforeOperDay(rs.getString("avgBeforeOperDay"));
				}
				if(StringUtils.isNotBlank(rs.getString("cost"))&&rs.getString("cost").startsWith(".")){
					vo.setCost("0"+rs.getString("cost"));
				}else{
					vo.setCost(rs.getString("cost"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgBedFee"))&&rs.getString("avgBedFee").startsWith(".")){
					vo.setAvgBedFee("0"+rs.getString("avgBedFee"));
				}else{
					vo.setAvgBedFee(rs.getString("avgBedFee"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgDrugFee"))&&rs.getString("avgDrugFee").startsWith(".")){
					vo.setAvgDrugFee("0"+rs.getString("avgDrugFee"));
				}else{
					vo.setAvgDrugFee(rs.getString("avgDrugFee"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgOperFee"))&&rs.getString("avgOperFee").startsWith(".")){
					vo.setAvgOperFee("0"+rs.getString("avgOperFee"));
				}else{
					vo.setAvgOperFee(rs.getString("avgOperFee"));
				}
				if(StringUtils.isNotBlank(rs.getString("avgCheckTreat"))&&rs.getString("avgCheckTreat").startsWith(".")){
					vo.setAvgCheckTreat("0"+rs.getString("avgCheckTreat"));
				}else{
					vo.setAvgCheckTreat(rs.getString("avgCheckTreat"));
				}
				if(StringUtils.isNotBlank(rs.getString("drugProp"))&&rs.getString("drugProp").startsWith(".")){
					vo.setDrugProp("0"+rs.getString("drugProp"));
				}else{
					vo.setDrugProp(rs.getString("drugProp"));
				}
				return vo;
			}
		});
		if(DischargeStatisticsVoList.size()>0){
			return DischargeStatisticsVoList;
		}
		return new ArrayList<DischargeStatisticsVo>();
	}


	/**  
	 * 出院病种统计  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalDischargeStat(String startTime, String endTime, String deptCode, String page, String rows, String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("select  count(1) total from (select count(1) "
				+ "from (select t.main_diagicdname icd_name,t.main_diagicd icd_code,t.inpatient_no "
				+ "from (select mcb.main_diagicdname,mcb.main_diagicd,mcb.inpatient_no,mcb.dept_code from t_emr_base mcb  where mcb.case_stus in ('3', '4') and (fun_splitstring(mcb.henanadd2012, '|', 29) !=  'DOC' "
				+ "or fun_splitstring(mcb.henanadd2012, '|', 29) is null) "
				+ "and mcb.out_date >= to_Date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "and mcb.out_date <=  to_Date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')) t,"
				+ "(select d.INPATIENT_NO  "
				+ "from t_inpatient_out_fees d where d.inpatient_no in "
				+ "(select mcb.inpatient_no from t_emr_base mcb where mcb.case_stus in ('3', '4')  and (fun_splitstring(mcb.henanadd2012,  '|',  29) != 'DOC'  "
				+ "or fun_splitstring(mcb.henanadd2012, '|', 29) is null) "
				+ "and mcb.out_date >= to_Date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "and mcb.out_date <=  to_Date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')) "
				+ "GROUP BY d.INPATIENT_NO) t3  where t.inpatient_no (+)= t3.inpatient_no ");
			Map<String,String> paraMap=new HashMap<String,String>();		
			if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and t.dept_code in('"+deptCode+"')");
				}else{
					sql.append("and  t.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
				}
				sql.append(" "
				+ "group by t.inpatient_no, main_diagicdname, main_diagicd) aa group by icd_name, icd_code  "
				+ ") AA  "); 
				
				return namedParameterJdbcTemplate.query(sql.toString(),paraMap ,new RowMapper<Integer>(){
					@Override
					public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
						return rs.getInt("total");
					}
					
				}).get(0);
	}

	/**  
	 * 出院病种统计  从mongdb中查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DischargeStatisticsVo> queryDischargeStatForDB(String startTime, String endTime, String deptCode, String page, String rows, String menuAlias) {
		// TODO Auto-generated method stub
		return null;
	}
}
