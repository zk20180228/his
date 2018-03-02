package cn.honry.patinent.idcard.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface IdcardDAO extends EntityDao<PatientIdcard>{
	/**
	 * @Description:获取分页列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	List<PatientIdcard> getPage(PatientIdcard entity, String page, String rows);

	/**
	 * @Description:获取总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	int getTotal(PatientIdcard entity);
	/**
	 * @Description:根绝idcardid查询实体
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	PatientIdcard queryById(String patientIdcard);
	/**
	 * @Description:根据IDcardNo 查询实体
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
	String checkIdcardVSMedicalNO(String idcardNo, String medicalrecordId,String idcardId);
	
	/**
	 * @Description:验证患者是否已存在
	 * @Author：  zpty
	 * @CreateDate： 2015-12-24
	 * @version: V1.0
	 * @param name 姓名
	 * @param sex 性别
	 * @param birthday 生日
	 * @param contact 卡类型
	 * @param certificate 身份证号
	 * @param number 电话
	 * @param patientCitys 省市县
	 * @param pid 
	 * @return boolean  
	 * @version 1.0
	**/
	String checkIdcardName(String name, String sex, String birthday, String contact, String certificate, String number, String patientCitys,String pid);

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
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	List<SysEmployee> findEmpByjobNo(String jobNo);
	
	/**
	 * @Description:通过就诊卡ID查找就诊卡
	 * @Author：  zpty
	 * @CreateDate： 2016-03-27
	 * @return void  
	 * @version 1.0
	**/
	PatientIdcard queryOldIdcardById(String idcardId);
	
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
	 * @Description:根据IDcard ID 取患者账户
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return OutpatientAccount 实体
	**/
	OutpatientAccount queryByIdcardIdOut(String idcardId);
}

