package cn.honry.oa.activiti.bpm.base.service;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.service.BaseService;

public interface OaBpmConfBaseService extends BaseService<OaBpmConfBase>{

	/**
	 * 根据流程定义key和版本号 获取流程配置
	 * @param processDefinitionKey
	 * @param processDefinitionVersion
	 * @return
	 */
	OaBpmConfBase findUnique(String processDefinitionKey,int processDefinitionVersion);
	
	/**
	 * 获取所有有效的流程配置
	 * @return
	 */
	List<OaBpmConfBase> getList();
	
}
