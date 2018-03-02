package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.dao.BedsAndNursingLevelsDao;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;

@Repository("bedsAndNursingLevelsDao")
@SuppressWarnings({ "all" })
public class BedsAndNursingLevelsDaoImpl extends
		HibernateEntityDao<BedsAndNursingLevelsVo> implements
		BedsAndNursingLevelsDao {

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
	 * 床位使用情况统计
	 * 
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<BedsAndNursingLevelsVo> queryBeds() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select TO_CHAR(count(t.bed_state)) AS value, d.code_name AS name"
				+ " from t_business_hospitalbed t "
				+ "left join t_business_dictionary d on t.bed_state = d.code_encode "
				+ "where d.code_type = 'bedtype' group by t.bed_state, d.code_name");

		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<BedsAndNursingLevelsVo> BedsAndNursingLevelsVoList = namedParameterJdbcTemplate
				.query(sql.toString(), paraMap,
						new RowMapper<BedsAndNursingLevelsVo>() {
							@Override
							public BedsAndNursingLevelsVo mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								BedsAndNursingLevelsVo vo = new BedsAndNursingLevelsVo();
								vo.setValue(rs.getString("value"));
								vo.setName(rs.getString("name"));
								return vo;
							}
						});
		if (BedsAndNursingLevelsVoList != null
				&& BedsAndNursingLevelsVoList.size() > 0) {
			return BedsAndNursingLevelsVoList;
		}
		return null;
	}

	/**
	 * 
	 * 护理级别统计
	 * 
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<BedsAndNursingLevelsVo> queryNursingLevels() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.TEND AS name, TO_CHAR(COUNT(T.TEND)) AS value FROM T_INPATIENT_INFO_NOW T  GROUP BY T.TEND");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<BedsAndNursingLevelsVo> BedsAndNursingLevelsVoList = namedParameterJdbcTemplate
				.query(sql.toString(), paraMap,
						new RowMapper<BedsAndNursingLevelsVo>() {
							@Override
							public BedsAndNursingLevelsVo mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								BedsAndNursingLevelsVo vo = new BedsAndNursingLevelsVo();
								vo.setName(rs.getString("name"));
								vo.setValue(rs.getString("value"));
								return vo;
							}
						});
		if (BedsAndNursingLevelsVoList != null
				&& BedsAndNursingLevelsVoList.size() > 0) {
			return BedsAndNursingLevelsVoList;
		}
		return null;
	}
}
