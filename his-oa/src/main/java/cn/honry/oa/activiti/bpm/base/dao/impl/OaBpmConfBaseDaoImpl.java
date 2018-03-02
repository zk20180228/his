package cn.honry.oa.activiti.bpm.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.base.dao.OaBpmConfBaseDao;

/**
 * 流程配置DAO实现类
 * @author luyanshou
 *
 */

@Repository("oaBpmConfBaseDao")
@SuppressWarnings({ "all" })
public class OaBpmConfBaseDaoImpl extends HibernateEntityDao<OaBpmConfBase>
		implements OaBpmConfBaseDao {

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 根据流程定义key和版本号 获取流程配置
	 * @param processDefinitionKey
	 * @param processDefinitionVersion
	 * @return
	 */
	public OaBpmConfBase findUnique(String processDefinitionKey,int processDefinitionVersion){
		String hql="from OaBpmConfBase t where t.processDefinitionKey=? and t.processDefinitionVersion=?";
		List<OaBpmConfBase> list = this.createQuery(hql, processDefinitionKey,processDefinitionVersion).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取所有有效的流程配置
	 * @return
	 */
	public List<OaBpmConfBase> getList(){

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ");
		sb.append(" b.ID as id, ");
		sb.append(" M.name_ || ' (' || b.process_definition_id || ')' as processDefinitionId ");
		sb.append(" FROM ");
		sb.append(" t_oa_bpm_conf_base b ");
		sb.append(" LEFT JOIN act_re_procdef P ON b.process_definition_id = P .id_ ");
		sb.append(" LEFT JOIN act_re_model M ON M.deployment_id_ = P.deployment_id_ ");
		sb.append(" WHERE ");
		sb.append(" b.stop_flg = 0 ");
		sb.append(" AND b.del_flg = 0 ");
		sb.append(" AND M.name_ IS NOT NULL ");
		sb.append(" ORDER BY b.CREATETIME desc ");

		List<OaBpmConfBase> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(OaBpmConfBase.class));
		if(list!=null&&list.size()>=0){
			return list;
		}

		return new ArrayList<>();
	}
}
