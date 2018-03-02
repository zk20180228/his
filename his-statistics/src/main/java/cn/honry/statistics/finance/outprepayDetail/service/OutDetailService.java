package cn.honry.statistics.finance.outprepayDetail.service;

import java.util.List;

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.service.BaseService;
import cn.honry.utils.FileUtil;

@SuppressWarnings({"all"})
public interface OutDetailService extends BaseService<OutpatientOutprepay>{
	
	/***
	 * 患者充值明细统计数据
	 * @Title: queryOutprepay 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page 开始页数<
	 * @param rows 查询条数
	 * @param medicalrecordId 患者病历号
	 * @param beginDate 检索开始时间
	 * @param endDate 检索结束时间
	 * @return List<OutpatientOutprepay> 预交金实体集合
	 * @version 1.0
	 */
	List<OutpatientOutprepay> queryOutprepay(String page,String rows,String medicalrecordId,String beginDate,String endDate);
	
	/***
	 * 患者充值明细统计条数
	 * @Title: queryOutprepay 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page 开始页数<
	 * @param rows 查询条数
	 * @param medicalrecordId 患者病历号
	 * @param beginDate 检索开始时间
	 * @param endDate 检索结束时间
	 * @return int 数据总条数
	 * @version 1.0
	 */
	int queryOutprepayTotal(String page,String rows,String medicalrecordId,String beginDate,String endDate);
	
	/***
	 * 统计患者操作流水
	 * @Title: queryAccountrecord 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page
	 * @param rows
	 * @param medicalrecordId
	 * @param beginDate
	 * @param endDate
	 * @return List<OutpatientAccountrecord>
	 * @version 1.0
	 */
	List<OutpatientAccountrecord> queryRecord(String page,String rows,String medicalrecordId,String beginDate,String endDate);
	
	/***
	 * 统计患者操作流水条数
	 * @Title: queryRecordTotal 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param page
	 * @param rows
	 * @param medicalrecordId
	 * @param beginDate
	 * @param endDate
	 * @return int
	 * @version 1.0
	 */
	int queryRecordTotal(String page,String rows,String medicalrecordId,String beginDate,String endDate);
	
	/***
	 * 患者表
	 * @Title: queryPatient 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @return List<Patient>
	 * @version 1.0
	 */
	List<Patient> queryPatient();
	
	/***
	 * 病历号模糊查询
	 * @Title: querymedicalrecord 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @param medicalrecord
	 * @return List<Patient>
	 * @version 1.0
	 */
	List<Patient> querymedicalrecord(String medicalrecord);
	/**
	 * @author conglin
	 * @See 通过就诊卡或身份证查询病历号
	 * @param ic
	 * @param idCard
	 * @return
	 */
	public List<Patient> cardQueryMedicalrecord(String ic, String idCard,String codeCertificate);
	
	/***
	 * 统计患者操作流水导出和打印用，不带分页
	 * @Title: queryRecordList 统计患者操作流水导出和打印用，不带分页
	 * @param medicalrecordId 患者病历号
	 * @param beginDate 检索开始时间
	 * @param endDate 检索结束时间
	 * @author dtl 
	 * @date 2017年5月17日
	 */
	List<OutpatientAccountrecord> queryRecordList(String medicalrecordId,String beginDate,String endDate);
	
	/** 患者充值明细统计数据导出和打印用，不带分页
	* @Title: queryOutprepayList 患者充值明细统计数据导出和打印用，不带分页
	* @Description: 患者充值明细统计数据导出和打印用，不带分页
	* @param medicalrecordId 患者病历号
	* @param beginDate 检索开始时间
	* @param endDate 检索结束时间
	* @author dtl 
	* @date 2017年5月17日
	*/
	List<OutpatientOutprepay> queryOutprepayList(String medicalrecordId,String beginDate,String endDate);

	/** 为导出充值明细填充数据
	* @Title: exportOutperpay 为导出充值明细填充数据
	* @Description: 为导出充值明细填充数据
	* @param list 充值明细数据
	* @param fUtil
	* @author dtl 
	* @date 2017年5月17日
	*/
	FileUtil exportOutperpay(List<OutpatientOutprepay> list, FileUtil fUtil);

	/** 为导出消费明细填充数据
	* @Title: exportRecord 为导出消费明细填充数据
	* @Description: 为导出消费明细填充数据
	* @param list 消费明细数据
	* @param fUtil
	* @author dtl 
	* @date 2017年5月17日
	*/
	FileUtil exportRecord(List<OutpatientAccountrecord> list, FileUtil fUtil);
}
