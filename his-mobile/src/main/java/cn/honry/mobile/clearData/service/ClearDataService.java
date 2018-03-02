package cn.honry.mobile.clearData.service;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.service.BaseService;

public interface ClearDataService extends BaseService<MBlackList>{

	void clear(String flag) throws Exception;

	/**  
	 * 
	 * 推送通知
	 * @Author: zxl
	 * @CreateDate: 2018年1月12日 下午3:41:48 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月12日 下午3:41:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	void sendMes()  throws Exception ;

}
