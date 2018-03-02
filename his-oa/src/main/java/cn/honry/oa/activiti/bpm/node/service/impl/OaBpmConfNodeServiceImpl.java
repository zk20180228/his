package cn.honry.oa.activiti.bpm.node.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.oa.activiti.bpm.node.dao.OaBpmConfNodeDao;
import cn.honry.oa.activiti.bpm.node.service.OaBpmConfNodeService;
import cn.honry.oa.activiti.bpm.utils.ExtendVo;

/**
 * 流程节点配置Service实现类
 * @author user
 *
 */
@Service("oaBpmConfNodeService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmConfNodeServiceImpl implements OaBpmConfNodeService {

	@Autowired
	@Qualifier(value = "oaBpmConfNodeDao")
	private OaBpmConfNodeDao oaBpmConfNodeDao;
	
	public void setOaBpmConfNodeDao(OaBpmConfNodeDao oaBpmConfNodeDao) {
		this.oaBpmConfNodeDao = oaBpmConfNodeDao;
	}

	@Override
	public OaBpmConfNode get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmConfNode arg0) {

		oaBpmConfNodeDao.save(arg0);
	}

	/**
	 * 根据流程节点code和流程配置id获取节点信息
	 * @param code 节点code
	 * @param bpmConfBase 流程配置
	 * @return
	 */
	public List<OaBpmConfNode> getList(String code,OaBpmConfBase bpmConfBase){
		return oaBpmConfNodeDao.getList(code, bpmConfBase);
	}
	
	/**
	 *  根据流程节点code和流程配置id获取节点信息
	 * @param code 节点code
	 * @param bpmConfBase 流程配置
	 * @return
	 */
	public OaBpmConfNode findUinque(String code,OaBpmConfBase bpmConfBase){
		List<OaBpmConfNode> list = oaBpmConfNodeDao.getList(code, bpmConfBase);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据流程定义ID和节点code获取节点扩展信息
	 * @param processDefinitionId 流程定义ID
	 * @param code 节点code
	 * @return
	 */
	public String getExtend(String processDefinitionId,String code){
		return oaBpmConfNodeDao.getExtend(processDefinitionId, code);
	}

	 /**
	  * 节点code获取节点扩展信息
	  * @param processDefinitionId 流程定义ID
	  * @param code 节点code
	  * @return
	  */
	@Override
	public List<OaBpmConfNode> getConfNodeListByConfBaseCode(String code) {
		return oaBpmConfNodeDao.getConfNodeListByConfBaseCode(code);
	}

	@Override
	public void saveNode(Map<String, ExtendVo> dataMap) {
		oaBpmConfNodeDao.saveNode(dataMap);
	}
}
