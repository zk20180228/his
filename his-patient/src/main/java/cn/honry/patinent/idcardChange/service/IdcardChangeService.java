package cn.honry.patinent.idcardChange.service;

import java.util.List;

import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;

/**
 * @Description： 就诊卡变更信息
 * GH
 * @CreateDate：2017年2月20日
 * @version 1.0
 */
public interface IdcardChangeService extends BaseService<PatientIdcardChange>{

	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡总条数
	 * @return
	 */ 
	public int getTotal(String patientId,String startTime,String endTime,String type);
	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡变更记录
	 * @return
	 */ 
	public List<PatientIdcardChange> queryChange(String page,String rows,String patientId,String startTime,String endTime,String type);
	
	/**
	 * 查询用户Map
	 * @author zpty
	 * @CreateDate：2017-05-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<User> queryUserRecord();
}
