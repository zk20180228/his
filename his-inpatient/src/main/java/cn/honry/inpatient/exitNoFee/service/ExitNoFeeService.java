package cn.honry.inpatient.exitNoFee.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.exitNoFee.vo.FeeVo;
import cn.honry.inpatient.exitNoFee.vo.InpatientInfoVo;

public interface ExitNoFeeService extends BaseService<InpatientInfo> {

	/**
	 * @Description: 获取患者信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-9
	 * @param  inpatientInfoSerch：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoVo> getInpatientInfo(InpatientInfoNow inpatientInfoSerch) throws Exception;

	/**
	 * @Description:无费住院，病床表的床位状态BED_STATE要置成空床状态
	 * @Author： yeguanqun
	 * @CreateDate： 2015-12-9
	 * @param ids：住院信息表的主键id
	 * @param inpatientInfo：住院信息表实体类
	 * @version 1.0
	 * @throws Exception 
	**/
	String changeHospitalState(String ids,InpatientInfoNow inpatientInfo) throws Exception;
	
	/**
	 * @Description:收费
	 * @Author： yeguanqun
	 * @CreateDate： 2016-2-19
	 * @param feeVoList：费用结算vo集合
	 * @version 1.0
	 * @throws Exception 
	**/
	public Map<String, Object> saveInpatientFeeInfo(List<FeeVo> feeVoList) throws Exception;
	/**
	 * @Description:查询登录密码-校验登录密码
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param user：用户信息实体类   
	 * @return void  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<User> confirmPassword(User user) throws Exception;
	/**
	 * @Description:获取患者信息
	 * @Author： yeguanqun
	 * @CreateDate： 2016-4-15
	 * @param inpatientNo：患者住院流水号
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryInpatientInfo(String inpatientNo) throws Exception;
	
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
	 * @throws Exception 
	 *
	 */
	String queryInpatientNoByMid(String medicalrecordId) throws Exception;
	List<InpatientInfoVo> getInpatientInfoByInNo(InpatientInfoNow inpatientInfo) throws Exception;
	
}
