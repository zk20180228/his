package cn.honry.outpatient.medicalRecordModel.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessMedicalRecord;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;
/**  
 *  
 * @className：MedicalRecord
 * @Description：  电子病历模版表
 * @Author：ldl
 * @CreateDate：2015-7-13   
 * @version 1.0
 *
 */
public interface MedicalRecordModelService extends BaseService<BusinessMedicalRecord>{
	/**  
	 *  
	 * @Description：  保存电子病历模版表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-13 下午05：43
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	void saveOrUpdateRecord(BusinessMedicalRecord medicalRecord);
	/**  
	 *  
	 * @Description：  电子病历模版树
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	List<TreeJson> medicalRecordTree();
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
	 * @param type 
	 *
	 */
	List<TreeJson> medicalRecordOtherTree(String type);

}
