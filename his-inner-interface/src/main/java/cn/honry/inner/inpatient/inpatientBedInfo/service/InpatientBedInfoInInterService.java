package cn.honry.inner.inpatient.inpatientBedInfo.service;

import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.service.BaseService;


/**
 * ClassName: InpatientBedInfoService 
 * @author lt
 * @date 2015-6-26
 */
public interface InpatientBedInfoInInterService extends BaseService<InpatientBedinfo> {
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	**/
	InpatientInfo queryByMedical(String medicalNo);
}
