package cn.honry.inner.baseinfo.icd.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface IcdInInterDAO extends EntityDao<BusinessIcd>{

	List<BusinessIcd> getCombobox(String s);
	
	/**
	 * 根据code获取icd10
	 * @param code
	 * @return
	 */
	BusinessIcd10 getIcd10ByCode(String code);
	
	/**
	 * 获取Icd10数据
	 * @return
	 */
	List<BusinessIcd10> getICD10Info(String q);

}
