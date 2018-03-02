package cn.honry.statistics.finance.chargeBill.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;

/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单service
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
public interface ChargeBillService extends BaseService<ChargeBillVo>{
	/**
	 * @Description:通过病历号住院主表的患者信息
	 * @Author：  tcj
	 * @param medId 病历号
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryInpatientInfo(String medId) throws Exception;
	/**
	 * @Description:通过住院流水号查询患者的费用信息（住院药品明细表、住院费药品明细表）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @param inpatientNo 住院流水号
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @param sendfalg 发放状态
	 * @return
	 * @throws Exception 
	 */
	List<ChargeBillVo> queryDatagridinfo(String inpatientNo,String startTime,String endTime,String sendfalg) throws Exception;
	/**
	 * @Description:查询住院科室用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	List<SysDepartment> queryZYDept();
	/**
	 * @Description:查询患者弹窗
	 * @Author：  tcj
	 * @param medicalrecordId 6位病历号
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	 * @throws Exception 
	**/
	List<Patient> queryPatientbill(String medicalrecordId) throws Exception;
	/**
	 * @Description:查询用户用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	List<User> queryEmpbill();

}
