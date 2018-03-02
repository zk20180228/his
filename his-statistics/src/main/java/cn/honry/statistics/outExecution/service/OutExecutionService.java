package cn.honry.statistics.outExecution.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;

public interface OutExecutionService  extends BaseService<InpatientOrderNow>{
	
	/**查询药品医嘱流水号   查询出对应数据条数来判断
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public Map<String, Object> queryLSHList(String queryBlh,String queryLsh,String startTime,String endTime );
	
	/**查询医嘱列表  药品
	 * GH
	 * @param String queryLsh住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public Map<String, Object> queryDrugLists(String queryLsh,String page,String rows,String startTime,String endTime );
	
	
	/**查询医嘱列表  非药品
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public  Map<String, Object> queryUnDrugList(String queryLsh,String page,String rows,String startTime,String endTime );
	/**  
	 *  
	 * @Description： 查询医嘱执行单信息
	 * @param deptCode 
	 * @Author：donghe
	 * @CreateDate：2016年11月29日  
	 * @version 1.0
	 *
	 */
	List<InpatientExecbill> queryDocAdvExe(String ids,String billName, String deptCode);

	public List queryDeptList();

	public Map queryInpatientByDept(String deptCode);

	public String treeInpatient(String id, String deptCode,String startTime,String endTime);

	List<InpatientExecbill> queryDocAdvExe(String ids, String billName,SysDepartment dept);

	public List<InpatientExecundrugNow> queryExecundrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String patNoData);

	public int queryExecundrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String patNoData);

	public List queryExecdrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String patNoData);

	public int queryExecdrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String patNoData);
	/**
	 * 返回药嘱类型 1:药品执行 2:非药品执行
	 * @param billNo
	 * @return 
	 */
	public Integer queryDrugBillDetail(String billNo);
}
