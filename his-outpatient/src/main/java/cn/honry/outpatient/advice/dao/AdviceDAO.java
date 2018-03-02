package cn.honry.outpatient.advice.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.outpatient.advice.vo.DrugAndUnDrugVo;
import cn.honry.outpatient.advice.vo.InpatientInfoVo;
import cn.honry.outpatient.advice.vo.InspectionReportList;
import cn.honry.outpatient.advice.vo.IreportPatientVo;
import cn.honry.outpatient.advice.vo.KeyValueVo;
import cn.honry.outpatient.advice.vo.LisVo;
import cn.honry.outpatient.advice.vo.OdditionalitemAndUnDrugVo;
import cn.honry.outpatient.advice.vo.OutpatientVo;
import cn.honry.outpatient.advice.vo.PatientVo;
import cn.honry.outpatient.advice.vo.RecipelInfoVo;
import cn.honry.outpatient.advice.vo.RegisterMainVo;
import cn.honry.outpatient.advice.vo.ViewInfoVo;

/**  
 *  
 * 门诊医嘱  DAO
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface AdviceDAO extends EntityDao<OutpatientRecipedetailNow>{
	
	/**  
	 *  
	 * 获得信息树-异步查询-查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	List<KeyValueVo> queryAdviceTreeForPatient(String type);

	/**  
	 *  
	 * 获得信息树-异步查询-查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	List<KeyValueVo> queryAdviceTreeForRegister(String type, String id);

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:idCardNo就诊卡号type类型1待诊2已诊
	 *
	 */
	List<PatientVo> queryPatientByidCardNo(String idCardNo,String type);

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:clinicNo门诊号
	 *
	 */
	PatientVo queryPatientByclinicNo(String clinicNo);
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-查询信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<ViewInfoVo> getViewInfoPage(String page, String rows,ViewInfoVo vo);
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getViewInfoTotal(ViewInfoVo vo);
	
	/**  
	 *  
	 * @Description：  获得医生职级和药品等级对照关系key药品等级编码value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> queryJudgeDocDrugGradeMap();
	
	/**  
	 *  
	 * @Description：   门诊医嘱-根据药房和id查询药品或非药品库存信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-10 下午06:00:21  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-10 下午06:00:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	DrugAndUnDrugVo finfDrugAndUnDrugById(String adviceId, String minusDeptHid);
	
	/**  
	 *  
	 * @Description： 获得药品或非药品的附材
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-29 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-29 下午03:17:28  
	 * @ModifyRmk：  ty医嘱类型1药品2非药品 drugUsage药品的用法 undrugId非药品的id deptId当前登录科室
	 * @version 1.0
	 *
	 */
	List<OdditionalitemAndUnDrugVo> findOdditionalitem(Integer ty, String drugUsage,String undrugId,String deptId);
	
	/**  
	 *  
	 * @Description：  根据看诊号获得历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientRecipedetailNow> queryMedicalrecordHisList(String clinicNo,String para,String q);
	
	/**  
	 *  
	 * @Description： 根据药品或非药品获得项目
	 * @Author：liudelin
	 * @CreateDate：2015-12-4
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-28 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	ViewInfoVo findDrugAndUnDrugById(String id,Integer drugFlag);

	/**  
	 *  
	 * @Description：查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValueVo> queryHisAdvice(String patientNo,String isParDb);

	/**  
	 *  
	 * @Description： 根据处方id获得处方
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OutpatientRecipedetailNow> getAdviceListByIds(String id);

	/**  
	 *  
	 * @Description： 获得医生可开立的全部特限药品
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> querySpecialDrugMap();

	/**  
	 *  
	 * @Description：  查询该医师是否有审核权限
	 * @Author：aizhonghua
	 * @CreateDate：2015-01-21 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-01-21 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean queryAuditing();

	/**  
	 *  
	 * @Description： 获得待审核的患者信息树
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValueVo> queryAuditTree(String id);

	/**  
	 *  
	 * @Description：查询历史医嘱（加载更多）
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValueVo> queryHisAdviceNext(String id,String patientNo,String para);

	/**  
	 *  
	 * 查询时间
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	StatVo findMaxMin();

	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, Object> queryPatientInfo(String page, String rows,String startTime, String endTime, List<String> tnL,String type,String para,String vague);

	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RecipelInfoVo> getRecipelInfoRows(String recipeNo, String tab);
	/**  
	 * 
	 * 打印医嘱患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<RegisterMainVo> getRegisterMainVo(List<String> array);
	/**  
	 * 
	 * 打印医嘱处方信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<OutpatientVo> getOutpatientVo(String clinicCode,String patientNo);
	/**  
	 * 
	 * 打印检查单患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月8日 下午5:21:38 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月8日 下午5:21:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<IreportPatientVo> getIreportPatientVo(String patientNo,String clinicCode);
	
	/**  
	 *  
	 * 获得住院患者信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31    
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> getRows(String page,String rows,String startTime,String endTime,String type,String para,String vague,List<String> tnL);
	
	/**  
	 *  
	 * 根据住院号查询医嘱信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientInfoVo> getInpatientInfoRows(String inpatientNo,String tab);
	List<LisVo> findLis(String cardNo,String page, String rows);
	Integer findLisNum(String cardNo);
	List<InspectionReportList> findLisDetail(String inspectionId);
}
