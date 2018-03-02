package cn.honry.inner.baseinfo.hospitalbed.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.dao.EntityDao;

/**
 * 
 * @date 2015-05-20
 * @author sht
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface HospitalbedInInterDAO extends EntityDao<BusinessHospitalbed>{
	
	/**
	 * 根据id获取对象
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param id
	 * @return
	 */
	BusinessHospitalbed get(String id);
	
	/**
	 * 保存与更新
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param entity
	 */
	void saveOrUpdate(BusinessHospitalbed entity);
	
	/**
	 * 病房空床分页查询
	 * @param hql
	 * @param page
	 * @param rows
	 * @CreateDate：2015-8-21 上午11:57:26  
	 * @return
	 */
	List<BusinessHospitalbed> getPage(String hql, String page, String rows);
	
	/**
	 * 病房空床总条数
	 * @param hql
	 * @param page
	 * @param rows
	 * @CreateDate：2015-8-21 上午11:57:26  
	 * @return
	 */
	int getTotal(String hql);
}
