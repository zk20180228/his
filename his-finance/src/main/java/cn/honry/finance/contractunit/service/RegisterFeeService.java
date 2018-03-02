package cn.honry.finance.contractunit.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.service.BaseService;


public interface RegisterFeeService  extends BaseService<RegisterFee>{

	/**  
	 *  
	 * @Description：  合同单位数据
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterFee> getPage(String page, String rows,RegisterFee registerFee);
	/**  
	 *  
	 * @Description：  合同单位数据数量
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterFee registerFee);
	/**  
	 *  
	 * @Description：  挂号费维护添加&修改
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void save(RegisterFee registerFee,String treeId);
	/**  
	 *  
	 * @Description：  删除
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-4 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void del(String id);
	/**  
	 *  
	 * @Description：  挂号级别
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterGrade> gradeFeeCombobox();
	/**  
	 *  
	 * @Description：  查询费用修改数据
	 * @Author：ldl
	 * @CreateDate：2015-10-15  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterFee> findRegisterFee(String ids);
	/**  
	 * 
	 * @Description：  验证挂号级别是否存在
	 * @Author：lyy
	 * @CreateDate：2015-11-26 下午05:37:56  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:37:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	boolean queryFeeValidate(String unitId,String gradeId);
	/**
	 * @Description 根据单位id查询最大的排序号
	 * @author  marongbin
	 * @createDate： 2017年1月10日 上午10:18:46 
	 * @modifier 
	 * @modifyDate：
	 * @param unitId
	 * @return: String
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Integer queryOrder(String unitId);
	
}