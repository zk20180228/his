package cn.honry.oa.activiti.delegateInfo.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaDelegateInfo;
import cn.honry.base.service.BaseService;
import cn.honry.oa.activiti.delegateInfo.vo.DelegateInfoVo;

/**
 * 代理配置service接口
 * @author luyanshou
 *
 */
public interface OaDelegateInfoService extends BaseService<OaDelegateInfo> {

	/**
	 * 获取代理人
	 * @param assignee 负责人
	 * @param processDefinitionId 流程定义id
	 * @param activityId 节点id
	 * @param tenantId 租户id
	 * @return
	 */
	Map<String,String> getAttorney(String assignee,String processDefinitionId,String activityId,String tenantId);
	
	/**
	 * 查询我的代理
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<OaDelegateInfo> queryMyDelegate(int page,int rows);
	
	/**
	 * 查询我的代理总记录数
	 * @return int 
	 */
	int queryMyDelegateRotal();
	
	/**
	 * 业务名称
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<DelegateInfoVo> queryProcess();
	
	/**
	 * 根据业务名称code 查询环节
	 * @param page
	 * @param rows
	 * @return List<OaDelegateInfo> 
	 */
	List<OaBpmConfNode> queryOaBpmConfNode(String proDefId);
	
	/**
	 * 添加我的代理
	 * @param OaDelegateInfo oadeInfo
	 */
	void addOaDelegateInfo(OaDelegateInfo oadeInfo);
	
	/**
	 * 删除我的代理
	 * @param OaDelegateInfo oadeInfo
	 */
	void delMydelegateInfo(String id);
	
	/**
	 * 根据id查询我的代理
	 * @param String id
	 */
	OaDelegateInfo  findMydelegateInfo(String id);
}
