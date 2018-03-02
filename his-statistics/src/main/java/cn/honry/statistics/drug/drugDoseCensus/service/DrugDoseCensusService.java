package cn.honry.statistics.drug.drugDoseCensus.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.utils.FileUtil;

/**
 * 住院发药工作量统计service
 * @author  lyy
 * @createDate： 2016年6月20日 下午3:46:03 
 * @modifier lyy
 * @modifyDate：2016年6月20日 下午3:46:03
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface DrugDoseCensusService extends BaseService<DrugOutstore>{
	/**
	 * 查询所有发药工作量统计list集合
	 * @author  lyy
	 * @createDate： 2016年6月20日 下午3:51:12 
	 * @modifier lyy
	 * @modifyDate：2016年6月20日 下午3:51:12
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param statVo 
	 * @throws Exception 
	 */
	List<DrugOutstore> getPageDrugDose(String startData,String endData,String drugstore,String page, String rows, StatVo statVo) throws Exception;
	/**
	 * 获取药房科室
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:00:46 
	 * @modifier lyy
	 * @modifyDate：2016年7月6日 上午10:00:46
	 * @param：    name 下拉框即使查询
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> queryStoreDept(String name) throws Exception;
	/**
	 * 科室渲染查询
	 * @throws Exception 
	 */
	Map<String, String> getStoreDEptMap() throws Exception;
	
	/**
	 * 查询发药工作量统计总条数
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:23:18 
	 * @modifier lyy
	 * @modifyDate：2016年6月21日 上午10:23:18
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param statVo 
	 * @throws Exception 
	 */
	int getTatalDrugDose(String startData, String endData, String drugstore, StatVo statVo) throws Exception;
	/**
	 * 导出发药工作量统计
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午6:46:15 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午6:46:15
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugOutstore> expQueryDrugDoseCensus(String startData, String endData, String drugstore,StatVo statVo);
	/**
	 * 导出
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午6:46:52 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午6:46:52
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	FileUtil export(List<DrugOutstore> list, FileUtil fUtil) throws Exception;
	
	/**
	 * 导出
	 * @author  获取最大最小时间
	 * @createDate： 2016年6月24日 下午6:46:52 
	 * @modifier lyy
	 * @version 1.0
	 * @throws Exception 
	 */
	StatVo findMaxMin() throws Exception;

	
	/**  
	 * 住院统计工作量 
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	List<DrugOutstore> queryDrugDoseCen(String startData,String endData, String deptCode, String page, String rows,String menuAlias);
	/**  
	 * 住院统计工作量  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	int getTotalDrugDoseCen(String startData, String endData,String deptCode, String menuAlias);
	
}
