package cn.honry.inner.technical.terminalApply.dao;

import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Description：门诊医技终端dao 
 * @Author：kjh
 * @CreateDate：2016-1-12 上午11:59:17
 * @Modifier：wanxing
 * @ModifyDate：2016-3-31 下午15:15:31  
 */
@SuppressWarnings({"all"})
public interface TerminalApplyInInterDAO extends EntityDao<TecTerminalApply>{
	
	/**  
	 * @Description： 获得原有医技信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：wanxing
	 * @ModifyDate：2016-04-05 下午16:36:25  
	 * @ModifyRmk：  将hql中拼接参数的形式修改为预编译的形式
	 * @version 1.0
	 */
	TecTerminalApply getApplyApplyNo(String applyNo);
	
}
