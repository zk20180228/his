package cn.honry.inpatient.costDerate.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.costDerate.vo.DerateVo;
import cn.honry.inpatient.costDerate.vo.ThreeSearchVo;
import cn.honry.utils.TreeJson;

public interface CostDerateService extends BaseService<InpatientDerate>{
	/**  
	 * @Description：  根据病历号查询最新的接诊记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	DerateVo queryInpatientInfoObj(String medicalrecordId,String deptCode) throws Exception;
	/**  
	 * @Description：  根据住院流水号查询ThreeSarchVo的信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<ThreeSearchVo> queryThreeSearch(String inpatientNo) throws Exception;
	/**  
	 * @Description：  根据住院流水号查询费用减免表中的记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientDerate> queryDerate(String inpatientNo) throws Exception;
	/**  
	 * @Description：  保存费用减免信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-14
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveDerateDategrid(String derateJson,String no) throws Exception;
	/**  
	 * @Description：  查询最小费用名称Map
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<MinfeeStatCode> quertFreeCodeMap() throws Exception;
	/**  
	 * @Description： 根据最小费用名称分组查询减免金额的和
	 * @Author：TCJ
	 * @CreateDate：2016-2-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientDerate> queryDerateMoneySum(String inpatientNo) throws Exception;
	/**  
	 * @Description： 通过患者Id从住院主表中查询病历号
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	InpatientInfoNow querNurseCharge(String inpatientId);

	/**  
	 *  
	 * @Description：本区患者树
	 * @Author：tuchuanjiang
	 * @CreateDate：2016-3-21
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<TreeJson> patientNarTree(String deptCode, String id) throws Exception;

	/**  
	 * @Description： 根据病历号或住院号查询信息
	 * @Author：donghe
	 * @CreateDate：2016-3-21
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> queryInpatientInfoList(String inpatientNo) throws Exception;
	/**
	 * 医生下拉框渲染
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> empComboboxDerate() throws Exception;
	/**
	 * userMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 * @throws Exception 
	 */
	List<User> queryUserDerate() throws Exception;
	
	/**
	 * 渲染操作员
	 * @Author：zhuxiaolu
	 * @version 1.0
	 * @throws Exception 
	 */
	List<User> queryUserDerates() throws Exception;


}
