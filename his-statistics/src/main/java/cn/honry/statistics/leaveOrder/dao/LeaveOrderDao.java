package cn.honry.statistics.leaveOrder.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;

@SuppressWarnings({"all"})
public interface LeaveOrderDao {

	/**查询住院信息   标识 已出院    
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public List<InpatientInfoNow> queryInfoNows(String queryBlh,String queryLsh,String startTime,String endTime);
	
	
	
	
	
	/**查询医嘱列表  药品 总条数
	 * GH
	 * @param tnL 
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public int queryDrugTotal(List<String> tnL, String queryName,String startTime,String endTime);
	
	/**查询医嘱列表
	 * GH
	 * @param tnL 
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecdrugNow> queryDrugList(List<String> tnL, String queryName,String startTime,String endTime,String page,String rows );
	
	
	/**查询医嘱列表 非  药品 总条数
	 * GH
	 * @param tnL 
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public int queryUnDrugTotal(List<String> tnL, String queryName,String startTime,String endTime);
	
	/**查询医嘱列表  非药品
	 * GH
	 * @param tnL 
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecundrugNow> queryUnDrugList(List<String> tnL, String queryName,String startTime,String endTime,String page,String rows );
}
