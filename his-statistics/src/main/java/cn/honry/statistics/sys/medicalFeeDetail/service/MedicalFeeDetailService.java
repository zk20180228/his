package cn.honry.statistics.sys.medicalFeeDetail.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeDetailsVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;

public interface MedicalFeeDetailService extends BaseService<InpatientInfo> {
	/**
	 * @Description:获取费用名称信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<FeeNameVo> queryFeeName() throws Exception;
	/**
	 * @Description:获取费用明细信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-2
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<FeeDetailsVo> queryFeeDetails() throws Exception;
	/**
	 * @Description:获取患者信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-21
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId,String startTime,String endTime) throws Exception;

	/**
	 * 在院患者医药费明细
	 * @param medicalrecordId 病例号
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param page 第几页
	 * @param rows 一页多少条
	 * @return 结果集合
	 */
	Map<String, Object> queryFeeDetailsByES(String medicalrecordId, String startTime, String endTime, String page, String rows);
	/**
	 * @Description:获取统计大类信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-6-20
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<MinfeeStatCode> queryMinfeeStat(String feeCode) throws Exception;
	/**
	 * @Description:获取统计大类信息
	 * @return List<MinfeeStatCode>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<FeeNameVo> queryFeeNameToMinfee() throws Exception;


}
