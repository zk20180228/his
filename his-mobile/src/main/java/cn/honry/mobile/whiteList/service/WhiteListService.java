package cn.honry.mobile.whiteList.service;

import java.util.List;

import cn.honry.base.bean.model.MWhiteList;
import cn.honry.base.service.BaseService;

public interface WhiteListService extends BaseService<MWhiteList>{

	/** 经过封装后的查询出的白名单分页信息
	* @param pageUtil 经过封装后的分页信息
	* @return PageUtil<MWhiteList> 经过封装后的查询出的白名单分页信息
	* @author zxl
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	List<MWhiteList> getPagedWhiteList(String rows, String page, MWhiteList mWhiteList) throws Exception;
	
	/**  
	 * 
	 * <p>获得分页总条数</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月18日 上午9:17:37 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月18日 上午9:17:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param mWhiteList
	 * @return
	 * @throws Exception:
	 * @throws:
	 * @return: Integer 返回值类型
	 *
	 */
	Integer getCount(MWhiteList mWhiteList) throws Exception;

	/** 通过id删除白名单信息
	* @param ids 版本ID
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void delWhiteLists(String ids) throws Exception;
	
	/** 保存或修改白名单信息
	* @param MWhiteList 白名单信息实体
	* @author zxl
	* @param flg 
	* @return 
	* @throws Exception 
	* @date 2017年6月20日
	*/
	void saveOrUpdate(MWhiteList mWhiteList, String flg) throws Exception;

	/** 通过用户账户获得白名单信息
	* @param id 版本ID
	* @return MWhiteList  白名单信息实体
	* @author zxl 
	 * @param machineCode 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	MWhiteList findWhiteByUserAccunt(String userAccunt, String machineCode) throws Exception;

	/** 通过id移动至黑名单
	* @param id 版本ID
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void moveBlack(String ids) throws Exception;
	
	/** 通过用户id获得白名单信息(查看页面)
	* @param id 版本ID
	* @return mWhiteList  白名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MWhiteList getMWhiteById(String id);
}
