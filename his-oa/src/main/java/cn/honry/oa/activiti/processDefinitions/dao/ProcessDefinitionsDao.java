package cn.honry.oa.activiti.processDefinitions.dao;

import java.util.List;

import cn.honry.oa.activiti.processDefinitions.vo.ProcessDefinitionsVO;

public interface ProcessDefinitionsDao {
	List<ProcessDefinitionsVO> getProcessDefinitionsList(String name,Integer page,Integer rows);
	int getProcessDefinitionsTotal(String name);
}
