package cn.honry.inner.statistics.hosIncomeCount.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.inner.statistics.hosIncomeCount.dao.ZyIncomeDao;
import cn.honry.inner.statistics.hosIncomeCount.vo.MapVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

/**
 * 
 * @Description:将住院收入统计的数据初始化到mongo中
 * @author:zhangkui 
 * @time:2017年6月24日 下午5:00:34
 */
@Repository("zyIncomeDao")
@SuppressWarnings({"all"})
public class ZyIncomeDaoImpl extends HibernateDaoSupport implements ZyIncomeDao {

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
	
	
	
	@Override
	public List<MapVo> init_ZYSRTJ_dataByDay(String date) {
				Date dateGet = DateUtils.parseDateY_M_D(date);
				String dateZy = parameterInnerDAO.getParameterByCode("saveTime");
				if("1".equals(dateZy)){
					dateZy="30";
				}
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime=null;
				String tableZyName=null;
				try {
					dTime = df.parse(df.format(new Date()));
					Date zTime = DateUtils.addDay(dTime,-Integer.parseInt(dateZy)+1);
					
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
				
				//查询费用汇总表(在线表),得到费用名，费用，费用对应的院区
				StringBuffer sb02 = new StringBuffer();
				sb02.append(" SELECT ");
				sb02.append(" SUM(NVL(I.TOT_COST,0)) AS VALUE,D.DEPT_NAME AS DEPTNAME, C.FEE_STAT_NAME AS NAME ");
				sb02.append(" FROM ");
				sb02.append(tableZyName+ " I,T_DEPARTMENT D,T_CHARGE_MINFEETOSTAT C ");
				sb02.append(" WHERE ");
				sb02.append(" I.INHOS_DEPTCODE= D.DEPT_CODE AND C.MINFEE_CODE=I.FEE_CODE AND C.REPORT_CODE='ZY01' ");
				sb02.append(" AND I.EXT_FLAG1='0' AND I.STOP_FLG=0 AND I.DEL_FLG=0 ");
				sb02.append(" AND I.FEE_DATE >=TO_DATE('"+date+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS') ");
				sb02.append(" AND I.FEE_DATE <=TO_DATE('"+date+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS') ");
				sb02.append(" GROUP BY ");
				sb02.append(" C.FEE_STAT_NAME,D.DEPT_NAME ");
				
				List<MapVo> list02=namedParameterJdbcTemplate.query(sb02.toString(), new RowMapper<MapVo>() {
					@Override
					public MapVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						MapVo vo = new MapVo();
						vo.setName(rs.getString("NAME"));
						vo.setDeptName(rs.getString("DEPTNAME"));
						vo.setValue(rs.getDouble("VALUE"));
						return vo;
					}
				});
		
		
				return list02;
	}

	@Override
	public void save(Object obj) {
		
		getHibernateTemplate().save(obj);
	}

}
