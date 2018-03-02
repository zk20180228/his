package cn.honry.statistics.finance.coststatistics.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.utils.FileUtil;
/**
 * 病人费用汇总查询service层
 * @author  lyy
 * @createDate： 2016年6月24日 上午11:00:52 
 * @modifier lyy
 * @modifyDate：2016年6月24日 上午11:00:52
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface CostStatisticsService extends BaseService<InpatientFeeInfo> {
	/**
	 *  病人费用汇总查询(分页list集合)
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午11:01:25 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午11:01:25
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientFeeInfo> getPageCostStatistics(String firstData, String endData, String inpatientNo,String page,String rows);
	/**
	 * 病人费用汇总查询(总条数)
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午11:22:26 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午11:22:26
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalCostStatistics(String firstData, String endData, String inpatientNo);
	/**
	 * 导出查询方法
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午2:59:30 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午2:59:30
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientFeeInfo> queryCostStatistice(String firstData, String endData, String inpatientNo);
	/**
	 * 导出内容方法 FileUtil
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午3:40:22 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午3:40:22
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	FileUtil export(List<InpatientFeeInfo> list, FileUtil fUtil);
	
	/**
	 * 根据病历号获取患者住院信息
	 * @author  zhuxiaolu
	 * @createDate： 2016年6月24日 下午3:40:22 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId);
	String getMedicalrecordId(String idCard);

}
