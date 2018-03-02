package cn.honry.inner.system.clearStoTerminal.dao;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.dao.EntityDao;

/** <p>门诊终端定时任务接口</p>
* @ClassName: StoTerminalDAO 
* @author dtl
* @date 2017年3月20日
*  
*/
@SuppressWarnings({"all"})
public interface ClearStoTerminalDAO extends EntityDao<StoTerminal>{

	/**  将配药台待发药品数、已发药品数、均分次数归零
	* @Title: clearStoTerminal 
	* @author dtl 
	* @date 2017年3月20日
	*/
	void clearStoTerminal();

}
