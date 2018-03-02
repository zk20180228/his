package cn.honry.oa.activiti.bpm.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.oa.activiti.bpm.base.dao.OaBpmConfBaseDao;
import cn.honry.oa.activiti.bpm.base.service.OaBpmConfBaseService;

/**
 * 流程配置Service实现类
 * @author user
 *
 */
@Service("oaBpmConfBaseService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmConfBaseServiceImpl implements OaBpmConfBaseService {

	@Autowired
	@Qualifier(value = "oaBpmConfBaseDao")
	private OaBpmConfBaseDao oaBpmConfBaseDao;
	
	public void setOaBpmConfBaseDao(OaBpmConfBaseDao oaBpmConfBaseDao) {
		this.oaBpmConfBaseDao = oaBpmConfBaseDao;
	}

	@Override
	public OaBpmConfBase get(String arg0) {
		return oaBpmConfBaseDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmConfBase arg0) {

		oaBpmConfBaseDao.save(arg0);
	}

	/**
	 * 根据流程定义key和版本号 获取流程配置
	 * @param processDefinitionKey
	 * @param processDefinitionVersion
	 * @return
	 */
	public OaBpmConfBase findUnique(String processDefinitionKey,int processDefinitionVersion){
		return oaBpmConfBaseDao.findUnique(processDefinitionKey, processDefinitionVersion);
	}
	
	/**
	 * 获取所有有效的流程配置
	 * @return
	 */
	public List<OaBpmConfBase> getList(){
		return oaBpmConfBaseDao.getList();
	}
}
