package cn.honry.inner.baseinfo.icd.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.service.BaseService;

public interface IcdInnerService extends BaseService<BusinessIcd>{

	/**  
	 *  
	 * @Description：icd下拉框的方法
	 * @Author：zhangjin
	 * @CreateDate：2016-7-22  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessIcd> getCombobox(String ids);
	
	/**
	 * 根据code获取Icd10
	 * @param code
	 * @return
	 */
	BusinessIcd10 getIcd10ByCode(String code);
	
	/**
	 * 获取Icd10数据
	 * @return
	 */
	List<BusinessIcd10> getICD10Info();
	
	/**
	 * 及时查询获取Icd10数据
	 * @param q
	 * @return
	 */
	List<BusinessIcd10> getIcd10ByQ(String q);
	
}
