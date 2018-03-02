package cn.honry.oa.activiti.bpm.user.service;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.base.service.BaseService;

/**
 * 用户配置Service接口
 * @author luyanshou
 *
 */
public interface OaBpmConfUserService extends BaseService<OaBpmConfUser> {

	/**
	 * 根据流程定义id和节点id获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param nodeId 节点配置id
	 * @return
	 */
	List<OaBpmConfUser> getConfUserList(String processDefinitionId,String nodeId);
	
	/**
	 * 根据流程定义id和节点code获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param code 节点配置code
	 * @return
	 */
	List<OaBpmConfUser> getUserList(String processDefinitionId,String code);
}
