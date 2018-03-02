package cn.honry.oa.activiti.bpm.user.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.user.dao.OaBpmConfUserDao;

/**
 * 用户配置DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaBpmConfUserDao")
@SuppressWarnings({ "all" })
public class OaBpmConfUserDaoImpl extends HibernateEntityDao<OaBpmConfUser>
		implements OaBpmConfUserDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 根据流程定义id和节点id获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param nodeId 节点配置id
	 * @return
	 */
	public List<OaBpmConfUser> getConfUserList(String processDefinitionId,String nodeId){
		StringBuffer sbf = new StringBuffer("select u.id as id,u.value as value ,u.type as type ,u.status as status");
		sbf.append(" ,u.priority as priority from t_oa_bpm_conf_user u ");
		sbf.append(" left join t_oa_bpm_conf_node n on  u.node_id=n.id ");
		sbf.append(" left join t_oa_bpm_conf_base b on b.id=n.conf_base_code ");
		sbf.append(" where n.id=? and b.process_definition_id=? and u.del_flg=0 and u.stop_flg=0 ");
		List<OaBpmConfUser> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString())
				.addScalar("id").addScalar("value").addScalar("type",Hibernate.INTEGER)
				.addScalar("status", Hibernate.INTEGER).addScalar("priority",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(OaBpmConfUser.class))
				.setParameter(0, nodeId).setParameter(1,processDefinitionId).list();
		return list;
	}
	
	/**
	 * 根据流程定义id和节点code获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param code 节点配置code
	 * @return
	 */
	public List<OaBpmConfUser> getUserList(String processDefinitionId,String code){
		StringBuffer sbf = new StringBuffer("select u.id as id,u.value as value ,u.type as type ,u.status as status");
		sbf.append(" ,u.priority as priority from t_oa_bpm_conf_user u ");
		sbf.append(" left join t_oa_bpm_conf_node n on  u.node_id=n.id ");
		sbf.append(" left join t_oa_bpm_conf_base b on b.id=n.conf_base_code ");
		sbf.append(" where n.code=? and b.process_definition_id=? and u.del_flg=0 and u.stop_flg=0 ");
		List<OaBpmConfUser> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString())
				.addScalar("id").addScalar("value").addScalar("type",Hibernate.INTEGER)
				.addScalar("status", Hibernate.INTEGER).addScalar("priority",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(OaBpmConfUser.class))
				.setParameter(0, code).setParameter(1,processDefinitionId).list();
		return list;
	}
}
