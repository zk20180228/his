package cn.honry.outpatient.advice.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.advice.vo.AdviceVo;
import cn.honry.outpatient.advice.vo.InpatientInfoVo;
import cn.honry.outpatient.advice.vo.InspectionReportList;
import cn.honry.outpatient.advice.vo.IreportPatientVo;
import cn.honry.outpatient.advice.vo.LisVo;
import cn.honry.outpatient.advice.vo.OutpatientVo;
import cn.honry.outpatient.advice.vo.PatientVo;
import cn.honry.outpatient.advice.vo.RecipelInfoVo;
import cn.honry.outpatient.advice.vo.RegisterMainVo;
import cn.honry.outpatient.advice.vo.ViewInfoVo;
import cn.honry.utils.TreeJson;

/**  
 * 门诊医嘱  Service
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface AdviceService extends BaseService<OutpatientRecipedetailNow>{

	/**  
	 *  
	 * 获得信息树-异步查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	String queryAdviceTree(String type,String id);

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
	 * @Description：  获得药房信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午07:01:34  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午07:01:34  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	SysDepartment getPharmacyInfoById(String pharmacyId);
	
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
	 * @Description：  获得医嘱项目信息-查询信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<ViewInfoVo> getViewInfoPage(String page,String rows, ViewInfoVo vo);
	
	/**  
	 *  
	 * @Description：  获得渲染数据-频次
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessFrequency> queryColorInfoFrequencyList();
	
	/**  
	 *  
	 * @Description：  获得渲染数据-执行科室
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysDepartment> queryColorInfoExeDeptList();
	
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
	 * @Description：  获得药品等级key药品等级id value等级编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> queryJudgeDrugGradeMap();
	
	/**  
	 *  
	 * @Description： 获得全部药品等级key药品等级id value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> queryJudgeDrugGradeAllMap();
	
	/**  
	 *  
	 * @Description：  通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:56:08  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:56:08  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PatientIdcard getIdcardByPatientNo(String patientNo);
	
	/**  
	 *  
	 * @Description：  根据病历号和看诊序号查询当天的挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:53:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:53:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegistrationNow getRegisterInfoByPatientNoAndNo(String patientNo, String no);
	
	/**  
	 *  
	 * @Description：  保存医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> savaAdviceInfo(List<AdviceVo> voList,String patientNo, PatientIdcard idcard,RegistrationNow registration);
	
	/**  
	 *  
	 * @Description：  获得历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<AdviceVo> queryMedicalrecordHisList(String clinicNo,String para,String q);
	
	/**  
	 *  
	 * @Description：  保存组套信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void savaStackInfo(String json,String stackFlag,String stackRemark,String stackInputCode,String stackSource,String stackName,String stackType,String stackInpmertype,String parent);

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
	String queryHisAdvice(String patientNo);

	/**  
	 *  
	 * @Description：查询病历
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：
	 * @param:clinicNo 看诊号
	 * @version 1.0
	 *
	 */
	OutpatientMedicalrecord queryRecord(String clinicNo);

	/**  
	 *  
	 * @Description： 删除医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, Object> delAdvice(String id);

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
	String queryAuditTree(String id);

	/**  
	 *  
	 * @Description： 审核
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String savaAdviceAuditing(String id, Integer start, String remarks);

	/**  
	 *  
	 * @Description： 查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, Object> searchInfoHid(String idCardNo);

	/**  
	 *  
	 * @Description：  获得系统类别
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> queryJudgeSysTypeAllMap();

	/**  
	 *  
	 * @Description：查询历史医嘱（加载更多）
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param：id收否分库patientNo就诊卡号para分区时间
	 */
	List<TreeJson> queryHisAdviceNext(String id,String patientNo,String para);

	/**  
	 *  
	 * @Description： 查询特限药申请
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  clinicNo门诊号code药品编码
	 * @version 1.0
	 *
	 */
	DrugSpedrug querySpeDrugApply(String clinicNo, String code);

	
	/**  
	 *  
	 * @Description： 查询特限药申请组套
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<DrugSpedrug> querySpeDrugApplyStack(String clinicNo, String para);

	/**  
	 *  
	 * 查询处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, Object> queryPatientInfo(String page, String rows,String startTime, String endTime,String type,String para,String vague);

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
	Map<String,Object> getRows(String page,String rows,String startTime,String endTime,String type,String para,String vague);
	
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
	/**
	 * 根据就诊卡。查询lis
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月12日 下午7:36:33 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月12日 下午7:36:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param cardNo
	 * @return:
	 * @throws:
	 * @return: List<LisVo>
	 *
	 */
	List<LisVo> findLis(String cardNo,String page, String rows);
	/**
	 * 根据就诊卡查询lis总数
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月12日 下午8:08:39 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月12日 下午8:08:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param cardNo
	 * @return:
	 * @throws:
	 * @return: Integer
	 *
	 */
	Integer findLisNum(String cardNo);
	/**
	 * lis详细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月13日 上午10:45:33 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月13日 上午10:45:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inspectionId
	 * @return:
	 * @throws:
	 * @return: List<InspectionReportList>
	 *
	 */
	List<InspectionReportList> findLisDetail(String inspectionId);
}
