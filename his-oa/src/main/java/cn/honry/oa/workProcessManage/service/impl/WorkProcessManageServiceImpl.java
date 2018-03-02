package cn.honry.oa.workProcessManage.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.workProcessManage.dao.WorkProcessManageDao;
import cn.honry.oa.workProcessManage.service.WorkProcessManageService;
import cn.honry.oa.workProcessManage.vo.ProcessInfoVo;
import cn.honry.oa.workProcessManage.vo.WorkProcessManageVo;
import cn.honry.utils.DateUtils;

@Service("workProcessManageService")
@Transactional
public class WorkProcessManageServiceImpl implements WorkProcessManageService{

		@Resource
		private WorkProcessManageDao workProcessManageDao;
		
		@Resource
		private ProcessEngine processEngine;
		public void setProcessEngine(ProcessEngine processEngine) {
			this.processEngine = processEngine;
		}
	
	
		@Override
		public List<WorkProcessManageVo> fatherMenuList() throws Exception {
			
			return workProcessManageDao.fatherMenuList();
		}
	
		@Override
		public List<WorkProcessManageVo> spreadSonMenu(String menuCode)throws Exception {
			
			return workProcessManageDao.spreadSonMenu(menuCode);
		}
	
		@Override
		public List<ProcessInfoVo> queryProcessList(String menuCode)throws Exception {
/*****************************************************先注释掉这部分，后边会用到********************************/			
//			//查询流程定义列表
//			//1.获得流程定义的查询对象
//			ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
//			//添加过滤条件
//			processDefinitionQuery.processDefinitionCategory(menuCode);
//			List<ProcessDefinition> list = processDefinitionQuery.list();
//			
//			return conversionProDefToWPMVo(list);
/*****************************************************先注释掉这部分，后边会用到********************************/		
			
			return workProcessManageDao.queryProcessList(menuCode);
		}

		@Override
		public String queryProcessInfo(String processId) throws Exception {

/*****************************************************先注释掉这部分，后边会用到********************************/	
//			//获得流程定义的查询对象
//			ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
//			//过滤条件
//			processDefinitionQuery.processDefinitionId(processId);
//			ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
//			return processDefinition.getDescription();//得到流程定义的说明
/*****************************************************先注释掉这部分，后边会用到********************************/	
			
			return workProcessManageDao.queryProcessInfo(processId);
		}

		/**
		 * 
		 * <p> 将List<ProcessDefinition> 转换为 List<ProcessInfoVo> ，后者是前台需要的属性</p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月28日 下午8:30:33 
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月28日 下午8:30:33 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param list
		 * @return
		 * @throws:
		 *
		 */
		public List<ProcessInfoVo> conversionProDefToProInfoVo(List<ProcessDefinition> list){
			
//			private String id;//id
//			private String title;//标题
//			private String createTime;//创建时间
//			private String processExplain;//流程说明
			ArrayList<ProcessInfoVo> rsList = new ArrayList<ProcessInfoVo>();
			if(list!=null){
				for(ProcessDefinition pdf: list){
					ProcessInfoVo vo = new ProcessInfoVo();
					//得到部署流程定义查询对象
					DeploymentQuery query = processEngine.getRepositoryService().createDeploymentQuery();
					query.deploymentId(pdf.getDeploymentId());
					Deployment deployment = query.singleResult();
					//通过流程定义查询部署的流程定义的id,进而查询出部署的时间
					vo.setCreateTime(DateUtils.formatDateY_M_D_H_M_S(deployment.getDeploymentTime()));
					vo.setId(pdf.getId());
					vo.setProcessExplain(pdf.getDescription());
					vo.setTitle(pdf.getName());
					
					rsList.add(vo);
				}
			}			
			
			return rsList;
		}
}
