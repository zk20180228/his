package cn.honry.statistics.operation.syntheticalStat.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.syntheticalStat.vo.InvoiceInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.MedicalInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.PatientInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.RegisterInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.TreeInfoVo;

/**  
 *  
 * @className：SyntheticalStatDAO
 * @Description： 门诊综合查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface SyntheticalStatDao extends EntityDao<RegisterInfoVo>{

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
	Map<String,Object> getRegisterInfo(String page, String rows,String startTime, String endTime, String type, String para, String vague,List<String> tnL);
	
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
	List<TreeInfoVo> queryMedicalTree(String recordNo);

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
	
	/**  
	 *  
	 * 获取最小最大时间
	 * @Author：zhangjin
	 * @CreateDate：2016-12-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	StatVo findMaxMin();
	
	/**  
	 *  
	 * 获取最小最大时间(收费明细表)
	 * @Author：zhangjin
	 * @CreateDate：2016-12-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	StatVo findMaxMinfee();

}
