package cn.honry.mobile.blackListManage.service;

import java.util.List;

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.service.BaseService;

public interface BlackListManageService  extends BaseService<MMobileTypeManage>{

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
	 * 删除
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
	void delBlackManage(String ids)  throws Exception;

	/**  
	 * 
	 * 保存
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 下午5:09:22 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 下午5:09:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:mobileTypeManage
	 * @param:flg 区分是否移至白名单标记
	 * @throws:
	 * @return: 
	 *
	 */
	void save(MMobileTypeManage mobileTypeManage, String flg)  throws Exception;


	/**  
	 * 
	 * 移至黑名单
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
	void moveWhite(String ids);

	List<String> synCach()  throws Exception;
}
