package cn.honry.inpatient.bill.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

public interface OfficinaTreeService extends BaseService<SysDepartment>{
	/**
	 *  
	 * @Description：  药房药库信息
	 * @Author：donghe
	 * @CreateDate：2015-12-24 下午03:55:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> QueryTreeDepartmen(int flag);
}
