package cn.honry.oa.activiti.bpm.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfUser;
import cn.honry.oa.activiti.bpm.user.dao.OaBpmConfUserDao;
import cn.honry.oa.activiti.bpm.user.service.OaBpmConfUserService;

/**
 * 用户配置Service实现类
 * @author luyanshou
 *
 */
@Service("oaBpmConfUserService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmConfUserServiceImpl  implements OaBpmConfUserService{

	@Autowired
	@Qualifier(value = "oaBpmConfUserDao")
	private OaBpmConfUserDao oaBpmConfUserDao;
	
	public void setOaBpmConfUserDao(OaBpmConfUserDao oaBpmConfUserDao) {
		this.oaBpmConfUserDao = oaBpmConfUserDao;
	}

	@Override
	public OaBpmConfUser get(String arg0) {
		return oaBpmConfUserDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OaBpmConfUser arg0) {
		oaBpmConfUserDao.save(arg0);
	}

	/**
	 * 根据流程定义id和节点id获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param nodeId 节点配置id
	 * @return
	 */
	public List<OaBpmConfUser> getConfUserList(String processDefinitionId,String nodeId){
		return oaBpmConfUserDao.getConfUserList(processDefinitionId, nodeId);
	}
	
	/**
	 * 根据流程定义id和节点code获取用户配置信息
	 * @param processDefinitionId 流程定义id
	 * @param code 节点配置code
	 * @return
	 */
	public List<OaBpmConfUser> getUserList(String processDefinitionId,String code){
		return oaBpmConfUserDao.getUserList(processDefinitionId, code);
	}
}
