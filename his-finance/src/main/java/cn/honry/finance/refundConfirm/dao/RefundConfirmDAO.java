package cn.honry.finance.refundConfirm.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.dao.EntityDao;

public interface RefundConfirmDAO extends EntityDao<InpatientCancelitem>{
	/**  
	 * @Description：  查询患者退费申请
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：billNo 发票号
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> query(String billNo);
	/**  
	 * @Description：  根据退费申请Id、病历号查询记录（部分信息）
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  abing 增加备注
	 * @param：drugApplyIds 退费申请记录
	 * @param：medicalRecord 病历号
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> findByIds(String drugApplyIds);
	/**  
	 * @Description：  根据处方号 和处方流水号查询收费记录
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：recipeNo 处方号
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryByRecipeNo(String recipeNo, Integer sequenceNo);
	/**  
	 * @Description：  根据发票号查询发票记录
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：invoiceNo 发票号
	 * @version 1.0
	 */
	FinanceInvoiceInfoNow queryByInfo(String invoiceNo);
	/**  
	 * @Description：  根据退费申请Id查询记录
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：drugApplyIds ids
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> findByApplyIds(String drugApplyIds);

}
