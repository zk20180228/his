package cn.honry.statistics.finance.coststatistics.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
/**
 * 病人费用汇总查询dao层
 * @author  lyy
 * @createDate： 2016年6月24日 上午11:03:03 
 * @modifier lyy
 * @modifyDate：2016年6月24日 上午11:03:03
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public interface CostStatisticsDAO extends EntityDao<InpatientFeeInfo> {
	/**
	 * 病人费用汇总查询（分页list集合）
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午11:04:16 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午11:04:16
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @param tnL 
	 */
	List<InpatientFeeInfo> getPageCostStatistics(List<String> tnL, String firstData, String endData, String inpatientNo,String page, String rows);
	/**
	 * 病人费用汇总查询(总条数)
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午11:25:37 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午11:25:37
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalCostStatistics(List<String> tnL, String firstData, String endData, String inpatientNo);
	/**
	 * 导出查询方法
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午3:00:26 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午3:00:26
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientFeeInfo> queryCostStatistice(List<String> tnL, String firstData, String endData, String inpatientNo);
	
	/**
	 * 根据病历号获取患者住院信息
	 * @author  zhuxiaolu
	 * @createDate： 2016年6月24日 下午3:40:22 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId);
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	StatVo findMaxMin();
	String getMedicalrecordId(String idCard);

}
