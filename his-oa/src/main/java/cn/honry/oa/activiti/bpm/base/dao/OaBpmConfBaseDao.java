package cn.honry.oa.activiti.bpm.base.dao;

import java.util.List;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.dao.EntityDao;
/**
 * 流程配置DAO接口
 * @author user
 *
 */
public interface OaBpmConfBaseDao extends EntityDao<OaBpmConfBase> {

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
