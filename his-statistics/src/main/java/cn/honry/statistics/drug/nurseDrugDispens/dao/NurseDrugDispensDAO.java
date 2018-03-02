package cn.honry.statistics.drug.nurseDrugDispens.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensDetailVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;

public interface NurseDrugDispensDAO extends EntityDao<InpatientInfo> {
	/**
	 * @Description:获取摆药汇总信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @param tnL 
	 * @param rows 
	 * @param page 
	 * @throws Exception 
	**/
	List<DrugDispensSumVo> queryDrugDispensSum(List<String> tnL, InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc,String type, String page, String rows) throws Exception;
	/**
	 * @Description:获取摆药明细信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @param tnL 
	 * @param rows 
	 * @param page 
	 * @param flg 
	 * @throws Exception 
	**/
	List<DrugDispensDetailVo> queryDrugDispensDetail(List<String> tnL, InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc, String type, String page, String rows) throws Exception;
	
	/**
	 * @Description:获取当前表中最大及最小时间
	 * @Author：zhuxiaolu
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	StatVo findMaxMin() throws Exception;

	
	/**
	 * @Description:获取患者树(患者费用查询)
	 * @Author：zhuxiaolu
	 * @CreateDate： 2016-1—5
	 * @param @param 
	 * @return 
	 * @version 1.0
	 * @param endTime2 
	 * @throws Exception 
	**/
	List<InfoInInterVo> treeNurseCharge(String deptId, String type,
			InpatientInfoNow inpatientInfo, String a, String startTime,
			String endTime) throws Exception;
	
	
	/**
	 * @Description:查询患者是否存在
	 * @Author：zhuxiaolu
	 * @CreateDate： 2016-1—5
	 * @param @param 
	 * @return 
	 * @version 1.0
	 * @param endTime2 
	 * @throws Exception 
	**/
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
	List<DrugBillclass> queryBillNameList();
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
	int queryDrugDispensSumTotal(List<String> tnL,
			InpatientInfoNow inpatientInfo, String startTime, String endTime,
			String drugName, String inpatientNoSerc, String type);
	int queryDrugDispensDetailTotal(List<String> tnL,
			InpatientInfoNow inpatientInfo, String startTime, String endTime,
			String drugName, String inpatientNoSerc, String type);
}
