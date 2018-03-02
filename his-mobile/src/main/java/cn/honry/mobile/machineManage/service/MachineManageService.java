package cn.honry.mobile.machineManage.service;

import java.util.List;

import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.service.BaseService;

public interface MachineManageService  extends BaseService<MMachineManage>{

	/** 经过封装后的查询出的设备管理分页信息
	* @param pageUtil 经过封装后的分页信息
	* @return PageUtil<MMachineManage> 经过封装后的查询出的设备管理分页信息
	* @author zxl
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	List<MMachineManage> getPagedMachineList(String page, String rows, MMachineManage machineManage) throws Exception;

	/**  
	 * 
	 * <p>分页总条数</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午6:06:56 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午6:06:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param machineManage
	 * @return: Integer 分页总条数
	 *
	 */
	Integer getCount(MMachineManage machineManage) throws Exception;
	/** 通过id删除设备信息
	* @param ids 版本ID
	* @author zxl 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void delMachines(String ids) throws Exception;

	/** 根据用户账户查询设备信息
	* @param userAccunt 用户账户
	* @author zxl
	 * @return 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	List<MMachineManage> findMachineByUserAccunt(String userAccunt) throws Exception;

	/** 通过id挂失或激活设备信息
	* @param ids 版本ID
	* @param flg 1.激活状态   2挂失状态
	* @author zxl 
	 * @param userAndMach 用户账号及设备码
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void updateLossOrActivate(String ids,String flg, String userAndMach) throws Exception;

	/** 通过id移至白名单或黑名单设备信息
	* @param ids 版本ID
	* @param flg 1.移至白名单  2 移至黑名单
	* @author zxl 
	 * @param userAndMach 用户账户和设备码 
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	void updateWhiteOrBlack(String ids, String flg, String userAndMach) throws Exception;

	/**  
	 * 
	 * <p>修改添加保存</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午6:33:34 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午6:33:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param mMachineManage
	 * @throws Exception:
	 *
	 */
	void save(MMachineManage mMachineManage) throws Exception;

	/**  
	 * 
	 * 通过设备码获取设备信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午6:33:34 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午6:33:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param machineCode
	 * @throws Exception:
	 *
	 */
	MMachineManage getMachineByMachineCode(String machineCode);

	/**  
	 * 
	 * 通过SIM码获取设备信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午6:33:34 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午6:33:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param mobileNum
	 * @throws Exception:
	 *
	 */
	List<MMachineManage> getMachineByMobileNum(List<String> mobileNum);



}
