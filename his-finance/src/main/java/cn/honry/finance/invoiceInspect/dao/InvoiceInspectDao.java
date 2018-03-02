package cn.honry.finance.invoiceInspect.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoiceInspect.vo.InvoiceInspectVo;

@SuppressWarnings({"all"})
public interface InvoiceInspectDao {

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
	 * @Description：  查询收费员下拉框
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> queryBalanceOpcd(String q) throws Exception;
	/**  
	 * @Description：  保存发票核查列表
	 * @Author：tcj
	 * @CreateDate：2016-2-1  上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveDatagrid(List arrlist,String intype,String eid) throws Exception;
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
