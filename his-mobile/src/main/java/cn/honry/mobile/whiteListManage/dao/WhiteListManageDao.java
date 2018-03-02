package cn.honry.mobile.whiteListManage.dao;

import java.util.List;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.dao.EntityDao;

public interface WhiteListManageDao  extends EntityDao<MMobileTypeManage>{

	/**  
	 * 
	 * 白名单（短信）管理列表数据
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
	List<MMobileTypeManage> getWhiteManageList(String rows, String page,
			String mobileCategory) throws Exception;

	/**  
	 * 
	 * 白名单（短信）管理总条数
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
	Integer getWhiteManageCount(String mobileCategory) throws Exception;
	
	/**  
	 * 
	 * 校验该信息是否存在于白名单
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
	List<MMobileTypeManage> checkExist(String mobileCategory,String type)  throws Exception;
	
	/**  
	 * 
	 * 白名单数据初始化
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: 
	 *
	 */
	List<String> getInitData()   throws Exception;

	/**  
	 * 
	 * 清空白名单数据
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: 
	 *
	 */
	void clearData()  throws Exception;

}
