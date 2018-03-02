package cn.honry.oa.activiti.bpm.form.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.form.dao.OaBpmConfFormDao;

/**
 * 表单配置DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaBpmConfFormDao")
@SuppressWarnings({ "all" })
public class OaBpmConfFormDaoImpl extends HibernateEntityDao<OaBpmConfForm>
		implements OaBpmConfFormDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	
	/**
	 * 根据节点id获取表单配置
	 * @param node 节点id
	 * @return
	 */
	public OaBpmConfForm getFormByNodeId(String node){
		if(StringUtils.isBlank(node)){
			return null;
		}
		String hql=" from OaBpmConfForm t where t.stop_flg=0 and t.del_flg=0 and t.confNodeCode=?";
		List<OaBpmConfForm> list = this.createQuery(hql, node).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据流程定义id和节点code获取表单配置
	 * @param processDefinitionId 流程定义id
	 * @param code 节点code
	 * @return
	 */
	public List<OaBpmConfForm> getFormList(String processDefinitionId,String code){
		StringBuffer hql=new StringBuffer("from OaBpmConfForm t left join OaBpmConfNode n with t.confNodeCode=n.id ");
		hql.append("left join OaBpmConfBase b with b.id=n.bpmConfBase.id where b.processDefinitionId=? and n.code=?");
		List<OaBpmConfForm> list = this.createQuery(hql.toString(), processDefinitionId,code).list();
		return list;
	}
	
	/**
	 * 根据流程定义ID、表单code和节点code查询该节点绑定的表单属性集合
	 * @param code 表单code
	 * @param node 节点code
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	public String getFormProperties(String code,String node,String processDefinitionId){
		StringBuffer sbf =new StringBuffer("select f.properties from t_oa_bpm_conf_form f");
		sbf.append(" left join t_oa_bpm_conf_node n on f.conf_node_code= n.id ");
		sbf.append(" left join t_oa_bpm_conf_base t on n.conf_base_code= t.id ");
		sbf.append(" where t.process_definition_id=? and f.value=? and n.code=? and f.stop_flg=0 and f.del_flg=0");
		List<String> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString()).setParameter(0, processDefinitionId)
		.setParameter(1, code).setParameter(2, node).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return "";
	}
	
	/**
	 * 获取record序列
	 * @return
	 */
	public String getRecordSeq(){
		String sql="select SEQ_OA_KV_RECORD.NEXTVAL FROM DUAL";
		List list = this.getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}
}
