package cn.honry.finance.invoiceInspect.service;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoiceInspect.vo.InvoiceInspectVo;
import cn.honry.utils.TreeJson;

@SuppressWarnings({"all"})
public interface InvoiceInspectService {
	/**  
	 * @Description：  查询发票类型树
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<TreeJson> queryInvoiceType(String id) throws Exception;
	/**  
	 * @Description：  查询待审核列表
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InvoiceInspectVo> queryInvoiceInfoList(Date beginTime,
			Date endTime, String balanceOpcd, String encode,String page,String rows) throws Exception;
	/**  
	 * @Description：  保存发票核查列表
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void saveDatagrid(String rows,String intype) throws Exception;
	/**  
	 * @Description：  查询收费员下拉框
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryBalanceOpcd(String q) throws Exception;
	/**  
	 * @Description：  查询总记录数
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTotal(Date beginTime, Date endTime, String balanceOpcd, String encode) throws Exception;
	

}
