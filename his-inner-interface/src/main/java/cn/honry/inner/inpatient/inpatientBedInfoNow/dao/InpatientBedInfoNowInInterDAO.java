package cn.honry.inner.inpatient.inpatientBedInfoNow.dao;

import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;


/**
 * ClassName: InpatientBedInfoDAO 
 * @Description: 住院床位使用记录表接口
 * @author lt
 * @date 2015-6-26
 */
public interface InpatientBedInfoNowInInterDAO extends EntityDao<InpatientBedinfoNow>{

	/**
	 * @Description:通过主键ID获取InpatientBedinfo
	 * @Author：  TCJ
	 * @CreateDate： 2016-1-6
	 * @return InpatientBedinfo  
	 * @version 1.0
	**/
	InpatientBedinfoNow queryBedInfoByMainID(String id);
	
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	**/
	InpatientInfoNow queryByMedical(String medicalNo);
	
}
