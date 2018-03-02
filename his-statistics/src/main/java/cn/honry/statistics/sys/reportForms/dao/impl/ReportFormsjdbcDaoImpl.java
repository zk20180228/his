package cn.honry.statistics.sys.reportForms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsjdbcDao;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.statistics.util.SortMapUtils;
import cn.honry.utils.DateUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * ReportFormsjdbcDao实现
 * @author hzr
 *
 */
@Repository("reportFormsjdbcDao")
@SuppressWarnings({ "all" })
public class ReportFormsjdbcDaoImpl implements ReportFormsjdbcDao {
	

	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	// 把数据存入mongodb中
	public static final String TABLENAME = "MZGXSRTJ_DAY";//门诊各项收入统计日表
	public static final String TABLENAME_MONTH = "MZGXSRTJ_MONTH";//门诊各项收入统计月表
	public static final String TABLENAME_YEAR = "MZGXSRTJ_YEAR";//门诊各项收入统计年表
	
	@Override
	public List<ReportVo>  getEncode() {
		Map<String,String> map = new HashMap<String,String>();
		String sql="SELECT m.MINFEE_CODE as minfeecode,m.FEE_STAT_CODE as feestatcode,m.FEE_STAT_NAME as feestatname FROM T_CHARGE_MINFEETOSTAT m WHERE m.REPORT_CODE = 'MZ01'";
		List<ReportVo> list =namedParameterJdbcTemplate.query(sql, new RowMapper<ReportVo>() {
			@Override
			public ReportVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportVo vo  = new ReportVo();
				vo.setMinfeecode(rs.getString("minfeecode"));
				vo.setFeestatcode(rs.getString("feestatcode"));
				vo.setFeestatname(rs.getString("feestatname"));
				return vo;
			}
			
		});
		
		return list;
	}
	
	public List<StatisticsVo> getlist(List<String> table,List<ReportVo> gcode,String dept,String expxrts, String stime, String etime){
		List<String> deptcode=null;
		List<StatisticsVo> list=null;
		if(dept!=null){
			deptcode = Arrays.asList(dept.split(","));
		}
		
		//检查费
		StringBuffer jcfcode = new StringBuffer();
		
		//治疗费
		StringBuffer zlfcode = new StringBuffer();
		
		//放射费
		StringBuffer fsfcode = new StringBuffer();
		
		//化验费
		StringBuffer hyfcode = new StringBuffer();
		
		//输血费
		StringBuffer sxfcode = new StringBuffer();
		
		//西药费
		StringBuffer xyfcode = new StringBuffer();
		
		//中成药费
		StringBuffer zcyfcode = new StringBuffer();
		
		//中草药费
		StringBuffer zcysfcode = new StringBuffer();
		for (ReportVo vo : gcode) {
			if(("07").equals(vo.getFeestatcode())){
				jcfcode.append(vo.getMinfeecode()+",");
			}
			if(("05").equals(vo.getFeestatcode())){
				zlfcode.append(vo.getMinfeecode()+",");
			}
			if(("08").equals(vo.getFeestatcode())){
				fsfcode.append(vo.getMinfeecode()+",");
			}
			if(("09").equals(vo.getFeestatcode())){
				hyfcode.append(vo.getMinfeecode()+",");
			}
			if(("01").equals(vo.getFeestatcode())){
				xyfcode.append(vo.getMinfeecode()+",");
			}
			if(("02").equals(vo.getFeestatcode())){
				zcyfcode.append(vo.getMinfeecode()+",");
			}
			if(("03").equals(vo.getFeestatcode())){
				zcysfcode.append(vo.getMinfeecode()+",");
			}
			if(("11").equals(vo.getFeestatcode())){
				sxfcode.append(vo.getMinfeecode()+",");
			}
			
		}
		StringBuffer sb = new StringBuffer();
		
			sb.append("	SELECT min(doct) doct,min(dept) dept,regDate, deptCode, docterCode,SUM(jcfkds) jcfkds,SUM(jcfje) jcfje,SUM(zlfkds) zlfkds,");
			sb.append("SUM(zlfje) zlfje,SUM(fsfkds) fsfkds,SUM(fsfje) fsfje,SUM(hyfkds) hyfkds,SUM(hyfje) hyfje,SUM(sxfkds) sxfkds,SUM(sxfje) sxfje,");
			sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0)-NVL(SUM(sxfkds),0)-NVL(SUM(hyfkds),0)-NVL(SUM(fsfkds),0)-NVL(SUM(zlfkds),0)-NVL(SUM(jcfkds),0) qtsrkds,");
			sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0)-NVL(SUM(sxfje),0)-NVL(SUM(hyfje),0)-NVL(SUM(fsfje),0)-NVL(SUM(zlfje),0)-NVL(SUM(jcfje),0) ylsfje,");
			sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0) ylsrkds,");
			sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0) MedicalCost,SUM(xyfkds) xyfkds,");
			sb.append("SUM(xyfje) xyfje,SUM(zcyfkds) zcyfkds,SUM(zcyfje) zcyfje,SUM(zcykds) zcykds,SUM(zcyje) zcyje,");
			sb.append("NVL(SUM(xyfkds),0)+NVL(SUM(zcyfkds),0)+NVL(SUM(zcykds),0) ypsrkds,");
			sb.append("NVL(SUM(xyfje),0)+NVL(SUM(zcyfje),0)+NVL(SUM(zcyje),0) ypsrkje,SUM(zkds) zkds,SUM(zje) zje");
			sb.append(" from (");
			if(table==null||table.size()<=0){
				list= new ArrayList<StatisticsVo>();
				StatisticsVo eachTotal = new StatisticsVo();
				eachTotal.setDept("合计");
				eachTotal.setInspectNum(0);
				eachTotal.setInspectCost(0.0);
				eachTotal.setTreatmentNum(0);
				eachTotal.setTreatmentCost(0.0);
				eachTotal.setRadiationNum(0);
				eachTotal.setRadiationCost(0.0);
				eachTotal.setBloodNum(0);
				eachTotal.setBloodCost(0.0);
				eachTotal.setTestNum(0);
				eachTotal.setTestCost(0.0);
				eachTotal.setOtherNum(0);
				eachTotal.setOtherCost(0.0);
				eachTotal.setMedicalNum(0);
				eachTotal.setMedicalCost(0.0);
				eachTotal.setWesternNum(0);
				eachTotal.setWesternCost(0.0);
				eachTotal.setChineseNum(0);
				eachTotal.setChineseCost(0.0);
				eachTotal.setHerbalNum(0);
				eachTotal.setHerbalCost(0.0);
				eachTotal.setAllNum(0);
				eachTotal.setAllCost(0.0);
				eachTotal.setTotle(0.0);;
				list.add(eachTotal);
				return list;
			}else{
						for (int i = 0; i < table.size(); i++) {
							if(i!=0){
								sb.append(" UNION ALL ");
							}
							
							//检查费开单数
							sb.append("SELECT min(e.EMPLOYEE_NAME) doct,min(p.DEPT_NAME) dept,TO_CHAR(f.REG_DATE,'yyyy-mm-dd') regDate,f.REG_DPCD deptCode,f.DOCT_CODE docterCode,DECODE(f.FEE_CODE, ");
							String[] splitkds = jcfcode.toString().split(",");
							for (String string : splitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0 ) jcfkds,");
							
							//检查费金额
							sb.append("DECODE(f.FEE_CODE, ");
							String[] splitje = jcfcode.toString().split(",");
							for (String string : splitje) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) jcfje,");
							
							//治疗费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] zlfsplitkds = zlfcode.toString().split(",");
							for (String string : zlfsplitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0) zlfkds,");
							
							//治疗费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] zlfsplitje = zlfcode.toString().split(",");
							for (String string : zlfsplitje) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) zlfje,");
							
							//放射费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] fsfsplitkds = fsfcode.toString().split(",");
							for (String string : fsfsplitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0) fsfkds,");
							
							//放射费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] fsfsplitjs = fsfcode.toString().split(",");
							for (String string : fsfsplitjs) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) fsfje,");
							
							//化验费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] hyfsplitkds = hyfcode.toString().split(",");
							for (String string : hyfsplitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0) hyfkds,");
							
							//化验费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] hyfsplitjs = hyfcode.toString().split(",");
							for (String string : hyfsplitjs) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) hyfje,");
							
							//输血费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] sxfsplitkds = sxfcode.toString().split(",");
							for (String string : sxfsplitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0) sxfkds,");
							
							//输血费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] sxfsplitjs = sxfcode.toString().split(",");
							for (String string : sxfsplitjs) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) sxfje,");
							
							//西药费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] xyfsplitkds = xyfcode.toString().split(",");
							for (String string : xyfsplitkds) {
								sb.append("'"+string).append("',1 ,");
							}
							sb.append("0) xyfkds,");
							
							//西药费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] xyfsplitje = xyfcode.toString().split(",");
							for (String string : xyfsplitje) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) xyfje,");
							
							//中成药开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] zcyfsplitkds = zcyfcode.toString().split(",");
							for (String string : zcyfsplitkds) {
								sb.append("'"+string).append("',1,");
							}
							sb.append("0) zcyfkds,");
							
							//中成药费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] zcyfsplitje = zcyfcode.toString().split(",");
							for (String string : zcyfsplitje) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) zcyfje,");
							
							//中草药费开单数
							sb.append("DECODE(f.FEE_CODE,");
							String[] zcysfsplitkds = zcysfcode.toString().split(",");
							for (String string : zcysfsplitkds) {
								sb.append("'"+string).append("',1,");
							}
							sb.append("0) zcykds,");
							
							//中草药费金额
							sb.append("DECODE(f.FEE_CODE,");
							String[] zcysfsplitje = zcysfcode.toString().split(",");
							for (String string : zcysfsplitje) {
								sb.append("'"+string+"'").append(",f.TOT_COST,");
							}
							sb.append("0) zcyje,");
							
							sb.append("COUNT(f.FEE_CODE) zkds,f.TOT_COST zje FROM  ");
							sb.append(table.get(i)).append(" f ");
							sb.append(" ,T_DEPARTMENT P,T_EMPLOYEE E ");
							sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 ");
							if(StringUtils.isNotBlank(dept)){
								sb.append(" AND f.REG_DPCD in (:dept)");
							}
							if(StringUtils.isNotBlank(expxrts)){
								sb.append(" AND f.DOCT_CODE in (:expxrts)");
							}
							if(StringUtils.isNotBlank(stime)){
								sb.append(" AND f.REG_DATE>=TO_DATE(:stime, 'yyyy-MM-dd') ");
							}
							if(StringUtils.isNotBlank(etime)){
								sb.append(" AND f.REG_DATE<TO_DATE(:etime, 'yyyy-MM-dd') ");
							}
							
							sb.append(" AND P.DEPT_CODE=F.REG_DPCD AND E.EMPLOYEE_JOBNO=F.DOCT_CODE ");
							sb.append(" GROUP BY F.REG_DPCD,F.DOCT_CODE,F.FEE_CODE,F.TOT_COST,F.REG_DATE ");
						}
							sb.append(" )GROUP BY DEPTCODE,DOCTERCODE,REGDATE ");
						
						Map<String, Object> paraMap = new HashMap<String, Object>();
						if(StringUtils.isNotBlank(dept)){
							paraMap.put("dept", deptcode);
						}
						if(StringUtils.isNotBlank(expxrts)){
							paraMap.put("expxrts", expxrts);
						}
						if(StringUtils.isNotBlank(stime)){
							paraMap.put("stime", stime);
						}
						if(StringUtils.isNotBlank(etime)){
							paraMap.put("etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(etime),1))));
						}
						list =namedParameterJdbcTemplate.query(sb.toString(), paraMap,new RowMapper<StatisticsVo>() {
							@Override
							public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
								StatisticsVo vo = new StatisticsVo();
								vo.setRegDate(rs.getString("regDate"));
								vo.setDeptCode(rs.getString("deptCode"));
								vo.setDocterCode(rs.getString("docterCode"));
								
								vo.setName(rs.getString("doct"));
								vo.setDept(rs.getString("dept"));
								vo.setInspectNum(rs.getInt("jcfkds"));
								vo.setInspectCost(rs.getDouble("jcfje"));
								vo.setTreatmentNum(rs.getInt("zlfkds"));
								vo.setTreatmentCost(rs.getDouble("zlfje"));
								vo.setRadiationNum(rs.getInt("fsfkds"));
								vo.setRadiationCost(rs.getDouble("fsfje"));
								vo.setBloodNum(rs.getInt("sxfkds"));
								vo.setBloodCost(rs.getDouble("sxfje"));
								vo.setTestNum(rs.getInt("hyfkds"));
								vo.setTestCost(rs.getDouble("hyfje"));
								vo.setOtherNum(rs.getInt("qtsrkds"));
								vo.setOtherCost(rs.getDouble("ylsfje"));
								vo.setMedicalNum(rs.getInt("ylsrkds"));
								vo.setMedicalCost(rs.getDouble("MedicalCost"));
								vo.setWesternNum(rs.getInt("xyfkds"));
								vo.setWesternCost(rs.getDouble("xyfje"));
								vo.setChineseNum(rs.getInt("zcyfkds"));
								vo.setChineseCost(rs.getDouble("zcyfje"));
								vo.setHerbalNum(rs.getInt("zcykds"));
								vo.setHerbalCost(rs.getDouble("zcyje"));
								vo.setAllNum(rs.getInt("ypsrkds"));
								vo.setAllCost(rs.getDouble("ypsrkje"));
								vo.setTotle(rs.getDouble("zje"));
								return vo;
							}
						});
						int totalInspectNum = 0;
						Double totalInspectCost = 0.0;
						int totalTreatmentNum = 0;
						Double totalTreatmentCost = 0.0;
						int totalRadiationNum = 0;
						Double totalRadiationCost = 0.0;
						int totalBloodNum = 0;
						Double totalBloodCost = 0.0;
						int totalTestNum = 0;
						Double totalTestCost = 0.0;
						int totalOtherNum = 0;
						Double totalOtherCost = 0.0;
						int totalMedicalNum = 0;
						Double totalMedicalCost = 0.0;
						int totalWesternNum = 0;
						Double totalWesternCost = 0.0;
						int totalChineseNum = 0;
						Double totalChineseCost = 0.0;
						int totalHerbalNum = 0;
						Double totalHerbalCost = 0.0;
						int totalAllNum = 0;
						Double totalAllCost = 0.0;
						Double totalTotle = 0.0;
						for(StatisticsVo svo : list){
							totalInspectNum += svo.getInspectNum();
							totalInspectCost += svo.getInspectCost();
							totalTreatmentNum += svo.getTreatmentNum();
							totalTreatmentCost += svo.getTreatmentCost();
							totalRadiationNum += svo.getRadiationNum();
							totalRadiationCost += svo.getRadiationCost();
							totalBloodNum += svo.getBloodNum();
							totalBloodCost += svo.getBloodCost();
							totalTestNum += svo.getTestNum();
							totalTestCost += svo.getTestCost();
							totalOtherNum += svo.getOtherNum();
							totalOtherCost += svo.getOtherCost();
							totalMedicalNum += svo.getMedicalNum();
							totalMedicalCost += svo.getMedicalCost();
							totalWesternNum += svo.getWesternNum();
							totalWesternCost += svo.getWesternCost();
							totalChineseNum += svo.getChineseNum();
							totalChineseCost += svo.getChineseCost();
							totalHerbalNum += svo.getHerbalNum();
							totalHerbalCost += svo.getHerbalCost();
							totalAllNum += svo.getAllNum();
							totalAllCost += svo.getAllCost();
							totalTotle += svo.getTotle();
						}
						StatisticsVo eachTotal = new StatisticsVo();
						eachTotal.setDept("合计");
						eachTotal.setInspectNum(totalInspectNum);
						eachTotal.setInspectCost(totalInspectCost);
						eachTotal.setTreatmentNum(totalTreatmentNum);
						eachTotal.setTreatmentCost(totalTreatmentCost);
						eachTotal.setRadiationNum(totalRadiationNum);
						eachTotal.setRadiationCost(totalRadiationCost);
						eachTotal.setBloodNum(totalBloodNum);
						eachTotal.setBloodCost(totalBloodCost);
						eachTotal.setTestNum(totalTestNum);
						eachTotal.setTestCost(totalTestCost);
						eachTotal.setOtherNum(totalOtherNum);
						eachTotal.setOtherCost(totalOtherCost);
						eachTotal.setMedicalNum(totalMedicalNum);
						eachTotal.setMedicalCost(totalMedicalCost);
						eachTotal.setWesternNum(totalWesternNum);
						eachTotal.setWesternCost(totalWesternCost);
						eachTotal.setChineseNum(totalChineseNum);
						eachTotal.setChineseCost(totalChineseCost);
						eachTotal.setHerbalNum(totalHerbalNum);
						eachTotal.setHerbalCost(totalHerbalCost);
						eachTotal.setAllNum(totalAllNum);
						eachTotal.setAllCost(totalAllCost);
						eachTotal.setTotle(totalTotle);;
						list.add(eachTotal);
						return list;
						
			}
	}

	@Override
	public List<IncomeVo> queryfeedetall(List<String> tlList, List<ReportVo> encode, String emp, String sDate, String eDate) {
		
		//检查费
		StringBuffer jcfcode = new StringBuffer();
		
		//治疗费
		StringBuffer zlfcode = new StringBuffer();
		
		//放射费
		StringBuffer fsfcode = new StringBuffer();
		
		//化验费
		StringBuffer hyfcode = new StringBuffer();
		
		//输血费
		StringBuffer sxfcode = new StringBuffer();
		
		//西药费
		StringBuffer xyfcode = new StringBuffer();
		
		//中成药费
		StringBuffer zcyfcode = new StringBuffer();
		
		//中草药费
		StringBuffer zcysfcode = new StringBuffer();
		for (ReportVo vo : encode) {
			if(("07").equals(vo.getFeestatcode())){
				jcfcode.append(vo.getMinfeecode()+",");
			}
			if(("05").equals(vo.getFeestatcode())){
				zlfcode.append(vo.getMinfeecode()+",");
			}
			if(("08").equals(vo.getFeestatcode())){
				fsfcode.append(vo.getMinfeecode()+",");
			}
			if(("09").equals(vo.getFeestatcode())){
				hyfcode.append(vo.getMinfeecode()+",");
			}
			if(("01").equals(vo.getFeestatcode())){
				xyfcode.append(vo.getMinfeecode()+",");
			}
			if(("02").equals(vo.getFeestatcode())){
				zcyfcode.append(vo.getMinfeecode()+",");
			}
			if(("03").equals(vo.getFeestatcode())){
				zcysfcode.append(vo.getMinfeecode()+",");
			}
			if(("11").equals(vo.getFeestatcode())){
				sxfcode.append(vo.getMinfeecode()+",");
			}
		}
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT regDate,SUM(jcfkds) jcfkds,SUM(jcfje) jcfje,SUM(zlfkds) zlfkds,SUM(zlfje) zlfje,");
			sb.append("SUM(fsfkds) fsfkds,SUM(fsfje) fsfje,SUM(hyfkds) hyfkds,SUM(hyfje) hyfje,SUM(sxfkds) sxfkds,SUM(sxfje) sxfje,");
			sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0)-NVL(SUM(sxfkds),0)-NVL(SUM(hyfkds),0)-NVL(SUM(fsfkds),0)-NVL(SUM(zlfkds),0)-NVL(SUM(jcfkds),0) qtsrkds,");
			sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0)-NVL(SUM(sxfje),0)-NVL(SUM(hyfje),0)-NVL(SUM(fsfje),0)-NVL(SUM(zlfje),0)-NVL(SUM(jcfje),0) ylsfje,");
			sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0) ylsrkds,");
			sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0) MedicalCost,");
			sb.append("SUM(xyfkds) xyfkds,SUM(xyfje) xyfje,SUM(zcyfkds) zcyfkds,SUM(zcyfje) zcyfje,SUM(zcykds) zcykds,SUM(zcyje) zcyje,");
			sb.append("NVL(SUM(xyfkds),0)+NVL(SUM(zcyfkds),0)+NVL(SUM(zcykds),0) ypsrkds,");
			sb.append("NVL(SUM(xyfje),0)+NVL(SUM(zcyfje),0)+NVL(SUM(zcyje),0) ypsrje,");
			sb.append("SUM(zkds) zkds,SUM(zje) zje FROM (");
			for (int i = 0; i < tlList.size(); i++) {
				if(i>0){
					sb.append(" UNION ALL ");
				}
				
				//检查费开单数
				sb.append("SELECT TO_CHAR(f.REG_DATE,'yyyy-MM') regDate,DECODE(f.FEE_CODE, ");
				String[] splitkds = jcfcode.toString().split(",");
				for (String string : splitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0 ) jcfkds,");
				
				//检查费金额
				sb.append("DECODE(f.FEE_CODE, ");
				String[] splitje = jcfcode.toString().split(",");
				for (String string : splitje) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) jcfje,");
				
				//治疗费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] zlfsplitkds = zlfcode.toString().split(",");
				for (String string : zlfsplitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0) zlfkds,");
				
				//治疗费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] zlfsplitje = zlfcode.toString().split(",");
				for (String string : zlfsplitje) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) zlfje,");
				
				//放射费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] fsfsplitkds = fsfcode.toString().split(",");
				for (String string : fsfsplitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0) fsfkds,");
				
				//放射费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] fsfsplitjs = fsfcode.toString().split(",");
				for (String string : fsfsplitjs) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) fsfje,");
				
				//化验费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] hyfsplitkds = hyfcode.toString().split(",");
				for (String string : hyfsplitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0) hyfkds,");
				
				//化验费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] hyfsplitjs = hyfcode.toString().split(",");
				for (String string : hyfsplitjs) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) hyfje,");
				
				//输血费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] sxfsplitkds = sxfcode.toString().split(",");
				for (String string : sxfsplitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0) sxfkds,");
				
				//输血费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] sxfsplitjs = sxfcode.toString().split(",");
				for (String string : sxfsplitjs) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) sxfje,");
				
				//西药费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] xyfsplitkds = xyfcode.toString().split(",");
				for (String string : xyfsplitkds) {
					sb.append("'"+string).append("',1 ,");
				}
				sb.append("0) xyfkds,");
				
				//西药费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] xyfsplitje = xyfcode.toString().split(",");
				for (String string : xyfsplitje) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) xyfje,");
				
				//中成药开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] zcyfsplitkds = zcyfcode.toString().split(",");
				for (String string : zcyfsplitkds) {
					sb.append("'"+string).append("',1,");
				}
				sb.append("0) zcyfkds,");
				
				//中成药费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] zcyfsplitje = zcyfcode.toString().split(",");
				for (String string : zcyfsplitje) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) zcyfje,");
				
				//中草药费开单数
				sb.append("DECODE(f.FEE_CODE,");
				String[] zcysfsplitkds = zcysfcode.toString().split(",");
				for (String string : zcysfsplitkds) {
					sb.append("'"+string).append("',1,");
				}
				sb.append("0) zcykds,");
				
				//中草药费金额
				sb.append("DECODE(f.FEE_CODE,");
				String[] zcysfsplitje = zcysfcode.toString().split(",");
				for (String string : zcysfsplitje) {
					sb.append("'"+string+"'").append(",f.TOT_COST,");
				}
				sb.append("0) zcyje,");
				
				sb.append("COUNT(f.FEE_CODE) zkds,f.TOT_COST zje FROM  ");
				sb.append(tlList.get(i)).append(" f ");
				sb.append(" WHERE f.DEL_FLG = 0 AND f.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(emp)){
					sb.append(" AND f.DOCT_CODE=:emp");
				}
				if(StringUtils.isNotBlank(sDate)){
					sb.append(" AND f.REG_DATE>=TO_DATE(:stime, 'yyyy-MM-dd') ");
					sb.append(" AND f.REG_DATE<=TO_DATE(:etime, 'yyyy-MM-dd') ");
				}
				sb.append(" GROUP BY f.REG_DATE,f.FEE_CODE,f.TOT_COST");
			}
			sb.append(" )GROUP BY regDate ORDER BY regDate");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(emp)){
			paraMap.put("emp", emp);
		}
		if(StringUtils.isNotBlank(sDate)){
			paraMap.put("stime", sDate);
			paraMap.put("etime", eDate);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		List<IncomeVo> list = namedParameterJdbcTemplate.query(sb.toString(), paraMap,new RowMapper<IncomeVo>() {
			@Override
			public IncomeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				IncomeVo vo = new IncomeVo();
				vo.setDates(rs.getString("regDate"));
				vo.setInspectNum(rs.getInt("jcfkds"));
				vo.setInspectCost(rs.getDouble("jcfje"));
				vo.setTreatmentNum(rs.getInt("zlfkds"));
				vo.setTreatmentCost(rs.getDouble("zlfje"));
				vo.setRadiationNum(rs.getInt("fsfkds"));
				vo.setRadiationCost(rs.getDouble("fsfje"));
				vo.setTestNum(rs.getInt("hyfkds"));
				vo.setTestCost(rs.getDouble("hyfje"));
				vo.setBloodNum(rs.getInt("sxfkds"));
				vo.setBloodCost(rs.getDouble("sxfje"));
				vo.setOtherNum(rs.getInt("qtsrkds"));
				vo.setOtherCost(rs.getDouble("ylsfje"));
				vo.setMedicalNum(rs.getInt("ylsrkds"));
				vo.setMedicalCost(rs.getDouble("MedicalCost"));
				vo.setWesternNum(rs.getInt("xyfkds"));
				vo.setWesternCost(rs.getDouble("xyfje"));
				vo.setChineseNum(rs.getInt("zcyfkds"));
				vo.setChineseCost(rs.getDouble("zcyfje"));
				vo.setHerbalNum(rs.getInt("zcykds"));
				vo.setHerbalCost(rs.getDouble("zcyje"));
				vo.setAllNum(rs.getInt("ypsrkds"));
				vo.setAllCost(rs.getDouble("ypsrje"));
				vo.setTotle(rs.getDouble("zje"));
				return vo;
			}
		});
		
		if(list.size()==0){
			String sTime = sDate.substring(0, sDate.lastIndexOf("-"));
			IncomeVo vo = new IncomeVo();
			vo.setDates(sTime);
			vo.setInspectNum(0);
			vo.setInspectCost(0.0);
			vo.setTreatmentNum(0);
			vo.setTreatmentCost(0.0);
			vo.setRadiationNum(0);
			vo.setRadiationCost(0.0);
			vo.setTestNum(0);
			vo.setTestCost(0.0);
			vo.setBloodNum(0);
			vo.setBloodCost(0.0);
			vo.setOtherNum(0);
			vo.setOtherCost(0.0);
			vo.setMedicalNum(0);
			vo.setMedicalCost(0.0);
			vo.setWesternNum(0);
			vo.setWesternCost(0.0);
			vo.setChineseNum(0);
			vo.setChineseCost(0.0);
			vo.setHerbalNum(0);
			vo.setHerbalCost(0.0);
			vo.setAllNum(0);
			vo.setAllCost(0.0);
			vo.setTotle(0.0);
			list.add(vo);
		}
	
		return list;
	}
	
    
   public List<StatisticsVo> listStatisticsQueryByMongo(String deptCodes,String expxrtCodes,String beginDate, String endDate) throws Exception{
	   	List<StatisticsVo> list = new ArrayList<StatisticsVo>();
		StatisticsVo vo =null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBList condList02 = new BasicDBList(); 
		
		//部门编号:expxrtCodes
		if(deptCodes!=null&&deptCodes.length()>0){
			String[] dcodes= deptCodes.split(",");
			for( String dcode:dcodes ){
				condList.add(new BasicDBObject("deptCode", dcode));
			}
		}
		
		//医生编号：expxrtCodes
		if(expxrtCodes!=null&&expxrtCodes.length()>0){
			String[] ecodes= expxrtCodes.split(",");
			for( String ecode:ecodes ){
				condList.add(new BasicDBObject("docterCode", ecode));
			}
		}
		if(StringUtils.isNotBlank(deptCodes)||StringUtils.isNotBlank(expxrtCodes)){
			bdObject.put("$or", condList);
		}
		
		if(StringUtils.isNotBlank(beginDate)){
		
			//大于等于		
			bdObjectTimeS.put("regDate",new BasicDBObject("$gte",beginDate));
			condList02.add(bdObjectTimeS);
		}
		
		if(StringUtils.isNotBlank(endDate)){
		
			//小于
			bdObjectTimeE.put("regDate",new BasicDBObject("$lt",endDate));
			condList02.add(bdObjectTimeE);
		}
		
		if(StringUtils.isNotBlank(endDate)||StringUtils.isNotBlank(beginDate)){
			bdObject.put("$and", condList02);
		}
		DBCursor cursor=new MongoBasicDao().findAlldataBySort(TABLENAME,bdObject,"regDate");
	
		while (cursor.hasNext()) {
			DBObject dbCursor = cursor.next();
			vo = new StatisticsVo();
			
			String regDate = (String) dbCursor.get("regDate");
			String deptCode = (String) dbCursor.get("deptCode");
			String docterCode = (String) dbCursor.get("docterCode");
			String name = (String) dbCursor.get("name");
			String dept = (String) dbCursor.get("dept");

			String inspectnumstr = (String) dbCursor.get("inspectnum");
			Integer inspectnum = Integer.parseInt(inspectnumstr);

			String inspectcoststr = (String) dbCursor.get("inspectcost");
			Double inspectcost = Double.parseDouble(inspectcoststr);

			String treatmentnumstr = (String) dbCursor.get("treatmentnum");
			Integer treatmentnum = Integer.parseInt(treatmentnumstr);

			String treatmentcoststr = (String) dbCursor.get("treatmentcost");
			Double treatmentcost = Double.parseDouble(treatmentcoststr);

			String radiationnumstr = (String) dbCursor.get("radiationnum");
			Integer radiationnum = Integer.parseInt(radiationnumstr);

			String radiationcoststr = (String) dbCursor.get("radiationcost");
			Double radiationcost = Double.parseDouble(radiationcoststr);

			String bloodnumstr = (String) dbCursor.get("bloodnum");
			Integer bloodnum = Integer.parseInt(bloodnumstr);

			String bloodcoststr = (String) dbCursor.get("bloodcost");
			Double bloodcost = Double.parseDouble(bloodcoststr);

			String testnumstr = (String) dbCursor.get("testnum");
			Integer testnum = Integer.parseInt(testnumstr);

			String testcoststr = (String) dbCursor.get("testcost");
			Double testcost = Double.parseDouble(testcoststr);

			String othernumstr = (String) dbCursor.get("othernum");
			Integer othernum = Integer.parseInt(othernumstr);

			String othercoststr = (String) dbCursor.get("othercost");
			Double othercost = Double.parseDouble(othercoststr);

			String medicalnumstr = (String) dbCursor.get("medicalnum");
			Integer medicalnum = Integer.parseInt(medicalnumstr);

			String medicalcoststr = (String) dbCursor.get("medicalcost");
			Double medicalcost = Double.parseDouble(medicalcoststr);

			String westernnumstr = (String) dbCursor.get("westernnum");
			Integer westernnum = Integer.parseInt(westernnumstr);

			String westerncoststr = (String) dbCursor.get("westerncost");
			Double westerncost = Double.parseDouble(westerncoststr);

			String chinesenumstr = (String) dbCursor.get("chinesenum");
			Integer chinesenum = Integer.parseInt(chinesenumstr);

			String chinesecoststr = (String) dbCursor.get("chinesecost");
			Double chinesecost = Double.parseDouble(chinesecoststr);

			String herbalnumstr = (String) dbCursor.get("herbalnum");
			Integer herbalnum = Integer.parseInt(herbalnumstr);

			String herbalcoststr = (String) dbCursor.get("herbalcost");
			Double herbalcost = Double.parseDouble(herbalcoststr);

			String allnumstr = (String) dbCursor.get("allnum");
			Integer allnum = Integer.parseInt(allnumstr);

			String allcoststr = (String) dbCursor.get("allcost");
			Double allcost = Double.parseDouble(allcoststr);

			String totlestr = (String) dbCursor.get("totle");
			Double totle = Double.parseDouble(totlestr);

			vo.setRegDate(regDate);
			vo.setDeptCode(deptCode);
			vo.setDocterCode(docterCode);
			vo.setName(name);
			vo.setDept(dept);

			vo.setInspectNum(inspectnum);
			vo.setInspectCost(inspectcost);
			vo.setTreatmentNum(treatmentnum);
			vo.setTreatmentCost(treatmentcost);
			vo.setRadiationNum(radiationnum);
			vo.setRadiationCost(radiationcost);
			vo.setBloodNum(bloodnum);
			vo.setBloodCost(bloodcost);
			vo.setTestNum(testnum);
			vo.setTestCost(testcost);
			vo.setOtherNum(othernum);
			vo.setOtherCost(othercost);
			vo.setMedicalNum(medicalnum);
			vo.setMedicalCost(medicalcost);
			vo.setWesternNum(westernnum);
			vo.setWesternCost(westerncost);
			vo.setChineseNum(chinesenum);
			vo.setChineseCost(chinesecost);
			vo.setHerbalNum(herbalnum);
			vo.setHerbalCost(herbalcost);
			vo.setAllNum(allnum);
			vo.setAllCost(allcost);
			vo.setTotle(totle);
			list.add(vo);
		}
		
		//去重合计
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		for (StatisticsVo st : list) {
			if (map.containsKey(st.getDeptCode() + st.getDocterCode())) {
				StatisticsVo m = (StatisticsVo) map.get(st.getDeptCode() + st.getDocterCode());
				
				//将数据进行叠加
				st.setInspectNum(st.getInspectNum()+vo.getInspectNum());
				st.setInspectCost(st.getInspectCost()+vo.getInspectCost());
				st.setTreatmentNum(st.getTreatmentNum()+vo.getTreatmentNum());
				st.setTreatmentCost(st.getTreatmentCost()+vo.getTreatmentCost());
				st.setRadiationNum(st.getRadiationNum()+vo.getRadiationNum());
				st.setRadiationCost(st.getRadiationCost()+vo.getRadiationCost());
				st.setBloodNum(st.getBloodNum()+vo.getBloodNum());
				st.setBloodCost(st.getBloodCost()+vo.getBloodCost());
				st.setTestNum(st.getTestNum()+vo.getTestNum());
				st.setTestCost(st.getTestCost()+vo.getTestCost());
				st.setOtherNum(st.getOtherNum()+vo.getOtherNum());
				st.setOtherCost(st.getOtherCost()+vo.getOtherCost());
				st.setMedicalNum(st.getMedicalNum()+vo.getMedicalNum());
				st.setMedicalCost(st.getMedicalCost()+vo.getMedicalCost());
				st.setWesternNum(st.getWesternNum()+vo.getWesternNum());
				st.setWesternCost(st.getWesternCost()+vo.getWesternCost());
				st.setChineseNum(st.getChineseNum()+vo.getChineseNum());
				st.setChineseCost(st.getChineseCost()+vo.getChineseCost());
				st.setHerbalNum(st.getHerbalNum()+vo.getHerbalNum());
				st.setHerbalCost(st.getHerbalCost()+vo.getHerbalCost());
				st.setAllNum(st.getAllNum()+vo.getAllNum());
				st.setAllCost(st.getAllCost()+vo.getAllCost());
				st.setTotle(st.getTotle()+vo.getTotle());
				
				//从新放入map,覆盖掉以前的
				map.put(st.getDeptCode() + st.getDocterCode(), st);
			} else {
				StatisticsVo v= new StatisticsVo();
				v.setInspectNum(st.getInspectNum());
				v.setInspectCost(st.getInspectCost());
				v.setTreatmentNum(st.getTreatmentNum());
				v.setTreatmentCost(st.getTreatmentCost());
				v.setRadiationNum(st.getRadiationNum());
				v.setRadiationCost(st.getRadiationCost());
				v.setBloodNum(st.getBloodNum());
				v.setBloodCost(st.getBloodCost());
				v.setTestNum(st.getTestNum());
				v.setTestCost(st.getTestCost());
				v.setOtherNum(st.getOtherNum());
				v.setOtherCost(st.getOtherCost());
				v.setMedicalNum(st.getMedicalNum());
				v.setMedicalCost(st.getMedicalCost());
				v.setWesternNum(st.getWesternNum());
				v.setWesternCost(st.getWesternCost());
				v.setChineseNum(st.getChineseNum());
				v.setChineseCost(st.getChineseCost());
				v.setHerbalNum(st.getHerbalNum());
				v.setHerbalCost(st.getHerbalCost());
				v.setAllNum(st.getAllNum());
				v.setAllCost(st.getAllCost());
				v.setTotle(st.getTotle());
				
				// 医生编号和科室编号相等要进行相加
				map.put(v.getDeptCode() + v.getDocterCode(), v);
			}

		}
		
		List<StatisticsVo> finallist = new ArrayList<StatisticsVo>();
		
		//把map中的数据转化为list
		if(map!=null&&map.size()>0){
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for(Entry e :entrySet){
				finallist.add((StatisticsVo)e.getValue());
			}
		}
		
		int totalInspectNum = 0;
		Double totalInspectCost = 0.0;
		int totalTreatmentNum = 0;
		Double totalTreatmentCost = 0.0;
		int totalRadiationNum = 0;
		Double totalRadiationCost = 0.0;
		int totalBloodNum = 0;
		Double totalBloodCost = 0.0;
		int totalTestNum = 0;
		Double totalTestCost = 0.0;
		int totalOtherNum = 0;
		Double totalOtherCost = 0.0;
		int totalMedicalNum = 0;
		Double totalMedicalCost = 0.0;
		int totalWesternNum = 0;
		Double totalWesternCost = 0.0;
		int totalChineseNum = 0;
		Double totalChineseCost = 0.0;
		int totalHerbalNum = 0;
		Double totalHerbalCost = 0.0;
		int totalAllNum = 0;
		Double totalAllCost = 0.0;
		Double totalTotle = 0.0;
		for (StatisticsVo svo : finallist) {
			totalInspectNum += svo.getInspectNum();
			totalInspectCost += svo.getInspectCost();
			totalTreatmentNum += svo.getTreatmentNum();
			totalTreatmentCost += svo.getTreatmentCost();
			totalRadiationNum += svo.getRadiationNum();
			totalRadiationCost += svo.getRadiationCost();
			totalBloodNum += svo.getBloodNum();
			totalBloodCost += svo.getBloodCost();
			totalTestNum += svo.getTestNum();
			totalTestCost += svo.getTestCost();
			totalOtherNum += svo.getOtherNum();
			totalOtherCost += svo.getOtherCost();
			totalMedicalNum += svo.getMedicalNum();
			totalMedicalCost += svo.getMedicalCost();
			totalWesternNum += svo.getWesternNum();
			totalWesternCost += svo.getWesternCost();
			totalChineseNum += svo.getChineseNum();
			totalChineseCost += svo.getChineseCost();
			totalHerbalNum += svo.getHerbalNum();
			totalHerbalCost += svo.getHerbalCost();
			totalAllNum += svo.getAllNum();
			totalAllCost += svo.getAllCost();
			totalTotle += svo.getTotle();
		}
		StatisticsVo eachTotal = new StatisticsVo();
		eachTotal.setDept("合计");
		eachTotal.setInspectNum(totalInspectNum);
		eachTotal.setInspectCost(totalInspectCost);
		eachTotal.setTreatmentNum(totalTreatmentNum);
		eachTotal.setTreatmentCost(totalTreatmentCost);
		eachTotal.setRadiationNum(totalRadiationNum);
		eachTotal.setRadiationCost(totalRadiationCost);
		eachTotal.setBloodNum(totalBloodNum);
		eachTotal.setBloodCost(totalBloodCost);
		eachTotal.setTestNum(totalTestNum);
		eachTotal.setTestCost(totalTestCost);
		eachTotal.setOtherNum(totalOtherNum);
		eachTotal.setOtherCost(totalOtherCost);
		eachTotal.setMedicalNum(totalMedicalNum);
		eachTotal.setMedicalCost(totalMedicalCost);
		eachTotal.setWesternNum(totalWesternNum);
		eachTotal.setWesternCost(totalWesternCost);
		eachTotal.setChineseNum(totalChineseNum);
		eachTotal.setChineseCost(totalChineseCost);
		eachTotal.setHerbalNum(totalHerbalNum);
		eachTotal.setHerbalCost(totalHerbalCost);
		eachTotal.setAllNum(totalAllNum);
		eachTotal.setAllCost(totalAllCost);
		eachTotal.setTotle(totalTotle);
		
		finallist.add(eachTotal);
		
		return finallist;
	}
	
	
   //同比,月,日
   public Map TBTotalIncome(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode) throws Exception {
   	DBCursor cursor=null;
   	Map map = new LinkedHashMap();
   	List arrayList = new ArrayList();
   	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   	
   	BasicDBObject bdObject = new BasicDBObject();
   	if("2".equals(dateSign)){
   		String[] time=conMonth(searchTime, dateSign);
   		for(int i=0;i<time.length;i++){
   			bdObject.append("regDate",time[i]);
			cursor=new MongoBasicDao().findAlldata(TABLENAME_MONTH,bdObject);
   			Double money=0.0;
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME_MONTH);
   			if(!flg||!cursor.hasNext()){
   				
   				//表不存在，或者表中没数据去oracle中查询
   				//拼接sql
   				//执行sql,得到结果
   				String beginDate =time[i]+"-01";
   				String lastDay= getLastDay(beginDate);
   				String endDate=time[i]+"-"+lastDay;
   				String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   				List<StatisticsVo> list = getlist(tlList,encode,null,null, beginDate, eTime);
   				if(list!=null&&list.size()>0){
   					
   					//得到最后一个，得到总金额
   					money=list.get(list.size()-1).getTotle();
   				}
   			}else{
   				while (cursor.hasNext()) {
   					DBObject dbCursor = cursor.next();
   					String totle=(String) dbCursor.get("totle");
   					money+=Double.parseDouble(totle);
   				}
   			}
   			arrayList.add(money);
   		}
   		map.put("value", arrayList);
   		map.put("name", Arrays.asList(time));
   		return map;
   	}else if("3".equals(dateSign)){
   		String[] time=conMonth(searchTime, dateSign);
   		for(int i=0;i<time.length;i++){
   			bdObject.append("regDate", time[i]);
			cursor=new MongoBasicDao().findAlldata(TABLENAME,bdObject);
   			Double money=0.0;
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME);
   			if(!flg||!cursor.hasNext()){
   				
   				//表不存在，或者表中没数据去oracle中查询
   				//拼接sql
   				//执行sql,得到结果
   				String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(time[i]), 1));//结束时间为下一天
   				List<StatisticsVo> list = getlist(tlList,encode,null,null, time[i], eTime);
   				if(list!=null&&list.size()>0){
   					
   					//得到最后一个，得到总金额
   					money=list.get(list.size()-1).getTotle();
   				}
   			}else{
   				while (cursor.hasNext()) {
   	   				DBObject dbCursor = cursor.next();
   	   				String totle=(String) dbCursor.get("totle");
   	   				money+=Double.parseDouble(totle);
   	   			}
   			}
   			arrayList.add(money);
   		}
   		map.put("value", arrayList);
   		map.put("name", Arrays.asList(time));
   		return map;
   	}
   	
   		return new LinkedHashMap();
   }



   //环比，年，月，日
 public Map HBTotalIncome(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode) throws Exception {
   	
	DBCursor cursor=null;
   	Map map = new LinkedHashMap();
   	List arrayList = new ArrayList();
   	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   	
   	BasicDBObject bdObject = new BasicDBObject();
   	
   	if("2".equals(dateSign)){//月
   		String[] time=conYear(searchTime, dateSign);
   		for(int i=0;i<time.length;i++){
   			bdObject.append("regDate",time[i]);
			cursor=new MongoBasicDao().findAlldata(TABLENAME_MONTH,bdObject);
   			Double money=0.0;
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME_MONTH);
   			if(!flg||!cursor.hasNext()){
   				
   				//表不存在，或者表中没数据去oracle中查询
   				//拼接sql
   				//执行sql,得到结果
   				String beginDate =time[i]+"-01";
   				String lastDay= getLastDay(beginDate);
   				String endDate=time[i]+"-"+lastDay;
   				String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   				List<StatisticsVo> list = getlist(tlList,encode,null,null, beginDate, eTime);
   				if(list!=null&&list.size()>0){
   					
   					//得到最后一个，得到总金额
   					money=list.get(list.size()-1).getTotle();
   				}
   			}else{
   				while (cursor.hasNext()) {
   	   				DBObject dbCursor = cursor.next();
   	   				String totle=(String) dbCursor.get("totle");
   	   				money+=Double.parseDouble(totle);
   	   			}
   			}
   			arrayList.add(money);
   		}
   		map.put("value", arrayList);
   		map.put("name", Arrays.asList(time));
   		return map;
   		
   	}else if("3".equals(dateSign)){//日
   		String[] time=conYear(searchTime, dateSign);
   		for(int i=0;i<time.length;i++){
   			bdObject.append("regDate", time[i]);
   			cursor=new MongoBasicDao().findAlldata(TABLENAME,bdObject);
   			Double money=0.0;
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME);
   			if(!flg||!cursor.hasNext()){
   				
   				//表不存在，或者表中没数据去oracle中查询
   				//拼接sql
   				//执行sql,得到结果
   				String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(time[i]), 1));//结束时间为下一天
   				List<StatisticsVo> list = getlist(tlList,encode,null,null, time[i], eTime);
   				if(list!=null&&list.size()>0){
   					
   					//得到最后一个，得到总金额
   					money=list.get(list.size()-1).getTotle();
   				}
   			}else{
   				while (cursor.hasNext()) {
   	   				DBObject dbCursor = cursor.next();
   	   				String totle=(String) dbCursor.get("totle");
   	   				money+=Double.parseDouble(totle);
   	   			}
   			}
   			arrayList.add(money);
   		}
   		map.put("value", arrayList);
   		map.put("name", Arrays.asList(time));
   		
   		return map;
   	
   	}else if("1".equals(dateSign)){//年
   		String[] time=conYear(searchTime, dateSign);
   		for(int i=0;i<time.length;i++){
			bdObject.put("regDate",time[i]);
			cursor=new MongoBasicDao().findAlldata(TABLENAME_YEAR,bdObject);
   			Double money=0.0;
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME_YEAR);
   			if(!flg||!cursor.hasNext()){
   	   			String  beginDate= time[i]+"-01-01";
   	   			String  endDate= time[i]+"-12-31";
   				
   	   			//表不存在，或者表中没数据去oracle中查询
   				//拼接sql
   				//执行sql,得到结果
   				String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   				List<StatisticsVo> list = getlist(tlList,encode,null,null, beginDate, eTime);
   				if(list!=null&&list.size()>0){
   					//得到最后一个，得到总金额
   					money=list.get(list.size()-1).getTotle();
   				}
   			}else{
   				while (cursor.hasNext()) {
   	   				DBObject dbCursor = cursor.next();
   	   				String totle=(String) dbCursor.get("totle");
   	   				money+=Double.parseDouble(totle);
   	   			}
   			}
   			arrayList.add(money);
   		}
   		map.put("value", arrayList);
   		map.put("name", Arrays.asList(time));
   		
   		return map;
   	}
   	
   	return new LinkedHashMap();
   }




   //科室前5
   public Map deptTopF(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode) throws ParseException {
  
	//查询出这段时间的所有记录，按照key是科室名字，值是金额进行分组
   	//对map进行排序
   	//遍历map，放入list
   	//把list放入lsit，返回
   	DBCursor cursor=null;
   	BasicDBObject bdObject = new BasicDBObject();

   	List<StatisticsVo> list=null;
  	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   	
  
   	if("2".equals(dateSign)){//月
		bdObject.append("regDate",searchTime.substring(0,7));//yyyy-MM-dd
		cursor=new MongoBasicDao().findAlldata(TABLENAME_MONTH,bdObject);
   		boolean flg=new MongoBasicDao().isCollection(TABLENAME_MONTH);
   		if(!flg||!cursor.hasNext()){
   			String lastDay= getLastDay(searchTime);
   			String  beginDate= searchTime.substring(0, 7)+"-01";
   			String  endDate= searchTime.substring(0, 7)+"-"+lastDay;
   			//去oracle中查询
   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   			list=getlist(tlList,encode,null,null, beginDate, eTime);
   		}
   	
   	}else if("3".equals(dateSign)){//日
   		bdObject.append("regDate", searchTime);
   		cursor = new MongoBasicDao().findAlldata(TABLENAME, bdObject);
   		boolean flg=new MongoBasicDao().isCollection(TABLENAME);
   		if(!flg||!cursor.hasNext()){
   			
   			//去oracle中查询
   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(searchTime), 1));//结束时间为下一天
   			list=getlist(tlList,encode,null,null, searchTime, eTime);
   		}
   	
   	}else if("1".equals(dateSign)){//年
   			bdObject.append("regDate",searchTime.substring(0,4));
   			cursor=new MongoBasicDao().findAlldata(TABLENAME_YEAR,bdObject);
   	   		boolean flg=new MongoBasicDao().isCollection(TABLENAME_YEAR);
   	   		if(!flg||!cursor.hasNext()){
	   	   		String  beginDate= searchTime.substring(0, 4)+"-01-01";
	   			String  endDate= searchTime.substring(0, 4)+"-12-31";
   	   			
	   			//去oracle中查询
   	   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   	   			list=getlist(tlList,encode,null,null, beginDate, eTime);
   	   		}
   			
   	}
   	if(cursor!=null&&cursor.hasNext()){
   		Double money=0.0;
   		Map<String,Double> sortMap= new HashMap<String, Double>(); 
   		
   		//查询出这段时间的所有记录，按照key是科室名字，值是金额进行分组
   		while (cursor.hasNext()) {
   			DBObject dbCursor = cursor.next();
   			String deptName= (String) dbCursor.get("dept");
   			if(sortMap.containsKey(deptName)){
   				String totle=(String) dbCursor.get("totle");
   				money=Double.parseDouble(totle);
   				Double double1 = sortMap.get(deptName);
   				sortMap.put(deptName, double1+money);
   			}else{
   				String totle=(String) dbCursor.get("totle");
   				money=Double.parseDouble(totle);
   				sortMap.put(deptName, money);
   			}
   		}
   		
   		//对map进行降序
   		Entry[] entries =SortMapUtils.reverseMap(sortMap);
   		
   		//遍历map，放入list
   		List<Double> value= new ArrayList<Double>();
   		List<String> name= new ArrayList<String>();
   		
   		//把list放入map，返回
   		Map map = new LinkedHashMap();
   		
   		//返回前5
   		if(entries!=null&&entries.length>0&&entries.length<=5){
   			for(Entry e:entries){
   				value.add((Double) e.getValue());
   				name.add((String) e.getKey());			
   			}
   			value.add(0.00);
   			name.add("其他");
   			map.put("value", value);
   			map.put("name", name);
   			return map;
   		}else if(entries.length>5){
   			Double other= 0.00;
   			for(int i=0;i<entries.length;i++){
   				if(i<5){
   					
   					//把前五取出来
   					value.add((Double) entries[i].getValue());
   					name.add((String) entries[i].getKey());	
   				}
   				if(i>=5){
   					other+=(Double)entries[i].getValue();
   				}
   			}
   			value.add(other);
   			name.add("其他");
   			map.put("value", value);
   			map.put("name", name);
   			return map;
   		}
   	}else if(list!=null&&list.size()>0){
   		
   		//移除list集合的最后一个
   		list.remove(list.size()-1);
   		Map<String, Double> sortMap = new LinkedHashMap<String, Double>();
   		
   		//遍历list,按需存入map
   		for(StatisticsVo st:list){
   			if(sortMap.containsKey(st.getDept())){
   				Double stNew = sortMap.get(st.getDept());
   				stNew+=st.getTotle();
   				sortMap.put(st.getDept(), stNew);
   			}else{
   				sortMap.put(st.getDept(), st.getTotle());
   			}
   			
   		}
   		
   		//对map的value进行倒序
   		Entry[] entries =SortMapUtils.reverseMap(sortMap);
   		
   		//遍历，取出前5
   		//遍历map，放入list
   		List<Double> value= new ArrayList<Double>();
   		List<String> name= new ArrayList<String>();
   		
   		//把list放入map，返回
   		Map map = new LinkedHashMap();
   		
   		//返回前5
   		if(entries!=null&&entries.length>0&&entries.length<=5){
   			for(Entry e:entries){
   				value.add((Double) e.getValue());
   				name.add((String) e.getKey());			
   			}
   			value.add(0.00);
   			name.add("其他");
   			map.put("value", value);
   			map.put("name", name);
   			return map;
   		}else if(entries.length>5){
   			Double other= 0.00;
   			for(int i=0;i<entries.length;i++){
   				if(i<5){
   					
   					//把前五取出来
   					value.add((Double) entries[i].getValue());
   					name.add((String) entries[i].getKey());	
   				}
   				if(i>=5){
   					other+=(Double)entries[i].getValue();
   				}
   			}
   			value.add(other);
   			name.add("其他");
   			map.put("value", value);
   			map.put("name", name);
   			
   			return map;
   		}
   		
   	}
   	
   	return new LinkedHashMap();
   }




   //医生前5
   public Map docterTopF(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode) throws Exception {
   		
	    //查询出这段时间的所有记录，按照key是科室名字，值是金额进行分组
   		//对map进行排序
   		//遍历map，放入list
   		//把list放入lsit，返回
   		DBCursor cursor=null;
   		BasicDBObject bdObject = new BasicDBObject();

   		
   	  	List<StatisticsVo> list=null;
   	  	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   		
   	  	//月
   		if("2".equals(dateSign)){
   			bdObject.append("regDate",searchTime.substring(0,7));//yyyy-MM-dd
   			cursor=new MongoBasicDao().findAlldata(TABLENAME_MONTH,bdObject);
   	   		boolean flg=new MongoBasicDao().isCollection(TABLENAME_MONTH);
   	   		if(!flg||!cursor.hasNext()){
   	   			String lastDay= getLastDay(searchTime);
   	   			String  beginDate= searchTime.substring(0, 7)+"-01";
   	   			String  endDate= searchTime.substring(0, 7)+"-"+lastDay;
   	   		
   	   			//去oracle中查询
   	   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   	   			list=getlist(tlList,encode,null,null, beginDate, eTime);
   	   		}
   		
   		}else if("3".equals(dateSign)){//日
   			bdObject.append("regDate", searchTime);
   			cursor = new MongoBasicDao().findAlldata(TABLENAME, bdObject);
   			
   			boolean flg=new MongoBasicDao().isCollection(TABLENAME);
   	   		if(!flg||!cursor.hasNext()){
   	   			
   	   			//去oracle中查询
   	   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(searchTime), 1));//结束时间为下一天
   	   			list=getlist(tlList,encode,null,null, searchTime, eTime);
   	   		}
   		
   		}else if("1".equals(dateSign)){//年
				bdObject.append("regDate",searchTime.substring(0,4));//yyyy
				cursor=new MongoBasicDao().findAlldata(TABLENAME_YEAR,bdObject);
   				
   	   	   		boolean flg=new MongoBasicDao().isCollection(TABLENAME_YEAR);
   	   	   		if(!flg||!cursor.hasNext()){
   	   				String  beginDate= searchTime.substring(0, 4)+"-01-01";
   	   				String  endDate= searchTime.substring(0, 4)+"-12-31";
   	   	   			
   	   				//去oracle中查询
   	   	   			String eTime = dateFormat.format(DateUtils.addDay(dateFormat.parse(endDate), 1));//结束时间为下一天
   	   	   			list=getlist(tlList,encode,null,null, beginDate, eTime);
   	   	   		}
   	   			
   		}
   		if(cursor!=null&&cursor.hasNext()){
   			Double money=0.0;
   			Map<String,Double> sortMap= new HashMap<String, Double>(); 
   			
   			//查询出这段时间的所有记录，按照key是医生编号，值是金额进行分组
   			while (cursor.hasNext()) {
   				DBObject dbCursor = cursor.next();
   				String docterCode= (String) dbCursor.get("name");
   				if(sortMap.containsKey(docterCode)){
   					String totle=(String) dbCursor.get("totle");
   					money=Double.parseDouble(totle);
   					Double double1 = sortMap.get(docterCode);
   					sortMap.put(docterCode, double1+money);
   				}else{
   					String totle=(String) dbCursor.get("totle");
   					money=Double.parseDouble(totle);
   					sortMap.put(docterCode, money);
   				}
   			}
   			
   			//对map进行降序
   			Entry[] entries = SortMapUtils.reverseMap(sortMap);
   			
   			//遍历map，放入list
   			List<Double> value= new ArrayList<Double>();
   			List<String> name= new ArrayList<String>();
   			
   			//把list放入map，返回
   			Map map = new LinkedHashMap();
   			
   			//返回前5
   			if(entries!=null&&entries.length>0&&entries.length<=5){
   				for(Entry e:entries){
   					value.add((Double) e.getValue());
   					name.add((String) e.getKey());			
   				}
   				value.add(0.00);
   				name.add("其他");
   				map.put("value", value);
   				map.put("name", name);
   				return map;
   			}else if(entries.length>5){
   				Double other= 0.00;
   				for(int i=0;i<entries.length;i++){
   					if(i<5){
   						value.add((Double) entries[i].getValue());
   						name.add((String) entries[i].getKey());	
   					}
   					if(i>=5){
   						other+=(Double)entries[i].getValue();
   					}
   				}
   				value.add(other);
   				name.add("其他");
   				map.put("value", value);
   				map.put("name", name);
   				return map;
   			}
   		}else if(list!=null&list.size()>0){
   			
   			//移除list集合的最后一个
   	   		list.remove(list.size()-1);
   	   		Map<String, Double> sortMap = new LinkedHashMap<String, Double>();
   	   		
   	   		//遍历list,按需存入map
   	   		for(StatisticsVo st:list){
   	   			if(sortMap.containsKey(st.getName())){
   	   				Double stNew = sortMap.get(st.getName());
   	   				stNew+=st.getTotle();
   	   				sortMap.put(st.getName(), stNew);
   	   			}else{
   	   				sortMap.put(st.getName(), st.getTotle());
   	   			}
   	   		}
   	   		
   	   		//对map的value进行倒序
   	   		Entry[] entries =SortMapUtils.reverseMap(sortMap);
   	   		
   	   		//遍历，取出前5
   	   		//遍历map，放入list
   	   		List<Double> value= new ArrayList<Double>();
   	   		List<String> name= new ArrayList<String>();
   	   		
   	   		//把list放入map，返回
   	   		Map map = new LinkedHashMap();
   	   		
   	   		//返回前5
   	   		if(entries!=null&&entries.length>0&&entries.length<=5){
   	   			for(Entry e:entries){
   	   				value.add((Double) e.getValue());
   	   				name.add((String) e.getKey());			
   	   			}
   	   			value.add(0.00);
   	   			name.add("其他");
   	   			map.put("value", value);
   	   			map.put("name", name);
   	   			return map;
   	   		}else if(entries.length>5){
   	   			Double other= 0.00;
   	   			for(int i=0;i<entries.length;i++){
   	   				if(i<5){
   	   					
   	   					//把前五取出来
   	   					value.add((Double) entries[i].getValue());
   	   					name.add((String) entries[i].getKey());	
   	   				}
   	   				if(i>=5){
   	   					other+=(Double)entries[i].getValue();
   	   				}
   	   			}
   	   			value.add(other);
   	   			name.add("其他");
   	   			map.put("value", value);
   	   			map.put("name", name);
   	   			return map;
   	   		}
   		}
   		
   		return new LinkedHashMap();
   }

   
   
   /**
    * 环比，工具
    * @param date 时间 yyyy-MM-dd
    * @param dateSing 年，月，日 分别是1，2，3
    * @return 返回环比的时间字符串数组:如["2017-05","2017-04","2017-03","2017-02","2017-01","2016-12"]
    */
   public String[] conYear(String date,String dateSing){
	   	 Calendar ca = Calendar .getInstance();
	   	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	   	 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
	   	 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
	   	 String[] dateOne =date.split("-");
	   	 String[] strArr=new String[6];
	   	 if(dateOne.length!=3){
	   		 dateOne=sdf.format(new Date(System.currentTimeMillis())).split("-"); 
	   	 }
	   	 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
	   	for(int i=0;i<6;i++){
	   		if("1".equals(dateSing)){
	   			strArr[i]=sdf2.format(ca.getTime());
	   			ca.add(Calendar.YEAR, -1);
	   		}else if("2".equals(dateSing)){
	   			strArr[i]=sdf1.format(ca.getTime());
	   			ca.add(Calendar.MONTH, -1);
	   		}else{
	   			strArr[i]=sdf.format(ca.getTime());
	   			ca.add(Calendar.DATE, -1);
	   		}
	   	}
	   	 
	   	return strArr;
   }





   /**
    * 同比，工具
    * @param date 时间
    * @param dateSing
    * @return 返回同比的时间字符串数组["2017-05","2016-05","2015-05","2014-05","2013-05","2012-05"]
    */
   public String[] conMonth(String date,String dateSing){
   	
	   	String [] strArr=new String[6];
	   	String[] dateArr=date.split("-");
	   	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	   	if(dateArr.length!=3){
	   		dateArr=sdf.format(new Date(System.currentTimeMillis())).split("-");
	   	}
	   	int dateTemp=Integer.parseInt(dateArr[0]);
	   	for(int i=0;i<6;i++){
	   		if("2".equals(dateSing)){//月同比
	   			strArr[i]=(dateTemp-i)+"-"+dateArr[1];
	   		}else {
	   				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
	   		}
	   	}
	   	
	   	return strArr;
   } 
   
  
   //获取某月的最后一天
   /**
    * <p> 获取输入时间对应的月的最后一天，闰年29天，平年28天，时间格式必须是长度大于：yyyy-MM-dd的格式</p>
    * @Author: zhangkui
    * @CreateDate: 2017年7月3日 下午4:54:59 
    * @Modifier: zhangkui
    * @ModifyDate: 2017年7月3日 下午4:54:59 
    * @ModifyRmk:  
    * @version: V1.0
    * @param: date 长度大yyyy-MM-dd
    * @throws:
    * @return: String 月份最后一天的字符串表示形式
    *
    */
   public String getLastDay(String date){
	   	date= date.substring(0, 7);
	   	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	   	Date time =null;
	   	try {
	   		 time = dateFormat.parse(date);
	   	} catch (ParseException e) {
	   		e.printStackTrace();
	   	}
	   	 Calendar  calendar =  Calendar.getInstance(); 
	   	 calendar.setTime(time);
	   	 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
	   	 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	   	 
	     return dateFormat1.format(lastDate).substring(8, 10);
   }
   
	/**
	 * 查询门诊下的科室code和name
	 */
    @Override
    public List<SysDepartment> getDept() {
        String sql = "SELECT t.DEPT_ID AS id,t.DEPT_CODE AS deptCode,t.DEPT_NAME AS deptName FROM T_DEPARTMENT t WHERE t.DEPT_TYPE='C'";
        List<SysDepartment> list = namedParameterJdbcTemplate.query(sql, new RowMapper<SysDepartment>() {
            @Override
            public SysDepartment mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                SysDepartment vo = new SysDepartment();
                vo.setId(rs.getString("id"));
                vo.setDeptCode(rs.getString("deptCode"));
                vo.setDeptName(rs.getString("deptName"));
                return vo;
            }
        });
        return list;
    }
	
}
