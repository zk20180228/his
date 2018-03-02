package cn.honry.statistics.leaveOrder.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;


public interface LeaveOrderService {

	
	/**查询药品医嘱流水号   查询出对应数据条数来判断
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientInfoNow> queryLSHList(String queryBlh,String queryLsh,String startTime,String endTime );
	
	/**查询医嘱列表  药品
	 * GH
	 * @param String queryLsh住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecdrugNow> queryDrugLists(String queryLsh,String page,String rows,String startTime,String endTime );
	/**查询医嘱列表  药品总记录数
	 * GH
	 * @param String queryLsh住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	int queryDrugListsTotal(String queryLsh,String startTime,String endTime );
	/**查询医嘱列表  非药品
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecundrugNow> queryUnDrugList(String queryLsh,String page,String rows,String startTime,String endTime );
	/**查询医嘱列表  非药品总记录数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	int queryUnDrugListTotal(String queryLsh,String startTime,String endTime );
}
