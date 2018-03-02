package cn.honry.outpatient.medicalrecord.service;

import java.util.List;

import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.service.BaseService;


public interface MedicalrecordService extends BaseService<OutpatientMedicalrecord>{
	/**  
	 *  
	 * @Description：添加&修改
	 *@Author：wujiao
	 * @CreateDate：2015-7-9 上午15:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-9 上午15:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdatemedicalrecord(OutpatientMedicalrecord medicalrecord);
	
	/**  
	 * 根据门诊号和病历号查询患者是否有病历信息
	 * @Description：  获得护士站
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean findMedicalByNoAndCode(String patientNo, String clinicCode);
	
	/**  
	 *  
	 * @Description：获取执行科室ID
	 * @Author：huangbiao
	 * @CreateDate：2016-3-119上午11:56:59  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<String> getDeptId(String clinicCode);
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：tangfeishuai
	 * @CreateDate：2016-03-23下午15:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<String> findExecDpcdByCodeAndPatino(String clinicCode, String patientNo);
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：wanxing
	 * @CreateDate：2016-03-09 下午18:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<String> findRecipeNo(String clinicCode,String patientNo);

}
