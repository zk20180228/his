package cn.honry.statistics.drug.pharmacy.dao;

import java.util.ArrayList;
import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.pharmacy.vo.CopyOfPharmacyVoSecond;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
/**
 * 住院部取药统计dao层
 * @author  lyy
 * @createDate： 2016年6月22日 上午10:23:41 
 * @modifier lyy
 * @modifyDate：2016年6月22日 上午10:23:41
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface DrugPharmacyDAO extends EntityDao<PharmacyVo> {
	/**
	 * 查询所有住院部取药信息
	 * @author  lyy
	 * @createDate： 2016年6月22日 上午10:28:11 
	 * @modifier lyy
	 * @modifyDate：2016年6月22日 上午10:28:11
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<PharmacyVo> getPageDrugPharmacy(List<String> tnL,String startData,String endData,String  drugCostType,String drugstore,String page,String rows) throws Exception;
	/**
	 * 查询所有住院部取药信息总量
	 * @author  lyy
	 * @createDate： 2016年6月22日 上午10:28:11 
	 * @modifier lyy
	 * @modifyDate：2016年6月22日 上午10:28:11
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getPageDrugPharmacyTotal(List<String> tnL,String startData,String endData,String  drugCostType,String drugstore);
	/**
	 * 报表查询所有住院部取药信息
	 * @param tnL
	 * @param startData
	 * @param endData
	 * @param drugCostType
	 * @param drugstore
	 * @return
	 * @throws Exception 
	 */
	List<CopyOfPharmacyVoSecond> getPageDrugPharmacyPDF(List<String> tnL,String startData,String endData,String  drugCostType,String drugstore) throws Exception;
	/**
	 * 查询出库表中时间的
	 * @return
	 * @throws Exception 
	 */
	StatVo findMaxMin() throws Exception;
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
	List<PharmacyVo> queryDrugPharmacyNew(List<String> tnL, String startData,String endData, String drugCostType, String drugstore, String page,String rows);
	
	/**
	 * 查询所有的住院部取药列表  总条数
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param tnL 
	 */
	int getTotalDrugPharmacyNew(List<String> tnL, String startData, String endData,String drugCostType, String drugstore);

}
