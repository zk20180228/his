package cn.honry.inpatient.amobileApply.Service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessAdvdrugnature;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessOdditionalitem;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.docAdvManage.vo.AdviceLong;
import cn.honry.inpatient.docAdvManage.vo.ProInfoVo;
import cn.honry.inpatient.docAdvManage.vo.UnitsVo;

public interface DocManageServiceApp extends BaseService<InpatientOrder>{
	/**  
	 *  
	 * @Description： 查询医嘱资料
	 * @Author：yeguanqun
	 * @param decmpsState：长期医嘱临时医嘱标识
	 * @param inpatientNo：患者住院流水号
	 * @param recordId：页面医嘱范围查询下拉框参数
	 * @CreateDate：2015-12-22   
	 * @version 1.0
	 *
	 */
	public List<InpatientOrder> queryInpatientOrder(String decmpsState,String inpatientNo,String recordId);
	/**  
	 *  
	 * @Description： 查询医嘱类型资料
	 * @Author：yeguanqun
	 * @param inpatientKind：医嘱类型对象
	 * @CreateDate：2016-4-12   
	 * @version 1.0
	 *
	 */
	List<InpatientKind> queryDocAdvType(InpatientKind inpatientKind);
	/**  
	 *  
	 * @Description：查询公费患者项目范围的特殊字符标识
	 * @Author：yeguanqun
	 * @param itemType：药品非药品标识
	 * @CreateDate：2015-12-25   
	 * @version 1.0
	 *
	 */
	public String queryDrugOrNodrug(String itemType);
	/**  
	 *  
	 * @Description： 查询计量单位资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-22   
	 * @version 1.0
	 *
	 */
	public List<UnitsVo> queryUnits();
	/**  
	 *  
	 * @Description： 查询项目信息
	 * @Author：yeguanqun
	 * @param name：项目名称
	 * @param type：系统类别代码
	 * @param sysTypeName：系统类别名称
	 * @param id：项目id
	 * @CreateDate：2015-12-27   
	 * @version 1.0
	 *
	 */
	public List<ProInfoVo> querySysInfo(String page, String rows,String name,String type,String sysTypeName,String typeCode,String id);
	/**  
	 *  
	 * @Description： 查询项目列表信息总数量
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-26   
	 * @version 1.0
	 *
	 */
	int querySysInfoTotal(String name,String type,String sysTypeName,String typeCode,String id);
	/**  
	 *  
	 * @Description： 查询包装单位资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryDrugpackagingunit();
	/**  
	 *  
	 * @Description： 查询非药品单位资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryNonmedicineencoding();
	/**  
	 *  
	 * @Description： 查询药品等级资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	public Map<String, String>  queryDruggrade();
	/**  
	 *  
	 * @Description： 查询执行科室资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	public Map<String, String>  queryImplDepartment(String deptCode);
	/**  
	 *  
	 * @Description： 查询库存资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryDrugStorage();
	/**  
	 *  
	 * @Description： 查询系统类别资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	public Map<String, String> querySystemtype();
	/**  
	 *  
	 * @Description： 查询频次资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryFrequency();
	/**  
	 *  
	 * @Description： 查询频次资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryDrugUsemode();		
	/**  
	 *  
	 * @Description： 保存表格中新增的医嘱数据
	 * @Author：yeguanqun
	 * @param 
	 * @CreateDate：2015-12-19 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param empJobNo 
	 * @param deptCode 
	 * @throws Exception 
	 *
	 */
	public Map<String,Object> saveOrUpdateInpatientOrder(InpatientOrderNow inpatientOrder,String str, String empJobNo, String deptCode) throws Exception;
	/**  
	 *  
	 * @Description： 查询附材资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2015-12-31   
	 * @version 1.0
	 *
	 */
	public List<BusinessOdditionalitem> queryOdditionalitem(String code,String deptId,int drugFlag);
	/**  
	 *  
	 * @Description： 查询药品等级医生职级对照
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	public List<SysDruggraDecontraStrank> queryDruggraDecontraStrank(
			String userId, String drugGrade);
	/**  
	 *  
	 * @Description：查询长期医嘱限制性药品性质表
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	public List<BusinessAdvdrugnature> queryAdvdrugnature(String drugNature);

	/**  
	 *  
	 * @Description： 查询历史医嘱资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	List<InpatientOrderNow> getPage(String page, String rows,InpatientOrderNow inpatientOrderSerch,String recordId);

	/**  
	 *  
	 * @Description： 查询历史医嘱资料总条数
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	int getTotal(InpatientOrderNow inpatientOrderSerch,String recordId);

	/**  
	 *  
	 * @Description： 查询科室资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-12   
	 * @version 1.0
	 *
	 */
	List<SysDepartment> queryPage(String page, String rows,SysDepartment departmentSerch);

	/**  
	 *  
	 * @Description： 查询科室资料总条数
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-12   
	 * @version 1.0
	 *
	 */
	int queryTotal(SysDepartment departmentSerch);
	/**  
	 *  
	 * @Description： 医生职级是否受到开药职级限制
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-13  
	 * @version 1.0
	 *
	 */
	int queryAuditInfo(String userId,String parameterCode);
	/**  
	 *  
	 * @Description： 上级医生审核修改医嘱数据
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-14 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void updateMoStat(InpatientOrderNow inpatientOrder);
	/**  
	 *  
	 * @Description：  删除医嘱信息
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-14 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void delDocAdvInfo(String id);
	/**  
	 *  
	 * @Description： 查询频次表code
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-13  
	 * @version 1.0
	 *
	 */
	public List<BusinessFrequency> queryBusinessFrequency(String frequencyCode);
	/**  
	 *  
	 * @Description： 查询频次表code
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-13  
	 * @version 1.0
	 *
	 */
	public List<BusinessFrequency> queryFrequency(String frequencyEncode);
	/**  
	 *  
	 * @Description： 根据id查询非药品
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-13  
	 * @version 1.0
	 *
	 */
	public List<DrugUndrug> queryNoDrugInfo(String id);
	/**  
	 *  
	 * @Description： 医嘱停止与作废
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-15 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void obsoleteAdvice(InpatientOrderNow inpatientOrder,String timeFlag,String stopTime,String advStopReasonId,String advStopReason,String adviceJson);
	/**  
	 *  
	 * @Description： 保存特殊频次
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-20 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void updateSpeFreInfo(InpatientOrderNow inpatientOrder);	
	/**  
	 *  
	 * @Description： 查询医嘱资料
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-1-21   
	 * @version 1.0
	 *
	 */
	public List<InpatientOrderNow> queryInpatientOrderById(String id,String combNo,String mainDrug,String subtblFlag);
	/**  
	 *  
	 * @Description：  获得渲染数据-检查部位Map
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-03-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public Map<String, String> queryCheckpointMap();	
	/**  
	 *  
	 * @Description：  获得渲染数据-样本类型Map
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-03-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public Map<String, String> querySampleTeptMap();
	/**  
	 *  
	 * @Description： 合同单位List
	 * @Author：yeguanqun
	 * @param
	 * @CreateDate：2016-03-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessContractunit> queryReglist();
	/**  
	 *  
	 * @Description： 频次下拉获取 先获取创建科室为当前用户登陆科室的频次 若为空 则获取创建科室为ROOT的频次
	 * @Author：hedng
	 * @param
	 * @CreateDate：2016-10-13 上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public List<BusinessFrequency> queryFrequencyList();
	/**  
	 *  
	 * @Description： 重整医嘱
	 * @Author：donghe
	 * @param
	 * @CreateDate：2017-2-9 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String reformAdvice(String adviceJson);
	/**
	 * @Description：开立医嘱时判断是否存在相对应的摆药单
	 * @Author：donghe
	 * @param drugCode  药品code
	 * @param deptCode  发药科室
	 * @param typeCode  医嘱类型
	 * @param useCode  用法
	 * @return
	 */
	public String dispensingSf(String drugCode,String deptCode,String typeCode,String useCode);
	/**  
	 * 
	 * <p> 查询挂号信息 </p>
	 * @Author: donghe
	 * @CreateDate: 2016年10月28日 下午4:31:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Map
	 *
	 */
	Map<String, Object> getoutPatient(String startTime,String endTime,String condition,String type,String page,String rows);
	/**  
	 * 
	 * <p> 根据门诊号查询门诊处方信息 </p>
	 * @Author: donghe
	 * @CreateDate: 2017年4月20日 下午4:31:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<OutpatientRecipedetailNow>
	 *
	 */
	List<OutpatientRecipedetailNow>  queryOutpatientRecipedetail(String clinicCode,String startTime,String endTime);
	/**
	 * @see 打印长医嘱
	 * @param inpatientNo
	 * @return list
	 */
	List<AdviceLong> printAdviceLong(String inpatientNo,String flag);
	/**
	 * @see 打印历史长医嘱
	 * @param inpatientNo
	 * @return list
	 */
	public List<AdviceLong> printAdvicehis(String inpatientNo, String flag);
}
