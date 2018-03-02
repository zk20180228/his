package cn.honry.statistics.finance.inpatientFeeBalSum.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.inpatientFeeBalSum.vo.FeeBalSumVo;

public interface InpatientFeeBalSumDAO extends EntityDao<InpatientInfo>  {

	/**
	* @Title: queryInpatientFeeBalSum
	* @Description: 病人医药费结算汇总分页
	* @param @return   
	* @return List<FeeBalSumVo>    
	* @date 2016年6月24日上午9:00:01
	 */
	List<FeeBalSumVo> getPage(List<String> tnL,String page, String rows,String etime,String stime,String typeSerc,String deptCode) throws Exception ;

	/**  
	 *  
	 * @Description：病人医药费结算汇总总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-6-24   
	 * @version 1.0
	 * @param tnL 
	 *
	 */
	int getTotal(List<String> tnL, String startTime,String endTime,String typeSerc,String deptCode) throws Exception ;
	
	/**
	* @Title: getFeeStatName
	* @Description: 查询统计费用名称
	* @param @param feeStatCode
	* @param @return   
	* @return List<MinfeeStatCode>    
	* @date 2016年6月25日下午6:06:33
	 */
	List<MinfeeStatCode> getFeeStatName(String feeStatCode) throws Exception ;
	/**
	* @param tnL 
	 * @Title: queryInpatientFeeBalSum
	* @Description: 病人医药费结算汇总
	* @param @return   
	* @return List<FeeBalSumVo>    
	* @date 2016年6月24日上午9:00:01
	 */
	List<FeeBalSumVo> getFeeBalSum(List<String> tnL, String startTime, String endTime, String typeSerc) throws Exception ;

	/**
	* @param tnL 
	 * @Title: queryInpatientFeeBalSum
	* @Description: 查询最大最小费用
	* @param @return   
	* @return List<FeeBalSumVo>    
	* @date 2016年6月24日上午9:00:01
	 */
	StatVo findMaxMin() throws Exception ;

}
