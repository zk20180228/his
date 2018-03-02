package cn.honry.inner.baseinfo.clinic.service;

import java.util.Map;

import cn.honry.base.bean.model.Clinic;
import cn.honry.base.service.BaseService;

/**  
 *  
 * 接口：诊室
 * @Author：aizhonghua
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface ClinicInInterService  extends BaseService<Clinic>{
	
	/**  
	 *  
	 * @Description：  获得诊室code和name
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-12 下午03:48:09  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-12 下午03:48:09  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> queryClinicIdAndNameMap();
}
