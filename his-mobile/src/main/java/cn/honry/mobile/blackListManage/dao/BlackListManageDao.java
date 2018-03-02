package cn.honry.mobile.blackListManage.dao;

import java.util.List;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.dao.EntityDao;

public interface BlackListManageDao  extends EntityDao<MMobileTypeManage>{

	/**  
	 * 
	 * 黑名单（短信）管理列表数据
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<MMobileTypeManage> getBlackManageList(String rows, String page,
			String mobileCategory) throws Exception;

	/**  
	 * 
	 * 黑名单（短信）管理总条数
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	Integer getBlackManageCount(String mobileCategory) throws Exception;

	/**  
	 * 
	 * 获取所有黑名单数据黑名单（短信）管理
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<String> getBlackList() throws Exception;
}
