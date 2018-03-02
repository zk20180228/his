package cn.honry.inner.statistics.totalIncomeCount.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.totalIncomeCount.dao.TotalIncomeCountDao;
import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import freemarker.template.utility.DateUtil;

/**
 * @Description:总收入情况统计初始化数据到mongodb中
 * @author:zhangkui
 * @time:2017年6月21日 下午5:44:27
 */
@Repository("totalIncomeCountDao")
@SuppressWarnings({"all"})
public class TotalIncomeCountDaoImpl extends HibernateDaoSupport  implements TotalIncomeCountDao{
	
	// 为HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<MapVo> init_ZSRQKTJ_dataByDay(String date) {
		Date dateGet = DateUtils.parseDateY_M_D(date);
		
		//获得在线表门诊数据保留的天数
		String dateMz = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		String dateZy = parameterInnerDAO.getParameterByCode("saveTime");//获取住院表保存的天数，单位月，1月按30天计算
		if("1".equals(dateZy)){
			dateZy="30";
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime=null;
		String tableMzName=null;
		String tableZyName=null;
		try {
			dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateMz)+1);
			Date zTime = DateUtils.addDay(dTime,-Integer.parseInt(dateZy)+1);
			if(DateUtils.compareDate(dateGet, cTime)==-1){//传入时间小于最小时间
				
				//获取时间差（年）
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), date);
				tableMzName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1).get(0);//获取传入时间所在的分区
			}else{//传入时间大于等于最小时间
				tableMzName = "T_OUTPATIENT_FEEDETAIL_NOW";
			}
			
			if(DateUtils.compareDate(dateGet, zTime)==-1){//传入时间小于住院在线表的最小时间，去历史表查询
				
				//获取时间差（年）
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(zTime), date);
				tableZyName=ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1).get(0);//获取传入时间所在的分区
			}else{
				tableZyName="T_INPATIENT_FEEINFO_NOW";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		
		//查询处方明细表(在线表),得到费用名，费用，费用对应的院区
		sb.append(" SELECT SUM(NVL(F.TOT_COST,0)) AS FEE,D.DEPT_AREA_NAME AS AREANAME,C.FEE_STAT_NAME AS FEENAME ");
		sb.append(" FROM ");
		sb.append(tableMzName);
		sb.append(" F, ");
		sb.append(" T_DEPARTMENT D, ");
		sb.append(" T_CHARGE_MINFEETOSTAT C ");
		sb.append(" WHERE ");
		sb.append(" F.DOCT_DEPT = D.DEPT_CODE AND C.MINFEE_CODE=F.FEE_CODE AND C.REPORT_CODE='MZ01' ");
		sb.append(" AND F.STOP_FLG=0 AND F.DEL_FLG=0 ");
		sb.append(" AND F.REG_DATE >=TO_DATE('"+date+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" AND F.REG_DATE <=TO_DATE('"+date+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" GROUP BY ");
		sb.append(" C.FEE_STAT_NAME,D.DEPT_AREA_NAME ");
		
		List<MapVo> list=namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<MapVo>() {
			@Override
			public MapVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MapVo vo = new MapVo();
				vo.setName(rs.getString("FEENAME"));
				vo.setAreaName(rs.getString("AREANAME"));
				vo.setValue(rs.getDouble("FEE"));
				return vo;
			}
		});
		
		//查询费用汇总表(在线表),得到费用名，费用，费用对应的院区
		StringBuffer sb02 = new StringBuffer();
		sb02.append(" SELECT ");
		sb02.append(" SUM(NVL(I.TOT_COST,0)) AS FEE,D.DEPT_AREA_NAME AS AREANAME, C.FEE_STAT_NAME AS FEENAME ");
		sb02.append(" FROM ");
		sb02.append(tableZyName+ " I,T_DEPARTMENT D,T_CHARGE_MINFEETOSTAT C ");
		sb02.append(" WHERE ");
		sb02.append(" I.INHOS_DEPTCODE= D.DEPT_CODE AND C.MINFEE_CODE=I.FEE_CODE AND C.REPORT_CODE='ZY01' ");
		sb02.append(" AND I.EXT_FLAG1='0' AND I.STOP_FLG=0 AND I.DEL_FLG=0 ");
		sb02.append(" AND I.FEE_DATE >=TO_DATE('"+date+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS') ");
		sb02.append(" AND I.FEE_DATE <=TO_DATE('"+date+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS') ");
		sb02.append(" GROUP BY ");
		sb02.append(" C.FEE_STAT_NAME,D.DEPT_AREA_NAME ");
		
		List<MapVo> list02=namedParameterJdbcTemplate.query(sb02.toString(), new RowMapper<MapVo>() {
			@Override
			public MapVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MapVo vo = new MapVo();
				vo.setName(rs.getString("FEENAME"));
				vo.setAreaName(rs.getString("AREANAME"));
				vo.setValue(rs.getDouble("FEE"));
				return vo;
			}
		});
		
		List<MapVo> arrayList = new ArrayList<MapVo>();
		arrayList.addAll(list);
		arrayList.addAll(list02);
		
		
		return arrayList;
	}
	

	
	/**
	 * 保存日志
	 * @param obj
	 */
	public void save(Object obj){
		getHibernateTemplate().save(obj);
	}

	
}
