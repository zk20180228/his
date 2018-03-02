package cn.honry.statistics.bi.outpatient.outpatientFeeType.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.util.dateVo.DateVo;

/***
 * 门诊收费类型分析service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年7月26日 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface OutpatientFeeTypeService extends BaseService<OutpatientFeeTypeVo>{
	
	/***
	 * 得到门诊收费类型集合
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年7月26日 
	 * @version 1.0
	 */
	String queryOutpatientFeeType(DateVo datevo, String[] dimStringArray,int dateType,String dimensionValue); 
	
	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月29日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String , String> depMap();
	
	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年7月29日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> addressMap();
	
	/**
	 * @Description:年龄code与年龄name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> ageMap();
	
	/**
	 * @Description:学历code与学历name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> degreeMap();
	
	/**
	 * @Description:支付方式code与支付方式name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	Map<String, String> paywayMap();
}
