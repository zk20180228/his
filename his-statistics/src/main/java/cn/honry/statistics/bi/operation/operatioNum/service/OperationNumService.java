package cn.honry.statistics.bi.operation.operatioNum.service;

import java.util.Map;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.operation.operatioNum.vo.OperationNumVo;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OperationNumService extends BaseService<OperationNumVo>{
	
	/***
	 * 根据查询条件查询门诊收费的金额和比例
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年7月26日 
	 * @version 1.0
	 */
	Map<String, String> operationTypeMap();
	
	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> depMap(); 
	
	/**
	 * @param yearStart
	 * @param yearEnd
	 * @param quarterStart
	 * @param quarterEnd
	 * @param monthStart
	 * @param monthEnd
	 * @param dayStart
	 * @param dayEnd
	 * @param dimensionString
	 * @param dimensionOne
	 * @param dimensionTwo
	 * @param dimensionThree
	 * @return
	 */
	String queryOperationNum(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);
}
