package cn.honry.statistics.sys.outpatientInvoice.service;

import java.util.List;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.sys.outpatientInvoice.vo.InvoiceInfoVo;
import cn.honry.statistics.sys.outpatientInvoice.vo.OutpatientStaVo;

public interface OutpatientInvoiceService extends BaseService<OutpatientFeedetail>{
	
	/**  
	 * @Description：  根据发票号查询患者挂号信息
	 * @Author：ldl
	 * @CreateDate：2016-06-21
	 * @ModifyRmk：  
	 * @param：invoiceNo 发票号
	 * @version 1.0
	 */
	InvoiceInfoVo queryInvoiceInfoVo(String invoiceNo);
	
	/**  
	 * @Description：  根据发票号查询患者收费信息
	 * @Author：ldl
	 * @CreateDate：2016-06-21
	 * @ModifyRmk：  
	 * @param：invoiceNo 发票号
	 * @version 1.0
	 */
	List<OutpatientStaVo> findOutpatient(String invoiceNo);

}
