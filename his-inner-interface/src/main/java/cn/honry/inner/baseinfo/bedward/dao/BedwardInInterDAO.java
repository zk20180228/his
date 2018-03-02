package cn.honry.inner.baseinfo.bedward.dao;


import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.dao.EntityDao;

public interface BedwardInInterDAO extends EntityDao<BusinessBedward>{
	

	/**
	 * 根据ID查询病房信息
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param id
	 * @return
	 */
	BusinessBedward getBedwardById(String id);
	
}
