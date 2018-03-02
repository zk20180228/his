package cn.honry.oa.activiti.bpm.form.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.dao.EntityDao;

/**
 * 表单配置DAO接口
 * @author 
 *
 */
public interface OaBpmConfFormDao extends EntityDao<OaBpmConfForm> {

	/**
	 * 根据节点id获取表单配置
	 * @param node 节点id
	 * @return
	 */
	OaBpmConfForm getFormByNodeId(String node);
	
	/**
	 * 根据流程定义id和节点code获取表单配置
	 * @param processDefinitionId 流程定义id
	 * @param code 节点code
	 * @return
	 */
	List<OaBpmConfForm> getFormList(String processDefinitionId,String code);
	
	/**
	 * 根据流程定义ID、表单code和节点code查询该节点绑定的表单属性集合
	 * @param code 表单code
	 * @param node 节点code
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	String getFormProperties(String code,String node,String processDefinitionId);
	
	/**
	 * 获取record序列
	 * @return
	 */
	String getRecordSeq();
}
