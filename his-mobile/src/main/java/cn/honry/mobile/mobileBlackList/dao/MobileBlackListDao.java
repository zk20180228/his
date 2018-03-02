package cn.honry.mobile.mobileBlackList.dao;

import java.util.List;

import cn.honry.base.bean.model.MMobileBlackList;
import cn.honry.base.dao.EntityDao;

public interface MobileBlackListDao  extends EntityDao<MMobileBlackList>{

	/**  
	 * 
	 * 查询手机号黑名单管理列表数据
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 上午11:18:17 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 上午11:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:rows
	 * @param:page
	 * @param:queryName
	 * @param:type
	 * @throws:
	 * @return: 
	 *
	 */
	List<MMobileBlackList> getCellPhoneBlack(String rows, String page,
			String queryName, String type) throws Exception ;

	/**  
	 * 
	 * 查询手机号黑名单管理总条数
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 上午11:18:17 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 上午11:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	Integer getCellPhoneBlackCount(String queryName, String type) throws Exception ;
	
	/** 
	 * 根据手机号和类型校验该信息是否存在
	 * @param request
	 * @param response
	 * @author zxl
	 * @date 2017年6月20日
	 */
	List<MMobileBlackList> checkExist(String mobileNum, String type)  throws Exception;

	
	/**  
	 * 
	 * 根据类型获取黑名单数据
	 * @Author: zxl
	 * @CreateDate: 2018年1月13日 上午11:18:17 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月13日 上午11:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:ids
	 * @throws:
	 * @return: 
	 *
	 */
	List<String> synCach(String str) throws Exception;

}
