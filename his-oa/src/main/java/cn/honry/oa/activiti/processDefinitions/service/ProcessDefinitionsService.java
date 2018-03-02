package cn.honry.oa.activiti.processDefinitions.service;

import java.util.List;

import cn.honry.oa.activiti.processDefinitions.vo.ProcessDefinitionsVO;

/**
 * 流程定义Service接口
 * @author user
 *
 */
public interface ProcessDefinitionsService {

	/**
	 * 获取流程定义列表数据
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示数量
	 * @return
	 */
	 String getListByPage(int firstResult,int maxResults);
	 List<ProcessDefinitionsVO> getProcessDefinitionsList(String name,Integer page,Integer rows);
	int getProcessDefinitionsTotal(String name);
}
