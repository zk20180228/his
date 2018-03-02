package cn.honry.statistics.drug.drugDoseCensus.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;

/**
 * 住院发药量统计Dao
 * @author  lyy
 * @createDate： 2016年6月20日 下午3:46:53 
 * @modifier lyy
 * @modifyDate：2016年6月20日 下午3:46:53
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface DrugDoseCensusDAO extends EntityDao<DrugOutstore> {
	/**
	 * 查询所有发药工作量统计list集合
	 * @author  lyy
	 * @createDate： 2016年6月20日 下午3:54:36 
	 * @modifier lyy
	 * @modifyDate：2016年6月20日 下午3:54:36
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param tnL 
	 * @throws Exception 
	 */
	List<DrugOutstore> queryDrugDose(List<String> tnL, String startData,String endData,String drugstore,String page, String rows) throws Exception;
	/**
	 * 获取药房科室
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:03:31 
	 * @modifier lyy
	 * @modifyDate：2016年7月6日 上午10:03:31
	 * @param：    name 下拉框即使查询
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysDepartment> queryStoreDept(String name) throws Exception;
	/**
	 * 查询所有发药工作量统计总条数
	 * @author  lyy
	 * @createDate： 2016年6月21日 上午10:24:11 
	 * @modifier lyy
	 * @modifyDate：2016年6月21日 上午10:24:11
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTatalDrugDose(List<String> tnL, String startData,String endData,String drugstore) throws Exception;
	/**
	 * 查询发药工作量统计总条数
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午6:59:02 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午6:59:02
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DrugOutstore> expQueryDrugDoseCensus(List<String> tnL, String startData,String endData,String drugstore);
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author:zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	StatVo findMaxMin() throws Exception;
	Map<String, String> getStoreDEptMap() throws Exception;

	
	/**  
	 * 住院统计工作量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnL 
	 * @param deptCode 
	 */
	 List<DrugOutstore> queryDrugDoseCen(List<String> tnL, List<String> tnL1,String startData,String endData,String deptCode,String menuAlias,String page,String rows);
	/**  
	 * 住院统计工作量  mongdb查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	 List<DrugOutstore> queryDrugDoseCenForDB(String startData,String endData, String deptCode, String menuAlias, String page,	String rows);
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
	int getTotalDrugDoseCen(List<String> tnL, List<String> tnL1,String startData, String endData, String deptCode, String menuAlias);
	
}
