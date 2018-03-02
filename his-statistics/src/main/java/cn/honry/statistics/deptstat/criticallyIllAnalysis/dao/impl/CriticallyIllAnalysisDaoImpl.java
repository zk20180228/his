package cn.honry.statistics.deptstat.criticallyIllAnalysis.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.dao.CriticallyIllAnalysisDao;
import cn.honry.statistics.deptstat.criticallyIllAnalysis.vo.CriticallyIllAnalysisVo;
import cn.honry.statistics.deptstat.dischargeStatistics.vo.DischargeStatisticsVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("criticallyIllAnalysisDao")
@SuppressWarnings({ "all" })
public class CriticallyIllAnalysisDaoImpl extends HibernateEntityDao<CriticallyIllAnalysisVo> implements CriticallyIllAnalysisDao{
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
	 * 重症患者占比分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */

	@Override
	public List<CriticallyIllAnalysisVo> queryCriticallyIllAnalysis(String startTime,String endTime,String deptCode,String menuAlias) {
		String sql="Select  deptCode As deptCode,to_char(outPatient) As outPatient,to_char(allPatient) As allPatient,to_char(proportion,'00.00')  As proportion, to_char(illPatient)  As illPatient,to_char(illPatPro,'00.00')  As illPatPro "
					+ "From (select (select pp.dept_name from t_department pp where pp.dept_code=AA.dept_code) deptCode,outPatient,allPatient, "
					+ "round((outPatient * 100) / allPatient, 2) proportion,illPatient,round((outPatient * 100) / illPatient, 2) illPatPro  "
					+ "from (select cg.dept_code, sum(cg.out_normal) outPatient, "
					+ "(select sum(ccg.out_normal) from t_inpatient_dayreport ccg "
					+ "where ccg.date_stat >= TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and ccg.date_stat <= TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss')) allPatient, "
					+ "(select sum(ccg.out_normal) from t_inpatient_dayreport ccg "
					+ "where ccg.date_stat >= TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and ccg.date_stat <= TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and ccg.dept_code in (select cg.code_encode from t_business_dictionary cg  "
					+ "where cg.code_type = 'deptccu')) illPatient from t_inpatient_dayreport cg  "
					+ "where cg.date_stat >= TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss')  "
					+ "and cg.date_stat <=TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and cg.dept_code in (select cg.code_encode from t_business_dictionary cg "
					+ "where cg.code_type = 'deptccu') group by cg.dept_code) AA)  ";
		List<CriticallyIllAnalysisVo> list=this.getSession().createSQLQuery(sql)
				.addScalar("deptCode").addScalar("outPatient")
				.addScalar("allPatient").addScalar("proportion")
				.addScalar("illPatient").addScalar("illPatPro")
				.setResultTransformer(Transformers.aliasToBean(CriticallyIllAnalysisVo.class)).list();
		return list;
	}
	
	@Override
	public List<CriticallyIllAnalysisVo> queryCriticallyIllAnalysisForDB(String startTime, String endTime, String deptCode, String menuAlias) {
		return null;
	}

}
