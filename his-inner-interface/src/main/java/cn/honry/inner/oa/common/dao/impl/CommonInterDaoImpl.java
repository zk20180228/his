package cn.honry.inner.oa.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.oa.common.dao.CommonInterDao;

@Repository("commonInterDao")
public class CommonInterDaoImpl extends HibernateEntityDao<OaCommon> implements
		CommonInterDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OaCommon> findMyCommon(String account, String tableCode)
			throws Exception {
		String hql = "from OaCommon t where 1=1 ";
		if (StringUtils.isNotBlank(account)) {
			hql += " and t.userAccount = '" + account + "' ";
		}
		if (StringUtils.isNotBlank(tableCode)) {
			hql += " and t.tableCode = '" + tableCode + "' ";
		}
		List<OaCommon> list = this.getSession().createQuery(hql).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return new ArrayList<OaCommon>();
	}

	@Override
	public void delCommonById(String id) throws Exception {
		String sql = "delete from T_OA_COMMON where id='" + id + "'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public OaCommon findById(String id) throws Exception {
		String hql = "from OaCommon t where t.id= '" + id + "'";
		List<OaCommon> list = this.getSession().createQuery(hql).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new OaCommon();
	}

	@Override
	public List<OaCommon> findFrom(String account) {
		String sql = "select distinct(t.tablename ) tableName , t.tablecode tableCode from t_oa_common t where t.useraccount = '"
				+ account + "'";
		SQLQuery sqlQuery = super.getSession().createSQLQuery(sql)
				.addScalar("tableName").addScalar("tableCode");
		List<OaCommon> list = sqlQuery.setResultTransformer(
				Transformers.aliasToBean(OaCommon.class)).list();
		if (list.size() > 0 && list != null) {
			return list;
		}
		return new ArrayList<OaCommon>();
	}

	/**
	 * 根据流程ID查询流程创建人
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public String queryCreateUserNameById(String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select t.user_name from t_sys_user t");
		buffer.append(" where t.user_account = ");
		buffer.append(" (select * from (select i.createuser");
		buffer.append("  from t_oa_task_info i");
		buffer.append(" where i.process_instance_id =");
		buffer.append(" (select i.process_instance_id from t_oa_task_info i");
		buffer.append("  where i.id = '" + id + "')");
		buffer.append(" order by i.createtime)");
		buffer.append(" where rownum = '1')");
		return jdbcTemplate.queryForObject(buffer.toString(), String.class);
	}
}
