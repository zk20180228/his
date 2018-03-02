package cn.honry.statistics.deptstat.inpatientInfoTable.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;
import cn.honry.statistics.deptstat.inpatientInfoTable.dao.InpatientInfoTableDao;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.statistics.deptstat.operationDeptLevel.dao.OperationDeptLevelDao;
import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("inpatientInfoTableDao")
@SuppressWarnings({ "all" })
public class InpatientInfoTableDaoImpl extends HibernateEntityDao<InpatientInfoTableVo> implements InpatientInfoTableDao{
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
	/**  
	 * 住院病人动态报表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<InpatientInfoTableVo> queryInpatientInfoTable(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n,deptName,firstBeds,finalBeds,firstStayHos,withinInHos,otherTransIn,total,subTotal,cure,better,notCure,death,others,"
				+ "childBirth,familyPlan,transOther,finalStayHos,actOpenBedDays,avgOpenBeds,actUseBedDays,outPatUseBedDays,outAvgInDay,cureRate,betterRate, "
				+ "deathRate,bedTurnNum,avgBedWorkDay,bedUseRate,avgBeforeOperDay,criPatient,addBedDays,freeBedDays,hangBedDays,levelA   FROM( ");
		sql.append("SELECT nvl(BB.arcdeptname, '合计') deptName,SUM(firstBeds) firstBeds,SUM(finalBeds) finalBeds,SUM(firstStayHos) firstStayHos, "
				+ "SUM(withinInHos) withinInHos,SUM(otherTransIn) otherTransIn,SUM(total) total,SUM(subTotal) subTotal,SUM(cure) cure, "
				+ "SUM(better) better,SUM(notCure) notCure,SUM(death) death,SUM(others) others,SUM(childBirth) childBirth,SUM(familyPlan) familyPlan, "
				+ "SUM(transOther) transOther,SUM(finalStayHos) finalStayHos,SUM(actOpenBedDays) actOpenBedDays,round(SUM(actOpenBedDays) / '"+deptCode+"', 1) avgOpenBeds, "
				+ "SUM(actUseBedDays) actUseBedDays,SUM(outPatUseBedDays) outPatUseBedDays,"
				+ "decode(decode(SUM(outPatUseBedDays), 0, 0, SUM(total)), 0, null,  "
				+ "round(SUM(outPatUseBedDays) / SUM(total), 1)) outAvgInDay,"
				+ "(case when to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss') >= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss') then  "
				+ "decode(decode((SUM(cure) + SUM(others) + SUM(childBirth) + SUM(familyPlan)), 0, 0, SUM(total)),0, null,  "
				+ "round(((SUM(cure) + SUM(others) + SUM(childBirth) + SUM(familyPlan)) / SUM(total)) * 100, 1))  else  "
				+ "decode(decode(SUM(cure), 0, 0, SUM(subTotal)), 0, null, round(((SUM(cure)) / SUM(subTotal)) * 100, 1)) end) cureRate, "
				+ "decode(decode(SUM(better), 0, 0, SUM(total)), 0, null, round((SUM(better) / SUM(total)) * 100, 1)) betterRate, "
				+ "decode(decode(SUM(death), 0, 0, SUM(total)), 0, null, round((SUM(death) / SUM(total)) * 100, 1)) deathRate,  "
				+ "decode(round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0, null, round((SUM(total) + SUM(transOther)) / round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 1)) bedTurnNum, "
				+ "decode(round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0, null, round(SUM(actUseBedDays) / round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0)) avgBedWorkDay,  "
				+ "decode(SUM(actOpenBedDays), 0, null, round((SUM(actUseBedDays) / SUM(actOpenBedDays)) * 100, 1)) bedUseRate,  "
				+ "decode(decode(SUM(beforeOperDay), 0, 0, sum(operation)), 0, null, round((SUM(beforeOperDay) / sum(operation)), 3)) avgBeforeOperDay,  "
				+ "SUM(criPatient) criPatient, sum(addBedDays) addBedDays, sum(freeBedDays) freeBedDays, sum(hangBedDays) hangBedDays, sum(levelA)  levelA   "
				+ "FROM (select PARDEP_CODE, PARDEP_NAME, DEPT_CODE, DEPT_NAME, SORT_ID  from (select cg.arcbigdeptcode PARDEP_CODE, cg.arcbigdeptname PARDEP_NAME, "
				+ "cg.arcdeptcode DEPT_CODE, cg.arcdeptname DEPT_NAME, cg.arcdeptsortid SORT_ID, "
				+ "row_number() over(partition by cg.arcdeptcode, cg.arcdeptname order by cg.arcdeptsortid desc) as row_index   from t_arclog_inpatient_dayreport cg  "
				+ "where cg.date_stat >= to_date('"+startTime+"',  'yyyy-mm-dd HH24:mi:ss') and cg.date_stat <= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')  "
				+ "group by cg.arcdeptcode, cg.arcdeptname, cg.arcbigdeptcode, cg.arcbigdeptname, cg.arcdeptsortid) t  where 1=1  ");
				Map<String, Object> paraMap = new HashMap<String, Object>();		
				if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and t.dept_code in('"+deptCode+"')");
				}else{
					sql.append(" and t.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
				}
				sql.append("  and row_index = 1 order by SORT_ID) AA, "
				+ "(select t1.arcdeptname, sum(decode(date_stat, trunc(to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss')), bed_stand, 0)) firstBeds, "
				+ "sum(decode(date_stat, trunc(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')), bed_stand,  0)) finalBeds, "
				+ "sum(decode(date_stat, trunc(to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss')), beginning_num, 0)) firstStayHos, "
				+ "sum(t1.in_normal) withinInHos, sum(t1.in_transfer) otherTransIn, sum(t1.out_transfer) transOther,  "
				+ "sum(decode(date_stat, trunc(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')), end_num,  0)) finalStayHos,  "
				+ "sum(t1.bed_stand) actOpenBedDays, sum(t1.end_num) actUseBedDays, sum(t1.heavy_num + t1.danger_num) criPatient, "
				+ "sum(case when (t1.end_num - t1.QIGUANQIEKAINUM - t1.bed_stand) > 0 then (t1.end_num - t1.QIGUANQIEKAINUM - t1.bed_stand) else  0 end) addBedDays, "
				+ "sum(case when t1.bed_stand - t1.end_num > 0 then t1.bed_stand - t1.end_num else 0 end) freeBedDays,  "
				+ "sum(t1.QIGUANQIEKAINUM) hangBedDays,sum(t1.ONELVCARENUM) levelA  from t_arclog_inpatient_dayreport t1  "
				+ "where t1.date_stat >= to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss') and t1.date_stat <= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss') "
				+ "group by t1.arcdeptname) BB, (select arcdeptname, sum(total) total, sum(subTotal) subTotal, sum(cure) cure, sum(better) better,  "
				+ "sum(notCure) notCure, sum(death) death, sum(familyPlan) familyPlan, sum(childBirth) childBirth, "
				+ "(sum(total) - sum(subTotal) - sum(familyPlan) - sum(childBirth)) others, sum(diagnosis) diagnosis, "
				+ "sum(beforeOperDay) beforeOperDay, sum(operation) operation, sum(outPatUseBedDays) outPatUseBedDays   "
				+ "from (SELECT t2.arcdeptname, count(distinct t2.inpatient_no) total, sum(DECODE(t2.zg, '1', 1, '2', 1, '3', 1, '4', 1, 0)) subTotal,  "
				+ "sum(DECODE(t2.zg, '1', 1, 0)) cure,  sum(DECODE(t2.zg, '2', 1, 0)) better, sum(DECODE(t2.zg, '3', 1, 0)) notCure,  "
				+ "sum(DECODE(t2.zg, '4', 1, 0)) death, sum(DECODE(t2.zg, '6', 1, 0)) familyPlan,  sum(DECODE(t2.zg, '7', 1, 0)) childBirth, 0 diagnosis,  "
				+ "(select sum(CB.beforeoper_days) from (select p.*, row_number() over(partition by p.inpatient_no order by p.oper_type desc) as row_index  "
				+ "from t_operation_detail p  where p.stat_flag = '0') CB  where CB.row_index = 1  and CB.inpatient_no = t2.inpatient_no) beforeOperDay,  "
				+ "sum(case when t2.OPERATION_CODE is null or t2.OPERATION_CODE = '' then  0  else  1 end) operation, "
				+ "sum(t2.pi_days) outPatUseBedDays  FROM t_emr_arclog_base t2  "
				+ "WHERE trunc(T2.OUT_DATE) >= to_date('2017-03-01', 'yyyy-mm-dd HH24:mi:ss')  and trunc(t2.out_date) <= to_date('2017-04-01', 'yyyy-mm-dd HH24:mi:ss') "
				+ "and t2.case_stus in ('3', '4')  group by t2.arcdeptname, inpatient_no) group by arcdeptname) CC  "
				+ "where AA.DEPT_NAME = BB.arcdeptname and BB.arcdeptname = CC.arcdeptname(+)  and AA.DEPT_CODE in ('"+deptCode+"')  GROUP BY rollup(BB.arcdeptname)  "
				+ "ORDER BY nvl((select g.ARCDEPTSORTID  from (select ccg.arcdeptsortid,  ccg.arcdeptname, ccg.arclogyear,  ccg.arclogmonth  "
				+ "from t_arclog_inpatient_dayreport ccg  where ccg.arclogyear || ccg.arclogmonth >=  to_char(to_date('"+startTime+"',  'yyyy-mm-dd HH24:mi:ss'), 'yyyyMM')  "
				+ "and ccg.arclogyear || ccg.arclogmonth <= to_char(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss'), 'yyyyMM') group by ccg.arcdeptsortid,  "
				+ "ccg.arcdeptname,  ccg.arclogyear,  ccg.arclogmonth) g where g.arcdeptname = BB.arcdeptname  and rownum = 1), nvl2(BB.arcdeptname, 9998, 9999) ");
		sql.append( ") "); 
		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<InpatientInfoTableVo> InpatientInfoTableVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInfoTableVo>() {
			@Override
			public InpatientInfoTableVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				InpatientInfoTableVo vo = new InpatientInfoTableVo();
				vo.setDeptName(rs.getString("deptName"));
				vo.setFirstBeds(rs.getString("firstBeds"));
				vo.setFinalBeds(rs.getString("finalBeds"));
				vo.setFirstStayHos(rs.getString("firstStayHos"));
				vo.setWithinInHos(rs.getString("withinInHos"));
				vo.setOtherTransIn(rs.getString("otherTransIn"));
				vo.setTotal(rs.getString("total"));
				vo.setSubTotal(rs.getString("subTotal"));
				vo.setCure(rs.getString("cure"));
				vo.setBetter(rs.getString("better"));
				vo.setNotCure(rs.getString("notCure"));
				vo.setDeath(rs.getString("death"));
				vo.setOthers(rs.getString("others"));
				vo.setChildBirth(rs.getString("childBirth"));
				vo.setFamilyPlan(rs.getString("familyPlan"));
				vo.setTransOther(rs.getString("transOther"));
				vo.setFinalStayHos(rs.getString("finalStayHos"));
				vo.setActOpenBedDays(rs.getString("actOpenBedDays"));
				vo.setAvgOpenBeds(rs.getString("avgOpenBeds"));
				vo.setActUseBedDays(rs.getString("actUseBedDays"));
				vo.setOutPatUseBedDays(rs.getString("outPatUseBedDays"));
				vo.setOutAvgInDay(rs.getString("outAvgInDay"));
				vo.setCureRate(rs.getString("cureRate"));
				vo.setBetterRate(rs.getString("betterRate"));
				vo.setDeathRate(rs.getString("deathRate"));
				vo.setBedTurnNum(rs.getString("bedTurnNum"));
				vo.setAvgBedWorkDay(rs.getString("avgBedWorkDay"));
				vo.setBedUseRate(rs.getString("bedUseRate"));
				vo.setAvgBeforeOperDay(rs.getString("avgBeforeOperDay"));
				vo.setCriPatient(rs.getString("criPatient"));
				vo.setAddBedDays(rs.getString("addBedDays"));
				vo.setFreeBedDays(rs.getString("freeBedDays"));
				vo.setHangBedDays(rs.getString("hangBedDays"));
				vo.setLevelA(rs.getString("levelA "));
				
				return vo;
			}
			
		});
		if(InpatientInfoTableVoList.size()>0){
			return InpatientInfoTableVoList;
		}
		return new ArrayList<InpatientInfoTableVo>();
	}
	
	/**  
	 * 住院病人动态报表  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalInpatientInfoTable(String startTime,String endTime,String deptCode,String menuAlias,String page,String rowss) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("SELECT nvl(BB.arcdeptname, '合计') deptName,SUM(firstBeds) firstBeds,SUM(finalBeds) finalBeds,SUM(firstStayHos) firstStayHos, "
				+ "SUM(withinInHos) withinInHos,SUM(otherTransIn) otherTransIn,SUM(total) total,SUM(subTotal) subTotal,SUM(cure) cure, "
				+ "SUM(better) better,SUM(notCure) notCure,SUM(death) death,SUM(others) others,SUM(childBirth) childBirth,SUM(familyPlan) familyPlan, "
				+ "SUM(transOther) transOther,SUM(finalStayHos) finalStayHos,SUM(actOpenBedDays) actOpenBedDays,round(SUM(actOpenBedDays) / '"+deptCode+"', 1) avgOpenBeds, "
				+ "SUM(actUseBedDays) actUseBedDays,SUM(outPatUseBedDays) outPatUseBedDays,"
				+ "decode(decode(SUM(outPatUseBedDays), 0, 0, SUM(total)), 0, null,  "
				+ "round(SUM(outPatUseBedDays) / SUM(total), 1)) outAvgInDay,"
				+ "(case when to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss') >= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss') then  "
				+ "decode(decode((SUM(cure) + SUM(others) + SUM(childBirth) + SUM(familyPlan)), 0, 0, SUM(total)),0, null,  "
				+ "round(((SUM(cure) + SUM(others) + SUM(childBirth) + SUM(familyPlan)) / SUM(total)) * 100, 1))  else  "
				+ "decode(decode(SUM(cure), 0, 0, SUM(subTotal)), 0, null, round(((SUM(cure)) / SUM(subTotal)) * 100, 1)) end) cureRate, "
				+ "decode(decode(SUM(better), 0, 0, SUM(total)), 0, null, round((SUM(better) / SUM(total)) * 100, 1)) betterRate, "
				+ "decode(decode(SUM(death), 0, 0, SUM(total)), 0, null, round((SUM(death) / SUM(total)) * 100, 1)) deathRate,  "
				+ "decode(round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0, null, round((SUM(total) + SUM(transOther)) / round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 1)) bedTurnNum, "
				+ "decode(round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0, null, round(SUM(actUseBedDays) / round(SUM(actOpenBedDays) / '"+deptCode+"', 1), 0)) avgBedWorkDay,  "
				+ "decode(SUM(actOpenBedDays), 0, null, round((SUM(actUseBedDays) / SUM(actOpenBedDays)) * 100, 1)) bedUseRate,  "
				+ "decode(decode(SUM(beforeOperDay), 0, 0, sum(operation)), 0, null, round((SUM(beforeOperDay) / sum(operation)), 3)) avgBeforeOperDay,  "
				+ "SUM(criPatient) criPatient, sum(addBedDays) addBedDays, sum(freeBedDays) freeBedDays, sum(hangBedDays) hangBedDays, sum(levelA)  levelA   "
				+ "FROM (select PARDEP_CODE, PARDEP_NAME, DEPT_CODE, DEPT_NAME, SORT_ID  from (select cg.arcbigdeptcode PARDEP_CODE, cg.arcbigdeptname PARDEP_NAME, "
				+ "cg.arcdeptcode DEPT_CODE, cg.arcdeptname DEPT_NAME, cg.arcdeptsortid SORT_ID, "
				+ "row_number() over(partition by cg.arcdeptcode, cg.arcdeptname order by cg.arcdeptsortid desc) as row_index   from t_arclog_inpatient_dayreport cg  "
				+ "where cg.date_stat >= to_date('"+startTime+"',  'yyyy-mm-dd HH24:mi:ss') and cg.date_stat <= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')  "
				+ "group by cg.arcdeptcode, cg.arcdeptname, cg.arcbigdeptcode, cg.arcbigdeptname, cg.arcdeptsortid) t  where 1=1  ");
				Map<String,String> paraMap=new HashMap<String,String>(); 		
				if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and t.dept_code in('"+deptCode+"')");
				}else{
					sql.append(" and t.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
				}
				sql.append("  and row_index = 1 order by SORT_ID) AA, "
				+ "(select t1.arcdeptname, sum(decode(date_stat, trunc(to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss')), bed_stand, 0)) firstBeds, "
				+ "sum(decode(date_stat, trunc(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')), bed_stand,  0)) finalBeds, "
				+ "sum(decode(date_stat, trunc(to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss')), beginning_num, 0)) firstStayHos, "
				+ "sum(t1.in_normal) withinInHos, sum(t1.in_transfer) otherTransIn, sum(t1.out_transfer) transOther,  "
				+ "sum(decode(date_stat, trunc(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss')), end_num,  0)) finalStayHos,  "
				+ "sum(t1.bed_stand) actOpenBedDays, sum(t1.end_num) actUseBedDays, sum(t1.heavy_num + t1.danger_num) criPatient, "
				+ "sum(case when (t1.end_num - t1.QIGUANQIEKAINUM - t1.bed_stand) > 0 then (t1.end_num - t1.QIGUANQIEKAINUM - t1.bed_stand) else  0 end) addBedDays, "
				+ "sum(case when t1.bed_stand - t1.end_num > 0 then t1.bed_stand - t1.end_num else 0 end) freeBedDays,  "
				+ "sum(t1.QIGUANQIEKAINUM) hangBedDays,sum(t1.ONELVCARENUM) levelA  from t_arclog_inpatient_dayreport t1  "
				+ "where t1.date_stat >= to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss') and t1.date_stat <= to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss') "
				+ "group by t1.arcdeptname) BB, (select arcdeptname, sum(total) total, sum(subTotal) subTotal, sum(cure) cure, sum(better) better,  "
				+ "sum(notCure) notCure, sum(death) death, sum(familyPlan) familyPlan, sum(childBirth) childBirth, "
				+ "(sum(total) - sum(subTotal) - sum(familyPlan) - sum(childBirth)) others, sum(diagnosis) diagnosis, "
				+ "sum(beforeOperDay) beforeOperDay, sum(operation) operation, sum(outPatUseBedDays) outPatUseBedDays   "
				+ "from (SELECT t2.arcdeptname, count(distinct t2.inpatient_no) total, sum(DECODE(t2.zg, '1', 1, '2', 1, '3', 1, '4', 1, 0)) subTotal,  "
				+ "sum(DECODE(t2.zg, '1', 1, 0)) cure,  sum(DECODE(t2.zg, '2', 1, 0)) better, sum(DECODE(t2.zg, '3', 1, 0)) notCure,  "
				+ "sum(DECODE(t2.zg, '4', 1, 0)) death, sum(DECODE(t2.zg, '6', 1, 0)) familyPlan,  sum(DECODE(t2.zg, '7', 1, 0)) childBirth, 0 diagnosis,  "
				+ "(select sum(CB.beforeoper_days) from (select p.*, row_number() over(partition by p.inpatient_no order by p.oper_type desc) as row_index  "
				+ "from t_operation_detail p  where p.stat_flag = '0') CB  where CB.row_index = 1  and CB.inpatient_no = t2.inpatient_no) beforeOperDay,  "
				+ "sum(case when t2.OPERATION_CODE is null or t2.OPERATION_CODE = '' then  0  else  1 end) operation, "
				+ "sum(t2.pi_days) outPatUseBedDays  FROM t_emr_arclog_base t2  "
				+ "WHERE trunc(T2.OUT_DATE) >= to_date('2017-03-01', 'yyyy-mm-dd HH24:mi:ss')  and trunc(t2.out_date) <= to_date('2017-04-01', 'yyyy-mm-dd HH24:mi:ss') "
				+ "and t2.case_stus in ('3', '4')  group by t2.arcdeptname, inpatient_no) group by arcdeptname) CC  "
				+ "where AA.DEPT_NAME = BB.arcdeptname and BB.arcdeptname = CC.arcdeptname(+)  and AA.DEPT_CODE in ('"+deptCode+"')  GROUP BY rollup(BB.arcdeptname)  "
				+ "ORDER BY nvl((select g.ARCDEPTSORTID  from (select ccg.arcdeptsortid,  ccg.arcdeptname, ccg.arclogyear,  ccg.arclogmonth  "
				+ "from t_arclog_inpatient_dayreport ccg  where ccg.arclogyear || ccg.arclogmonth >=  to_char(to_date('"+startTime+"', 'yyyy-mm-dd HH24:mi:ss'), 'yyyyMM')  "
				+ "and ccg.arclogyear || ccg.arclogmonth <= to_char(to_date('"+endTime+"', 'yyyy-mm-dd HH24:mi:ss'), 'yyyyMM') group by ccg.arcdeptsortid,  "
				+ "ccg.arcdeptname,  ccg.arclogyear,  ccg.arclogmonth) g where g.arcdeptname = BB.arcdeptname  and rownum = 1), nvl2(BB.arcdeptname, 9998, 9999) ");
		sql.append( ") "); 
		sql.append(")");
		return namedParameterJdbcTemplate.query(sql.toString(),paraMap ,new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("total");
			}
			
		}).get(0);
	}

	@Override
	public List<InpatientInfoTableVo> queryInpatientInfoTableForDB(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		
		BasicDBObject bdObject = new BasicDBObject();
		List<InpatientInfoTableVo> list=new ArrayList<InpatientInfoTableVo>();
		if(StringUtils.isNotBlank(deptCode)){
			BasicDBList deptList=new BasicDBList();
			String[] deptArr=deptCode.split(",");
			for(int i=0,len=deptArr.length;i<len;i++){
				deptList.add(new BasicDBObject("deptCode", deptArr[i]));
			}
			bdObject.append("$or", deptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("SSKSSSFJTJ", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			InpatientInfoTableVo vo=new  InpatientInfoTableVo();
			 dbCursor = cursor.next();
			 vo.setDeptName((String)dbCursor.get("deptName")); 
			 vo.setLevelA((String)dbCursor.get("levelA")); 
			list.add(vo);
			}
		return list;
	}
}
