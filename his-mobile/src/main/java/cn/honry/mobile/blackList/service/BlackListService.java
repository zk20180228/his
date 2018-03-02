package cn.honry.mobile.blackList.service;

import java.util.List;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.service.BaseService;

public interface BlackListService extends BaseService<MBlackList>{

	/** 经过封装后的查询出的黑名单分页信息
	* @param rows
	* @param page
	* @param mBlackList 经过封装后的mBlackList
	* @return List<MWhiteList> 黑名单分页信息
	* @author zxl
	* @throws Exception 
	* @date 2017年6月20日
	*/
	List<MBlackList> getPagedBlackList(String rows, String page, MBlackList mBlackList) throws Exception;
	
	/**  
	 * 
	 * <p>分页总条数</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午7:10:06 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午7:10:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param mBlackList 经过封装后的mBlackList
	 * @return: Integer 总条数
	 *
	 */
	Integer getCount(MBlackList mBlackList) throws Exception;

	/** 通过id删除黑名单信息
	* @param ids 版本ID
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void delBlackLists(String ids) throws Exception;

	/** 保存或修改黑名单信息
	* @param MBlackList 黑名单信息实体
	* @author zxl
	 * @param otherId 
	 * @param flg 
	 * @return 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void saveOrUpdate(MBlackList mBlackList, String flg) throws Exception;

	/** 通过用户账户获得黑名单信息
	* @param userAccunt 
	* @return MBlackList  黑名单信息集合
	* @author zxl 
	* @param machineCode 
	* @throws Exception 
	* @date 2017年6月20日
	*/
	MBlackList findBlackByUserAccunt(String userAccunt, String machineCode) throws Exception;

	/** 通过id移动至白名单信息
	* @param ids 
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void moveWhite(String ids) throws Exception;

	/** 通过用户id获得黑名单信息(查看页面经过渲染不是全字段)
	* @param id 版本ID
	* @return MBlackList  黑名单信息实体
	* @author zxl 
	* @date 2017年6月20日
	*/
	MBlackList getMBlackById(String id);

	/** 通过用户账户判断该信息是否挂失
	* @param id 版本ID
	* @return MMachineManage  
	* @author zxl 
	* @date 2017年6月20日
	*/
	List<MMachineManage> checkIsLost(String userAccount);
}
