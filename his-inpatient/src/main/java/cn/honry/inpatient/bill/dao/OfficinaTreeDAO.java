package cn.honry.inpatient.bill.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;

public interface OfficinaTreeDAO extends EntityDao<SysDepartment>{
	 /**
     * 药房药库信息
     * @author  dh
     * @createDate： 2015年12月24日 下午3:20:45 
     * @modifier dh
     * @modifyDate： 2015年12月24日 下午3:20:45 
     * @modifyRmk：  
     * @version 1.0
     */
	List<SysDepartment> findTreeType(int flag);
}
