package cn.honry.oa.activiti.bpm.listener.service;

import cn.honry.base.bean.model.OaBpmConfListener;
import cn.honry.base.service.BaseService;

/**
 * 监听器Service接口
 * @author user
 *
 */
public interface OaBpmConfListenerService extends
		BaseService<OaBpmConfListener> {

	/**
	 * 根据值、类型和所属节点id获取监听器
	 * @param value 值
	 * @param type 类型
	 * @param nodeCode 所属节点id
	 * @return
	 */
	OaBpmConfListener findUnique(String value,int type,String nodeCode);
}
