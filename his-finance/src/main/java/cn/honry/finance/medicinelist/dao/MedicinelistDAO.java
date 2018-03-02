package cn.honry.finance.medicinelist.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.medicinelist.vo.RecipeNoVo;
import cn.honry.finance.medicinelist.vo.SpeNalVo;
import cn.honry.finance.medicinelist.vo.UndrugAndWare;
import cn.honry.inner.vo.MedicalVo;

@SuppressWarnings({"all"})
public interface MedicinelistDAO extends EntityDao<OutpatientFeedetailNow>{
	/**  
	 * @Description：  根据当前登陆用户ID查询员工ID
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param：id 用户ID
	 * @version 1.0
	 */
	SysEmployee queryEmployee(String id);
	
	/**  
	 * @Description：  根据病历号或就诊卡号后6位，弹框显示患者信息，
	 * @Author：wfj
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<Patient> vagueFindPatientById(String patientNo);
	/**  
	 * @Description： 根据就诊卡查询病历号
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:cardNo 就诊卡号
	 * @version 1.0
	 */
	PatientIdcard queryPatientIdcardByBlh(String cardNo);
	/**  
	 * @Description： 根据病历号查询患者信息
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:patientNo 病历号
	 * @version 1.0
	 */
	List<Patient> findPatientById(String patientNo);
	/**  
	 * @Description：验证此病历号是否存在有效的就诊卡号
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:patientNo 病历号
	 * @version 1.0
	 */
	PatientIdcard queryIdcard(String patientNo);
	/**  
	 * @Description：根据患者Id查询是否在黑名单中
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:id 患者ID
	 * @version 1.0
	 */
	PatientBlackList queryBlackList(String patientNo);
	/**  
	 * @Description：根据患者病历号查询该患者挂号信息和挂号有效期
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:patientNo 病历号 
	 * @param:parameterValue 参数挂号有效期
	 * @version 1.0
	 */
	List<RegistrationNow> findRegisterInfo(String patientNo, String parameterValue);
	/**  
	 * @Description：查询挂号有效期（在系统参数中）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter queryParameterInfoTime();
	/**  
	 * @Description：  渲染科室
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> findDepartment();
	/**  
	 * @Description：  渲染合同单位
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BusinessContractunit> findContractunit();
	/**  
	 * @Description：  渲染人员
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> findEmployee();
	/**  
	 * @Description： 员工下拉（根据科室可联动）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:regDpcd 开立科室
	 * @version 1.0
	 */
	List<SysEmployee> findEmployeeList(String regDpcd);
	/**  
	 * @Description：根据门诊号查询出费用明细表中处方号
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	List<RecipeNoVo> findFeedetailRecipeNo(String clinicCode);
	/**  
	 * @Description：根据每一个处方号查询处方号下的处方
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:recipeNo 处方号
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> queryFeedetailList(String recipeNo);
	/**  
	 * @Description： 员工下拉（根据科室可联动）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:count 合同单位ID
	 * @version 1.0
	 */
	BusinessContractunit queryCountByPaykindCode(String count);
	/**  
	 * @Description： 根据处方号查询费用明细表中该是该处方号的信息，可能是多条
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:recipeNo 处方号 可能是多个处方号拼接的 "','"
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailDetails(String recipeNo);
	/**  
	 * @Description： 根据最小费用代码的encode查询统计大类中统计大类的encode和name
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:encode最小费用代码encode
	 * @version 1.0
	 */
	MinfeeStatCode minfeeStatCodeByEncode(String encode);
	/**  
	 * @Description：科室下拉框
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> quertComboboxDept();
	/**  
	 * @Description：模糊条件查询非药品数量
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param：undrugCodes 模糊条件（name code 拼音码 五笔码等）
	 * @version 1.0
	 */
	int getTotalUndrug(String undrugCodes);
	/**  
	 * @Description：根据模糊条件查询非药品，并且判断该非药品是否是物资，与是否库存充足
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param：undrugCodes 模糊条件（name code 拼音码 五笔码等） 
	 * @param：groupNo 组合号
	 * @version 1.0
	 */
	List<UndrugAndWare> findUndrugAndWareList(String undrugCodes,String page,String rows);
	/**  
	 * @Description：添加非药品时带出的辅材
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:undrugId 非药品ID
	 * @version 1.0
	 */
	List<MedicalVo> findOdditionalitemByTypeCode(String undrugId);
	/**  
	 * @Description：  渲染频次
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BusinessFrequency> findFrequency();
	/**  
	 * @Description：  根据门诊号查询挂号信息
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	RegistrationNow findRegisterInfoByNo(String clinicCode);
	/**  
	 * @Description：  查询收费序列
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryNOByclinicCode(String clinicCode);
	/**  
	 * @Description：  执行科室下拉（在可编辑表格中）
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> queryEdComboboxDept();
	/**  
	 * @Description：根据门诊号查询挂号信息
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	RegistrationNow queryInfoByNo(String clinicCode);
	/**  
	 * @Description：  查出所有医技科室
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> chargeImplementDepartmentList();
	/**  
	 * @Description：  在已经有医嘱收费的情况下 获取发票序号（根据门诊号）
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryFeedetailInvoiceNo(String no);
	/**  
	 * @Description：  根据处方号查出所有处方表中数据
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:recipedetailIds 处方号集合
	 * @version 1.0
	 */
	List<OutpatientRecipedetailNow> queryRecipedetailList(String recipedetailIds);
	/**  
	 * @Description：  根据非药品集合查出所有收费明细表中数据
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:feedetaiNotIds 收费明细表中非药品集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> saveOrUpdateFeedetailListNot(String feedetaiNotIds);
	/**  
	 * @Description：  根据药品集合查出所有收费明细表中数据
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:feedetailIds 收费明细表中非药品集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> saveOrUpdateFeedetailList(String feedetailIds);
	/**  
	 * @Description：根据处方号与药品属性获得数据
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:recipeNoArr 处方号集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> queryFeedetailRecipeNo(String recipeNoArr);
	/**  
	 * @Description：  查询最小数量的配药终端
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk： 
	 * @param:execDpcd 执行科室
	 * @version 1.0
	 */
	StoTerminal queryStoTerminal(String execDpcd);
	/**  
	 * @Description：  发药终端
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk： 
	 * @param:id 终端ID
	 * @version 1.0
	 */
	StoTerminal queryStoTerminalNo(String id);
	/**  
	 * @Description：药品表
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:feedetailIdsArr 药品ID集合
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryDrugInfoList(String feedetailIdsArr);
	/**  
	 * @Description：药品表（一条）
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:itemCode 药品ID
	 * @version 1.0
	 */
	DrugInfo queryDrugInfoById(String itemCode);
	/**  
	 * @Description：修改发票
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-02-03
	 * @ModifyRmk：  
	 * @param：id 领取人ID invoiceNo 发票号 invoiceType 发票类型
	 * @version 1.0
	 */
	void saveInvoiceFinance(String id, String invoiceNo, String invoiceType);
	/**  
	 * @Description： 预扣库存
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:feedetailIds 药品集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findDrugStorage(String feedetailIds);
	/**  
	 * @Description：查询库存表
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:execDpcd 执行科室  itemCode 药品ID
	 * @version 1.0
	 */
	DrugStockinfo findStockinfoList(String execDpcd, String itemCode);
	/**  
	 * @Description：药品专用
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param：itemCode 药品ID
	 * @version 1.0
	 */
	DrugInfo findDruginfoList(String itemCode);
	/**  
	 * @Description：查询患者账户根据病历号
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param：midicalrecordId 病历号
	 * @version 1.0
	 */
	OutpatientAccount getAccountByMedicalrecord(String midicalrecordId);
	/**  
	 * @Description：根据病历号查询账户明细表中当天的消费金额
	 * @Author：ldl
	 * @ModifyDate：2016-04-05
	 * @ModifyRmk：  
	 * @param：midicalrecordId 病历号
	 * @version 1.0
	 */
	List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId);
	/**  
	 * @Description：根据病历号和密码查询是否密码正确
	 * @Author：ldl
	 * @ModifyDate：2016-04-05
	 * @ModifyRmk：  
	 * @param：midicalrecordId 病历号
	 * @param:md5Hex 密码
	 * @version 1.0
	 */
	OutpatientAccount veriPassWord(String md5Hex, String patientNo);
	/**  
	 * @Description：按照特殊收费窗口查找配药台
	 * @Author：ldl
	 * @ModifyDate：2016-04-05
	 * @ModifyRmk：  
	 * @param：execDpcd 药房
	 * @param:id 收费科室
	 * @param:itemType 特殊收费窗口
	 * @version 1.0
	 */
	SpeNalVo querySpeNalVoBy(String execDpcd, String id,Integer itemType);
	/**  
	 * @Description：
	 * @Author：ldl
	 * @ModifyDate：2016-04-05
	 * @ModifyRmk：  
	 * @param：execDpcd 药房
	 * @param:id 收费科室
	 * @param:itemType 特殊收费窗口
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryOutFeedetail(String ids);
	/**  
	 * @Description：判断时候需要医技申请
	 * @Author：ldl
	 * @ModifyDate：2016-04-13
	 * @ModifyRmk：  
	 * @param: feedetaiNotIds 非药品ID
	 * @version 1.0
	 */
	DrugUndrug queryUnDrugById(String feedetailId);
	/**  
	 * @Description：统计非药品存放到VO中
	 * @Author：ldl
	 * @ModifyDate：2016-04-25
	 * @ModifyRmk：  
	 * @param:feedetaiNotIds 非药品ID
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findMatFee(String feedetaiNotIds);

}
