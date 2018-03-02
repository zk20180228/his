package cn.honry.oa.activiti.bpm.listener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaBpmConfListener;
import cn.honry.oa.activiti.bpm.listener.dao.OaBpmConfListenerDao;
import cn.honry.oa.activiti.bpm.listener.service.OaBpmConfListenerService;

/**
 * 监听器Service实现类
 * @author user
 *
 */
@Service("oaBpmConfListenerService")
@Transactional
@SuppressWarnings({ "all" })
public class OaBpmConfListenerServiceImpl implements OaBpmConfListenerService {

	@Autowired
	@Qualifier(value="oaBpmConfListenerDao")
	private OaBpmConfListenerDao oaBpmConfListenerDao;
	
	public void setOaBpmConfListenerDao(OaBpmConfListenerDao oaBpmConfListenerDao) {
		this.oaBpmConfListenerDao = oaBpmConfListenerDao;
	}

	@Override
	public OaBpmConfListener get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaBpmConfListener arg0) {

		oaBpmConfListenerDao.save(arg0);
	}

	/**
	 * 根据值、类型和所属节点id获取监听器
	 * @param value 值
	 * @param type 类型
	 * @param nodeCode 所属节点id
	 * @return
	 */
	public OaBpmConfListener findUnique(String value,int type,String nodeCode){
		return oaBpmConfListenerDao.findUnique(value, type, nodeCode);
	}
}
