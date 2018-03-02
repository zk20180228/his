package cn.honry.statistics.deptstat.deptSupervision.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.deptSupervision.dao.DeptSupervisionDao;
import cn.honry.statistics.deptstat.deptSupervision.vo.MonitorIndicatorsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Repository("deptSupervisionDao")
@SuppressWarnings("all")
public class DeptSupervisionDaoImpl implements DeptSupervisionDao {
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private CodeInInterDAO innerCodeDao;
	@Override
	public List<MonitorIndicatorsVo> queryDayReport(final String begin,final String end, String depts,
			String menuAlias, String campus) throws Exception {
		StringBuffer buffer=new StringBuffer(1500);
		//上期开始时间
		final String beforBegin= DateUtils.formatDateY_M_D((DateUtils.addYear(DateUtils.parseDateY_M_D(begin), -1)));
		//上期结束时间
		final String beforEnd=DateUtils.formatDateY_M_D((DateUtils.addYear(DateUtils.parseDateY_M_D(end), -1)));
		List<Map<String,String>> listDate=new ArrayList<Map<String,String>>();
		listDate.add(new HashMap<String, String>(1){{
				put(begin,end);
			}
		});
		listDate.add(new HashMap<String, String>(1){{
			put(beforBegin,beforEnd);
			}
		});
		buffer.append("Select DEPT_NAME As deptCode,");
		buffer.append("nowOutCases1 As nowOutCases,nowOutCases2 As beforOutCases,outDecrease3 As outDecrease,");
		buffer.append("nowDiagnosCases48 As nowDiagnosCases,beforDiagnosCases49 As beforDiagnosCases,outDecrease50 As diagnosDecrease,");
		buffer.append("nowInhCases4 As nowInhCases,beforInhCases5 As beforInhCases,outDecrease6 As inhDecrease,");
		buffer.append("nowOutHostCases7 As nowOutHostCases,beforOutHostCases8 As beforOutHostCases,outDecrease9 As outHostDecrease,");
		buffer.append("nowCriticalCases10 As nowCriticalCases,beforCriticalCases11 As beforCriticalCases,outDecrease12 As criticalDecrease,");
		buffer.append("nowRescueCases13 As nowRescueCases,beforRescueCases14 As beforRescueCases,outDecrease15 As rescueDecrease,");
		buffer.append("nowSurgicalCases16 As nowSurgicalCases,beforSurgicalCases17 As beforSurgicalCases,outDecrease18 As surgicalDecrease,");
		buffer.append("nowDaysCases19 As nowDaysCases,beforDaysCases20 As beforDaysCases,outDecrease21 As daysDecrease,");
		buffer.append("nowCureCases22 As nowCureCases,beforCureCases23 As beforCureCases,outDecrease24 As cureDecrease,");
		buffer.append("nowUnCureCases25 As nowUnCureCases,beforUnCureCases26 As beforUnCureCases,outDecrease27 As unCureDecrease,");
		buffer.append("nowBetterCases28 As nowBetterCases,beforBetterCases29 As beforBetterCases,outDecrease30 As betterDecrease,");
		buffer.append("nowBedUsedCases31 As nowBedUsedCases,beforBedUsedCases32 As beforBedUsedCases,outDecrease32 As bedUsedDecrease,");
		buffer.append("nowTurnsCases39 As nowTurnsCases,beforTurnsCases40 As beforTurnsCases,outDecrease41 As turnsDecrease,");
		buffer.append("nowBedWorkCases42 As nowBedWorkCases,beforBedWorkCases43 As beforBedWorkCases,outDecrease44 As bedWorkDecrease,");
		buffer.append("nowExpensesCases33 As nowExpensesCases,beforExpensesCases34 As beforExpensesCases,outDecrease35 As expensesDecrease,");
		buffer.append("nowDeathCases36 As nowDeathCases,beforDeathCases37 As beforDeathCases,outDecrease38 As deathDecrease,");
		buffer.append("outDecrease47 As returnRate ");
		buffer.append("From ( ");
		
			buffer.append("select jj.dept_code DEPT_NAME,jj.outCases as nowOutCases1,kk.outCases nowOutCases2,(jj.outCases - kk.outCases) outDecrease3,");
			buffer.append("jj.diagnosCases as nowDiagnosCases48, kk.diagnosCases as beforDiagnosCases49,(jj.diagnosCases - kk.diagnosCases ) outDecrease50,");
			buffer.append("jj.inhCases nowInhCases4,kk.inhCases beforInhCases5,(jj.inhCases - kk.inhCases) outDecrease6,");
			buffer.append("jj.outHostCases nowOutHostCases7,kk.outHostCases beforOutHostCases8,(jj.outHostCases - kk.outHostCases) outDecrease9,");
			buffer.append("jj.criticalCases nowCriticalCases10,kk.criticalCases beforCriticalCases11,(jj.criticalCases - kk.criticalCases) outDecrease12,");
			buffer.append("jj.rescueCases nowRescueCases13,kk.rescueCases beforRescueCases14,(jj.rescueCases - kk.rescueCases) outDecrease15,");
			buffer.append("jj.surgicalCases nowSurgicalCases16,kk.surgicalCases beforSurgicalCases17,(jj.surgicalCases - kk.surgicalCases) outDecrease18,");
			buffer.append("jj.daysCases nowDaysCases19,kk.daysCases beforDaysCases20,(jj.daysCases - kk.daysCases) outDecrease21,");
			buffer.append("jj.cureCases nowCureCases22,kk.cureCases beforCureCases23,(jj.cureCases - kk.cureCases) outDecrease24,");
			buffer.append("jj.unCureCases nowUnCureCases25,kk.unCureCases beforUnCureCases26,(jj.unCureCases - kk.unCureCases) outDecrease27,");
			buffer.append("jj.betterCases nowBetterCases28,kk.betterCases beforBetterCases29,(jj.betterCases - kk.betterCases) outDecrease30,");
			buffer.append("jj.bedUsedCases nowBedUsedCases31,kk.bedUsedCases beforBedUsedCases32,(jj.bedUsedCases - kk.bedUsedCases) outDecrease32,");
			buffer.append("jj.turnsCases nowTurnsCases39,kk.turnsCases beforTurnsCases40,(jj.turnsCases-kk.turnsCases) outDecrease41,");
			buffer.append("jj.bedWorkCases nowBedWorkCases42,kk.bedWorkCases beforBedWorkCases43,(jj.bedWorkCases-kk.bedWorkCases) outDecrease44,");
			buffer.append("jj.expensesCases nowExpensesCases33,kk.expensesCases beforExpensesCases34,(jj.expensesCases - kk.expensesCases) outDecrease35,");
			buffer.append("jj.deathCases nowDeathCases36,kk.deathCases beforDeathCases37,(jj.deathCases - kk.deathCases) outDecrease38,");
			buffer.append("null as outDecrease47 ");
			
			buffer.append("from  ");
			int i=0;
			Map<String,String> map=new HashMap<String,String>();
			for(Map<String,String> keys:listDate){
				for(String key:keys.keySet()){
			buffer.append("(SELECT bb.arcdeptcode,AA.DEPT_CODE DEPT_CODE,(select xxk.ygbqmc from t_drug_proportion xxk where xxk.bqbm =bb.arcdeptcode) prjname,");
			buffer.append("sum(outCases) outCases,decode(sum(docNum),0,0,round(SUM(outCases) / SUM(docNum),1)) diagnosCases,SUM(duringHos) inhCases,");
			buffer.append("SUM(totalCount) outHostCases,sum(critical) criticalCases,sum(rescue) rescueCases,sum(surgicalCases) surgicalCases,");
			buffer.append("decode(decode(SUM(outTotalBed),0,0,SUM(totalCount)), 0, 0, round(SUM(outTotalBed) / SUM(totalCount), 1)) daysCases,");
			buffer.append("decode(decode(SUM(cure),0,0,SUM(subtotal)), 0, 0, round(((SUM(cure)) / SUM(subtotal)) * 100, 1)) cureCases,");
			buffer.append("decode(decode(SUM(death),0,0,SUM(totalCount)), 0, 0, round((SUM(death) / SUM(totalCount)) * 100, 1)) unCureCases,");
			buffer.append("decode(decode(SUM(better),0,0,SUM(totalCount)), 0, 0, round((SUM(better) / SUM(totalCount)) * 100, 1)) betterCases,");
			buffer.append("decode(SUM(realBed),0,0,round((SUM(realUsedBed) / SUM(realBed)) * 100, 1)) bedUsedCases,");
			buffer.append("decode(round(SUM(realBed) / 31, 1),0, 0,round((SUM(totalCount) + SUM(turnOtherDept)) /round(SUM(realBed) / 31, 1),1)) turnsCases,");
			buffer.append("decode(round(SUM(realBed) / 31, 1),0,0,round(SUM(realUsedBed) /round(SUM(realBed) / 31, 1),0)) bedWorkCases,");
			buffer.append("decode(SUM(totalMoney),0,0,round((sum(totalMoney)/SUM(totalCount)),2)) expensesCases ,");
			buffer.append("sum(death) deathCases FROM ( ");
			
				buffer.append("select t.* from (select cg.arcbigdeptcode PARDEP_CODE,cg.arcbigdeptname PARDEP_NAME,");
				buffer.append("cg.arcdeptcode DEPT_CODE,cg.arcdeptname DEPT_NAME,cg.arcdeptsortid SORT_ID ");
				buffer.append("from t_ARCLOG_inpatient_dayreport cg ");
				buffer.append("where cg.date_stat >=TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and cg.date_stat <=  TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ) t ) AA,");
				
				buffer.append("(select t1.arcdeptcode, t1.arcdeptname,sum(t1.in_normal) duringHos,sum(t1.in_transfer) otherDept,");
				buffer.append("sum(t1.out_transfer) turnOtherDept,sum(t1.bed_stand) realBed,sum(t1.end_num) realUsedBed,");
				buffer.append("sum(t1.heavy_num + t1.danger_num) critical,sum(t1.salve_num) rescue ");
				buffer.append("from t_ARCLOG_inpatient_dayreport t1 ");
				buffer.append("where t1.date_stat >= TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t1.date_stat <= TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("group by t1.arcdeptcode,t1.arcdeptname) BB,");
				
				buffer.append("(select arcdeptname,sum(totalCount) totalCount,sum(subtotal) subtotal,sum(cure) cure,sum(better) better,sum(unCure) unCure,");
				buffer.append("sum(death) death,sum(surgicalNum) surgicalNum,sum(outTotalBed) outTotalBed ");
				buffer.append("from (SELECT t2.arcdeptname,count(distinct t2.inpatient_no) totalCount,");
				buffer.append("sum(DECODE(t2.zg, '1', 1,'2',1,'3',1,'4',1,0)) subtotal,");//subtotal
				buffer.append("sum(DECODE(t2.zg, '1', 1, 0)) cure,");//--cure
				buffer.append("sum(DECODE(t2.zg, '2', 1, 0)) better,");// --better
				buffer.append("sum(DECODE(t2.zg, '3', 1, 0)) unCure,");// --未cure
				buffer.append("sum(DECODE(t2.zg, '4', 1, 0)) death,");// --death  
				buffer.append("sum(case when t2.OPERATION_CODE is null or t2.OPERATION_CODE = '' then 0 else 1 end) surgicalNum,");
				buffer.append("sum(t2.pi_days) outTotalBed ");
				buffer.append("FROM t_emr_arclog_base t2 ");
				buffer.append("WHERE trunc(T2.OUT_DATE) >=TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and trunc(t2.out_date) <= TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and t2.case_stus in('3','4') ");
				buffer.append("group by t2.arcdeptname, inpatient_no) group by arcdeptname) CC,");
				
				buffer.append("(select arcdeptcode, arcdeptname, sum(surgicalCases) surgicalCases, sum(totalMoney) totalMoney ");
				buffer.append("from (select b.arcdeptcode,b.arcdeptname,b.inpatient_no,");
				buffer.append("(select count(1) from t_operation_detail p where p.inpatient_no=b.inpatient_no group by  p.inpatient_no) surgicalCases,");
				buffer.append("(select sum(t.tot_cost + t.balance_cost) from t_inpatient_info t ");
				buffer.append("where b.inpatient_no = t.inpatient_no) totalMoney ");
				buffer.append("from t_emr_arclog_base b ");
				buffer.append("where trunc(b.OUT_DATE) >= TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("and trunc(b.out_date) <= TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ) group by arcdeptcode, arcdeptname) DD,");
				
				buffer.append("(select cg.arcdeptcode,cg.arcdeptname,");
				buffer.append("(select  Sum(d.allnum) allnum FROM T_OUTPATIENT_DAYREPORT  d WHERE D.datestat >=TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') AND D.datestat <= TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("AND EXISTS (select e.employee_jobno from T_employee e  where e.dept_ID = cg.arcdeptcode and E.employee_Type = 'D' and e.employee_jobno = d.doc_code) ) outCases,");
				buffer.append("(select count(1) from T_Employee CE  where CE.dept_ID = cg.arcdeptcode and  CE.Employee_Type = 'D') docNum from t_ARCLOG_inpatient_dayreport cg ");
				buffer.append("where cg.date_stat >=TO_DATE('"+key+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and cg.date_stat <=  TO_DATE('"+keys.get(key)+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("group by cg.arcdeptcode,cg.arcdeptname,cg.arcbigdeptcode,cg.arcbigdeptname,cg.arcdeptsortid  )EE ");
				
				buffer.append("where  AA.DEPT_NAME=BB.arcdeptname and ");
				buffer.append("BB.arcdeptname = CC.arcdeptname(+)  ");
				
				if(StringUtils.isNotBlank(depts)){//如果科室不为空 查科室
					
					buffer.append("and BB.arcdeptcode in ('").append(depts.replace(",", "','")).append("') ");
				}else if(StringUtils.isNotBlank(campus)){//如果院区不为空  查院区下科室
					
					buffer.append("and BB.arcdeptcode in (select d.dept_code as code from t_department d  where d.hospital_id in('").append(campus.replace(",", "','")).append("') )");
				}else{//查询授权科室
					buffer.append("and BB.arcdeptcode in (").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				
				buffer.append("and BB.arcdeptname =dd.arcdeptname ");
				buffer.append("and BB.arcdeptname =EE.arcdeptname ");
				if(i==0){
					buffer.append("GROUP BY bb.arcdeptcode,BB.arcdeptname,AA.DEPT_CODE) jj left  join ");//此处应循环
					i++;
				}else{
					buffer.append(" GROUP BY bb.arcdeptcode,BB.arcdeptname,AA.DEPT_CODE) kk on jj.arcdeptcode = kk.arcdeptcode )");
				}
				
			}
		}
		List<MonitorIndicatorsVo> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<MonitorIndicatorsVo>() {

			@Override
			public MonitorIndicatorsVo mapRow(ResultSet rs, int arg1)
					throws SQLException {
				MonitorIndicatorsVo vo=new MonitorIndicatorsVo();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setNowOutCases(rs.getInt("nowOutCases"));
				vo.setBeforOutCases(rs.getInt("beforOutCases"));
				vo.setOutDecrease(rs.getDouble("outDecrease"));
				
				vo.setNowDiagnosCases(rs.getInt("nowDiagnosCases"));
				vo.setBeforDiagnosCases(rs.getInt("beforDiagnosCases"));
				vo.setDiagnosDecrease(rs.getDouble("diagnosDecrease"));
				
				vo.setNowInhCases(rs.getInt("nowInhCases"));
				vo.setBeforInhCases(rs.getInt("beforInhCases"));
				vo.setInhDecrease(rs.getDouble("inhDecrease"));
				
				vo.setNowOutHostCases(rs.getInt("nowOutHostCases"));
				vo.setBeforOutHostCases(rs.getInt("beforOutHostCases"));
				vo.setOutHostDecrease(rs.getDouble("outHostDecrease"));
				
				vo.setNowCriticalCases(rs.getInt("nowCriticalCases"));
				vo.setBeforCriticalCases(rs.getInt("beforCriticalCases"));
				vo.setCriticalDecrease(rs.getDouble("criticalDecrease"));
				
				vo.setNowRescueCases(rs.getInt("nowRescueCases"));
				vo.setBeforRescueCases(rs.getInt("beforRescueCases"));
				vo.setRescueDecrease(rs.getDouble("rescueDecrease"));
				
				vo.setNowSurgicalCases(rs.getInt("nowSurgicalCases"));
				vo.setBeforSurgicalCases(rs.getInt("beforSurgicalCases"));
				vo.setSurgicalDecrease(rs.getDouble("surgicalDecrease"));
				
				vo.setNowDaysCases(rs.getInt("nowDaysCases"));
				vo.setBeforDaysCases(rs.getInt("beforDaysCases"));
				vo.setDaysDecrease(rs.getDouble("daysDecrease"));
				
				vo.setNowCureCases(rs.getInt("nowCureCases"));
				vo.setBeforCureCases(rs.getInt("beforCureCases"));
				vo.setCureDecrease(rs.getDouble("cureDecrease"));
				
				vo.setNowUnCureCases(rs.getInt("nowUnCureCases"));
				vo.setBeforUnCureCases(rs.getInt("beforUnCureCases"));
				vo.setUnCureDecrease(rs.getDouble("unCureDecrease"));
				
				vo.setNowBetterCases(rs.getInt("nowBetterCases"));
				vo.setBeforBetterCases(rs.getInt("beforBetterCases"));
				vo.setBetterDecrease(rs.getDouble("betterDecrease"));
				
				vo.setNowBedUsedCases(rs.getInt("nowBedUsedCases"));
				vo.setBeforBedUsedCases(rs.getInt("beforBedUsedCases"));
				vo.setBedUsedDecrease(rs.getDouble("bedUsedDecrease"));
				
				vo.setNowTurnsCases(rs.getInt("nowTurnsCases"));
				vo.setBeforTurnsCases(rs.getInt("beforTurnsCases"));
				vo.setTurnsDecrease(rs.getDouble("turnsDecrease"));
				
				vo.setNowBedWorkCases(rs.getInt("nowBedWorkCases"));
				vo.setBeforBedWorkCases(rs.getInt("beforBedWorkCases"));
				vo.setBedWorkDecrease(rs.getDouble("bedWorkDecrease"));
				
				vo.setNowExpensesCases(rs.getDouble("nowExpensesCases"));
				vo.setBeforExpensesCases(rs.getDouble("beforExpensesCases"));
				vo.setExpensesDecrease(rs.getDouble("expensesDecrease"));
				
				vo.setNowDeathCases(rs.getInt("nowDeathCases"));
				vo.setBeforDeathCases(rs.getInt("beforDeathCases"));
				vo.setDeathDecrease(rs.getDouble("deathDecrease"));
				
				vo.setReturnRate(rs.getInt("returnRate"));
				
				return vo;
			}
			
		});
		
		if(list.size()>0){
			return list;
		}
		return new ArrayList<MonitorIndicatorsVo>();
	}

}
