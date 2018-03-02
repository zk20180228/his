package cn.honry.statistics.outExecution.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.leaveOrder.vo.InpatientExecDrugVo;
import cn.honry.statistics.leaveOrder.vo.InpatientExecUnDrugVo;

@SuppressWarnings({"all"})
public interface OutExecutionDAO extends EntityDao<InpatientExecDrugVo>{

	/**查询住院信息   标识 已出院    
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public List<InpatientInfoNow> queryInfoNows(String queryBlh,String queryLsh,String startTime,String endTime);
	
	
	/**查询医嘱列表  药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public int queryDrugTotal(String queryName,String startTime,String endTime);
	
	/**查询医嘱列表
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecDrugVo> queryDrugList(String queryName,String startTime,String endTime,String page,String rows );
	
	
	/**查询医嘱列表 非  药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	public int queryUnDrugTotal(String queryName,String startTime,String endTime);
	
	/**查询医嘱列表  非药品
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	public List<InpatientExecUnDrugVo> queryUnDrugList(String queryName,String startTime,String endTime,String page,String rows );
	/**  
	 *  
	 * @Description： 查询医嘱执行单信息
	 * @param dept 
	 * @Author：donghe
	 * @CreateDate：2016年11月29日  
	 * @version 1.0
	 *
	 */
	List<InpatientExecbill> queryDocAdvExe(String ids,String billName, SysDepartment dept);


	public List queryDeptList();


	public Map queryInpatientByDept(String deptCode);


//	public List queryInpatientList(List<String> tnL,String deptCode);


	public List queryOneList(List<String> tnL,String id,String deptCode,String startTime,String endTime);


	public List<InpatientExecbill> queryDocAdvExe(String ids, String billName,
			String deptCode);


	public List<InpatientExecundrugNow> queryExecundrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo);


	public int queryExecundrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo);


	public List queryExecdrugpage(List<String> tnL,String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo);


	public int queryExecdrugToatl(List<String> tnL,String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo);
	/**
	 * 返回药嘱类型 1:药品执行 2:非药品执行
	 * @param billNo
	 * @return 
	 */
	public Integer queryDrugBillDetail(String billNo);
}
