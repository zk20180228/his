package cn.honry.oa.activiti.processDefinitions.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.activiti.processDefinitions.dao.ProcessDefinitionsDao;
import cn.honry.oa.activiti.processDefinitions.service.ProcessDefinitionsService;
import cn.honry.oa.activiti.processDefinitions.vo.ProcessDefinitionsVO;
import cn.honry.utils.JSONUtils;

/**
 * 流程定义Service实现类
 * @author user
 *
 */
@Service("processDefinitionsService")
@Transactional
@SuppressWarnings({ "all" })
public class ProcessDefinitionsServiceImpl implements ProcessDefinitionsService {

	@Resource
	private ProcessEngine processEngine;//工作流引擎
	@Resource
	private ProcessDefinitionsDao processDefinitionsDao;//工作流引擎
	
	/**
	 * 获取流程定义列表数据
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示数量
	 * @return
	 */
	public String getListByPage(int firstResult,int maxResults){
		Map<String,Object>map =new HashMap<>();
		String tenantId="1";
		RepositoryService repositoryService = processEngine.getRepositoryService();//获取仓库服务对象
		long count = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(tenantId).count();//获取流程定义总数
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//获取流程定义列表(分页)
				.processDefinitionTenantId(tenantId).listPage(firstResult, maxResults);
		map.put("total", count);
		map.put("rows", list);
		String[] fields ={"id","name","category","version","description","suspended"};
		String json = JSONUtils.toExposeJson(map,false,"YYYY-MM-DD",fields);
		return json;
	}

	@Override
	public List<ProcessDefinitionsVO> getProcessDefinitionsList(String name,
			Integer page, Integer rows) {
		return processDefinitionsDao.getProcessDefinitionsList(name, page, rows);
	}

	@Override
	public int getProcessDefinitionsTotal(String name) {
		return processDefinitionsDao.getProcessDefinitionsTotal(name);
	}
	
}
