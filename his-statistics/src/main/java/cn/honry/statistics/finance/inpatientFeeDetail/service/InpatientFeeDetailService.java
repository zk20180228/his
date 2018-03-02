package cn.honry.statistics.finance.inpatientFeeDetail.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.CostDetailsVo;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.FeeDetailVo;
import cn.honry.utils.TreeJson;

public interface InpatientFeeDetailService extends BaseService<InpatientInfo> {
	/**
	 * @Description:获取费用汇总信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param inpatientInfo：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<FeeDetailVo> queryFeeInfo(InpatientInfo inpatientInfo) throws Exception;
	/**
	 * @Description:获取费用明细信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param inpatientInfo：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<CostDetailsVo> queryCostDetails(InpatientInfo inpatientInfo) throws Exception;
	/**  
	 *  患者树
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @throws Exception 
	 *
	 */
	List<TreeJson> queryPatientTree(String deptId,String flag) throws Exception;
	/**
	 * @Description:获取费用信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param inpatientInfo：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	InpatientInfoNow queryFeeInpatientInfo(String inpatientNo) throws Exception;
}
