package cn.honry.oa.information.service;

import java.util.List;

import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.base.service.BaseService;

public interface InformationAttachDownAuthService extends BaseService<InformationAttachDownAuth> {
	/**  
	 * <p>根据栏目id获取对应的附件 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午6:32:43 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午6:32:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * List<InformationAttachDownAuth>
	 */
	List<InformationAttachDownAuth> getAuthByMenuID(String infoid);
	/**  
	 * <p>根据文章id删除相应的附件权限 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月28日 下午7:18:15 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月28日 下午7:18:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * void
	 */
	void delAuth(String menuId);
}
