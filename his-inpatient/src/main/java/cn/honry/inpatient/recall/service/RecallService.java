package cn.honry.inpatient.recall.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBabyInfoNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.recall.vo.Vhinfo;

/**
 * 住院召回
 * @author  ldl
 * @date 2015-8-31 
 * @version 1.0
 */
public interface RecallService extends BaseService<InpatientInfoNow>{
	/**  
	 *  
	 * @Description：  查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo queryRecallList(String mcardNo, String inpatientNo,String bedId, String patientName) throws Exception;
	
	/**
	 * 根据患者病历号查询患者住院和病床信息(新表)
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInfoNow> queryInfoList(String mid) throws Exception;
	
	/**
	 * 根据病床id查询病床信息
	 * @param bedID
	 * @return
	 * @throws Exception 
	 */
	InpatientBedinfoNow getInfoByBedId(String bedID) throws Exception;
	/**
	 * 获得病床号下拉框
	 * @return
	 * @throws Exception 
	 */
	List<BusinessHospitalbed> getcombobox(String deptId) throws Exception;
	/**
	 * 根据母亲姓名查询
	 * @param motherName
	 * @return
	 * @throws Exception 
	 */
	VidBedInfo queryInfoListBYmotherName(String motherName) throws Exception;
	
	/**
	 * 获得母亲姓名
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	Patient getInfoBYName(String name) throws Exception;
	
	/**
	 * 根据病床号查询
	 * @throws Exception 
	 */
	BusinessHospitalbed getBedByName(String bedId) throws Exception;
	/**  
	 *  
	 * @Description：  业务判断查询（住院号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo RecallByInpatientNo(String inpatientNos) throws Exception;
	/**  
	 *  
	 * @Description：  业务判断查询（床号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @throws Exception 
	 */
	VidBedInfo RecallBedByName(String bedIds) throws Exception;
	/**  
	 *  
	 * @Description：  更新病床表信息
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 * @param houseDocCode 
	 * @param chargeDocCode 
	 * @param dutyNurseCode 
	 * @param chiefDocCode 
	 * @throws Exception 
	 */
	void updateBedid(String bedIds, String inpatientNos, String chiefDocCode, String dutyNurseCode, String chargeDocCode, String houseDocCode) throws Exception;
	 /**
     * 查询医生护士
	 * @throws Exception 
     */
	List<SysEmployee> getEmpCombobox(String empType) throws Exception;
	/**
	 * 查询婴儿信息表
	 * @throws Exception 
	 */
	InpatientBabyInfoNow babyInfoByInpatientNo(String inpatientNo) throws Exception;
	/**
	 * 根据住院流水号查询住院主表
	 * @throws Exception 
	 */
	InpatientInfoNow getInfoByMotherid(String inpatientNO) throws Exception;
	/**
	 * 根据病历号查询病人id
	 * @throws Exception 
	 */
	Patient getIdByMedId(String mid) throws Exception;
	/**
	 * 根据住院主表的床号查询病床信息
	 * @throws Exception 
	 */
	List<Vhinfo> getBedidByBusinessHospitalbed(String bedId) throws Exception;
	
	
	/**
	 * 根据患者病历号查询患者住院和病床信息(旧表)
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInfo> queryOldInfoList(String mid) throws Exception;

	/**
	 * 根据id查询患者住院和病床信息(旧表)
	 * @param mid
	 * @return
	 * @throws Exception 
	 */
	InpatientInfo queryOldbyId(String id) throws Exception;

	/**
	 * 更新旧表数据
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	void updateOld(String oid) throws Exception;
}
