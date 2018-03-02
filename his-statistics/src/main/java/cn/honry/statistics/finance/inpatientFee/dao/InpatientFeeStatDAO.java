package cn.honry.statistics.finance.inpatientFee.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.finance.inpatientFee.vo.FeeInfosVo;
import cn.honry.statistics.finance.inpatientFee.vo.InpatientInfosVo;

@SuppressWarnings({"all"})
public interface InpatientFeeStatDAO extends EntityDao<InpatientInfo>{
	/**
	 * @Description:获取患者信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param entity：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @param a 
	 * @throws Exception 
	**/
	List<InpatientInfosVo> queryInpatientInfo(InpatientInfo entity, String a) throws Exception;
	/**
	* @param a 
	 * @Title: 获取药品明细信息
	* @Description: 
	* @param @param inpatientNo
	* @param @return   
	* @return List<InpatientMedicineList>    
	 * @throws Exception 
	* @date 2016年6月6日下午6:46:10
	 */
	List<InpatientMedicineList> queryMedicineList(String inpatientNo, String a) throws Exception;
	/**
	* @Title: 获取非药品明细信息
	* @Description: 
	* @param inpatientNo
	 * @param a 
	* @param @return   
	* @return List<InpatientItemList>    
	 * @throws Exception 
	* @date 2016年6月6日下午6:47:49
	 */
	List<InpatientItemList> queryItemList(String inpatientNo, String a) throws Exception;
	/**
	* @Title: queryBalanceList
	* @Description: 获取结算信息
	* @param inpatientNo 住院流水号
	* @param a 
	* @param @return   
	* @return List<InpatientBalanceList>    
	 * @throws Exception 
	* @date 2016年6月7日下午6:40:45
	 */
	List<InpatientBalanceList> queryBalanceList(String inpatientNo, String a) throws Exception;
	/**
	* @Title: queryFeeInfo
	* @Description: 获取费用汇总信息
	* @param inpatientNo 住院流水号
	* @param a 
	* @param @return   
	* @return List<InpatientFeeInfo>    
	 * @throws Exception 
	* @date 2016年6月7日下午6:40:45
	 */
	List<InpatientFeeInfo> queryFeeInfo1(String inpatientNo, String a) throws Exception;
	/**
	* @Title: queryInPrepay
	* @Description: 获取预交金信息
	* @param inpatientNo 住院流水号
	 * @param a 
	* @param @return   
	* @return List<InpatientInPrepay>    
	 * @throws Exception 
	* @date 2016年6月7日下午6:40:45
	 */
	List<InpatientInPrepay> queryInPrepay(String inpatientNo, String a) throws Exception;
	/**
	* @Title: queryFeeInfo
	* @Description: 
	* @param inpatientNo  
	* @return List<InpatientFeeInfo>    
	 * @throws Exception 
	* @date 2016年6月7日下午8:05:01
	 */
	List<FeeInfosVo> queryFeeInfo(String inpatientNo, String a) throws Exception;
	/**
	* @Title: queryInPrepay
	* @Description: 获取结算信息
	* @param inpatientNo 住院流水号
	* @param @return   
	* @return List<InpatientInPrepay>    
	 * @throws Exception 
	* @date 2016年6月7日下午6:40:45
	 */
	List<InpatientBalanceHead> queryBalanceInfo(String inpatientNo, String a) throws Exception;
	/**  
	 *  
	 * @Description：  获得渲染数据-员工Map
	 * @Author：yeguanqun
	 * @CreateDate：2016-5-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	Map<String, String> queryEmployeeMap() throws Exception;
	/**  
	 *  
	 * @Description：  获得渲染数据-费用名称Map
	 * @Author：yeguanqun
	 * @CreateDate：2016-5-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	Map<String, String> queryFeeNameMap() throws Exception;
}
