package cn.honry.statistics.deptstat.patientDiseaseType.dao.impl;

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
import cn.honry.statistics.deptstat.patientDiseaseType.dao.PatientDiseaseTypeDAO;
import cn.honry.statistics.deptstat.patientDiseaseType.vo.PatientDiseaseType;
import cn.honry.utils.ShiroSessionUtils;
@Repository("patientDiseaseTypeDAO")
@SuppressWarnings({ "all" })
public class PatientDiseaseTypeDAOImpl  extends HibernateEntityDao<PatientDiseaseType> implements PatientDiseaseTypeDAO{
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 
	 * 患者疾病类型统计分析list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:56:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:56:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryPatientDisease(final String deptCode,
			final String sex, final String startTime, final String endTime,String page, String rows) throws Exception {
		final String sb=this.hql(deptCode, sex, startTime, endTime);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<PatientDiseaseType> voList = (List<PatientDiseaseType>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<PatientDiseaseType> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sb)
					   .addScalar("deptName",Hibernate.STRING)
					   .addScalar("cure",Hibernate.DOUBLE)
					   .addScalar("better",Hibernate.DOUBLE)
					   .addScalar("healed",Hibernate.DOUBLE)
					   .addScalar("death",Hibernate.DOUBLE)
					   .addScalar("other",Hibernate.DOUBLE)
					   .addScalar("normal",Hibernate.DOUBLE)
					   .addScalar("planning",Hibernate.DOUBLE);
				if(StringUtils.isNotBlank(deptCode)){
					queryObject.setParameterList("deptCode",deptCode.split(","));
				}
				if(StringUtils.isNotBlank(startTime)){
					queryObject.setParameter("startTime",startTime);
				}
				if(StringUtils.isNotBlank(endTime)){
					queryObject.setParameter("endTime",endTime );
				}
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(PatientDiseaseType.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<PatientDiseaseType>();
	}
	/**  
	 * 
	 * 患者疾病类型统计分析total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:58:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:58:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryTotal(String deptCode, String sex, String startTime,
			String endTime) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.hql(deptCode, sex, startTime, endTime)+" ) ";
		if (StringUtils.isNotBlank(deptCode)) {
			paraMap.put("deptCode", Arrays.asList(deptCode.split(",")));
		}
		if (StringUtils.isNotBlank(startTime)) {
			paraMap.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			paraMap.put("endTime", endTime);
		}
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String hql(String deptCode, String sex, String startTime,String endTime){
		StringBuffer hql = new StringBuffer();
		hql.append("select d.dept_name as deptName,a.cure as cure,a.better as better,a.healed as healed, ");
		hql.append("a.death as death,a.other as other,a.normal as normal,a.planning as planning from( ");
		hql.append("select t.dept_code, ");
		hql.append("sum(case when t.zg='1' then 1 else 0 end) as cure, ");
		hql.append("sum(case when t.zg='2' then 1 else 0 end) as better, ");
		hql.append("sum(case when t.zg='3' then 1 else 0 end) as healed,");
		hql.append("sum(case when t.zg='4' then 1 else 0 end) as death, ");
		hql.append("sum(case when t.zg='5' and t.zg is null then 1 else 0 end) as other, ");
		hql.append("sum(case when t.zg='6' then 1 else 0 end) as normal, ");
		hql.append("sum(case when t.zg='7' then 1 else 0 end) as planning from ( select * from t_emr_base b where 1=1 ");
		if (StringUtils.isNoneBlank(deptCode)) {
			hql.append("and b.dept_code in (:deptCode) ");
		}
		if (StringUtils.isNoneBlank(sex)) {
			hql.append("and b.SEX_CODE = '"+sex+"' ");
		}
		if (StringUtils.isNoneBlank(startTime)) {
			hql.append("and b.BIRTHDAY >= to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNoneBlank(endTime)) {
			hql.append("and b.BIRTHDAY <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		hql.append(")t group by t.dept_code)a ");
		hql.append("left join t_department d on d.dept_code = a.dept_code ");
		hql.append("where d.stop_flg=0 and d.del_flg=0");
		return hql.toString();
	}
	/**  
	 * 
	 * 未治愈ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryIcdHealed(String deptCode, String sex,
			String startTime, String endTime) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select e.icd_diagnosticname as icdName, a.num as icdNum ");
		sql.append("from (select t.icd_code, count(t.icd_code) as num ");
		sql.append("from t_business_diagnose t left join ( select * from t_emr_base b where 1=1 ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("and b.dept_code in (:deptCode) ");
		}
		if (StringUtils.isNoneBlank(sex)) {
			sql.append("and b.SEX_CODE = '"+sex+"' ");
		}
		if (StringUtils.isNoneBlank(startTime)) {
			sql.append("and b.BIRTHDAY >= to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNoneBlank(endTime)) {
			sql.append("and b.BIRTHDAY <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(") d on d.INPATIENT_NO = t.INPATIENT_NO ");
		sql.append("where t.icd_code is not null and t.icd_code <> '-' and t.icd_code <> '―' and d.zg = '3' and t.stop_flg = 0 and t.del_flg = 0 ");
		sql.append("group by t.icd_code) a left join T_BUSINESS_ICD10 e on e.icd_diagnosticcode = a.icd_code ");
		sql.append("where e.stop_flg = 0 and e.del_flg = 0 order by a.num desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(deptCode)) {
			paraMap.put("deptCode", Arrays.asList(deptCode.split(",")));
		}
		if (StringUtils.isNotBlank(startTime)) {
			paraMap.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			paraMap.put("endTime", endTime);
		}
		List<PatientDiseaseType> list = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PatientDiseaseType>() {
							@Override
							public PatientDiseaseType mapRow(ResultSet rs,int rowNum) throws SQLException {
								PatientDiseaseType vo = new PatientDiseaseType();
								vo.setIcdName(rs.getString("icdName"));
								vo.setIcdNum(rs.getInt("icdNum"));
								return vo;
							}
						});
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<PatientDiseaseType>();
	}
	/**  
	 * 
	 * 死亡ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PatientDiseaseType> queryIcdDeath(String deptCode, String sex,
			String startTime, String endTime) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select e.icd_diagnosticname as icdName, a.num as icdNum ");
		sql.append("from (select t.icd_code, count(t.icd_code) as num ");
		sql.append("from t_business_diagnose t left join ( select * from t_emr_base b where 1=1 ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("and b.dept_code in (:deptCode) ");
		}
		if (StringUtils.isNoneBlank(sex)) {
			sql.append("and b.SEX_CODE = '"+sex+"' ");
		}
		if (StringUtils.isNoneBlank(startTime)) {
			sql.append("and b.BIRTHDAY >= to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNoneBlank(endTime)) {
			sql.append("and b.BIRTHDAY <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(") d on d.INPATIENT_NO = t.INPATIENT_NO ");
		sql.append("where t.icd_code is not null and t.icd_code <> '-' and t.icd_code <> '―' and d.zg = '4' and t.stop_flg = 0 and t.del_flg = 0 ");
		sql.append("group by t.icd_code) a left join T_BUSINESS_ICD10 e on e.icd_diagnosticcode = a.icd_code ");
		sql.append("where e.stop_flg = 0 and e.del_flg = 0 order by a.num desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(deptCode)) {
			paraMap.put("deptCode", Arrays.asList(deptCode.split(",")));
		}
		if (StringUtils.isNotBlank(startTime)) {
			paraMap.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			paraMap.put("endTime", endTime);
		}
		List<PatientDiseaseType> list = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PatientDiseaseType>() {
							@Override
							public PatientDiseaseType mapRow(ResultSet rs,int rowNum) throws SQLException {
								PatientDiseaseType vo = new PatientDiseaseType();
								vo.setIcdName(rs.getString("icdName"));
								vo.setIcdNum(rs.getInt("icdNum"));
								return vo;
							}
						});
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<PatientDiseaseType>();
	}
}
