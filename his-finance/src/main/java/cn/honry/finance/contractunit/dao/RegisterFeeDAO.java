package cn.honry.finance.contractunit.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.EntityDao;



@SuppressWarnings({"all"})
public interface RegisterFeeDAO extends EntityDao<RegisterFee>{

	/**  
	 *  
	 * @Description：  挂号级别树查询
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterFee> findTree();
	
	/**  
	 *  
	 * @Description：  合同单位数据数量
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterFee entity);
	/**  
	 *  
	 * @Description：  合同单位数据
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterFee> getPage(RegisterFee entity, String page, String rows);
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
	 * @Description： 验证挂号级别是否存在 
	 * @Author：lyy
	 * @CreateDate：2015-11-26 下午05:37:56  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:37:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	boolean queryFeeValidate(String unitId, String gradeId);
	/**
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2017年1月10日 上午10:30:09 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Integer getOrderbyid(String unitId);
}
	
	