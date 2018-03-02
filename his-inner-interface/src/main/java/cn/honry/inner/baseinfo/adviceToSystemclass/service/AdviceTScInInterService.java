package cn.honry.inner.baseinfo.adviceToSystemclass.service;

import cn.honry.base.bean.model.BusinessAdvicetoSystemclass;
import cn.honry.base.service.BaseService;

/**  
 *  
 * 接口：医嘱项目及系统类别
 * @Author：aizhonghua
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface AdviceTScInInterService  extends BaseService<BusinessAdvicetoSystemclass>{

	/**  
	 * 
	 * <p> 根据医嘱类别及项目类别判断对照关系是否存在 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2017年2月21日 下午3:14:49 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2017年2月21日 下午3:14:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: BusinessAdvicetoSystemclass
	 *
	 */
	BusinessAdvicetoSystemclass getAdvtoSysByAtSt(String aType, String encode);
	
}
