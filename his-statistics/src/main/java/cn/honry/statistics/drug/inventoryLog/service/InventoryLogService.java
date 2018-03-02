package cn.honry.statistics.drug.inventoryLog.service;

import java.util.List;

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface InventoryLogService extends BaseService<DrugChecklogs>{

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
}
