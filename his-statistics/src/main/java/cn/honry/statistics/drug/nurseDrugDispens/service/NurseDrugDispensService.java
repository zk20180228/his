package cn.honry.statistics.drug.nurseDrugDispens.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensDetailVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.utils.TreeJson;

public interface NurseDrugDispensService extends BaseService<InpatientInfo>  {
	/**
	 * @Description:获取摆药汇总信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @param type 
	 * @param rows 
	 * @param page 
	 * @param id 
	 * @throws Exception 
	**/
	List<DrugDispensSumVo> queryDrugDispensSum(InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc, String flg, String type, String page, String rows) throws Exception;
	/**
	 * @Description:获取摆药明细信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @param flg 
	 * @param type 
	 * @param rows 
	 * @param page 
	 * @throws Exception 
	**/
	List<DrugDispensDetailVo> queryDrugDispensDetail(InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc, String flg, String type, String page, String rows) throws Exception;

	/**  
	 *  
	 * @Description： 获取患者树
	 * @param type 
	 * @throws Exception 
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-1-5  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> treeNurseCharge(String deptId, String id,
			String type, InpatientInfoNow inpatientInfo, String a, String startTime,
			String endTime) throws Exception;
	
	/**  
	 *  
	 * @Description： 查询患者是否存在
	 * @param type 
	 * @throws Exception 
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-1-5  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientInfoNow> findByinpatientNo(String inpatientNo) throws Exception;
	/**
	 * 
	 * @Description：查询摆药单
	 * @Author：wangshujuan
	 * @CreateDate：2017-7-4
	 * @Modifier：
	 * @ModifyDate：2017-7-4
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	Map<String, String> queryBillNameMap();
	/**
	 * 
	 * @Description：查询汇总信息总量
	 * @Author：wangshujuan
	 * @CreateDate：2017-7-4
	 * @Modifier：
	 * @ModifyDate：2017-7-4
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	int queryDrugDispensSumTotal(InpatientInfoNow inpatientInfo,
			String startTime, String endTime, String drugName,
			String inpatientNoSerc, String a, String type);
	/**
	 * 
	 * @Description：查询摆药单信息总量
	 * @Author：wangshujuan
	 * @CreateDate：2017-7-4
	 * @Modifier：
	 * @ModifyDate：2017-7-4
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	int queryDrugDispensDetailTotal(InpatientInfoNow inpatientInfo,
			String startTime, String endTime, String drugName,
			String inpatientNoSerc, String a, String type);
}
