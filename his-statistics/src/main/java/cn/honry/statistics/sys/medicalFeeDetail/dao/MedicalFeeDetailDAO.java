package cn.honry.statistics.sys.medicalFeeDetail.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeDetailsVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;

public interface MedicalFeeDetailDAO extends EntityDao<InpatientInfo>{
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
	 * @CreateDate： 2016-6-20
	 * @return List<FeeDetailsVo>  
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
