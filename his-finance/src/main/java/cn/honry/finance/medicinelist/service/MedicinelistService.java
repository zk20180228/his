package cn.honry.finance.medicinelist.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.finance.medicinelist.vo.FeeCodeVo;
import cn.honry.finance.medicinelist.vo.RecipedetailVo;
import cn.honry.finance.medicinelist.vo.UndrugAndWare;
import cn.honry.inner.vo.MedicalVo;


public interface MedicinelistService extends BaseService<OutpatientFeedetail>{
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
	 * @Description：  领取发票（工作方法）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：
	 * @param：id 员工ID
	 * @param:invoiceType 发票类型
	 * @version 1.0
	 */
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType);
	/**  
	 * @Description：  根据病历号或就诊卡号后6位，弹框显示患者信息，
	 * @Author：wfj
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<Patient> vagueFindPatientById(String patientNo);
	/**  
	 * @Description：  根据病历号或就诊卡号查询出患者信息和挂号记录
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:cardNo 就诊卡号
	 * @param：patientNo 病历号
	 * @version 1.0
	 */
	Map<String, Object> queryBlhOrCardNo(OutpatientFeedetailNow feedetail);
	/**  
	 * @Description：渲染科室
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> findDepartment();
	/**  
	 * @Description：渲染合同单位
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BusinessContractunit> findContractunit();
	/**  
	 * @Description：  渲染员工
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> findEmployee();
	/**  
	 * @Description：  多条查询挂号信息(弹窗显示)
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param:patientNo 病历号
	 * @version 1.0
	 */
	List<RegistrationNow> findRegisterInfo(String patientNo);
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
	 * @Description： 查询右上角的处方列表（处方号相同的处方会在一条数据中，并且将他们的金额合并算出总和）参数为门诊号，开立科室，开单医生
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:regDpcd 开立科室
	 * @param:clinicCode 门诊号
	 * @param:doctCode 开单科室
	 * @version 1.0
	 */
	List<RecipedetailVo> findFeedetailStatistics(String patientNo,String regDpcdName, String doctCodeName);
	/**  
	 * @Description： 员工下拉（根据科室可联动）
	 * @Author：ldl
	 * @CreateDate：2016-03-31
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
	List<MedicalVo> findFeedetailDetails(String recipeNo);
	/**  
	 * @Description：根据传过来的json查询统计大类的项目，赋值到新建的页面显示VO
	 * @Author：ldl
	 * @CreateDate：2016-03-31
	 * @ModifyRmk：  
	 * @param:jsonRowsList 所有在处方明细列表的处方（json串形式）
	 * @version 1.0
	 */
	List<FeeCodeVo> findMinfeeStatCodeByMinfeeCodes(String jsonRowsList);
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
	 * @param:page rows 分页属性
	 * @version 1.0
	 */
	List<UndrugAndWare> findUndrugAndWareList(String undrugCodes,String page,String rows);
	/**  
	 * @Description：根据最小单位代码encode查询统计大类数据
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:encode 最小费用代码encode
	 * @version 1.0
	 */
	MinfeeStatCode minfeeStatCodeByEncode(String encode);
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
	 * @Description：  划价保存
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 */
	void savePrice(String jsonRowsList, String clinicCode);
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
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:clinicCode 门诊号
	 * @version 1.0
	 */
	RegistrationNow queryInfoByNo(String clinicCode);
	/**  
	 * @Description：执行收费
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:parameterMap 参数集合MAP registerInfo 挂号信息 account 账户信息 jsonRowsList 医嘱信息
	 * @version 1.0
	 */
	Map<String,Object> saveCharge(Map<String, String> parameterMap,Registration registerInfo, OutpatientAccount account,String jsonRowsList);
	/**  
	 * @Description： 预扣库存
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:feedetailIds 药品集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findDrugStorage(String feedetailIds);
	/**  
	 * @Description：查询库存表
	 * @Author：ldl
	 * @ModifyDate：2016-04-01
	 * @ModifyRmk：  
	 * @param:execDpcd 执行科室  itemCode 药品ID
	 * @version 1.0
	 */
	DrugStockinfo findStockinfoList(String execDpcd, String itemCode);
	/**  
	 * @Description：药品专用
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
	 * @ModifyDate：2016-04-05
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
	 * @Description：检验医技是否确认
	 * @Author：ldl
	 * @CreateDate：2016-04-13
	 * @ModifyRmk： 
	 * @param: feedetaiNotIds 非药品集合
	 * @version 1.0
	 */
	Map<String, String> unDrugWarehouse(String feedetaiNotIds);
	
	/**
	 * @Description 系统类别渲染
	 * @author  marongbin
	 * @createDate： 2017年2月18日 下午2:16:56 
	 * @modifier 
	 * @modifyDate：
	 * @param type
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getSystemTypeMap(String type);
	/**
	 * @Description 根据处方明细ID获取处方明细
	 * @author  marongbin
	 * @createDate： 2017年2月21日 下午8:17:45 
	 * @modifier 
	 * @modifyDate：
	 * @param feeID
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String,String> getMoney(String feeID);
	
}
