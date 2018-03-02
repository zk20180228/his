package cn.honry.patinent.idcard.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

/**
 * 就诊卡
 * @author  lt
 * @date 2015-6-1 14：:4
 * @version 1.0
 */

public interface IdcardService extends BaseService<PatientIdcard>{

	/**  
	 *  
	 * @Description：保存或修改
	 * @Author：lt
	 * @CreateDate：2015-6-18  
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdate(PatientIdcard entity,Patient patient);

	/**
	 * @Description:获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<PatientIdcard> getPage(String page, String rows,PatientIdcard idcard);

	/**
	 * @Description:获取总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	int getTotal(PatientIdcard idcard);

	/**
	 * @Description:删除
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void del(String id);
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<PatientIdcard> findAll();

	/**
	 * @Description:根据id查询实体 
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	PatientIdcard queryById(String id);

	/**
	 * @Description:根据卡号IDcardNO 查询实体
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	PatientIdcard queryByIdcardNo(String idcardNo);
	/**
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	PatientIdcard getkh(String idcardNo);

	/**
	 * @Description:验证卡号和病历号是否已存在
	 * @Author：  lt
	 * @CreateDate： 2015-10-15
	 * @param @param idcardNo
	 * @param @param medicalrecordId
	 * @param @return   
	 * @return boolean  
	 * @version 1.0
	**/
	String checkIdcardNoVSMedicalNO(String idcardNo, String medicalrecordId,String idcardId);
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author： zpty
	 * @CreateDate： 2015-12-24
	 * @param @return   
	 * @return boolean  
	 * @version 1.0
	**/
	String checkIdcardName(String name, String sex, String birthday, String contact, String certificate, String number, String patientCitys,String pid);
	
	/**
	 * @Description:执行激活操作
	 * @Author：  lt
	 * @CreateDate： 2015-12-3
	 * @return void  
	 * @version 1.0
	**/
	boolean activateIdcard(String idcardId);

	/**  
	 *  
	 * @Description：  获得就诊卡号的Map
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-19 上午11:22:58  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-19 上午11:22:58  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> getCardNoMap();
	/**  
	 * @Description： 读卡查询信息
	 * @Author：wujiao
	 * @CreateDate：2016-3-23 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	PatientIdcard queryIdcadAllInfo(String idcardNo);
	
	/**
	 * @Description:执行挂失操作
	 * @Author：  lt
	 * @CreateDate： 2015-11-18
	 * @return void  
	 * @param zpty20160324从inpitaent包中移植过来
	 * @version 1.0
	**/
	InpatientAccount queryByIdcardId(String idcardId);
	
	/**
	 * @Description:saveorupdate方法
	 * @Author：  zpty
	 * @CreateDate： 2016-3-24
	 * @ModifyRmk：从inpatient包下引用过来  
	 * @version 1.0
	**/
	void inpatientAccountsaveOrUpdate(InpatientAccount entity);
	/**
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	SysEmployee findEmpByjobNo(String jobNo);
	
	/**
	 * @Description:通过就诊卡ID查找就诊卡
	 * @Author：  zpty
	 * @CreateDate： 2016-03-27
	 * @return void  
	 * @version 1.0
	**/
	PatientIdcard queryOldIdcardById(String idcardId);
	
	/**
	 * @Description:储存就诊卡变更数据
	 * @Author：  zpty
	 * @CreateDate： 2016-3-27
	 * @version 1.0
	**/
	void changeIdcardsaveOrUpdate(PatientIdcardChange entity);
	
	/**
	 * @Description:根据病历号获取住院账户信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return   
	 * @return InpatientAccount  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	InpatientAccount queryByMedical(String string);
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	void delByParentIdDetail(String id);
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	void delByParentIdRepaydetail(String id);
	
	/**
	 * @Description:物理删除账号信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return 
	 * @modify zpty20160327移植过来  
	 * @version 1.0
	**/
	void delInpatientAccount(String id);
	/**
	 * @Description:通过就诊卡号查询患者账户
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return OutpatientAccount 实体
	**/
	OutpatientAccount queryByIdcardIdOut(String idcardId);
	
	/**
	 * @Description:保存修改方法(门诊患者账户)
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return OutpatientAccount 实体
	**/
	void saveOrUpdateOut(OutpatientAccount entity);
	
}
