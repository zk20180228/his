package cn.honry.statistics.finance.inpatientFeeBalSum.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.finance.inpatientFeeBalSum.vo.FeeBalSumVo;
import cn.honry.utils.FileUtil;

public interface InpatientFeeBalSumService extends BaseService<InpatientInfo>  {
	/**
	* @Title: queryInpatientFeeBalSum
	* @Description: 病人医药费结算汇总
	* @param @return   
	* @return List<FeeBalSumVo>    
	* @date 2016年6月24日上午9:00:01
	 */
	List<FeeBalSumVo> getPage(String page, String rows,String typeSerc,String etime,String stime,String deptCode) throws Exception;

	/**  
	 *  
	 * @Description：病人医药费结算汇总总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-6-24   
	 * @version 1.0
	 *
	 */
	int getTotal(String stime, String etime,String typeSerc,String deptCode) throws Exception;
	
	/**
	* @Title: getFeeStatName
	* @Description: 查询统计费用名称
	* @param @param feeStatCode
	* @param @return   
	* @return List<MinfeeStatCode>    
	* @date 2016年6月25日下午6:06:33
	 */
	List<MinfeeStatCode> getFeeStatName(String feeStatCode) throws Exception;
	/**
	* @Title: queryInpatientFeeBalSum
	* @Description: 病人医药费结算汇总
	* @param @return   
	* @return List<FeeBalSumVo>    
	* @date 2016年6月24日上午9:00:01
	 */
	List<FeeBalSumVo> getFeeBalSum(String startTime, String endTime, String typeSerc) throws Exception;
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: yeguanqun
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	**/
	FileUtil export(List<FeeBalSumVo> list, FileUtil fUtil) throws Exception;
}
