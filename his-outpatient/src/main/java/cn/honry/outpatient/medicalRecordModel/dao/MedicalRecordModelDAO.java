package cn.honry.outpatient.medicalRecordModel.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessMedicalRecord;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：MedicalRecord 
 * @Description：  电子病历模版表
 * @Author：ldl
 * @CreateDate：2015-7-13   
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface MedicalRecordModelDAO extends EntityDao<BusinessMedicalRecord>{
	/**  
	 *  
	 * @Description：  保存电子病历模版表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-13 下午05：43
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	void saveOrUpdateRecord(BusinessMedicalRecord entity);
	/**  
	 *  
	 * @Description：  电子病历模板列表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param id 
	 * @param recordType 
	 *
	 */
	List<BusinessMedicalRecord> queryMedicalRecordList(String id, String recordType);
	/**  
	 *  
	 * @Description：  电子病历模版树(个别)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<BusinessMedicalRecord> medicalRecordOtherTree(int recordType);

}
