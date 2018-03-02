package cn.honry.outpatient.transfuse.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.OutpatientMixLiquid;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
/**   
* TransfuseService
* @description： 门诊配液service
* @author：tuchuanjiang
* @createDate：2016-06-21
* @version 1.0
 */
public interface TransfuseService extends BaseService<OutpatientMixLiquid>{
	/**
	 * 通过病历号查询患者的处方信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	List<OutpatientRecipedetailNow> queryPatientYZInfo(String clinicCode);
	/**
	 * 通过病历号查询患者信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	List<Patient> queryPatientInfo(String patientNo);
	/***
	 *  查询员工map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<SysEmployee> queryDoctrans();
	/***
	 *  查询科室map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<SysDepartment> queryDeptTrans();
	/***
	 *  查询频次map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<BusinessFrequency> queryFrequencyTrans();
	/***
	 *  保存院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	String saveform(String rowdata,String clinicCode);
	/***
	 *  根据门诊号查询院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<OutpatientMixLiquid> queryRecipedetail(String clinicCode);
	/***
	 *  根据门诊号查询院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<OutpatientMixLiquid> queryMixliquid(String clinicCode);
	/***
	 *  查询usermap
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<User> queryUsertrans();
	/***
	 *  查询员工list
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	List<SysEmployee> queryEmptrans();
	

}
