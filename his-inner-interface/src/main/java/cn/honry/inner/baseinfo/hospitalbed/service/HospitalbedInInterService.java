package cn.honry.inner.baseinfo.hospitalbed.service;


import cn.honry.base.bean.model.BusinessHospitalbed;

/**
 * 
 * @date 2015-05-20
 * @author sht
 * @version 1.0
 */

public interface HospitalbedInInterService {
	
	/**
	 * 根据ID查询病床信息
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 */
	BusinessHospitalbed getBedByid(String id);
	
	/**
	 * 保存与更新
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param entity
	 */
	void saveOrUpdate(BusinessHospitalbed entity);
	
	/**  
	 *  
	 * @Description：  逻辑删除
	 * @Author：zhenglin
	 * @version 1.0
	 *
	 */
	String deleteBedInfoById(String id);
}
