package cn.honry.inner.statistics.internalCompare1.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.internalCompare1.dao.InnerCompareDao;
import cn.honry.inner.statistics.internalCompare1.vo.InternalCompare1Vo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.utils.DateUtils;
@Repository("innerCompareDao")
public class InnerCompareDaoImpl implements InnerCompareDao {
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表
	private final String[] registerMain = {"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public void initCompare(String menuAlias, String type,String date) {
		Date beginDate=new Date();
		menuAlias=menuAlias+"_MONTH_NOW";
		Date search=DateUtils.parseDateY_M_D(date.substring(0, 7)+"-01");
		String pattint=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.addMonth(search, 1), -1));
		String time=DateUtils.formatDateY_M(DateUtils.addYear(search,-1));
		List<String> tnL=wordLoadDocDao.returnInTables(time+"-01", pattint, registerMain, "MZ");
		List<String> tnL1=wordLoadDocDao.returnInTables(time+"-01", pattint, inpatientInfo, "ZY");
//		for(String deptcode:deptCodeList){
		StringBuffer buffer= new StringBuffer(1000);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			buffer.append("select deptName,AA.deptCode,district,nvl(registerCountPrev,0) registerCountPrev,nvl(registerCount,0) registerCount,");
			buffer.append("nvl(inHospitalCountPrev,0) inHospitalCountPrev,nvl(inHospitalCount,0) inHospitalCount,");
			buffer.append("nvl(outHospitalCountPrev,0) outHospitalCountPrev,nvl(outHospitalCount,0) outHospitalCount,");
			buffer.append("nvl(avgInpatientCountPrev,0) avgInpatientCountPrev,nvl(avgInpatientCount,0) avgInpatientCount,");
			buffer.append("nvl(bedTurnoverCount,0) bedTurnoverCount,nvl(bedTurnoverCountPrev,0) bedTurnoverCountPrev,");
			buffer.append("nvl(realBed,0) realBed,nvl(realUsedBed,0) realUsedBed,");
			buffer.append("nvl(prevRealBed,0) prevRealBed,nvl(prevRealUsedBed,0) prevRealUsedBed,");
			buffer.append("nvl(PrevNodeath,0) PrevNodeath,nvl(prevDeath,0) prevDeath,");
			buffer.append("nvl(nodeath,0) nodeath,nvl(death,0) death ");
			
			buffer.append("from ");
			buffer.append("(SELECT d.DEPT_NAME as deptName,d.DEPT_CODE as deptCode,d.DEPT_AREA_CODE as district FROM T_DEPARTMENT d ) AA ,");
			buffer.append("(SELECT count(1) as registerCountPrev,DEPT_CODE deptCode  FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,REG_DATE ");
				buffer.append("from ").append(tnL.get(i));
			}
			buffer.append(") WHERE TO_CHAR(REG_DATE,'YYYY-MM') = '"+time+"' group by DEPT_CODE) BB,");
			buffer.append("(SELECT count(1)  as registerCount,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,REG_DATE ");
				buffer.append("from ").append(tnL.get(i));
			}
			buffer.append(" )WHERE TO_CHAR(REG_DATE,'YYYY-MM') = '"+date+"'  group by DEPT_CODE) CC,");
			buffer.append("(SELECT count(1) as inHospitalCountPrev,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,IN_DATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE TO_CHAR(IN_DATE,'YYYY-MM') = '"+time+"'  group by DEPT_CODE ) DD,");
			buffer.append("(SELECT count(1) as inHospitalCount,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,IN_DATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE TO_CHAR(IN_DATE,'YYYY-MM') = '"+date+"'  group by DEPT_CODE) EE ,");
			buffer.append("(SELECT count(1)  as outHospitalCountPrev,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,OUT_DATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE TO_CHAR(OUT_DATE,'YYYY-MM') = '"+time+"'  group by DEPT_CODE ) FF,");
			buffer.append("(SELECT count(1)  as outHospitalCount,DEPT_CODE deptCode  FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,OUT_DATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE TO_CHAR(OUT_DATE,'YYYY-MM') = '"+date+"'  group by DEPT_CODE) GG,"); 
			buffer.append("(SELECT ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1) as avgInpatientCountPrev ,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,OUT_DATE,IN_DATE,IN_STATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM')  ='"+time+"'  group by DEPT_CODE) HH,");
			buffer.append("(SELECT ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1)  as avgInpatientCount,DEPT_CODE deptCode FROM (");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("SELECT DEPT_CODE,OUT_DATE,IN_DATE,IN_STATE ");
				buffer.append("from ").append(tnL1.get(i));
			}
			buffer.append(") WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM')  = '"+date+"'  group by DEPT_CODE ) JJ,");
			buffer.append("(select sum(t1.out_transfer)  as bedTurnoverCount, t1.DEPT_CODE deptCode from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+date+"' group by  t1.DEPT_CODE) OO,");
			buffer.append("(select sum(t1.out_transfer) as bedTurnoverCountPrev, t1.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+time+"' group by  t1.DEPT_CODE) PP ,");
			buffer.append("(select sum(t1.bed_stand) realBed, sum(t1.end_num) realUsedBed,t1.DEPT_CODE deptCode from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+date+"' group by t1.DEPT_CODE) QQ,");
			buffer.append("(select sum(t1.bed_stand) prevRealBed,t1.DEPT_CODE deptCode ,sum(t1.end_num) prevRealUsedBed  from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT,'YYYY-MM') = '"+time+"'  group by t1.DEPT_CODE) WW,");
			buffer.append("(select sum(t.OUT_UNCURE+t.OUT_better+t.OUT_other+t.OUT_CURE) PrevNodeath ,sum(t.OUT_DEATH) prevDeath,t.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t where TO_CHAR(t.DATE_STAT, 'YYYY-MM') = '"+time+"' group by t.DEPT_CODE ) YY,");
			buffer.append("(select sum(t.OUT_UNCURE+t.OUT_better+t.OUT_other+t.OUT_CURE) nodeath ,sum(t.OUT_DEATH) death,t.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t where TO_CHAR(t.DATE_STAT, 'YYYY-MM') = '"+date+"' group by t.DEPT_CODE ) ZZ ");
			buffer.append("where AA.deptCode =BB.deptCode (+)  and AA.deptCode =CC.deptCode(+) and AA.deptCode =DD.deptCode(+) ");
			buffer.append("and AA.deptCode =EE.deptCode (+) and AA.deptCode =FF.deptCode (+) and AA.deptCode =GG.deptCode (+) ");
			buffer.append("and AA.deptCode =HH.deptCode(+) and AA.deptCode =JJ.deptCode(+) and AA.deptCode =OO.deptCode(+) ");
			buffer.append("and AA.deptCode =PP.deptCode (+) and AA.deptCode =QQ.deptCode (+) and AA.deptCode =WW.deptCode (+) and AA.deptCode=YY.deptCode (+) and AA.deptCode =ZZ.deptCode(+) ");
	
			List<InternalCompare1Vo> voList = namedParameterJdbcTemplate.query(buffer.toString(), paraMap, new RowMapper<InternalCompare1Vo>(){
				@Override
				public InternalCompare1Vo mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					
					InternalCompare1Vo vo = new InternalCompare1Vo();
					vo.setDeptName(rs.getString("deptName"));
					vo.setDeptCode(rs.getString("deptCode"));
					vo.setDistrict(rs.getString("district"));
					int registerCountPrev = rs.getInt("registerCountPrev");//门诊量 去年
					int registerCount = rs.getInt("registerCount");//门诊量 今年
					vo.setRegisterCountPrev(registerCountPrev);
					vo.setRegisterCount(registerCount);
					int inHospitalCountPrev = rs.getInt("inHospitalCountPrev");//入院人数去年
					int inHospitalCount = rs.getInt("inHospitalCount");//入院人数 今年
					vo.setInHospitalCountPrev(inHospitalCountPrev);
					vo.setInHospitalCount(inHospitalCount);
					int outHospitalCountPrev = rs.getInt("outHospitalCountPrev");//出院人数（上一年）
					int outHospitalCount = rs.getInt("outHospitalCount");//出院人数（当年）
					vo.setOutHospitalCountPrev(outHospitalCountPrev);
					vo.setOutHospitalCount(outHospitalCount);
					double avgInpatientCountPrev = rs.getDouble("avgInpatientCountPrev");//平均住院天数（上一年）
					double avgInpatientCount = rs.getDouble("avgInpatientCount");//平均住院天数（当年）
					vo.setAvgInpatientCountPrev(avgInpatientCountPrev);
					vo.setAvgInpatientCount(avgInpatientCount);
					Integer bedTurnoverCountPrev = rs.getInt("bedTurnoverCountPrev");//病床周转次数（上年）
					Integer bedTurnoverCount = rs.getInt("bedTurnoverCount");//病床周转次数（当年）
					vo.setBedTurnoverCount(bedTurnoverCount);
					vo.setBedTurnoverCountPrev(bedTurnoverCountPrev);
					int prevRealUsedBed=rs.getInt("prevRealUsedBed");//使用病床(上年)
					int prevRealBed=rs.getInt("prevRealBed");//病床(上年)
					vo.setPrevRealBed(prevRealBed);
					vo.setPrevRealUsedBed(prevRealUsedBed);
					int realUsedBed=rs.getInt("realUsedBed");//使用病床(当年)
					int realBed=rs.getInt("realBed");//病床(当年)
					vo.setRealUsedBed(realUsedBed);
					vo.setRealBed(realBed);
					int prevNodeath=rs.getInt("PrevNodeath");//危重未死（上年）
					vo.setPrevNodeath(prevNodeath);
					int prevDeath=rs.getInt("prevDeath");//危重死亡（上年）
					vo.setPrevDeath(prevDeath);
					int nodeath=rs.getInt("nodeath");//危重未死（当年）
					vo.setNodeath(nodeath);
					int death=rs.getInt("death");//危重死亡（当年）
					vo.setDeath(death);
					return vo;
				}			
			});
			DBObject query = new BasicDBObject();
			query.put("time", date);
			new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
			List<DBObject> userList = new ArrayList<DBObject>();
			for(InternalCompare1Vo vo:voList){
				BasicDBObject bdObject1 = new BasicDBObject();
//				bdObject1.append("fiCode", deptcode);
				bdObject1.append("time", date);
				bdObject1.append("deptName", vo.getDeptName());
				bdObject1.append("deptCode", vo.getDeptCode());
				bdObject1.append("district", vo.getDistrict());
				bdObject1.append("registerCountPrev", vo.getRegisterCountPrev());
				bdObject1.append("registerCount", vo.getRegisterCount());
				bdObject1.append("inHospitalCountPrev", vo.getInHospitalCountPrev());
				bdObject1.append("inHospitalCount", vo.getInHospitalCount());
				bdObject1.append("outHospitalCountPrev", vo.getOutHospitalCountPrev());
				bdObject1.append("outHospitalCount", vo.getOutHospitalCount());
				bdObject1.append("avgInpatientCountPrev", vo.getAvgInpatientCountPrev());
				bdObject1.append("avgInpatientCount", vo.getAvgInpatientCount());
				bdObject1.append("bedTurnoverCountPrev", vo.getBedTurnoverCountPrev());
				bdObject1.append("bedTurnoverCount", vo.getBedTurnoverCount());
				bdObject1.append("prevRealUsedBed", vo.getPrevRealUsedBed());
				bdObject1.append("prevRealBed", vo.getPrevRealBed());
				bdObject1.append("realUsedBed", vo.getRealUsedBed());
				bdObject1.append("realBed", vo.getRealBed());
				bdObject1.append("PrevNodeath", vo.getPrevNodeath());
				bdObject1.append("prevDeath", vo.getPrevDeath());
				bdObject1.append("nodeath", vo.getNodeath());
				bdObject1.append("death", vo.getDeath());
				userList.add(bdObject1);
			}
			new MongoBasicDao().insertDataByList(menuAlias, userList);
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, userList, date);
//		}
		
	}

}
