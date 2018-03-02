package cn.honry.oa.activiti.bpm.node.dao.impl;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.node.dao.OaBpmConfNodeDao;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;
import cn.honry.utils.JSONUtils;

/**
 * 流程节点配置DAO实现类
 * @author user
 *
 */
@Repository("oaBpmConfNodeDao")
@SuppressWarnings({ "all" })
public class OaBpmConfNodeDaoImpl extends HibernateEntityDao<OaBpmConfNode>
		implements OaBpmConfNodeDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据流程节点code和流程配置id获取节点信息
	 * @param code 节点code
	 * @param bpmConfBase 流程配置
	 * @return
	 */
	public List<OaBpmConfNode> getList(String code,OaBpmConfBase bpmConfBase){
		String hql=" from OaBpmConfNode where code=? and bpmConfBase=?";
		List<OaBpmConfNode> list = this.createQuery(hql, code,bpmConfBase).list();
		
		return list;
	}
	
	/**
	 * 根据流程定义ID和节点code获取节点扩展信息
	 * @param processDefinitionId 流程定义ID
	 * @param code 节点code
	 * @return
	 */
	public String getExtend(String processDefinitionId,String code){
		StringBuffer sbf = new StringBuffer("select t.extend from t_oa_bpm_conf_node t");
		sbf.append(" left join t_oa_bpm_conf_base b on t.conf_base_code=b.id ");
		sbf.append(" where b.process_definition_id=? and t.code=? and t.stop_flg=0 and t.del_flg=0");
		List<String> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString())
		.setParameter(0, processDefinitionId).setParameter(1, code).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return "";
	}

	 /**
	  * 节点code获取节点扩展信息
	  * @param processDefinitionId 流程定义ID
	  * @param code 节点code
	  * @return
	  */
	@Override
	public List<OaBpmConfNode> getConfNodeListByConfBaseCode(String code) {
		String hql=" from OaBpmConfNode where bpmConfBase.id=? order by name";
		List<OaBpmConfNode> list = this.find(hql, code);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public void saveNode(Map<String, ExtendVo> dataMap) {
		String sql = "UPDATE T_OA_BPM_CONF_NODE n SET n.EXTEND = ? WHERE n.ID = ?";
		for(Map.Entry<String,ExtendVo> m : dataMap.entrySet()){
			Object args[] = new Object[]{JSONUtils.toJson(m.getValue()),m.getKey()};  
			jdbcTemplate.update(sql,args); 
		}
	}
	
}
