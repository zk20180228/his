package cn.honry.statistics.operation.syntheticalStat.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.operation.syntheticalStat.vo.InvoiceInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.MedicalInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.PatientInfoVo;
import cn.honry.utils.TreeJson;


/**  
 *  
 * @className：SyntheticalStatService
 * @Description： 门诊综合查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface SyntheticalStatService{

	/**  
	 *  
	 * 查询患者信息 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> getRegisterInfo(String page, String rows,String startTime, String endTime, String type, String para, String vague);

	/**  
	 *  
	 * 查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PatientInfoVo queryPatientInfo(String patientId);


	/**  
	 *  
	 * 查询患者发票信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InvoiceInfoVo> queryInvoiceInfo(String registerNo,String tab);
	
	/**  
	 *  
	 * 查询患者历史医嘱树
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> queryMedicalTree(String registerNo);
	
	/**  
	 *  
	 * 查询患者历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<MedicalInfoVo> queryMedicalInfo(String registerNo,String tab);

}
