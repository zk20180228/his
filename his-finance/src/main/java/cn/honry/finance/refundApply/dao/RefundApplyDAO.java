package cn.honry.finance.refundApply.dao;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.EntityDao;

public interface RefundApplyDAO extends EntityDao<InpatientCancelitem>{
	/**  
	 * @Description： 根据门诊号查询挂号信息
	 * @Author：ldl
	 * @CreateDate：2016-06-27
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param clinicCode 门诊号
	 */
	RegistrationNow findInfo(String clinicCode);
	/**  
	 * @Description： 根据收费明细表中ID查询收费明细记录
	 * @Author：ldl
	 * @CreateDate：2016-06-27
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param id 收费明细表中ID
	 */
	OutpatientFeedetailNow queryFeedetail(String id);

}
