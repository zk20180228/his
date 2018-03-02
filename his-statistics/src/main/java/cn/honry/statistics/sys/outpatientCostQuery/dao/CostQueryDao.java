package cn.honry.statistics.sys.outpatientCostQuery.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.FinanceInvoiceInfo;
import cn.honry.base.bean.model.FinanceInvoicedetail;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface CostQueryDao extends EntityDao<OutpatientFeedetail>{
	
	/**  
	 * @Description：  根据发票号，开始时间，结束时间查询发票汇总
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @param： beginTime 开始时间
	 * @param： endTime 结束时间
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,Object> findInvoiceNoSummary(String invoiceNo, String beginTime,String endTime,String page,String rows) throws Exception;
	
	/**  
	 * @Description：  根据发票号，开始时间，结束时间查询发票明细
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @param： beginTime 开始时间
	 * @param： endTime 结束时间
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,Object> findInvoiceDetailed(String invoiceNo,String beginTime, String endTime,String page,String rows) throws Exception;
	
	/**  
	 * @Description：  根据发票号，开始时间，结束时间查询费用明细
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @param： beginTime 开始时间
	 * @param： endTime 结束时间
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,Object> findCostDetailed(String invoiceNo,String beginTime, String endTime,String page,String rows) throws Exception;
	

	
	
	/**  
	 * @Description：   渲染统计大类
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<MinfeeStatCode> itemFunction();

}
