package cn.honry.mobile.machineManage.dao;

import java.util.List;

import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.dao.EntityDao;

public interface MachineManageDao extends EntityDao<MMachineManage>{

	/** 根据查询条件获得总条数
	* @param machineManage 封装了查询条件的machineManage
	* @return Integer
	* @author zxl
	* @date 2017年6月20日
	*/
	Integer getTotal(MMachineManage machineManage) throws Exception;

	/** 根据查询条件获得一页数据
	* @param page 
	* @param rows 
	* @param machineManage 封装了查询条件的machineManage
	* @return List<MMachineManage>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MMachineManage> getList(String page, String rows, MMachineManage machineManage) throws Exception;
	

	/** 根据用户账户查询设备管理
	* @param userAccunt 用户账户
	* @return List<MMachineManage>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MMachineManage> getMachinesByUserAccunt(String userAccunt) throws Exception;

	/** 通过用户账户删除
	* @param mac 
	* @author zxl 
	* @date 2017年6月20日
	*/
	void delMacByAccount(MMachineManage mac);
	
	/** 通过用户账户移至白名单或黑名单设备信息
	* @param ids 版本ID
	* @param flg 1.移至白名单  2 移至黑名单
	* @author zxl 
	* @date 2017年6月20日
	*/
	void updateWOrB(MMachineManage machineManage);

	
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
	


	/**  
	 * 
	 * 通过账号和设备码获取设备信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午6:33:34 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午6:33:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param MMachineManage
	 * @throws Exception:
	 *
	 */
	MMachineManage getMacByAccountAndMach(MMachineManage mac);

	/** 根据用户账户查询已挂失设备管理
	* @param userAccunt 用户账户,多个用逗号拼接
	* @return List<MMachineManage>
	* @author zxl
	* @date 2017年6月20日
	*/
	List<MMachineManage> checkIsLost(String userAccount); 
}
