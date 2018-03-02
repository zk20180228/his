package cn.honry.inpatient.costDerate.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.inpatient.costDerate.vo.DerateVo;
import cn.honry.inpatient.costDerate.vo.ThreeSearchVo;
@SuppressWarnings({"all"})
public interface CostDerateDao extends EntityDao<InpatientDerate>{
	/**  
	 * @Description：  根据病历号查询最新的接诊记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	DerateVo queryInpatientInfoObj(String medicalrecordId,String deptCode);
	/**  
	 * @Description：  根据住院流水号查询ThreeSarchVo的信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<ThreeSearchVo> queryThreeSearch(String inpatientNo);
	/**  
	 * @Description：  根据住院流水号查询费用减免表中的记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientDerate> queryDerate(String inpatientNo);
	/**  
	 * @Description：  查询最小费用名称Map
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<MinfeeStatCode> quertFreeCodeMap();
	/**  
	 * @Description： 根据最小费用名称分组查询减免金额的和
	 * @Author：TCJ
	 * @CreateDate：2016-2-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientDerate> queryDerateMoneySum(String inpatientNo);
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
	 *
	 */
	List<InpatientInfoNow> patientNarTree(String deptCode);

	/**  
	 * @Description： 根据病历号或住院号查询信息
	 * @Author：donghe
	 * @CreateDate：2016-3-21
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientInfoNow> queryInpatientInfoList(String inpatientNo);
	/**
	 * 医生下拉框渲染
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	List<SysEmployee> empComboboxDerate();
	/**
	 * userMap查询
	 * @Author：tuchuanjiang	
	 * @version 1.0
	 */
	List<User> queryUserDerate();
	
	/**
	 * 渲染操作员
	 * @Author：zhuxiaolu
	 * @version 1.0
	 */
	List<User> queryUserDerates();
	
	
	/**
	 * 减免金额
	 * @Author：wsj
	 * @version 1.0
	 */
	void updateinpatientFeeInfoNow(InpatientDerate inpatientDerate);
	
}
