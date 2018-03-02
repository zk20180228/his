package cn.honry.inner.system.parameter.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.service.BaseService;

/**
 * 参数表
 * @author  lt
 * @date 2015-6-2 14：:4
 * @version 1.0
 */
public interface ParameterInnerService extends BaseService<HospitalParameter>{

	/**
	 * @Description:获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @param  page
	 * @param  rows
	 * @param  parameter
	 * @param n   
	 * @return List<HospitalParameter>  
	 * @version 1.0
	**/
	List<HospitalParameter> getPage(String page, String rows,HospitalParameter parameter);

	/**
	 * @Description:获取列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @param parameter
	 * @param    
	 * @return int  
	 * @version 1.0
	**/
	int getTotal(HospitalParameter parameter);

	/**
	 * @Description:删除方法
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @param @param id   
	 * @return void  
	 * @version 1.0
	**/
	void del(String id);

	/**
	 * @Description:根据参数代码取实体
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @param @param code
	 * @param @return   
	 * @return String  
	 * @version 1.0
	**/
	String getparameter(String code);
	/**
	 * @Description:根据参数代码,医院代码获取参数值
	 * @Author：  hedong
	 * @CreateDate： 2017-03-14
	 * @param paramCode 参数code
	 * @param hospitalCode 医院code
	 * @return 参数值
	 */
	String getParameterByCode(String paramCode,String hospitalCode);
	/**
	 * @Description:根据参数代码数组,医院代码数组获取多个参数值的Map。
	 * 需注意的是paramCodeArr（参数code数组）和hospitalCodeArr（医院code数组）长度需保持一致,否则会返回一个空的map。
	 * @Author：  hedong
	 * @CreateDate： 2017-03-14
	 * @param paramCodeArr 参数code数组
	 * @param hospitalCodeArr 医院code数组
	 * @return Map<String, String> 该map 由 paramCode_hospitalCode组成的key,paramValue组成的值组成
	 */
	Map<String,String> getParameterByCodeArr(String[] paramCodeArr,String[] hospitalCodeArr);
    /**
     * @Description:根据参数代码数组,一个医院代码获取多个参数值的Map。
     * 如任意一个参数为空则返回空map
     * @Author：  hedong
	 * @CreateDate： 2017-03-14
     * @param paramCodeArr  参数code数组
     * @param hospitalCode  医院code
     * @return Map<String, String> 该map 由 paramCode_hospitalCode组成的key,paramValue组成的值组成
     */
	Map<String, String> getParmetersByCodes(String[] paramCodeArr,
			String hospitalCode);
	/**
	 * 获取午别参数
	 * @param code
	 * @return
	 */
	List<HospitalParameter> getMiddyParameter(String code);

	/**  
	 * 
	 * 根据code获取参数值
	 * @Author: zxl
	 * @CreateDate: 2017年7月6日 下午3:37:40 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月6日 下午3:37:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	String getParameterByCode(String code);
	
}
