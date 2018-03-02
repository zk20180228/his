package cn.honry.statistics.drug.pharmacy.service;

import java.util.List;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.pharmacy.vo.CopyOfPharmacyVoSecond;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
import cn.honry.utils.FileUtil;
/**
 * 住院部取药统计service层
 * @author  lyy
 * @createDate： 2016年6月22日 上午10:16:52 
 * @modifier lyy
 * @modifyDate：2016年6月22日 上午10:16:52
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({ "all" })
public interface DrugPharmacyService extends BaseService<PharmacyVo>{
	/**
	 * 查询所有住院部取药信息
	 * @author  lyy
	 * @createDate： 2016年6月22日 上午10:19:12 
	 * @modifier lyy
	 * @modifyDate：2016年6月22日 上午10:19:12
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<PharmacyVo> getPageDrugPharmacy(String startData,String endData,String  drugCostType,String drugstore,String page,String rows) throws Exception;
	/**
	 * 查询所有住院部取药信息
	 * @author  lyy
	 * @createDate： 2016年6月22日 上午10:19:12 
	 * @modifier lyy
	 * @modifyDate：2016年6月22日 上午10:19:12
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getPageDrugPharmacyTotal(String startData,String endData,String  drugCostType,String drugstore);
	/**
	 * 打印报表
	 * @author  xcl
	 * @createDate： 2017年2月28日 下午7:16:09 
	 * @modifier xcl
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<CopyOfPharmacyVoSecond> getPageDrugPharmacyPDF(String startData,String endData,String  drugCostType,String drugstore) throws Exception;
	/**
	 * 导出数据
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午7:16:09 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午7:16:09
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	FileUtil export(List<CopyOfPharmacyVoSecond> list, FileUtil fUtil);
	/**
	 * 查询所有的住院部取药列表
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<PharmacyVo> queryDrugPharmacyNew(String startData, String endData,
			String drugCostType, String drugstore, String page, String rows);
	/**
	 * 查询所有的住院部取药列表
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalDrugPharmacyNew(String startData, String endData,
			String drugCostType, String drugstore);

}
