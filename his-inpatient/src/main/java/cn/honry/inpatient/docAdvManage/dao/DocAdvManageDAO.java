package cn.honry.inpatient.docAdvManage.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessAdvdrugnature;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessOdditionalitem;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.docAdvManage.vo.AdviceLong;
import cn.honry.inpatient.docAdvManage.vo.ProInfoVo;
import cn.honry.inpatient.docAdvManage.vo.UnitsVo;

public interface DocAdvManageDAO extends EntityDao<InpatientOrder>{
	/**  
	 *  
	 * @Description： 查询医嘱资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-22   
	 * @version 1.0
	 *
	 */
	List<InpatientOrder> queryInpatientOrder(String decmpsState,String inpatientNo,String recordId);
	/**  
	 *  
	 * @Description： 查询医嘱类型资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-12   
	 * @version 1.0
	 *
	 */
	List<InpatientKind> queryDocAdvType(InpatientKind inpatientKind);
	/**  
	 *  
	 * @Description： 查询药品资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-22   
	 * @version 1.0
	 *
	 */
	List<DrugInfo> queryDrugInfo();
	/**  
	 *  
	 * @Description： 查询非药品资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-22   
	 * @version 1.0
	 *
	 */
	List<DrugUndrug> queryNoDrugInfo(String id);
	/**  
	 *  
	 * @Description： 查询计量单位资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-26   
	 * @version 1.0
	 *
	 */
	List<UnitsVo> queryUnits();
	/**  
	 *  
	 * @Description： 查询项目列表信息
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-26   
	 * @version 1.0
	 *
	 */
	List<ProInfoVo> querySysInfo(String page, String rows,String name,String type,String sysTypeName,String typeCode,String id);
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
	Map<String, String> queryDrugpackagingunit();
	/**  
	 *  
	 * @Description： 查询非药品单位资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-13   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryNonmedicineencoding();
	/**  
	 *  
	 * @Description： 查询药品等级资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryDruggrade();
	/**  
	 *  
	 * @Description： 查询执行科室资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryImplDepartment(String deptCode); 
	/**  
	 *  
	 * @Description： 查询库存资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-28   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryDrugStorage(); 
	/**  
	 *  
	 * @Description： 查询系统类别资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	Map<String, String> querySystemtype(); 
	/**  
	 *  
	 * @Description： 查询频次资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryFrequency(); 
	/**  
	 *  
	 * @Description： 查询药品用法资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-29   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryDrugUsemode(); 
	/**  
	 *  
	 * @Description： 查询附材资料
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-31   
	 * @version 1.0
	 *
	 */
	List<BusinessOdditionalitem> queryOdditionalitem(String code,String deptId,int drugFlag); 
	/**  
	 *  
	 * @Description：根据id 查询医嘱资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-7   
	 * @version 1.0
	 *
	 */
	InpatientOrderNow queryInpatientOrder(String Id);
	/**  
	 *  
	 * @Description： 查询药品等级医生职级对照
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	List<SysDruggraDecontraStrank> queryDruggraDecontraStrank(String userId,String drugGrade);
	/**  
	 *  
	 * @Description： 查询长期医嘱限制性药品性质表
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	List<BusinessAdvdrugnature> queryAdvdrugnature(String drugNature);
	/**  
	 *  
	 * @Description： 查询历史医嘱资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	List<InpatientOrderNow> getPage(String page, String rows, InpatientOrderNow entity,String recordId);

	/**  
	 *  
	 * @Description： 查询历史医嘱资料总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	int getTotal(InpatientOrderNow entity,String recordId);
	/**  
	 *  
	 * @Description： 根据id查询频次表
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-16  
	 * @version 1.0
	 *
	 */
	List<BusinessFrequency> queryBusinessFrequency(String frequencyCode);
	/**  
	 *  
	 * @Description： 根据encode查询频次表
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-16  
	 * @version 1.0
	 *
	 */
	List<BusinessFrequency> queryFrequencyByEncode(String frequencyEncode);
	/**  
	 *  
	 * @Description： 查询医嘱资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-21   
	 * @version 1.0
	 *
	 */
	List<InpatientOrderNow> queryInpatientOrderById(String id,String combNo,String mainDrug,String subtblFlag);
	/**  
	 *  
	 * @Description： 查询医嘱最大顺序号
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-21   
	 * @version 1.0
	 *
	 */
	List<InpatientOrderNow> queryMaxInpatientOrderSortId();
	/**  
	 *  
	 * @Description：获得渲染数据-检查部位Map
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-31   
	 * @version 1.0
	 *
	 */
	Map<String, String> queryCheckpointMap(); 
	/**  
	 *  
	 * @Description：  获得渲染数据-样本类型Map
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> querySampleTeptMap();
	/**  
	 *  
	 * @Description：  合同单位List
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-31 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessContractunit> queryReglist();
	/**  
	 *  
	 * @Description：  物理删除医嘱
	 * @Author：yeguanqun
	 * @CreateDate：2016-5-6 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public void delInpatientOrder(String id);
	/**
	 * 查询职称
	 * @param userId
	 * @return
	 */
	public List<BusinessDictionary> queryCodeTitle(String userId);
	/**  
	 * @Description： 频次下拉获取 先获取创建科室为当前用户登陆科室的频次 若为空 则获取创建科室为ROOT的频次
	 * @Author：hedng
	 * @param
	 * @CreateDate：2016-10-13 上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BusinessFrequency> queryFrequencyList();
	/**  
	 * @Description：修改医嘱为重整状态
	 * @Author：donghe
	 * @param
	 * @CreateDate：2017-2-9 上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	int updateAdviceList(InpatientOrderNow orderNow);
	/**  
	 * @Description：根据id查询医嘱信息
	 * @Author：donghe
	 * @param
	 * @CreateDate：2017-2-9 上午10:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	InpatientOrderNow getinpaorder(String id);
	/**  
	 * 
	 * <p> 获得摆药信息 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月28日 下午4:31:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月28日 下午4:31:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: DrugBilllist
	 *
	 */
	DrugBilllist getListByProperty(String... exist);
	/**  
	 * 
	 * <p> 查询有效挂号信息 </p>
	 * @Author: donghe
	 * @CreateDate: 2016年10月28日 下午4:31:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Map
	 *
	 */
	Map<String, Object> getoutPatient(List<String> tnL,List<String> mainL,String startTime,String endTime,String condition,String type,String page,String rows);
	/**  
	 * 
	 * <p> 根据门诊号查询门诊处方信息 </p>
	 * @Author: donghe
	 * @CreateDate: 2017年4月20日 下午4:31:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: DrugBilllist
	 *
	 */
	List<OutpatientRecipedetailNow> queryOutpatientRecipedetail(List<String> tnL,String clinicCode,String startTime,String endTime);
	/**
	 * @see 打印长期/临时医嘱
	 * @param inpatientNo
	 * @return
	 */
	List<AdviceLong> printAdviceLong(String inpatientNo,String flag);
	/**
	 * @see 打印历史长期/临时医嘱
	 * @param inpatientNo
	 * @return
	 */
	List<AdviceLong> printAdvicehis(String inpatientNo,String flag);
}
