package cn.honry.statistics.bi.bistac.personnelInformation.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;
import cn.honry.statistics.bi.bistac.personnelInformation.dao.PersonnelInformationDAO;
import cn.honry.statistics.bi.bistac.personnelInformation.vo.PersonnelInformationVo;
@Repository("personnelInformationDAO")
@SuppressWarnings({ "all" })
public class PersonnelInformationDAOImpl  extends HibernateEntityDao<PersonnelInformationVo> implements PersonnelInformationDAO {
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 基础工具类,不支持参数名传参
	 */
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**  
	 * 
	 * 学历
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonnelInformationVo> queryEducation(String deptCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.name as name, sum(a.value) as value from ( ");
		sql.append("select d.code_name name, count(t.EMPLOYEE_EDUCATION) value from (select * from t_employee ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("where DEPT_ID = '"+deptCode+"'");
		}
		sql.append(") t left join T_BUSINESS_DICTIONARY d ");
		sql.append("on (t.EMPLOYEE_EDUCATION =d.code_encode) or (t.EMPLOYEE_EDUCATION = d.code_name) ");
		sql.append("where d.code_type = 'degree' and t.STOP_FLG = 0 and t.DEL_FLG = 0 and d.STOP_FLG = 0 and d.DEL_FLG = 0 ");
		sql.append("group by t.EMPLOYEE_EDUCATION, d.code_name ");
		sql.append("union all ");
		sql.append("select '不详' as name,count(1) as value from t_employee t where ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("t.DEPT_ID = '"+deptCode+"' and ");
		}
		sql.append("t.EMPLOYEE_EDUCATION is null and t.STOP_FLG = 0 and t.DEL_FLG = 0 ");
		sql.append(" ) a group by a.name");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<PersonnelInformationVo> PersonnelInformationVoList = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PersonnelInformationVo>() {
							@Override
							public PersonnelInformationVo mapRow(ResultSet rs,int rowNum) throws SQLException {
								PersonnelInformationVo vo = new PersonnelInformationVo();
								vo.setName(rs.getString("name"));
								vo.setValue(rs.getInt("value"));
								return vo;
							}
						});
		for (int i = 0; i < PersonnelInformationVoList.size(); i++) {
			if (PersonnelInformationVoList.get(i)!=null && PersonnelInformationVoList.get(i).getValue()==0) {
				PersonnelInformationVoList.remove(PersonnelInformationVoList.get(i));
			}
		}
		if (PersonnelInformationVoList != null && PersonnelInformationVoList.size() > 0) {
			return PersonnelInformationVoList;
		}
		return null;
	}
	/**  
	 * 
	 * 职称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonnelInformationVo> queryTitle(String deptCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select a.name as name, sum(a.value) as value from ( ");
		sql.append("select d.code_name name, count(t.EMPLOYEE_TITLE) value from (select * from t_employee ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("where DEPT_ID = '"+deptCode+"' ");
		}
		sql.append(") t left join T_BUSINESS_DICTIONARY d ");
		sql.append("on (t.EMPLOYEE_TITLE =d.code_encode) or (t.EMPLOYEE_TITLE = d.code_name) ");
		sql.append("where d.code_type = 'title' and t.STOP_FLG = 0 and t.DEL_FLG = 0 and d.STOP_FLG = 0 and d.DEL_FLG = 0 ");
		sql.append("group by t.EMPLOYEE_TITLE, d.code_name ");
		sql.append("union all ");
		sql.append("select '未知' as name,count(1) as value from t_employee t where ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("t.DEPT_ID = '"+deptCode+"' and ");
		}
		sql.append("t.EMPLOYEE_TITLE is null and t.STOP_FLG = 0 and t.DEL_FLG = 0 ");
		sql.append(" ) a group by a.name order by sum(a.value) desc )where rownum <= 5");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<PersonnelInformationVo> PersonnelInformationVoList = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PersonnelInformationVo>() {
							@Override
							public PersonnelInformationVo mapRow(ResultSet rs,int rowNum) throws SQLException {
								PersonnelInformationVo vo = new PersonnelInformationVo();
								vo.setName(rs.getString("name"));
								vo.setValue(rs.getInt("value"));
								return vo;
							}
						});
		for (int i = 0; i < PersonnelInformationVoList.size(); i++) {
			if (PersonnelInformationVoList.get(i)!=null && PersonnelInformationVoList.get(i).getValue()==0) {
				PersonnelInformationVoList.remove(PersonnelInformationVoList.get(i));
			}
		}
		if (PersonnelInformationVoList != null && PersonnelInformationVoList.size() > 0) {
			return PersonnelInformationVoList;
		}
		return null;
	}
	/**  
	 * 
	 * 性别 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonnelInformationVo> querySex(String deptCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.name as name, sum(a.value) as value from ( ");
		sql.append("select d.code_name name, count(t.EMPLOYEE_SEX) value from (select * from t_employee ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("where DEPT_ID = '"+deptCode+"' ");
		}
		sql.append(") t left join T_BUSINESS_DICTIONARY d ");
		sql.append("on t.EMPLOYEE_SEX = d.code_encode ");
		sql.append("where d.code_type = 'sex' and t.STOP_FLG = 0 and t.DEL_FLG = 0 and d.STOP_FLG = 0 and d.DEL_FLG = 0 ");
		sql.append("group by t.EMPLOYEE_SEX, d.code_name ");
		sql.append("union all ");
		sql.append("select '未知' as name,count(1) as value from t_employee t where ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("t.DEPT_ID = '"+deptCode+"' and ");
		}
		sql.append("t.EMPLOYEE_SEX is null and t.STOP_FLG = 0 and t.DEL_FLG = 0 ");
		sql.append(" ) a group by a.name");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<PersonnelInformationVo> PersonnelInformationVoList = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PersonnelInformationVo>() {
							@Override
							public PersonnelInformationVo mapRow(ResultSet rs,int rowNum) throws SQLException {
								PersonnelInformationVo vo = new PersonnelInformationVo();
								vo.setName(rs.getString("name"));
								vo.setValue(rs.getInt("value"));
								return vo;
							}
						});
		for (int i = 0; i < PersonnelInformationVoList.size(); i++) {
			if (PersonnelInformationVoList.get(i)!=null && PersonnelInformationVoList.get(i).getValue()==0) {
				PersonnelInformationVoList.remove(PersonnelInformationVoList.get(i));
			}
		}
		if (PersonnelInformationVoList != null && PersonnelInformationVoList.size() > 0) {
			return PersonnelInformationVoList;
		}
		return null;
	}
	/**  
	 * 
	 * 年龄
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PersonnelInformationVo> queryAge(String deptCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select name,count (name) as value from ( ");
		sql.append("select (case when age<=25 then '25岁以下' ");
		sql.append("when age>=26 and age<=30 then '26岁-30岁' ");
		sql.append("when age>=31 and age<=35 then '31岁-35岁' ");
		sql.append("when age>=36 and age<=40 then '36岁-40岁' ");
		sql.append("when age>=41 and age<=45 then '41岁-45岁' ");
		sql.append("when age>=46 and age<=50 then '46岁-50岁' ");
		sql.append("when age>=51 and age<=55 then '51岁-55岁' ");
		sql.append("when age>=56 and age<=60 then '56岁-60岁' ");
		sql.append("when age>=61 then '61岁以上' ");
		sql.append("else '未知' end) as name from ( ");
		sql.append("select floor(months_between(SYSDATE, employee_birthday) / 12) as age ");
		sql.append("from t_employee t where ");
		if (StringUtils.isNoneBlank(deptCode)) {
			sql.append("DEPT_ID = '"+deptCode+"' and ");
		}
		sql.append(" t.STOP_FLG = 0 and t.DEL_FLG = 0) ");
		sql.append(" )group by name order by name");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<PersonnelInformationVo> PersonnelInformationVoList = namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<PersonnelInformationVo>() {
							@Override
							public PersonnelInformationVo mapRow(ResultSet rs,int rowNum) throws SQLException {
								PersonnelInformationVo vo = new PersonnelInformationVo();
								vo.setName(rs.getString("name"));
								vo.setValue(rs.getInt("value"));
								return vo;
							}
						});
		if (PersonnelInformationVoList != null && PersonnelInformationVoList.size() > 0) {
			return PersonnelInformationVoList;
		}
		return null;
	}

}
