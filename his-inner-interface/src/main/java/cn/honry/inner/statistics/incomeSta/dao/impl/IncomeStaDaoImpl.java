package cn.honry.inner.statistics.incomeSta.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.inner.statistics.incomeSta.dao.IncomeStaDao;
import cn.honry.inner.statistics.incomeSta.vo.IncomeDetailVo;
import cn.honry.inner.statistics.incomeSta.vo.IncomeStatisticsVO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("incomeStaDao")
@SuppressWarnings({"all"})
public class IncomeStaDaoImpl extends HibernateDaoSupport implements IncomeStaDao {

	// 为HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public List<IncomeStatisticsVO> queryVo(String date) {

		Date dateGet = DateUtils.parseDateY_M_D(date);//获取到的日期
		String sDate=DateUtils.formatDateY_M_D(dateGet);//yyyy-MM-dd
		String eDate=DateUtils.formatDateY_M_D(DateUtils.addDay(dateGet, 1));//yyyy-MM-dd
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		String tableName;
		try {
			dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(dateGet, cTime)==-1){//获得到的时间和在线表的最小时间比较
				List<String> tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",date,date);
				tableName = tnL.get(0);
			}else{
				tableName = "T_OUTPATIENT_FEEDETAIL_NOW";
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT ");
			sb.append(" T.FEE AS FEE, ");
			sb.append(" O.CODE_NAME AS AREANAME, ");
			sb.append(" G.FEE_STAT_NAME AS FEENAME ");
			sb.append(" FROM ( ");
			
			sb.append(" SELECT ");
			sb.append(" SUM (F.TOT_COST) AS FEE, ");
			sb.append(" DECODE (F.AREA_CODE,0,1,NULL,1,F.AREA_CODE) AS AREACODE, ");
			sb.append(" C.FEE_STAT_CODE AS FEECODE ");
			sb.append(" from ");
			sb.append(	tableName+" F , ");
			sb.append(" T_CHARGE_MINFEETOSTAT C ");
			sb.append(" WHERE ");
			sb.append(" f.STOP_FLG = 0 ");
			sb.append(" AND f.DEL_FLG = 0 ");
			sb.append(" AND TRIM(C.REPORT_CODE) = 'MZ01' ");
			sb.append(" AND C.MINFEE_CODE = F.FEE_CODE ");
			sb.append(" AND F.FEE_DATE >= TO_DATE ('"+sDate+"', 'YYYY-MM-DD') ");
			sb.append(" AND F.FEE_DATE < TO_DATE ('"+eDate+"', 'YYYY-MM-DD') ");
			sb.append(" GROUP BY ");
			sb.append(" F.AREA_CODE, ");
			sb.append(" C.FEE_STAT_CODE ");
			sb.append(" ) T ");
			
			sb.append(" LEFT JOIN T_BUSINESS_DICTIONARY O ON O.CODE_ENCODE = T .AREACODE AND TRIM(O.CODE_TYPE) = 'hospitalArea' ");//数据库中字段的值是区别大小写的，因此，'hospitalArea',不能大写
			sb.append(" LEFT JOIN ( ");
			sb.append(" SELECT DISTINCT FEE_STAT_CODE,FEE_STAT_NAME ");
			sb.append(" FROM ");
			sb.append(" T_CHARGE_MINFEETOSTAT ");
			sb.append(" WHERE ");
			sb.append(" TRIM(REPORT_CODE) = 'MZ01' ");
			sb.append(" ) G  ");
			sb.append(" ON G.FEE_STAT_CODE = T .FEECODE ");
			
			List<IncomeStatisticsVO> list = getSessionFactory().getCurrentSession().createSQLQuery(sb.toString())
					.addScalar("areaname",Hibernate.STRING)
					.addScalar("feename",Hibernate.STRING)
					.addScalar("fee",Hibernate.DOUBLE)
					.setResultTransformer(Transformers.aliasToBean(IncomeStatisticsVO.class)).list();
			return list;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ArrayList<IncomeStatisticsVO>();
	}
	
	@Override
	public List<String> queryNameList() {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct t.fee_stat_code as value,t.fee_stat_name as name from t_charge_minfeetostat t where (t.report_code = 'ZY01' OR T.REPORT_CODE = 'MZ01') and t.stop_flg = 0 and t.del_flg = 0 order by t.fee_stat_code");
		List<IncomeDetailVo> list = getSessionFactory().getCurrentSession().createSQLQuery(sb.toString())
				.addScalar("name",Hibernate.STRING)
				.addScalar("value",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(IncomeDetailVo.class)).list();
		List<String> listS = new ArrayList<String>();
		for(IncomeDetailVo vo : list){
			listS.add(vo.getName());
		}
		return listS;
	}

}
