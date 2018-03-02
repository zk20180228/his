package cn.honry.inner.outpatient.inpatNumber.service;


import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.base.service.BaseService;

public interface InpatNumInnerService extends BaseService<InpatientNumber>{

	
	/**  
	 * @Description：  根据住院主表ID
	 * @Author：zhangjin
	 * @CreateDate：2016-11-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	void saveInpatNum(String id);
}
