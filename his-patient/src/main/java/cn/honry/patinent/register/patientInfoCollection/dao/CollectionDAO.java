package cn.honry.patinent.register.patientInfoCollection.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.dao.EntityDao;
import cn.honry.patinent.register.patientInfoCollection.vo.Collection;

@SuppressWarnings({"all"})
public interface CollectionDAO extends EntityDao<Patient>{

	/**  
	 *  
	 * @Description： 根据病历号查询
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Collection getById(String idNo);
	/**  
	 *  
	 * @Description： 根据id查询
	 * @Author：wujiao
	 * @CreateDate：2015-11-25 下午16:56:31
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-25 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Patient findPatientOldbyid(String id);

	
	/***
	 * 根据患者病历号模糊查询患者信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月18日
	 * @version 1.0
	 * @param: idNo 患者表id
	 */
	List<Patient> isSearchFrom(String idNo);
	
	/**  
	 *  
	 * @Description： 根据就诊卡号查询
	 * @Author：zpty
	 * @CreateDate：2016-6-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Collection getByIdcard(String idcard);
	/**  
	 *  
	 * @Description： 根据身份证号查询
	 * @Author：zpty
	 * @CreateDate：2016-6-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Collection getByCerNo(String cerNo);
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author：zhuxiaolu
	 * @CreateDate： 2015-12-24
	 * @param @return   
	 * @return boolean  
	 * @version 1.0
	**/
	String checkIdcardName(String name, String certificate, String certificatesno);
}
