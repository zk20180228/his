package cn.honry.inpatient.exitNoFee.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.exitNoFee.vo.InpatientInfoVo;

public interface ExitNoFeeDAO extends EntityDao<InpatientInfo>{

	/**
	 * @Description:获取患者信息
	 * @Author： yeguanqun
	 * @CreateDate： 2015-12-29
	 * @param entity：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	**/
	List<InpatientInfoVo> getInpatientInfo(InpatientInfoNow entity);

	/**
	 * @Description:无费住院，病床表的床位状态BED_STATE要置成空床状态
	 * @Author：yeguanqun
	 * @CreateDate： 2015-12-30
	 * @param ids：住院信息表的主键id
	 * @version 1.0
	 * @throws Exception 
	**/
	String changeHospitalState(String ids) throws Exception;
	/**  
	 *  
	 * @Description：根据Id查询患者信息
	 * @Author：yeguanqun
	 * @param id：住院信息表的主键id
	 * @CreateDate：2016-2-20   
	 * @version 1.0
	 *
	 */
	List<InpatientInfoVo> queryInpatientInfoById(String id);
	/**  
	 *  
	 * @Description：查询资料变更表中发生序号的最大值
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-20   
	 * @version 1.0
	 *
	 */
	List<InpatientShiftData> queryMaxHappenNo();
	/**  
	 *  
	 * @Description：根据住院流水号查询住院登记表信息
	 * @Author：yeguanqun
	 * @param inpatientNo：患者住院流水号
	 * @CreateDate：2016-2-20   
	 * @version 1.0
	 *
	 */
	List<InpatientInfoNow> queryInpatientInfo(String inpatientNo);
	/**  
	 *  
	 * @Description： 查询药品资料
	 * @Author：yeguanqun
	 * @param id：药品资料表id
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<DrugInfo> queryDrugInfo(String id);
	/**  
	 *  
	 * @Description： 查询医疗组对应医生表
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<BusinessMedicalGroupInfo> queryMedicalGroupInfo(String docCode);  
	/**  
	 *  
	 * @Description： 查询合同单位维护表-合同单位比例
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<BusinessContractunit> queryContractunit(String paykindCode);
	/**  
	 *  
	 * @Description： 查询药品明细资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<InpatientMedicineList> queryInpatientMedicineList(String recipeNo);
	/**  
	 *  
	 * @Description： 查询药品明细资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-5-16   
	 * @version 1.0
	 *
	 */
	List<InpatientMedicineList> queryInpatientMedicineList(String recipeNo,Integer sequenceNo);
	/**  
	 *  
	 * @Description： 查询非药品明细资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<InpatientItemList> queryInpatientItemList(String recipeNo);
	/**  
	 *  
	 * @Description： 查询非药品明细资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-5-16   
	 * @version 1.0
	 *
	 */
	List<InpatientItemList> queryInpatientItemList(String recipeNo,Integer sequenceNo);
	/**  
	 *  
	 * @Description： 查询非药品明细资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<InpatientFeeInfo> queryInpatientFeeInfo(String recipeNo);
	/**  
	 *  
	 * @Description： 查询社保项目信息表（自付比例）
	 * @Author：yeguanqun
	 * @CreateDate：2016-2-24   
	 * @version 1.0
	 *
	 */
	List<InsuranceSiitem> queryInsuranceSiitem(String itemCode);
	/**  
	 *  
	 * @Description：查询住院担保金表中的担保金额
	 * @Author：yeguanqun
	 * @param inpatientNo：患者住院流水号
	 * @CreateDate：2016-2-20   
	 * @version 1.0
	 *
	 */
	List<InpatientSurety> querysuretyCost(String inpatientNo);
	/**
	 * @Description:查询登录密码-校验登录密码
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	List<User> confirmPassword(User user);
	
	/**
	 * 
	 * 
	 * <p>根据病历号查询状态为R（住院登记）的患者住院流水号 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午2:21:33 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午2:21:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @return:
	 *
	 */
	String queryInpatientNoByMid(String medicalrecordId);
	
	/**
	 * 
	 * 
	 * <p>g根据住院流水号查询费用汇总表ID </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午2:23:26 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午2:23:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientNo 住院流水号
	 * @return: have 存在  none 不存在
	 *
	 */
	String isExitNoFee(String inpatientNo);
}
