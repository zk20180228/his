package cn.honry.inner.system.parameter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.parameter.service.ParameterInnerService;

/**
 *
 * @Title: ParameterServiceImpl.java
 * @Description：医院系统参数ServiceImpl
 * @Author：aizhonghua
 * @CreateDate：2016年4月13日 上午9:05:18 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version： 1.0：
 *
 */
@Service("parameterInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class ParameterInnerServiceImpl implements ParameterInnerService{
	
	/** 参数数据库操作类 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	/*@Autowired
	@Qualifier(value = "hospitalDAO")
	private HospitalDAO hospitalDAO;*/
	@Override
	public void removeUnused(String id) {
		
	}

	public HospitalParameter get(String id) {
		return parameterInnerDAO.get(id);
	}
	
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
	@Override
	public List<HospitalParameter> getPage(String page, String rows,
			HospitalParameter parameter) {
		return parameterInnerDAO.getPage(parameter, page, rows);
	}
	
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
	@Override
	public int getTotal(HospitalParameter parameter) {
		return parameterInnerDAO.getTotal(parameter);
	}
	
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
	@Override
	public void del(String ids) {
		String idStr[] = ids.split(",");
		for (String id : idStr) {
			parameterInnerDAO.removeById(id);
		}
//		OperationUtils.getInstance().conserve(ids,"参数管理","UPDATE","T_HOSPITAL_PARAMETER",OperationUtils.LOGACTIONDELETE);
	}
	
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
	@Override
	public String getparameter(String code) {
		String parameterValue = parameterInnerDAO.getParameterByCode(code);
		if("".equals(parameterValue)){
			return "";
		}
		return parameterValue;
	}

	@Override
	public void saveOrUpdate(HospitalParameter entity) {
	
		
	}

	@Override
	public String getParameterByCode(String paramCode, String hospitalCode) {
		return parameterInnerDAO.getParameterByCode(paramCode, hospitalCode);
	}

	@Override
	public Map<String, String> getParameterByCodeArr(String[] paramCodeArr,
			String[] hospitalCodeArr) {
		return parameterInnerDAO.getParameterByCodeArr(paramCodeArr,hospitalCodeArr);
	}

	@Override
	public Map<String, String> getParmetersByCodes(String[] paramCodeArr,
			String hospitalCode) {
		return parameterInnerDAO.getParmetersByCodes(paramCodeArr,hospitalCode);
	}

	/**
	 * 获取午别参数
	 * @param code
	 * @return
	 */
	public List<HospitalParameter> getMiddyParameter(String code) {
		 List<HospitalParameter> middyParameter = parameterInnerDAO.getStartAndEnd(code);
		 return middyParameter;
	}

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
	@Override
	public String getParameterByCode(String code) {
		return parameterInnerDAO.getParameterByCode(code);
	}


	

}
