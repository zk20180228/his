package cn.honry.statistics.deptstat.operationProportion.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface OperationProportionService extends BaseService<DrugChecklogs>{

	/***
	 * 盘点日志查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InventoryLogVo> queryInventoryLog(String Stime,String Etime,String dept,String page,String rows,String drug) throws Exception;
	
	/***
	 * 盘点日志查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryInventoryLogTotle(String Stime,String Etime,String dept,String drug) throws Exception;
	
	/**  
	 *  
	 * @Description：  科室下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<SysDepartment> getComboboxdept() throws Exception;

	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<DrugInfo> getComboboxdrug() throws Exception;

	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	List<InventoryLogVo> queryInvLogExp(String stime, String etime, String dept,
			String drug) throws Exception;

	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	**/
	FileUtil export(List<InventoryLogVo> list, FileUtil fUtil);
	/**
	 * @Description:根据时间和科室查询手术占比
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	List<OperationProportionVo> queryOperationProportion(String stime,
			String etime, String deptCode, String page, String rows) throws Exception;
	/**
	 * @Description:根据时间和科室查询符合条件总条数
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	int queryOperationProportionTotal(String dept, String stime, String etime) throws Exception;
	/**
	 * @Description:查询所有科室
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	Map<String, String> queryDeptMap();
	/**
	 * @Description:根据时间和科室从mongondb查询符合条件数据
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	List<OperationProportionVo> queryOperationProportionFromDB(String time,
			List<String> codeList, String page, String rows) throws Exception;
	/**
	 * @Description:将数据插入或更新到mogondb
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	void saveOrUpdateToDB(String stime, String etime) throws Exception;
	/**
	 * @Description:导出
	 * @Author： qh @CreateDate： 2017-6-14
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	FileUtil exportList(List<OperationProportionVo> list, FileUtil fUtil);
	/**
	 * @Description:数据初始化
	 * @Author： qh @CreateDate： 2017-7-27
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	void init_SSZBTJ(String beginDate, String endDate, Integer type);

}
