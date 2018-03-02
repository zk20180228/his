package cn.honry.inner.system.clearStoTerminal.service;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.service.BaseService;

/** <p>门诊终端定时任务接口</p>
* @ClassName: StoTerminalService 
* @author dtl
* @date 2017年3月20日
*  
*/
public interface ClearStoTerminalService  extends BaseService<StoTerminal>{
	/** 将配药台待发药品数、已发药品数、均分次数归零
	* @Title: clearStoTerminal 
	* @author dtl 
	* @date 2017年3月20日
	*/
	public void clearStoTerminal();
}
