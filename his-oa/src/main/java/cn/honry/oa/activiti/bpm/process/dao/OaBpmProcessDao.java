package cn.honry.oa.activiti.bpm.process.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;

/**
 * 流程配置DAO接口
 * @author luyanshou
 *
 */
public interface OaBpmProcessDao extends EntityDao<OaBpmProcess> {

	/**
	 * 根据租户id获取流程配置分页数据
	 * @param tenantId 租户id
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<OaBpmProcess> getListByPage(String name,String tenantId,int firstResult,int maxResults);
	
	/**
	 * 根据租户id获取流程配置总数
	 * @param tenantId 租户id
	 */
	int getTotal(String name,String tenantId);
	
	/**
	 * 获取流程定义信息列表及所属分类
	 * @return
	 */
	List<OaProcessVo> getCategoryInfo(int page, int rows,String param,String category,String treeId);
	List<OaProcessVo> getCategoryInfo();
	Integer getCategoryInfo(String param,String category,String treeId);
	
	/**
	 * 根据流程定义信息id获取配置信息
	 * @param processId
	 * @return
	 */
	OaBpmConfBase getConfBase(String processId);
	
	/**
	 * 根据流程定义id获取流程定义信息
	 * @param processDefinitionId
	 * @return
	 */
	OaBpmProcess getProcessInfo(String processDefinitionId);
}
